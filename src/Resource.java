import java.util.HashSet;

public class Resource {

    public Resource(String name, HashSet<Resource> allResources, int openTime, int closeTime) {
        this.name = name;
        this.allResources = allResources;
        allResources.add(this);
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.releaseTime = openTime;
    }

    final private String name;
    HashSet<Resource> allResources;
    final private int openTime;
    final private int closeTime;
    private int releaseTime;

    public String name() {
        return name;
    }
    public int openTime() {
        return openTime;
    }
    public int closeTime() {
        return closeTime;
    }

    public boolean canRunNow(Operation operation, Time time) {
        int canRunAt = Math.max(releaseTime, operation.availableTime());
        if (canRunAt <= time.local(this) && operation.precedentOperations().isEmpty())
            if (time.local(this) + operation.duration(this) <= closeTime
                || operation.interruptionAllowed(this))
                return true;
        return false;
    }

    public boolean isUnused(HashSet<OperationLot> allLots) {
        for (OperationLot lot : allLots)
            for (Operation operation : lot.getLot())
                if (operation.requiredResources().contains(this) && !operation.resourceReceived(this)) {
                    return false;
                }
        return true;
    }

    private boolean moveReleaseTime(int moveBy) {
        if (releaseTime + moveBy < closeTime) {
            releaseTime += moveBy;
            return false;
        }
        else {
            int difference = releaseTime + moveBy - closeTime;
            releaseTime = openTime + difference;
            return true;
        }
    }

    public void assign(Operation operation, Time time) {
        boolean wasLeap = moveReleaseTime(operation.duration(this));
        operation.receiveResource(this);
        if (wasLeap)
            time.increase(this, openTime + time.hoursInDay() - closeTime);
    }
}

