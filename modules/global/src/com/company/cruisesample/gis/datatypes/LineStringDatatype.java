package com.company.cruisesample.gis.datatypes;

import com.company.cruisesample.gis.utils.GeometryUtils;
import com.haulmont.chile.core.annotations.Ddl;
import com.haulmont.chile.core.datatypes.Datatype;
import com.vividsolutions.jts.geom.LineString;
import org.dom4j.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.Locale;
/**
 * Created by aleksey on 10/03/2017.
 */
@Ddl(dbms = "postgres", value = "geometry")
public class LineStringDatatype implements Datatype<LineString> {

    public final static String NAME = "GEOLINESTRING";

    public LineStringDatatype(Element element) {
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Class getJavaClass() {
        return LineString.class;
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value) {
        if (value == null) {
            return "";
        } else {
            return ((LineString) value).toText();
        }
    }

    @Nonnull
    @Override
    public String format(@Nullable Object value, Locale locale) {
        return format(value);
    }

    @Nullable
    @Override
    public LineString parse(@Nullable String value) throws ParseException {
        return GeometryUtils.wktStringToLineString(value);
    }

    @Nullable
    @Override
    public LineString parse(@Nullable String value, Locale locale) throws ParseException {
        return parse(value);
    }

}
