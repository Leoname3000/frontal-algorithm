import java.util.ArrayList;

public class OperationLot {

    public OperationLot(int startMoment, int limitMoment, int priority) {
        lot = new ArrayList<>();
        this.startMoment = startMoment;
        this.limitMoment = limitMoment;
        this.priority = priority;
    }
    public OperationLot(ArrayList<Operation> lot, int startMoment, int limitMoment, int priority) {
        this.lot = lot;
        this.startMoment = startMoment;
        this.limitMoment = limitMoment;
        this.priority = priority;
    }

    final private ArrayList<Operation> lot;

    final private int startMoment;
    final private int limitMoment;
    final private int priority;

    public ArrayList<Operation> getLot() {
        return lot;
    }

    public void add(Operation operation) {
        lot.add(operation);
    }
    public void remove(Operation operation) {
        lot.remove(operation);
    }
}
