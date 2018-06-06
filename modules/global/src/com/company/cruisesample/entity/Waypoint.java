package com.company.cruisesample.entity;

import javax.persistence.*;

import com.company.cruisesample.gis.converters.CubaPointWKTConverter;
import com.company.cruisesample.gis.datatypes.PointDatatype;
import com.haulmont.chile.core.annotations.MetaProperty;

import java.util.Date;

import com.haulmont.cuba.core.entity.StandardEntity;

@Table(name = "CRUISESAMPLE_WAYPOINT")
@Entity(name = "cruisesample$Waypoint")
public class Waypoint extends StandardEntity {
    private static final long serialVersionUID = 2740335642338212108L;

    @Convert(converter = CubaPointWKTConverter.class)
    @MetaProperty(datatype = PointDatatype.NAME, mandatory = true)
    @Column(name = "POINT", nullable = false)
    protected com.vividsolutions.jts.geom.Point point;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ETA")
    protected Date eta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTE_ID")
    protected Route route;

    public void setRoute(Route route) {
        this.route = route;
    }

    public Route getRoute() {
        return route;
    }


    public void setPoint(com.vividsolutions.jts.geom.Point point) {
        this.point = point;
    }

    public com.vividsolutions.jts.geom.Point getPoint() {
        return point;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getEta() {
        return eta;
    }


}