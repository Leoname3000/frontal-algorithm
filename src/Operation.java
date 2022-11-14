import java.util.HashMap;
import java.util.HashSet;

public class Operation {

    public Operation(String name,
                     OperationLot lot,
                     ResourceGroup requiredResources,
                     HashMap<Resource, Integer> duration,
                     HashMap<Resource, Boolean> interruptionAllowed)
    {
        this.name = name;
        this.lot = lot;
        lot.add(this);

        this.requiredResources = requiredResources;
        this.resourceReceived = new HashMap<>();
        for (Resource resource : requiredResources.getGroup())
            resourceReceived.put(resource, false);

        this.availableTime = lot.arrival();
        this.duration = duration;
        this.interruptionAllowed = interruptionAllowed;
    }

    final private String name;
    final private OperationLot lot;
    final private ResourceGroup requiredResources;
    final private HashMap<Resource, Boolean> resourceReceived;

    private int availableTime;
    final private HashMap<Resource, Integer> duration;
    final private HashMap<Resource, Boolean> interruptionAllowed;

    final private HashSet<Operation> precedentOperations = new HashSet<>();
    final private HashSet<Operation> followingOperations = new HashSet<>();

    public String name() {
        return name;
    }
    public OperationLot lot() {
        return lot;
    }
    public ResourceGroup requiredResources() {
        return requiredResources;
    }
    public boolean resourceReceived(Resource resource) {
        if (!requiredResources.contains(resource))
            throw new RuntimeException("Attempt to get data for un-required resource.");
        return resourceReceived.get(resource);
    }

    public int availableTime() {
        return availableTime;
    }

    public int duration(Resource resource) {
        return duration.get(resource);
    }

    public boolean interruptionAllowed(Resource resource) {
        return interruptionAllowed.get(resource);
    }

    public HashSet<Operation> precedentOperations() {
        return precedentOperations;
    }
    public HashSet<Operation> followingOperations() {
        return followingOperations;
    }

    public void addPrecedentOperation(Operation operation) {
        precedentOperations.add(operation);
    }
    public void addFollowingOperation(Operation operation) {
        followingOperations.add(operation);
    }
    private void removePrecedentOperation(Operation operation) {
        precedentOperations.remove(operation);
    }

    public void receiveResource(Resource resource) {
        if (!requiredResources.contains(resource))
            throw new RuntimeException("Attempt to receive un-required resource.");
        resourceReceived.put(resource, true);
        availableTime += duration(resource);
        removeIfDone();
    }

    private void removeIfDone() {
        boolean done = true;
        for (Resource resource : requiredResources.getGroup())
            if (!resourceReceived(resource)) {
                done = false;
                break;
            }
        if (done) {
            for (Operation followingOperation : followingOperations())
                followingOperation.removePrecedentOperation(this);
            lot.remove(this);
        }
    }
}
