package com.company.cruisesample.entity;

import javax.persistence.*;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import java.util.List;

@Table(name = "CRUISESAMPLE_ROUTE")
@Entity(name = "cruisesample$Route")
public class Route extends StandardEntity {
    private static final long serialVersionUID = -740433824286775135L;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @OrderBy("order")
    @JoinTable(name = "CRUISESAMPLE_ROUTE_STOP_LINK",
        joinColumns = @JoinColumn(name = "ROUTE_ID"),
        inverseJoinColumns = @JoinColumn(name = "STOP_ID"))
    @ManyToMany
    protected List<Stop> stops;

    @OrderBy("eta")
    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "route")
    protected List<Waypoint> waypoints;

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public List<Stop> getStops() {
        return stops;
    }


    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }




    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }



}