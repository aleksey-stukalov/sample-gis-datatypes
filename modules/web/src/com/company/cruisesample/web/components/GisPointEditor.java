package com.company.cruisesample.web.components;

import com.haulmont.charts.gui.components.map.MapViewer;
import com.haulmont.cuba.gui.components.Field;
import com.vividsolutions.jts.geom.Point;

/**
 * Created by Aleksey Stukalov on 05/06/2018.
 */
public interface GisPointEditor extends Field, MapViewer {
    String NAME = "GisPointEditor";

    Point getPoint();
    void setPoint(Point point);
}
