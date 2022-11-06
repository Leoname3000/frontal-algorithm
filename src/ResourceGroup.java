import java.util.HashSet;

public class ResourceGroup {

    public ResourceGroup() {
        group = new HashSet<>();
    }
    public ResourceGroup(HashSet<Resource> group) {
        this.group = group;
    }

    final private HashSet<Resource> group;

    public HashSet<Resource> getGroup() {
        return group;
    }

    public void add(Resource resource) {
        group.add(resource);
    }
}
