import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Solution {

    public Solution(HashSet<OperationLot> allLots) {
        solution = new HashMap<>();
        for (OperationLot lot : allLots)
            for (Operation operation : lot.getLot())
                solution.put(operation, new HashMap<>());
    }

    final private HashMap<Operation, HashMap<Resource, Integer>> solution;

    public void fixate(Operation operation, Resource resource, Time time) {
        solution.get(operation).put(resource, time.global(resource));
    }

    public void print() {
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
}
