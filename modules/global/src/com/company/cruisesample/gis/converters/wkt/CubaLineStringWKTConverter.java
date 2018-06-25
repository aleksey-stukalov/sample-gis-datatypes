package com.company.cruisesample.gis.converters.wkt;

import com.company.cruisesample.gis.utils.GeometryUtils;
import com.vividsolutions.jts.geom.LineString;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by aleksey on 10/03/2017.
 */

@Converter
public class CubaLineStringWKTConverter implements AttributeConverter<LineString, String> {

    @Override
    public String convertToDatabaseColumn(LineString lineString) {
        return lineString != null ? lineString.toText() : null;
    }

    @Override
    public LineString convertToEntityAttribute(String s) {
        return GeometryUtils.wktStringToLineString(s);
    }
 }
