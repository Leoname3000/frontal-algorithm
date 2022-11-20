import java.util.HashMap;
import java.util.HashSet;


public class Main {
    public static void main(String[] args) {


        HashSet<Resource> allResources = new HashSet<>();

        Resource res1 = new Resource("1", allResources, 10, 18);
        Resource res2 = new Resource("2", allResources, 9, 15);

        ResourceGroup group = new ResourceGroup();
        group.add(res1);
        group.add(res2);


        HashSet<OperationLot> allLots = new HashSet<>();

        OperationLot lot1 = new OperationLot(allLots, 8, 0);

        Operation op11 = new Operation("11", lot1, group,
                new HashMap<>() {{ put(res1, 2);     put(res2, 1); }},
                new HashMap<>() {{ put(res1, false); put(res2, false); }});
        Operation op12 = new Operation("12", lot1, group,
                new HashMap<>() {{ put(res1, 3);     put(res2, 3); }},
                new HashMap<>() {{ put(res1, false); put(res2, false); }});
        Operation op13 = new Operation("13", lot1, group,
                new HashMap<>() {{ put(res1, 3);     put(res2, 1); }},
                new HashMap<>() {{ put(res1, false); put(res2, false); }});

        OperationLot lot2 = new OperationLot(allLots, 16, 0);

        Operation op21 = new Operation("21", lot2, group,
                new HashMap<>() {{ put(res1, 2);     put(res2, 2); }},
                new HashMap<>() {{ put(res1, false); put(res2, false); }});
        Operation op22 = new Operation("22", lot2, group,
                new HashMap<>() {{ put(res1, 4);     put(res2, 3); }},
                new HashMap<>() {{ put(res1, false); put(res2, false); }});

        op11.addFollowingOperation(op12);
        op12.addPrecedentOperation(op11);
        op12.addFollowingOperation(op13);
        op13.addPrecedentOperation(op12);
        op21.addFollowingOperation(op22);
        op22.addPrecedentOperation(op21);


        Time time = new Time(allResources, 24);
        Solution solution = new Solution(allLots);
        int step = 0;

        while (!allOperationsDone(allLots)) {
            for (Resource resource : allResources) {
                if (allOperationsDone(allLots))
                    break;
                HashSet<Operation> front = front(solution, allLots, resource, time);
                if (front.isEmpty()) {
                    if (!resource.removeIfUnused(allLots)) {
                        System.out.println("Step " + step + ", time: " + time.global(resource) + ", no operation assigned");
                        time.increase(resource, 1);
                    }
                } else {
                    Operation currentOperation = orderByDuration(front, resource);
                    System.out.println("Step " + step + ", time: " + time.global(resource) + ", operation: " + currentOperation.name());
                    solution.fixate(currentOperation, resource, time);
                    resource.assign(currentOperation, time);
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
                    if (resource.canAssign(solution, operation, time))
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
