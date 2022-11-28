
public class AssignManager {

    public static boolean canAssign(Solution solution, Operation operation, Resource resource, Time time) {
        return resource.releaseTime() <= time.local(resource)
                && operation.lot().arrival() <= time.global(resource)
                && !operation.resourceReceived(resource)
                && operation.precedentOperations().isEmpty()
                && (time.local(resource) + operation.duration(resource) <= resource.closeTime()
                    || operation.interruptionAllowed(resource))
                && operation.canBeAssigned(solution, resource, time);
    }

    private static boolean willLeapThroughDay(Operation operation, Resource resource) {
        return resource.releaseTime() + operation.duration(resource) >= resource.closeTime();
    }

    public static void assign(Solution solution, Operation operation, Resource resource, Time time) {

        solution.fixate(operation, resource, time);
        operation.markResourceAsReceived(resource);
        resource.moveReleaseTime(operation.duration(resource));
        time.increase(resource, operation.duration(resource));

        if (willLeapThroughDay(operation, resource))
            if (operation.interruptionAllowed(resource))
                time.increase(resource, resource.openTime() + time.hoursInDay() - resource.closeTime());
            else
                throw new RuntimeException("Leap attempt for uninterruptible operation");

        if (OperationManager.operationIsDone(operation))
            OperationManager.removeOperation(operation);

        if (LotManager.lotIsEmpty(operation.lot()))
            LotManager.removeLot(operation.lot());

        if (ResourceManager.resourceIsUnused(resource))
            ResourceManager.removeResource(resource);

    }
}
