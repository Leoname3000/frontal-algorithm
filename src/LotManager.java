import java.util.HashSet;

public class LotManager {

    public static HashSet<OperationLot> allLots = new HashSet<>();

    public static OperationLot createLot(int arrival, int priority) {
        OperationLot lot = new OperationLot(arrival, priority);
        allLots.add(lot);
        return lot;
    }

    public static boolean lotIsEmpty(OperationLot lot) {
        return lot.isEmpty();
    }

    public static void removeLot(OperationLot lot) {
        allLots.remove(lot);
    }
}
