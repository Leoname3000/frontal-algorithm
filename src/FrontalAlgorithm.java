import java.util.HashSet;

public class FrontalAlgorithm {

    private static boolean allOperationsDone(HashSet<OperationLot> allLots) {
        for (OperationLot lot : allLots)
            if (!lot.getLot().isEmpty())
                return false;
        return true;
    }

    public static Solution run(HashSet<OperationLot> allLots, HashSet<Resource> allResources, Time time) {

        Solution solution = new Solution(allLots);

        HashSet<Front> allFronts = new HashSet<>();
        for (Resource resource : allResources)
            allFronts.add(new Front(allFronts, allLots, resource, time));

        while (!allOperationsDone(allLots))
            for (Front front : allFronts) {
                if (allOperationsDone(allLots))
                    break;
                front.form(solution);
                System.out.println(front);
                if (!front.checkIfEmpty())
                    front.pickByDuration(solution);
            }
        return solution;
    }
}
