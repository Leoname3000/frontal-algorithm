import java.util.HashMap;
import java.util.HashSet;

public class Time {

    public Time(HashSet<Resource> allResources, int hoursInDay) {
        this.hoursInDay = hoursInDay;
        this.globalTimeMap = new HashMap<>();
        for(Resource resource : allResources)
            globalTimeMap.put(resource, resource.openTime());
    }

    final private int hoursInDay;
    final private HashMap<Resource, Integer> globalTimeMap;

    public int hoursInDay() {
        return hoursInDay;
    }
    public int global(Resource resource) {
        return globalTimeMap.get(resource);
    }
    public int local(Resource resource) {
        return globalTimeMap.get(resource) % hoursInDay;
    }

    public void increase(Resource resource, int increaseBy) {
        globalTimeMap.put(resource, globalTimeMap.get(resource) + increaseBy);
    }
}
