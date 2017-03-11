package com.company.cruisesample.gis.converters;

import com.company.cruisesample.gis.utils.GeometryUtils;
import com.vividsolutions.jts.geom.Point;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by aleksey on 10/03/2017.
 */
@Converter(autoApply = true)
public class CubaPointWKTConverter implements AttributeConverter<Point, String> {

    @Override
    public String convertToDatabaseColumn(Point point) {
        return point != null ? point.toText() : null;
    }

    @Override
    public Point convertToEntityAttribute(String s) {
        return s != null ? GeometryUtils.wktStringToPoint(s) : null;
    }
}
