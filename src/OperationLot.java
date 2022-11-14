import java.util.HashSet;

public class OperationLot {

    public OperationLot(HashSet<OperationLot> allLots, int arrival, int priority) {
        allLots.add(this);
        lot = new HashSet<>();
        this.arrival = arrival;
        this.priority = priority;
    }

    final private HashSet<Operation> lot;

    final private int arrival;
    final private int priority;

    public int arrival() {
        return arrival;
    }
    public int priority() {
        return priority;
    }

    public HashSet<Operation> getLot() {
        return lot;
    }

    public void add(Operation operation) {
        lot.add(operation);
    }
    public void remove(Operation operation) {
        lot.remove(operation);
    }
}
