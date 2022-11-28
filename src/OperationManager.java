import java.util.HashMap;

public class OperationManager {

    public static Operation createOperation(String name,
                                            OperationLot lot,
                                            ResourceGroup requiredResources,
                                            HashMap<Resource, Integer> duration,
                                            HashMap<Resource, Boolean> interruptionAllowed) {

        Operation operation = new Operation(name, lot, requiredResources, duration, interruptionAllowed);
        lot.add(operation);
        return operation;
    }

    public static boolean operationIsDone(Operation operation) {
        for (Resource resource : operation.requiredResources().getGroup())
            if (!operation.resourceReceived(resource))
                return false;
        return true;
    }

    public static void removeOperation(Operation operation) {
        for (Operation following : operation.followingOperations())
            following.removePrecedentOperation(operation);
        operation.lot().remove(operation);
    }

    public static void addRelation(Operation precedent, Operation following) {
        if (precedent.lot() != following.lot())
            throw new RuntimeException("Attempt to relate operations from different lots");
        precedent.addFollowingOperation(following);
        following.addPrecedentOperation(precedent);
    }
}
