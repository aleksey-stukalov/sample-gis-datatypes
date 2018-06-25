package com.company.cruisesample.gis.converters.wkt;

import com.company.cruisesample.gis.utils.GeometryUtils;
import com.vividsolutions.jts.geom.MultiPolygon;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by aleksey on 10/03/2017.
 */
@Converter
public class CubaMultiPolygonWKTConverter implements AttributeConverter<MultiPolygon, String> {

    @Override
    public String convertToDatabaseColumn(MultiPolygon multiPolygon) {
        return multiPolygon != null ? multiPolygon.toText() : null;
    }

    @Override
    public MultiPolygon convertToEntityAttribute(String s) {
        return GeometryUtils.wktStringToMultiPolygon(s);
    }
}
