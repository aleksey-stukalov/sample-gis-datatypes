package com.company.cruisesample.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.company.cruisesample.gis.datatypes.PointDatatype;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.vividsolutions.jts.geom.Point;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.haulmont.cuba.core.entity.StandardEntity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Table(name = "CRUISESAMPLE_WAYPOINT")
@Entity(name = "cruisesample$Waypoint")
public class Waypoint extends StandardEntity {
    private static final long serialVersionUID = 2740335642338212108L;

    @MetaProperty(datatype = PointDatatype.NAME, mandatory = true)
    @Column(name = "POINT", nullable = false)
    protected Point point;

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


    public void setPoint(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public void setEta(Date eta) {
        this.eta = eta;
    }

    public Date getEta() {
        return eta;
    }


}