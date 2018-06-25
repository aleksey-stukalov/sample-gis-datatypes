package com.company.cruisesample.web.components;

import com.haulmont.charts.gui.map.model.Marker;
import com.haulmont.charts.web.gui.components.map.google.WebGoogleMapViewer;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.RequiredValueMissingException;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.components.ValidationFailedException;
import com.haulmont.cuba.gui.components.validators.BeanValidator;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.WeakItemChangeListener;
import com.vividsolutions.jts.geom.Point;

import java.util.*;
import java.util.function.Consumer;

import static com.haulmont.cuba.gui.ComponentsHelper.handleFilteredAttributes;

/**
 * Created by Aleksey Stukalov on 05/06/2018.
 */
public class WebGisPointEditor extends WebGoogleMapViewer implements GisPointEditor {

    protected static final int VALIDATORS_LIST_INITIAL_CAPACITY = 4;

    protected Datasource datasource;
    protected MetaPropertyPath metaPropertyPath;

    protected List<Validator> validators;

    protected Datasource.ItemChangeListener<Entity> securityItemChangeListener;
    protected WeakItemChangeListener securityWeakItemChangeListener;

    protected String requiredMessage;
    protected boolean required;
    protected boolean editable = true;
    protected Point value;

    private PointValueChangedListener pointValueChangedListener;


    //listener wires property change in datasource to setPoint call
    protected class PointValueChangedListener implements Datasource.ItemPropertyChangeListener {
        @Override
        public void itemPropertyChanged(Datasource.ItemPropertyChangeEvent e) {
            String property = getMetaPropertyPath().getMetaProperty().getName();
            if (property.equals(e.getProperty())) {
                Point newValue = e.getItem().getValue(property);
                setPoint(newValue);
            }
        }
    }

