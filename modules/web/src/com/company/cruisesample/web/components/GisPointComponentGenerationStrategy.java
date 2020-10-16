package com.company.cruisesample.web.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesTools;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ComponentGenerationContext;
import com.haulmont.cuba.gui.components.ComponentGenerationStrategy;
import com.haulmont.cuba.gui.components.factories.AbstractComponentGenerationStrategy;
import com.vividsolutions.jts.geom.Point;
import org.springframework.core.Ordered;

import javax.annotation.Nullable;
import javax.inject.Inject;

/**
 * Created by Aleksey Stukalov on 05/06/2018.
 */

@org.springframework.stereotype.Component(GisPointComponentGenerationStrategy.NAME)
public class GisPointComponentGenerationStrategy extends AbstractComponentGenerationStrategy implements Ordered {

    public static final String NAME = "cruisesample_GisPointComponentGeneration";

    @Inject
    public GisPointComponentGenerationStrategy(Messages messages, DynamicAttributesTools dynamicAttributesTools) {
        super(messages, dynamicAttributesTools);
    }

    @Inject
    public void setUiComponents(UiComponents uiComponents) {
        this.uiComponents = uiComponents;
    }

    @Nullable
    @Override
    public Component createComponent(ComponentGenerationContext context) {
        MetaClass metaClass = context.getMetaClass();
        MetaPropertyPath mpp = resolveMetaPropertyPath(metaClass, context.getProperty());

        if (mpp != null) {
            Range mppRange = mpp.getRange();
            if (mppRange.isDatatype()) {
                MetaProperty metaProperty = mpp.getMetaProperty();
                Class<?> javaType = metaProperty.getJavaType();
                if (Point.class.isAssignableFrom(javaType)) {
                    return createMap(context);
                }
            }
        }
        return null;
    }

    private Component createMap(ComponentGenerationContext context) {
        GisPointEditor pointEditor = uiComponents.create(GisPointEditor.class);
        pointEditor.setValueSource(context.getValueSource());
        return pointEditor;
    }

    @Override
    public int getOrder() {
        return ComponentGenerationStrategy.HIGHEST_PLATFORM_PRECEDENCE;
    }
}
