import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class Main {
    static int HOURS_IN_DAY = 13;
    public static void main(String[] args) {

        Resource res = new Resource("1", 0, 12);

        ResourceGroup group = new ResourceGroup();
        group.add(res);

        OperationLot lot1 = new OperationLot(0,12,0);

        Operation op11 = new Operation("11", group, lot1,
                new HashMap<Resource, Integer>() {{ put(res, 3); }},
                new HashMap<Resource, Integer>() {{ put(res, 0); }},
                new HashMap<Resource, Integer>() {{ put(res, 3); }},
                new HashMap<Resource, Boolean>() {{ put(res, false); }}
        );
        lot1.add(op11);
        Operation op12 = new Operation("12", group, lot1,
                new HashMap<Resource, Integer>() {{ put(res, 1); }},
                new HashMap<Resource, Integer>() {{ put(res, 9); }},
                new HashMap<Resource, Integer>() {{ put(res, 10); }},
                new HashMap<Resource, Boolean>() {{ put(res, false); }});
        lot1.add(op12);
        Operation op13 = new Operation("13", group, lot1,
                new HashMap<Resource, Integer>() {{ put(res, 2); }},
                new HashMap<Resource, Integer>() {{ put(res, 10); }},
                new HashMap<Resource, Integer>() {{ put(res, 12); }},
                new HashMap<Resource, Boolean>() {{ put(res, false); }});
        lot1.add(op13);

        OperationLot lot2 = new OperationLot(0, 12, 0);

        Operation op21 = new Operation("21", group, lot2,
                new HashMap<Resource, Integer>() {{ put(res, 3); }},
                new HashMap<Resource, Integer>() {{ put(res, 3); }},
                new HashMap<Resource, Integer>() {{ put(res, 6); }},
                new HashMap<Resource, Boolean>() {{ put(res, false); }});
        lot2.add(op21);
        Operation op22 = new Operation("22", group, lot2,
                new HashMap<Resource, Integer>() {{ put(res, 3); }},
                new HashMap<Resource, Integer>() {{ put(res, 6); }},
                new HashMap<Resource, Integer>() {{ put(res, 9); }},
                new HashMap<Resource, Boolean>() {{ put(res, false); }});
        lot2.add(op22);

        op11.addFollowingOperation(op12);
        op12.addPrecedentOperation(op11);
        op12.addFollowingOperation(op13);
        op13.addPrecedentOperation(op12);
        op21.addFollowingOperation(op22);
        op22.addPrecedentOperation(op21);

        HashSet<OperationLot> lots = new HashSet<>();
        lots.add(lot1);
        lots.add(lot2);

        HashSet<Resource> resources = new HashSet<>();
        resources.add(res);

        HashMap<Resource, Integer> globalTime = new HashMap<>();
        for (Resource resource : resources) {
            globalTime.put(resource, 0);
        }

        HashMap<Operation, HashMap<Resource, Integer>> solution = new HashMap<>();

        while (allOperationsDone(lots)) {
            for (Resource resource : resources) {
                int localTime = globalTime.get(resource) % HOURS_IN_DAY;
                HashSet<Operation> front = front(lots, resource, localTime);
                Operation currentOperation = orderByDuration(front, resource);
                assign(currentOperation, resource, localTime);
                solution.put(currentOperation, globalTime);
                globalTime.put(resource, globalTime.get(resource) + currentOperation.duration(resource));
            }
        }

        //System.out.println("[Hello world!] Solution length = " + solution.size());

        for (Map.Entry<Operation, HashMap<Resource, Integer>> entry : solution.entrySet()) {
            Operation operation = entry.getKey();
            HashMap<Resource, Integer> timeMap = entry.getValue();
            System.out.println("Operation " + operation.name() + ": [");
            for (Map.Entry<Resource, Integer> entry1 : timeMap.entrySet()) {
                Resource resource = entry1.getKey();
                int time = entry1.getValue();
                System.out.println("   Res " + resource.name() + ": " + time);
            }
            System.out.println("]");
        }
    }

    static boolean allOperationsDone(HashSet<OperationLot> lots) {
        boolean done = true;
        for (OperationLot lot : lots)
            if (!lot.getLot().isEmpty())
                done = false;
        return done;
    }

    static HashSet<Operation> front(HashSet<OperationLot> lots, Resource resource, int localTime) {
        HashSet<Operation> front = new HashSet<>();
        for (OperationLot lot : lots)
            for (Operation operation : lot.getLot())
                if (operation.requiredResources().getGroup().contains(resource))
                    if (resource.canRunNow(operation, localTime))
                        front.add(operation);
        return front;
    }

    static Operation orderByDuration(HashSet<Operation> front, Resource resource) {
        if (front.isEmpty())
            return null;
        Operation bestOperation = (Operation) front.toArray()[0];
        int minDuration = bestOperation.duration(resource);
        for (Operation operation : front)
            if (operation.duration(resource) < minDuration) {
                bestOperation = operation;
                minDuration = operation.duration(resource);
            }
        return bestOperation;
    }

    static void assign(Operation operation, Resource resource, int localTime) {
        if (localTime + operation.duration(resource) < resource.closeTime()) {
            resource.setReleaseTime(localTime + operation.duration(resource));
        }
        else {
            resource.setReleaseTime(resource.openTime() + operation.duration(resource)
                    - (resource.closeTime() - localTime));
        }
        for (Operation followingOperation : operation.followingOperations())
            followingOperation.removePrecedentOperation(operation);
        operation.lot().remove(operation);
    }
}