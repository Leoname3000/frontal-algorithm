import java.util.HashMap;
import java.util.Map;

public class Solution {

    public Solution() {
        solution = new HashMap<>();
        for (OperationLot lot : LotManager.allLots)
            for (Operation operation : lot.getLot())
                solution.put(operation, new HashMap<>());
    }

    public Solution(HashMap<Operation, HashMap<Resource, Integer>> solution) {
        this.solution = solution;
    }

    final private HashMap<Operation, HashMap<Resource, Integer>> solution;

    public HashMap<Operation, HashMap<Resource, Integer>> getSolution() {
        return solution;
    }

    public HashMap<Resource, Integer> assignmentMap(Operation operation) {
        return solution.get(operation);
    }

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

    @Override
    public boolean equals(Object object) {
        if (getClass() != object.getClass())
            return false;
        Solution evaluated = (Solution) object;
        return solution.equals(evaluated.getSolution());
    }
}
