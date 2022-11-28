import java.util.HashSet;

public class FrontManager {

    public static HashSet<Front> allFronts = new HashSet<>();

    public static void generateAllFronts(Time time) {
        for (Resource resource : ResourceManager.allResources)
            createFront(resource, time);
    }

    public static Front createFront(Resource resource, Time time) {
        Front front = new Front(resource, time);
        allFronts.add(front);
        return front;
    }

    public static boolean frontIsEmpty(Front front) {
        return front.isEmpty();
    }

    public static void removeFront(Front front) {
        allFronts.remove(front);
    }
}
