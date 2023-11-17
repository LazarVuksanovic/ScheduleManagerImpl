package rs.raf.schedulemanagerimpl;

import rs.raf.Place;

import java.util.Map;

public class PlaceImpl extends Place {

    public PlaceImpl() {
        super();
    }

    public PlaceImpl(String name){
        super(name);
    }

    public PlaceImpl(String name, String location){
        super();
        super.setName(name);
        super.setLocation(location);
    }

    public PlaceImpl(String name, String location, Map<String, Integer> properties) {
        super(name, location, properties);
    }

    @Override
    public String toString() {
        return "PlaceImpl{" +
                "name=" + super.getName() +
                "location=" + super.getLocation() +
                "properties=" + super.getProperties() +
                "}\n";
    }


}
