package com.company.cruisesample.web.route;

import com.company.cruisesample.entity.Port;
import com.company.cruisesample.entity.Route;
import com.company.cruisesample.entity.Stop;
import com.company.cruisesample.entity.Waypoint;
import com.company.cruisesample.service.RoutingService;
import com.company.cruisesample.web.components.MapViewUtils;
import com.haulmont.charts.gui.components.map.MapViewer;
import com.haulmont.charts.gui.map.model.GeoPoint;
import com.haulmont.charts.gui.map.model.Marker;
import com.haulmont.charts.gui.map.model.Polyline;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RouteEdit extends AbstractEditor<Route> {

    @Inject
    private CollectionDatasource<Stop, UUID> stopsDs;

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

    @Inject
    private Metadata metadata;

    @Override
    public void ready() {
        super.ready();

        portsTableRemove.setConfirm(false);
        synchronizePortsOnMap();
        paintRoute();

        stopsDs.addCollectionChangeListener(e -> {
            synchronizePortsOnMap();
            clearWaypoints();
            paintRoute();
        });

        mapViewer.setMinZoom(2);
        if (stopsDs.getItems().size() == 0) {
            mapViewer.setZoom(2);
            mapViewer.setCenter(mapViewer.createGeoPoint(0, 0));
        }

        optionsPortsDs.addItemChangeListener(e -> {
            if (e.getItem() != null) {
                Stop s = metadata.create(Stop.class);

                //unsafe! only for showcase purpose
                if (stopsDs.getItems().size() == 0)
                    s.setOrder(0);
                else {
                    Integer maxOrder = stopsDs.getItems().stream().map(Stop::getOrder).max(Integer::compareTo).get();
                    s.setOrder(maxOrder + 1);
                }

                s.setPort(e.getItem());
                stopsDs.addItem(s);
                lookupPortsField.setValue(null);
            }
        });

    }

    @Override
    protected boolean preCommit() {
        String name = getItem().getStops().stream()
                .map(Stop::getPort)
                .map(Port::getName)
                .reduce((s1, s2) -> s1 + " → " + s2).get();
        getItem().setName(name);
        return super.preCommit();
    }

    public void onButtonCalculateRouteClick() {
        clearWaypoints();
        List<Waypoint> waypoints = routingService.calculateRoute(new ArrayList<>(stopsDs.getItems()));
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
        List<Port> ports = stopsDs.getItems().stream().map(Stop::getPort).collect(Collectors.toList());
        ports.forEach(port -> {
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
            if (!ports.contains(p)) {
                mapViewer.removeMarker(marker);
                portMarkerMap.remove(p);
                optionsPortsDs.includeItem(p);
            }
        });

        autoscaleMap();
    }

    protected void autoscaleMap() {
        List<Port> ports = stopsDs.getItems().stream().map(Stop::getPort).collect(Collectors.toList());

        if (ports.size() == 0)
            return;

        double minLon = ports.stream()
                .min(Comparator.comparing(port -> port.getLocation().getX()))
                .get().getLocation().getX();

        double maxLon = ports.stream()
                .max(Comparator.comparing(port -> port.getLocation().getX()))
                .get().getLocation().getX();

        double minLat = ports.stream()
                .min(Comparator.comparing(port -> port.getLocation().getY()))
                .get().getLocation().getY();

        double maxLat = ports.stream()
                .max(Comparator.comparing(port -> port.getLocation().getY()))
                .get().getLocation().getY();

        mapViewer.fitToBounds(mapViewer.createGeoPoint(maxLat, maxLon),
                mapViewer.createGeoPoint(minLat, minLon));
    }
}