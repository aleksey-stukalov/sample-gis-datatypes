package com.company.cruisesample.gis.converters.postgis;


import com.company.cruisesample.gis.utils.GeometryUtils;
import com.vividsolutions.jts.geom.MultiPolygon;
import org.postgis.Geometry;
import org.postgis.PGgeometry;
import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.SQLException;

/**
 * Created by Aleksey Stukalov on 08/06/2018.
 */
@Converter
public class CubaMultiPolygonPostgisConverter implements AttributeConverter<MultiPolygon, Object> {
    @Override
    public Object convertToDatabaseColumn(MultiPolygon multiPolygon) {
        if (multiPolygon == null) {
            return null;
        }

        try {
            Geometry g = PGgeometry.geomFromString(multiPolygon.toText());
            g.setSrid(multiPolygon.getSRID());
            return new PGgeometry(g);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to parse postgis geometry", e);
        }
    }

    @Override
    public MultiPolygon convertToEntityAttribute(Object o) {
        if (o == null) {
            return null;
        }
        PGgeometry g;
        try {
            g = new PGgeometry(((PGobject)o).getValue());
        } catch (SQLException e) {
            throw new RuntimeException("Unable to create geometry object", e);
        }
        Geometry geom = g.getGeometry();
        MultiPolygon mp = GeometryUtils.wktStringToMultiPolygon(geom.getTypeString() + geom.getValue());
        if (mp != null) {
            mp.setSRID(geom.getSrid());
        }
        return mp;
    }
}

