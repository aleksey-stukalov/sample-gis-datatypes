package com.company.cruisesample.gis.converters.postgis;

import com.company.cruisesample.gis.utils.GeometryUtils;
import com.vividsolutions.jts.geom.Polygon;
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
public class CubaPolygonPostgisConverter implements AttributeConverter<Polygon, Object> {
    @Override
    public Object convertToDatabaseColumn(Polygon polygon) {
        if (polygon == null) {
            return null;
        }

        try {
            Geometry g = PGgeometry.geomFromString(polygon.toText());
            g.setSrid(polygon.getSRID());
            return new PGgeometry(g);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to parse postgis geometry", e);
        }
    }

    @Override
    public Polygon convertToEntityAttribute(Object o) {
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
        Polygon p = GeometryUtils.wktStringToPolygon(geom.getTypeString() + geom.getValue());
        p.setSRID(geom.getSrid());
        return p;
    }
}
