import java.util.HashSet;

public class ResourceGroup {

    public ResourceGroup() {
        group = new HashSet<>();
    }

    final private HashSet<Resource> group;

    public HashSet<Resource> getGroup() {
        return group;
    }

    public void add(Resource resource) {
        group.add(resource);
    }
    public boolean contains(Resource resource) {
        return group.contains(resource);
    }
}
