package com.company.cruisesample.web.port;

import com.company.cruisesample.entity.Port;
import com.haulmont.cuba.gui.components.AbstractEditor;
//import com.company.cruisesample.web.components.MapViewUtils;
//import com.haulmont.charts.gui.components.map.MapViewer;
//import com.haulmont.charts.gui.map.model.Marker;
//import com.haulmont.cuba.gui.data.Datasource;
//import com.vividsolutions.jts.geom.Point;
//
//import javax.inject.Inject;

public class PortEdit extends AbstractEditor<Port> {

//    @Inject
//    private Datasource<Port> portDs;
//
//    @Inject
//    private MapViewer mapViewer;
//
//    @Override
//    public void ready() {
//        super.ready();
//
//        putMarker(mapViewer, portDs.getItem().getLocation());
//
//        mapViewer.addMapClickListener(e -> {
//            Point newLocation = MapViewUtils.geoPoint2Point(e.getPosition());
//            getItem().setLocation(newLocation);
//            putMarker(mapViewer, portDs.getItem().getLocation());
//        });
//
//        mapViewer.addMarkerDragListener(e -> {
//            Point newLocation = MapViewUtils.geoPoint2Point(e.getMarker().getPosition());
//            getItem().setLocation(newLocation);
//        });
//    }
//
//    protected void putMarker(MapViewer map, Point location) {
//        map.clearMarkers();
//        if (location != null) {
//            Marker m = MapViewUtils.addMarker(map, "Port", location, true);
//            map.setCenter(m.getPosition());
//        }
//    }

}