    public WebGisPointEditor() {
        //adding listeners for input
        addMapClickListener(e -> {
            if (isEditable()) {
                setPoint(MapViewUtils.geoPoint2Point(e.getPosition()));
            }
        });

        addMarkerDragListener(e -> {
            if (isEditable()) {
                setPoint(MapViewUtils.geoPoint2Point(e.getMarker().getPosition()));
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setDatasource(Datasource datasource, String property) {
        // initial consistency checks
        if ((datasource == null && property != null) || (datasource != null && property == null))
            throw new IllegalArgumentException("Datasource and property should be either null or not null at the same time");

        //skip method if there is no difference between old and new values
        if (datasource == this.datasource && ((metaPropertyPath != null && metaPropertyPath.toString().equals(property)) ||
                (metaPropertyPath == null && property == null)))
            return;

        if (this.datasource != null) {
            //clean up obsolete rubbish
            metaPropertyPath = null;

            this.datasource.removeItemChangeListener(securityWeakItemChangeListener);
            securityWeakItemChangeListener = null;

            this.datasource.removeItemPropertyChangeListener(pointValueChangedListener);
            pointValueChangedListener = null;

            this.datasource = null;

            validators.removeIf(v -> v instanceof BeanValidator);
        }

        if (datasource != null) {
            // initiate local fields
            this.datasource = datasource;
            metaPropertyPath = MetadataUtils.getMetaPropertyPath(datasource.getMetaClass(), property);

            // initiate required and editable attributes from metainformation
            setRequired(MetadataUtils.getInitialRequiredValue(metaPropertyPath.getMetaProperty()));
            setRequiredMessage(MetadataUtils.getInitialRequiredMessage(metaPropertyPath));
            setEditable(!metaPropertyPath.getMetaProperty().isReadOnly());

            // support row-level security boilerplate
            handleFilteredAttributes(this, this.datasource, metaPropertyPath);
            securityItemChangeListener = e -> handleFilteredAttributes(this, this.datasource, metaPropertyPath);
            securityWeakItemChangeListener = new WeakItemChangeListener(this.datasource, securityItemChangeListener);
            this.datasource.addItemChangeListener(securityWeakItemChangeListener);

            //initiate validator for @NotNull
            Optional.ofNullable(MetadataUtils.getBeanValidator(metaPropertyPath)).ifPresent(this::addValidator);

            // set initial value
            datasource.addItemChangeListener(l -> {
                if (l.getItem() != null)
                    setPoint(l.getItem().getValue(property));
            });

            //wire property change in datasource to setPoint call,
            //which in its turn changes the value field and repositions marker on the map
            pointValueChangedListener = new PointValueChangedListener();
            datasource.addItemPropertyChangeListener(pointValueChangedListener);
        }
    }

    protected void placeMarker() {
        clearMarkers();
        if (value != null) {
            Marker result = createMarker("Port", MapViewUtils.point2GeoPoint(this, value), true);
            addMarker(result);

            setCenter(result.getPosition());
        }
    }

    @Override
    public Point getPoint() {
        return value;
    }

    @Override
    public void setPoint(Point point) {
        // check if value is not the same to prevent endless recursion
        // otherwise setPoint will fire event, event will call setPoint, etc.
        if (!Objects.equals(point, value)) {
            Point preValue = value;
            value = point;

            //reposition marker
            placeMarker();

            //wire changes of the value field to the corresponding datasource item property
            if (datasource != null && metaPropertyPath != null)
                datasource.getItem().setValue(metaPropertyPath.getMetaProperty().getName(), value);

            //fire event signalling that value has been changed
            getEventRouter().fireEvent(ValueChangeListener.class, ValueChangeListener::valueChanged, new ValueChangeEvent(this, preValue, value));
        }
    }

    //------------- more or less boilerplate code -------------

    @Override
    public <T> T getValue() {
        return (T) value;
    }

    @Override
    public void setValue(Object value) {
        setPoint((Point) value);
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public void setRequiredMessage(String msg) {
        this.requiredMessage = msg;
    }

    @Override
    public String getRequiredMessage() {
        return requiredMessage;
    }

    @Override
    public void addValidator(Validator validator) {
        if (validators == null) {
            validators = new ArrayList<>(VALIDATORS_LIST_INITIAL_CAPACITY);
        }
        if (!validators.contains(validator)) {
            validators.add(validator);
        }
    }

    @Override
    public void removeValidator(Validator validator) {
        if (validators != null) {
            validators.remove(validator);
        }
    }

    @Override
    public Collection<Validator> getValidators() {
        return Collections.unmodifiableCollection(
                Optional.ofNullable(validators)
                        .orElse(Collections.emptyList())
        );
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        getEventRouter().addListener(ValueChangeListener.class, listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        getEventRouter().removeListener(ValueChangeListener.class, listener);
    }

    @Override
    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    @Override
    public void validate() throws ValidationException {
        if (hasValidationError()) {
            setValidationError(null);
        }

        if (!isVisible() || !isEditableWithParent() || !isEnabled()) {
            return;
        }

        Object value = getValue();
        if (value == null) {
            if (isRequired()) {
                throw new RequiredValueMissingException(getRequiredMessage(), this);
            } else {
                return;
            }
        }

        if (validators != null) {
            try {
                for (Field.Validator validator : validators) {
                    validator.validate(value);
                }
            } catch (ValidationException e) {
                setValidationError(e.getDetailsMessage());

                throw new ValidationFailedException(e.getDetailsMessage(), this, e);
            }
        }
    }

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    //------------------------ NOT SUPPORTED ------------------------

    @Override
    public String getContextHelpText() {
        return null;
    }

    @Override
    public void setContextHelpText(String contextHelpText) {

    }

    @Override
    public boolean isContextHelpTextHtmlEnabled() {
        return false;
    }

    @Override
    public void setContextHelpTextHtmlEnabled(boolean enabled) {

    }

    @Override
    public Consumer<ContextHelpIconClickEvent> getContextHelpIconClickHandler() {
        return null;
    }

    @Override
    public void setContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> handler) {

    }

    @Override
    @Deprecated
    public MetaProperty getMetaProperty() {
        return null;
    }

    @Override
    @Deprecated
    public void addListener(ValueListener listener) {
        //not implemented
    }

    @Override
    @Deprecated
    public void removeListener(ValueListener listener) {
        //not implemented
    }

}
