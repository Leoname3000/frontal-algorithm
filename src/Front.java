import java.util.HashSet;

public class Front {

    public Front(Resource resource, Time time) {
        this.resource = resource;
        this.time = time;
        this.front = new HashSet<>();
    }
    final private Resource resource;
    final private Time time;
    final private HashSet<Operation> front;

    public Resource resource() {
        return resource;
    }

    public void update(Solution solution) {
        for (OperationLot lot : LotManager.allLots)
            for (Operation operation : lot.getLot())
                if (operation.requiredResources().contains(resource)
                     && AssignManager.canAssign(solution, operation, resource, time))
                        front.add(operation);
    }

    public Operation chooseByDuration() {

        if (front.isEmpty())
            throw new RuntimeException("Attempt to order empty front.");

        Operation bestOperation = (Operation) front.toArray()[0];
        int minDuration = bestOperation.duration(resource);

        for (Operation operation : front)
            if (operation.duration(resource) < minDuration) {
                bestOperation = operation;
                minDuration = operation.duration(resource);
            }
        front.remove(bestOperation);
        return bestOperation;
    }

    public boolean isEmpty() {
        return front.isEmpty();
    }

    @Override
    public String toString() {
        String reportString = "Front for resource " + resource.name() + " at time " + time.global(resource) + ": ( ";
        for (Operation operation : front)
            reportString += operation.name() + " ";
        reportString += ")";
        return reportString;
    }
}
