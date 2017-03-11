package com.company.cruisesample.gis.datatypes;

import com.company.cruisesample.gis.utils.GeometryUtils;
import com.haulmont.chile.core.datatypes.Datatype;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.dom4j.Element;

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
public class MultiPolygonDatatype implements Datatype<MultiPolygon> {
    public final static String NAME = "multipolygon";

    public MultiPolygonDatatype(Element element) {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class getJavaClass() {
        return Polygon.class;
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value) {
        if (value == null) {
            return "";
        } else {
            return ((MultiPolygon) value).toText();
        }
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value, Locale locale) {
        return format(value);
    }

    @Nullable
    @Override
    public MultiPolygon parse(@Nullable String value) throws ParseException {
        return GeometryUtils.wktStringToMultiPolygon(value);
    }

    @Nullable
    @Override
    public MultiPolygon parse(@Nullable String value, Locale locale) throws ParseException {
        return GeometryUtils.wktStringToMultiPolygon(value);
    }

    @Nullable
    @Override
    public MultiPolygon read(ResultSet resultSet, int index) throws SQLException {
        return null;
    }

    @Override
    public void write(PreparedStatement statement, int index, @Nullable Object value) throws SQLException {

    }

    @Override
    public int getSqlType() {
        return Types.OTHER;
    }
}
