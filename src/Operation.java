import java.util.HashMap;
import java.util.HashSet;

public class Operation {

    public Operation(String name,
                     ResourceGroup requiredResources,
                     OperationLot lot,
                     HashMap<Resource, Integer> duration,
                     HashMap<Resource, Integer> arrival,
                     HashMap<Resource, Integer> deadline,
                     HashMap<Resource, Boolean> interruptionAllowed)
    {
        this.name = name;
        this.requiredResources = requiredResources;
        this.lot = lot;

        this.duration = duration;
        this.arrival = arrival;
        this.deadline = deadline;
        this.interruptionAllowed = interruptionAllowed;
    }
    final private String name;
    final private ResourceGroup requiredResources;
    final private OperationLot lot;

    final private HashMap<Resource, Integer> duration;
    final private HashMap<Resource, Integer> arrival;
    final private HashMap<Resource, Integer> deadline;
    final private HashMap<Resource, Boolean> interruptionAllowed;

    final private HashSet<Operation> precedentOperations = new HashSet<>();
    final private HashSet<Operation> followingOperations = new HashSet<>();

    public String name() {
        return name;
    }
    public ResourceGroup requiredResources() {
        return requiredResources;
    }
    public OperationLot lot() {
        return lot;
    }

    public int arrival(Resource resource) {
        return arrival.get(resource);
    }
    public int duration(Resource resource) {
        return duration.get(resource);
    }
    public int deadline(Resource resource) {
        return deadline.get(resource);
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
    public void removePrecedentOperation(Operation operation) {
        precedentOperations.remove(operation);
    }
}
