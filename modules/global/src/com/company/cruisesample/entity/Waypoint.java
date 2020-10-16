package com.company.cruisesample.entity;

import com.company.cruisesample.gis.converters.postgis.CubaPointPostgisConverter;
import com.company.cruisesample.gis.datatypes.PointDatatype;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;
import java.util.Date;
import com.vividsolutions.jts.geom.Point;

@Table(name = "CRUISESAMPLE_WAYPOINT")
@Entity(name = "cruisesample$Waypoint")
public class Waypoint extends StandardEntity {
    private static final long serialVersionUID = 2740335642338212108L;

    @Convert(converter = CubaPointPostgisConverter.class)
    @MetaProperty(datatype = PointDatatype.NAME, mandatory = true)
    @Column(name = "POINT", nullable = false)
    protected Point point;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ETA")
    protected Date eta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROUTE_ID")
    protected Route route;

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }


    public void setRoute(Route route) {
        this.route = route;
    }

    public Route getRoute() {
        return route;
    }


    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getEta() {
        return eta;
    }


}