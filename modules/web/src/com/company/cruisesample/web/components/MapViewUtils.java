package com.company.cruisesample.web.components;

import com.company.cruisesample.gis.utils.GeometryUtils;
import com.haulmont.charts.gui.components.map.MapViewer;
import com.haulmont.charts.gui.map.model.GeoPoint;
import com.haulmont.charts.gui.map.model.Marker;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

/**
 * Created by aleksey on 11/03/2017.
 */
public class MapViewUtils {

    public static GeoPoint point2GeoPoint(MapViewer map, Point p) {
        return map.createGeoPoint(p.getY(), p.getX());
    }

    public static Point geoPoint2Point(GeoPoint gp) {
        return GeometryUtils.getGeometryFactory().createPoint(new Coordinate(gp.getLongitude(), gp.getLatitude()));
    }

    public static Marker createMarker(MapViewer map, String caption, Point p, Boolean draggable) {
        return map.createMarker(caption, point2GeoPoint(map, p), draggable);
    }

    public static Marker addMarker(MapViewer map, String caption, Point p, Boolean draggable) {
        Marker result = createMarker(map, caption, p, draggable);
        map.addMarker(result);
        return result;
    }
}
