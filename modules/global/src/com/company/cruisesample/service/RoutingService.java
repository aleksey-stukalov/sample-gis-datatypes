package com.company.cruisesample.service;


import com.company.cruisesample.entity.Stop;
import com.company.cruisesample.entity.Waypoint;

import java.util.List;

public interface RoutingService {
    String NAME = "cruisesample_RoutingService";

    List<Waypoint> calculateRoute(List<Stop> stops);
}