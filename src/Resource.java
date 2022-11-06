public class Resource {

    public Resource(String name, int openTime, int closeTime) {
        this.name = name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.releaseTime = openTime;
    }

    final private String name;
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
    public int releaseTime() {
        return releaseTime;
    }

    private int canRunAt(Operation operation) {
        return Math.max(releaseTime, operation.arrival(this));
    }
    public boolean canRunNow(Operation operation, int localTime) {
        int canRunAt = canRunAt(operation);
        if (canRunAt <= localTime && operation.precedentOperations().isEmpty())
            if (localTime + operation.duration(this) <= operation.deadline(this)
                && localTime + operation.duration(this) <= closeTime
                || operation.interruptionAllowed(this))
                return true;
        return false;
    }

    public void setReleaseTime(int localTime) {
        if (localTime == closeTime)
            localTime = openTime;
        if (releaseTime <= localTime && localTime < closeTime) {
            releaseTime = localTime;
        } else {
            throw new RuntimeException("ERROR! Attempt to set incorrect release time.");
        }
    }
}

