package com.company.cruisesample.gis.converters.postgis;

import com.company.cruisesample.gis.utils.GeometryUtils;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.postgis.Geometry;
import org.postgis.PGgeometry;
import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.SQLException;


/**
 * Created by Aleksey Stukalov on 08/06/2018.
 */
@Converter(autoApply = false)
public class CubaPointPostgisConverter implements AttributeConverter<Point, Object> {

    @Override
    public Object convertToDatabaseColumn(Point point) {
        if (point == null) {
            return null;
        } else {
            Coordinate c = point.getCoordinate();
            if (c == null) {
                return null;
            }

            org.postgis.Point p = new org.postgis.Point();

            if (!Double.isNaN(c.x)) {
                p.setX(c.x);
            }

            if (!Double.isNaN(c.y)) {
                p.setY(c.y);
            }

            if (!Double.isNaN(c.z)) {
                p.setZ(c.z);
            }

            p.setSrid(point.getSRID());
            return new PGgeometry(p);
        }
    }

    @Override
    public Point convertToEntityAttribute(Object o) {
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
        if (geom instanceof org.postgis.Point) {
            org.postgis.Point pgPoint = (org.postgis.Point) geom;
            GeometryFactory f = GeometryUtils.getGeometryFactory();
            Point p = f.createPoint(new Coordinate(pgPoint.getX(), pgPoint.getY(), pgPoint.getZ()));
            return p;
        } else {
            throw new RuntimeException("Unable to parse Point from data retrieved from database");
        }
    }
}

