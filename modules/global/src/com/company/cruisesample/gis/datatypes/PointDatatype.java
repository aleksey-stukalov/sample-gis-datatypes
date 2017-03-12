package com.company.cruisesample.gis.datatypes;

import com.company.cruisesample.gis.utils.GeometryUtils;
import com.haulmont.chile.core.datatypes.Datatype;
import com.vividsolutions.jts.geom.Point;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by aleksey on 10/03/2017.
 */
public class PointDatatype implements Datatype<Point> {

    public final static String NAME = "point";


    public PointDatatype() {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class getJavaClass() {
        return Point.class;
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value) {
        return value == null ? "" : ((Point)value).toText();
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value, Locale locale) {
        return format(value);
    }

    @Nullable
    @Override
    public Point parse(@Nullable String value) throws ParseException {
        return GeometryUtils.wktStringToPoint(value);
    }

    @Nullable
    @Override
    public Point parse(@Nullable String value, Locale locale) throws ParseException {
        return parse(value);
    }
}
