package com.company.cruisesample.web.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.charts.gui.map.model.Marker;
import com.haulmont.charts.web.gui.components.map.google.WebGoogleMapViewer;
import com.haulmont.cuba.gui.components.RequiredValueMissingException;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.components.ValidationFailedException;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.ValueBinding;
import com.haulmont.cuba.gui.components.data.value.ValueBinder;
import com.vividsolutions.jts.geom.Point;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Aleksey Stukalov on 05/06/2018.
 */
public class WebGisPointEditor extends WebGoogleMapViewer implements GisPointEditor {

    protected static final int VALIDATORS_LIST_INITIAL_CAPACITY = 4;

    protected ValueBinding<Point> valueBinding;

    protected List<Consumer<Point>> validators; // lazily initialized list

    protected String requiredMessage;
    protected boolean required;
    protected boolean editable = true;
    protected Point value;

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

            //fire event signalling that value has been changed
            publish(ValueChangeEvent.class, new ValueChangeEvent<>(this, preValue, value));
        }
    }

    //------------- more or less boilerplate code -------------

    @Override
    public Point getValue() {
        return value;
    }

    @Override
    public void setValue(Point value) {
        setPoint(value);
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
    public void addValidator(Consumer<? super Point> validator) {
        if (validators == null) {
            validators = new ArrayList<>(VALIDATORS_LIST_INITIAL_CAPACITY);
        }
        if (!validators.contains(validator)) {
            validators.add((Consumer<Point>) validator);
        }
    }

    @Override
    public void removeValidator(Consumer<Point> validator) {
        if (validators != null) {
            validators.remove(validator);
        }
    }

    @Override
    public Collection<Consumer<Point>> getValidators() {
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

        Point value = getValue();
        if (value == null) {
            if (isRequired()) {
                throw new RequiredValueMissingException(getRequiredMessage(), this);
            } else {
                return;
            }
        }

        if (validators != null) {
            try {
                for (Consumer<Point> validator : validators) {
                    validator.accept(value);
                }
            } catch (ValidationException e) {
                setValidationError(e.getDetailsMessage());

                throw new ValidationFailedException(e.getDetailsMessage(), this, e);
            }
        }
    }

    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<Point>> listener) {
        return getEventHub().subscribe(ValueChangeEvent.class, (Consumer) listener);
    }

    @Override
    public void removeValueChangeListener(Consumer<ValueChangeEvent<Point>> listener) {
        unsubscribe(ValueChangeEvent.class, (Consumer) listener);
    }

    @Override
    public void setValueSource(ValueSource<Point> valueSource) {
        if (this.valueBinding != null) {
            valueBinding.unbind();

            this.valueBinding = null;
        }

        if (valueSource != null) {
            ValueBinder binder = beanLocator.get(ValueBinder.class);

            this.valueBinding = binder.bind(this, valueSource);

            this.valueBinding.activate();
        }
    }

    @Override
    public ValueSource<Point> getValueSource() {
        return valueBinding != null ? valueBinding.getSource() : null;
    }
}
