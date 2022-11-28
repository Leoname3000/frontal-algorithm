import java.util.HashMap;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {

        Resource res1 = ResourceManager.createResource("1", 10, 18);
        Resource res2 = ResourceManager.createResource("2", 9, 15);

        ResourceGroup group = new ResourceGroup();
        group.add(res1);
        group.add(res2);


        OperationLot lot1 = LotManager.createLot(8, 0);

        Operation op11 = OperationManager.createOperation("11", lot1, group,
                new HashMap<>() {{ put(res1, 2);     put(res2, 1); }},
                new HashMap<>() {{ put(res1, false); put(res2, false); }});
        Operation op12 = OperationManager.createOperation("12", lot1, group,
                new HashMap<>() {{ put(res1, 3);     put(res2, 3); }},
                new HashMap<>() {{ put(res1, false); put(res2, false); }});
        Operation op13 = OperationManager.createOperation("13", lot1, group,
                new HashMap<>() {{ put(res1, 3);     put(res2, 1); }},
                new HashMap<>() {{ put(res1, false); put(res2, false); }});

        OperationLot lot2 = LotManager.createLot(16, 0);

        Operation op21 = OperationManager.createOperation("21", lot2, group,
                new HashMap<>() {{ put(res1, 2);     put(res2, 2); }},
                new HashMap<>() {{ put(res1, false); put(res2, false); }});
        Operation op22 = OperationManager.createOperation("22", lot2, group,
                new HashMap<>() {{ put(res1, 4);     put(res2, 3); }},
                new HashMap<>() {{ put(res1, false); put(res2, false); }});

        OperationManager.addRelation(op11, op12);
        OperationManager.addRelation(op12, op13);
        OperationManager.addRelation(op21, op22);


        Time time = new Time(ResourceManager.allResources, 24);
        Solution solution = new Solution();
        int step = 0;

        while (!allOperationsDone(LotManager.allLots)) {
            for (Resource resource : ResourceManager.allResources) {
                if (allOperationsDone(LotManager.allLots))
                    break;
                HashSet<Operation> front = front(solution, LotManager.allLots, resource, time);
                if (front.isEmpty()) {
                    if (ResourceManager.resourceIsUnused(resource)) {
                        ResourceManager.removeResource(resource);
                    } else {
                        System.out.println("Step " + step + ", time: " + time.global(resource) + ", no operation assigned");
                        time.increase(resource, 1);
                    }
                } else {
                    Operation currentOperation = orderByDuration(front, resource);
                    System.out.println("Step " + step + ", time: " + time.global(resource) + ", operation: " + currentOperation.name());
                    solution.fixate(currentOperation, resource, time);
                    AssignManager.assign(solution, currentOperation, resource, time);
                    time.increase(resource, currentOperation.duration(resource));
                }
                step++;
            }
        }
        solution.print();
    }

    static boolean allOperationsDone(HashSet<OperationLot> allLots) {
        for (OperationLot lot : allLots)
            if (!lot.getLot().isEmpty())
                return false;
        return true;
    }

    static HashSet<Operation> front(Solution solution, HashSet<OperationLot> allLots, Resource resource, Time time) {
        HashSet<Operation> front = new HashSet<>();
        for (OperationLot lot : allLots)
            for (Operation operation : lot.getLot())
                if (operation.requiredResources().contains(resource) && !operation.resourceReceived(resource))
                    if (AssignManager.canAssign(solution, operation, resource, time))
                        front.add(operation);
        return front;
    }

    static Operation orderByDuration(HashSet<Operation> front, Resource resource) {
        if (front.isEmpty())
            throw new RuntimeException("Attempt to order empty front.");
        Operation bestOperation = (Operation) front.toArray()[0];
        int minDuration = bestOperation.duration(resource);
        for (Operation operation : front)
            if (operation.duration(resource) < minDuration) {
                bestOperation = operation;
                minDuration = operation.duration(resource);
            }
        return bestOperation;
    }
}
