import java.util.HashSet;

public class Front {

    public Front(HashSet<Front> allFronts, HashSet<OperationLot> allLots, Resource resource, Time time) {
        this.allFronts = allFronts;
        allFronts.add(this);
        this.allLots = allLots;
        this.resource = resource;
        this.time = time;
        this.front = new HashSet<>();
    }

    final private HashSet<Front> allFronts;
    final private HashSet<OperationLot> allLots;
    final private Resource resource;
    final private Time time;
    final private HashSet<Operation> front;

    public Resource resource() {
        return resource;
    }

    public void form(Solution solution) {
        for (OperationLot lot : allLots)
            for (Operation operation : lot.getLot())
                if (operation.requiredResources().contains(resource)
                     && resource.canAssign(solution, operation, time))
                        front.add(operation);
    }

    private void removeSelf() {
        allFronts.remove(this);
    }

    public boolean checkIfEmpty() {
        if (front.isEmpty()) {
            if (resource.removeIfUnused(allLots))
                removeSelf();
            else
                time.increase(resource, 1);
            return true;
        }
        else
            return false;
    }

    private void pick(Solution solution, Operation operation) {
        solution.fixate(operation, resource, time);
        resource.assign(operation, time);
        time.increase(resource, operation.duration(resource));
        front.remove(operation);
    }

    public Operation pickByDuration(Solution solution) {

        if (front.isEmpty())
            throw new RuntimeException("Attempt to order empty front.");

        Operation bestOperation = (Operation) front.toArray()[0];
        int minDuration = bestOperation.duration(resource);

        for (Operation operation : front)
            if (operation.duration(resource) < minDuration) {
                bestOperation = operation;
                minDuration = operation.duration(resource);
            }

        pick(solution, bestOperation);
     git   return bestOperation;
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
