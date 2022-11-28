import java.util.HashSet;

public class FrontalAlgorithm {

    private static boolean allOperationsDone(HashSet<OperationLot> allLots) {
        for (OperationLot lot : allLots)
            if (!lot.getLot().isEmpty())
                return false;
        return true;
    }

    public static Solution run(HashSet<OperationLot> allLots, HashSet<Resource> allResources, Time time) {

        LotManager.allLots = allLots;
        ResourceManager.allResources = allResources;
        FrontManager.generateAllFronts(time);
        Solution solution = new Solution();

        while (!allOperationsDone(allLots))
            for (Front front : FrontManager.allFronts) {
                if (allOperationsDone(allLots))
                    break;
                front.update(solution);
                System.out.println(front);
                if (FrontManager.frontIsEmpty(front))
                    if (ResourceManager.resourceIsUnused(front.resource()))
                        FrontManager.removeFront(front);
                    else
                        time.increase(front.resource(), 1);
                else {
                    Operation bestOperation = front.chooseByDuration();
                    AssignManager.assign(solution, bestOperation, front.resource(), time);
                }

            }
        return solution;
    }
}
