package com.company.cruisesample.web.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.BeanValidation;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.validators.BeanValidator;

import javax.validation.constraints.NotNull;
import javax.validation.metadata.BeanDescriptor;

/**
 * Created by Aleksey Stukalov on 05/06/2018.
 */
public class MetadataUtils {

    public static boolean getInitialRequiredValue(MetaProperty metaProperty) {
        //check not null
        boolean notNullUiComponent = Boolean.TRUE.equals(metaProperty.getAnnotations()
                .get(NotNull.class.getName() + "_notnull_ui_component"));

        return notNullUiComponent || metaProperty.isMandatory();
    }

    public static String getInitialRequiredMessage(MetaPropertyPath metaPropertyPath) {
        return AppBeans.get(MessageTools.class)
                .getDefaultRequiredMessage(metaPropertyPath.getMetaClass(), metaPropertyPath.getMetaProperty().getName());
    }

    public static MetaPropertyPath getMetaPropertyPath(MetaClass metaClass, String property) {
        MetaPropertyPath metaPropertyPath = AppBeans.get(MetadataTools.NAME, MetadataTools.class)
                .resolveMetaPropertyPath(metaClass, property);
        if (metaPropertyPath == null)
            throw new IllegalArgumentException(String.format("Could not resolve property path '%s' in '%s'", property, metaClass));
        return metaPropertyPath;
    }

    public static BeanValidator getBeanValidator(MetaPropertyPath metaPropertyPath) {
        MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
        MetaClass propertyEnclosingMetaClass = metadataTools.getPropertyEnclosingMetaClass(metaPropertyPath);
        Class enclosingJavaClass = propertyEnclosingMetaClass.getJavaClass();

        if (enclosingJavaClass != KeyValueEntity.class
                && !DynamicAttributesUtils.isDynamicAttribute(metaPropertyPath.getMetaProperty())) {
            BeanValidation beanValidation = AppBeans.get(BeanValidation.NAME);
            javax.validation.Validator validator = beanValidation.getValidator();
            BeanDescriptor beanDescriptor = validator.getConstraintsForClass(enclosingJavaClass);

            if (beanDescriptor.isBeanConstrained()) {
                return new BeanValidator(enclosingJavaClass, metaPropertyPath.getMetaProperty().getName());
            }
        }
        return null;
    }

}
