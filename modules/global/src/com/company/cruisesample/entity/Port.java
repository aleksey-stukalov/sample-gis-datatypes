package com.company.cruisesample.entity;

import com.company.cruisesample.gis.converters.postgis.CubaPointPostgisConverter;
import com.company.cruisesample.gis.datatypes.PointDatatype;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.vividsolutions.jts.geom.Point;

@NamePattern("%s|name")
@Table(name = "CRUISESAMPLE_PORT")
@Entity(name = "cruisesample$Port")
public class Port extends StandardEntity {
    private static final long serialVersionUID = 5971964558755197442L;

    @Column(name = "NAME", nullable = false)
    protected String name;
    
    @Convert(converter = CubaPointPostgisConverter.class)
    @MetaProperty(datatype = PointDatatype.NAME, mandatory = true)
    @Column(name = "LOCATION", nullable = false)
    protected Point location;

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}