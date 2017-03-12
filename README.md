# sample-gis-datatypes

The application extends standard CUBA datatypes with geo-specific types from the opensource [JTS Topology Suite](https://en.wikipedia.org/wiki/JTS_Topology_Suite):

1. Point
2. LineString
3. Polygon
4. Multi Polygon 

Using JTS your CUBA application amplifies its power with 
[Geometric functions](https://en.wikipedia.org/wiki/JTS_Topology_Suite#Geometric_functions), 
[Spatial structures and algorithms](https://en.wikipedia.org/wiki/JTS_Topology_Suite#Spatial_structures_and_algorithms) 
and [I/O capabilities](https://en.wikipedia.org/wiki/JTS_Topology_Suite#I.2FO_capabilities), 
coming along with the topology suite. 
So, your application has all essential features to become a full-scale geographic information system.

![screenshot](https://github.com/aleksey-stukalov/sample-gis-datatypes/blob/master/screenshots/sample-gis-screenshot.png)

Note: the application uses the [Charts and Maps](https://www.cuba-platform.com/add-ons) premium add-on and requires the commercial subscription.

### Adding a Custom Datatype

General information on datatypes is available in the 
[corresponding section](https://doc.cuba-platform.com/manual-latest/datatype.html) 
of the official platform manual. You can find implementation of geo types 
in [this folder](https://github.com/aleksey-stukalov/sample-gis-datatypes/tree/master/modules/global/src/com/company/cruisesample/gis/datatypes) 
of the project. They are also declared in the [metadata.xml](https://github.com/aleksey-stukalov/sample-gis-datatypes/blob/master/modules/global/src/com/company/cruisesample/metadata.xml) file.

This project demonstrates the process of storing and reading a complex datatype such as geo objects. 
So, while persisting the objects are converted to the [WKT](https://en.wikipedia.org/wiki/Well-known_text) 
format and persisted as text. While reading this text will be parsed back into the objects, 
so that you can work with them normally from the source code. Such behaviour is achieved 
by using [JPA converters](https://github.com/aleksey-stukalov/sample-gis-datatypes/tree/master/modules/global/src/com/company/cruisesample/gis/converters). 
The converters should also be registered in the [persistence.xml](https://github.com/aleksey-stukalov/sample-gis-datatypes/blob/master/modules/global/src/com/company/cruisesample/persistence.xml) file.

To apply the converters for a field it should be annotated 
with the ```@Convert(converter = SomeConverter.class)``` annotation. 
See the ```location``` field of the [```Port```](https://github.com/aleksey-stukalov/sample-gis-datatypes/blob/master/modules/global/src/com/company/cruisesample/entity/Port.java) 
entity in the sample project as an example.

```
public class Port extends StandardEntity {
    
    ...
    
    @Convert(converter = CubaPointWKTConverter.class)
    @MetaProperty(datatype = PointDatatype.NAME, mandatory = true)
    @Column(name = "LOCATION", nullable = false)
    protected Point location;
    
    ...
    
    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
    
    ...
    
}
```

### Known Issues

- Ports reordering in the route editor
- Datatypes are not DB independent. 
To switch to another DB change the ```sqlType``` attribute to match you DBMS in the [metadata.xml](https://github.com/aleksey-stukalov/sample-gis-datatypes/blob/master/modules/global/src/com/company/cruisesample/metadata.xml) file