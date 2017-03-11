package com.company.cruisesample.web.route;

import com.company.cruisesample.entity.Port;
import com.company.cruisesample.entity.Route;
import com.company.cruisesample.entity.Waypoint;
import com.company.cruisesample.gis.utils.MapViewUtils;
import com.company.cruisesample.service.RoutingService;
import com.haulmont.charts.gui.components.map.MapViewer;
import com.haulmont.charts.gui.map.model.GeoPoint;
import com.haulmont.charts.gui.map.model.Marker;
import com.haulmont.charts.gui.map.model.Polyline;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RouteEdit extends AbstractEditor<Route> {

    @Inject
    private CollectionDatasource<Port, UUID> portsDs;

    @Inject
    private MapViewer mapViewer;

    @Inject
    private LookupField lookupPortsField;

    @Inject
    private CollectionDatasource<Port, UUID> optionsPortsDs;

    @Inject
    private CollectionDatasource<Waypoint, UUID> waypointsDs;

    @Inject
    private RoutingService routingService;

    @Named("portsTable.remove")
    private RemoveAction portsTableRemove;

    private BidiMap portMarkerMap = new DualHashBidiMap();

    private Polyline routePolyline;

    @Override
    public void ready() {
        super.ready();

        portsTableRemove.setConfirm(false);
        synchronizePortsOnMap();
        paintRoute();

        portsDs.addCollectionChangeListener(e -> {
            synchronizePortsOnMap();
            clearWaypoints();
            paintRoute();
        });

        mapViewer.setMinZoom(2);
        if (portsDs.getItems().size() == 0) {
            mapViewer.setZoom(2);
            mapViewer.setCenter(mapViewer.createGeoPoint(0, 0));
        }

        optionsPortsDs.addItemChangeListener(e -> {
            if (e.getItem() != null) {
                portsDs.addItem(e.getItem());
                lookupPortsField.setValue(null);
            }
        });

    }

    @Override
    protected boolean preCommit() {
        String name = getItem().getPorts().stream().map(Port::getName).reduce((s1, s2) -> s1 + " → " + s2).get();
        getItem().setName(name);
        return super.preCommit();
    }

    public void onButtonCalculateRouteClick() {
        clearWaypoints();
        List<Waypoint> waypoints = routingService.calculateRoute(new ArrayList<>(portsDs.getItems()));
        waypoints.forEach(wp -> {
            wp.setRoute(getItem());
            waypointsDs.addItem(wp);
        });

        paintRoute();
    }

    protected void paintRoute() {
        if (routePolyline == null) {
            routePolyline = mapViewer.createPolyline();
            routePolyline.setStrokeWeight(3);
            routePolyline.setStrokeOpacity(0.8);
            routePolyline.setGeodesic(true);
            routePolyline.setStrokeColor("#000080");
        }
        else
            mapViewer.removePolyline(routePolyline);


        List<GeoPoint> coordinates = new ArrayList<>();
        for (Waypoint wp : waypointsDs.getItems()) {
            coordinates.add(MapViewUtils.point2GeoPoint(mapViewer, wp.getPoint()));
        }

        routePolyline.setCoordinates(coordinates);
        mapViewer.addPolyline(routePolyline);
    }

    protected void clearWaypoints() {
        if (waypointsDs != null) {
            List<Waypoint> wpts = new ArrayList<>();
            waypointsDs.getItems().forEach(wpts::add);
            for (Waypoint wp : wpts)
                waypointsDs.removeItem(wp);
        }
    }

    protected void synchronizePortsOnMap() {
        portsDs.getItems().forEach(port -> {
                    Marker m = (Marker) portMarkerMap.get(port);
                    if (m == null) {
                        m = MapViewUtils.addMarker(mapViewer, port.getName(), port.getLocation(), false);
                        portMarkerMap.put(port, m);
                        m.setIconUrl("http://localhost:8080/app/VAADIN/themes/base/anchor.png");
                        optionsPortsDs.excludeItem(port);
                    }
                });

        mapViewer.getMarkers().forEach(marker -> {
            Port p = (Port) portMarkerMap.getKey(marker);
            if (!portsDs.getItems().contains(p)) {
                mapViewer.removeMarker(marker);
                portMarkerMap.remove(p);
                optionsPortsDs.includeItem(p);
            }
        });

        autoscaleMap();
    }

    protected void autoscaleMap() {
        if (portsDs.getItems().size() == 0)
            return;

        double minLon = portsDs.getItems().stream().map(port -> port.getLocation().getX())
                .min(Double::compare)
                .get();

        double maxLon = portsDs.getItems().stream().map(port -> port.getLocation().getX())
                .max(Double::compare)
                .get();

        double minLat = portsDs.getItems().stream().map(port -> port.getLocation().getY())
                .min(Double::compare)
                .get();

        double maxLat = portsDs.getItems().stream().map(port -> port.getLocation().getY())
                .max(Double::compare)
                .get();

        mapViewer.fitToBounds(mapViewer.createGeoPoint(maxLat, maxLon),
                mapViewer.createGeoPoint(minLat, minLon));
    }
}