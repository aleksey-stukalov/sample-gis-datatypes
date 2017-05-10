package com.company.cruisesample.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s. %s|order,port")
@Table(name = "CRUISESAMPLE_STOP")
@Entity(name = "cruisesample$Stop")
public class Stop extends StandardEntity {
    private static final long serialVersionUID = -2248162467534127610L;

    @Column(name = "ORDER_", nullable = false)
    protected Integer order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PORT_ID")
    protected Port port;

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getOrder() {
        return order;
    }

    public void setPort(Port port) {
        this.port = port;
    }

    public Port getPort() {
        return port;
    }


}