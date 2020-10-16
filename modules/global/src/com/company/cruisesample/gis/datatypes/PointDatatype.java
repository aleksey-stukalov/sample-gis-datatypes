package com.company.cruisesample.gis.datatypes;

import com.company.cruisesample.gis.utils.GeometryUtils;
import com.haulmont.chile.core.annotations.Ddl;
import com.haulmont.chile.core.datatypes.Datatype;
import com.vividsolutions.jts.geom.Point;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by aleksey on 10/03/2017.
 */
@Ddl(dbms = "postgres", value = "geometry")
public class PointDatatype implements Datatype<Point> {

    public final static String NAME = "GEOPOINT";


    public PointDatatype() {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class getJavaClass() {
        return com.vividsolutions.jts.geom.Point.class;
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value) {
        return value == null ? "" : ((com.vividsolutions.jts.geom.Point)value).toText();
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value, Locale locale) {
        return format(value);
    }

    @Nullable
    @Override
    public com.vividsolutions.jts.geom.Point parse(@Nullable String value) throws ParseException {
        return GeometryUtils.wktStringToPoint(value);
    }

    @Nullable
    @Override
    public com.vividsolutions.jts.geom.Point parse(@Nullable String value, Locale locale) throws ParseException {
        return parse(value);
    }
}
