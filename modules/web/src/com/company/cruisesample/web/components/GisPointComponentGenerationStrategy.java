package com.company.cruisesample.web.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.AbstractComponentGenerationStrategy;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ComponentGenerationContext;
import com.haulmont.cuba.gui.components.ComponentGenerationStrategy;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
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
        GisPointEditor pointEditor = componentsFactory.createComponent(GisPointEditor.class);
        pointEditor.setDatasource(context.getDatasource(), context.getProperty());
        return pointEditor;
    }

    @Override
    public int getOrder() {
        return ComponentGenerationStrategy.HIGHEST_PLATFORM_PRECEDENCE;
    }

    @Inject
    public GisPointComponentGenerationStrategy(Messages messages) {
        super(messages);
    }

    @Inject
    public void setComponentsFactory(ComponentsFactory componentsFactory){
        this.componentsFactory = componentsFactory;
    }

}
