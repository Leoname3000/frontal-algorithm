import java.util.HashSet;

public class OperationLot {

    public OperationLot(int arrival, int priority) {
        lot = new HashSet<>();
        this.arrival = arrival;
        this.priority = priority;
    }

    final private HashSet<Operation> lot;
    final private int arrival;
    final private int priority;

    public HashSet<Operation> getLot() {
        return lot;
    }
    public int arrival() {
        return arrival;
    }
    public int priority() {
        return priority;
    }


    public void add(Operation operation) {
        lot.add(operation);
    }
    public void remove(Operation operation) {
        lot.remove(operation);
    }

    public boolean isEmpty() {
        return lot.isEmpty();
    }

    @Override
    public boolean equals(Object object) {
        if (getClass() != object.getClass())
            return false;
        OperationLot evaluated = (OperationLot) object;
        return lot.equals(evaluated.getLot());
    }
}
