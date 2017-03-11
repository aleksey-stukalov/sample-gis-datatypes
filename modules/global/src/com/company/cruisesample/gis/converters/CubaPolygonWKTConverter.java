package com.company.cruisesample.gis.converters;

import com.company.cruisesample.gis.utils.GeometryUtils;
import com.vividsolutions.jts.geom.Polygon;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by aleksey on 10/03/2017.
 */
@Converter(autoApply = true)
public class CubaPolygonWKTConverter implements AttributeConverter<Polygon, String> {
    @Override
    public String convertToDatabaseColumn(Polygon polygon) {
        return polygon != null ? polygon.toText() : null;
    }

    @Override
    public Polygon convertToEntityAttribute(String s) {
        return GeometryUtils.wktStringToPolygon(s);
    }
}
