package rs.raf.schedulemanagerimpl;

import rs.raf.Place;

import java.util.Map;

public class PlaceImpl extends Place {

    public PlaceImpl() {
        super();
    }

    public PlaceImpl(String name, String location, Map<String, Integer> properties) {
        super(name, location, properties);
    }
}
