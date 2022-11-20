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

    public boolean canAssign(Solution solution, Operation operation, Time time) {
        return releaseTime <= time.local(this)
                && operation.lot().arrival() <= time.global(this)
                && !operation.resourceReceived(this)
                && operation.precedentOperations().isEmpty()
                && (time.local(this) + operation.duration(this) <= closeTime
                    || operation.interruptionAllowed(this))
                && operation.canBeAssigned(solution, this, time);
    }

    private void removeSelf() {
        allResources.remove(this);
    }
    public boolean removeIfUnused(HashSet<OperationLot> allLots) {
        for (OperationLot lot : allLots)
            for (Operation operation : lot.getLot())
                if (operation.requiredResources().contains(this) && !operation.resourceReceived(this)) {
                    return false;
                }
        removeSelf();
        return true;
    }

    private boolean wasLeapWhileMoveReleaseTime(int moveBy) {
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
        operation.receiveResource(this);
        if (wasLeapWhileMoveReleaseTime(operation.duration(this)))
            time.increase(this, openTime + time.hoursInDay() - closeTime);
    }
}
