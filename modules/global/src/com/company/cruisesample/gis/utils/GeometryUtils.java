package com.company.cruisesample.gis.utils;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.util.AffineTransformation;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.apache.commons.lang.StringUtils;

/**
 * Created by aleksey on 10/03/2017.
 */
public class GeometryUtils {

    //WGS84 datum
    public static final int SRID_4326 = 4326;

    public static Double getLatitude(Coordinate c) {
        return c != null ? c.getOrdinate(Coordinate.Y) : null;
    }

    public static Double getLongitude(Coordinate c) {
        return c != null ? c.getOrdinate(Coordinate.X) : null;
    }

    public static Double getAltitude(Coordinate c) {
        return c != null ? c.getOrdinate(Coordinate.Z) : null;
    }

    public static Polygon wktStringToPolygon(String s) {
        Geometry g = wktStringToGeometry(s);
        if (g == null) {
            return null;
        }

        if (!(g instanceof Polygon)) {
            throw new RuntimeException("Polygon data expected, found " + g.getGeometryType());
        }

        return (Polygon) g;
    }

    public static LineString wktStringToLineString(String s) {
        Geometry g = wktStringToGeometry(s);
        if (g == null) {
            return null;
        }

        if (!(g instanceof LineString)) {
            throw new RuntimeException("LineString data expected, found " + g.getGeometryType());
        }

        return (LineString) g;
    }

    public static Point wktStringToPoint(String s) {
        Geometry g = wktStringToGeometry(s);
        if (g == null) {
            return null;
        }

        if (!(g instanceof Point)) {
            throw new RuntimeException("Point data expected, found " + g.getGeometryType());
        }

        return (Point) g;
    }

    public static MultiPolygon wktStringToMultiPolygon(String s) {
        Geometry g = wktStringToGeometry(s);
        if (g == null) {
            return null;
        }

        if (g instanceof MultiPolygon) {
            return (MultiPolygon) g;
        } else if (g instanceof Polygon) {
            Polygon p = (Polygon) g;
            return new MultiPolygon(new Polygon[]{p}, getGeometryFactory());
        } else {
            throw new RuntimeException("Polygon or MultiPolygon data expected, found " + g.getGeometryType());
        }
    }

    public static Geometry wktStringToGeometry(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }

        WKTReader reader = new WKTReader();
        Geometry g;
        try {
            g = reader.read(s);
        } catch (ParseException e) {
            throw new RuntimeException("Cannot parse Geometry data from WKT format", e);
        }

        if (g == null) {
            return null;
        }
        return g;
    }

    public static Geometry convertTo3857SRID(Geometry geometry) {
        //affine tranbsformation that switches X and Y coordinates
        AffineTransformation transformation = new AffineTransformation(0, 1, 0, 1, 0, 0);
        Geometry transformed = transformation.transform(geometry);
        transformed.setSRID(3857);
        return transformed;
    }

    public static GeometryFactory getGeometryFactory() {
        return new GeometryFactory(new PrecisionModel(), SRID_4326);
    }
}
