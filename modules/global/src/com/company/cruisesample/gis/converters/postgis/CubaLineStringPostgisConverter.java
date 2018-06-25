package com.company.cruisesample.gis.converters.postgis;

import com.company.cruisesample.gis.utils.GeometryUtils;
import com.vividsolutions.jts.geom.LineString;
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
public class CubaLineStringPostgisConverter implements AttributeConverter<LineString, Object> {

    @Override
    public Object convertToDatabaseColumn(LineString lineString) {
        if (lineString == null) {
            return null;
        }

        try {
            Geometry g = PGgeometry.geomFromString(lineString.toText());
            g.setSrid(lineString.getSRID());
            return new PGgeometry(g);
        } catch (SQLException e) {
            throw new RuntimeException("Unable to parse postgis geometry", e);
        }
    }

    @Override
    public LineString convertToEntityAttribute(Object o) {
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
        LineString ls = GeometryUtils.wktStringToLineString(geom.getTypeString() + geom.getValue());
        if (ls != null) {
            ls.setSRID(geom.getSrid());
        }
        return ls;
    }
}
