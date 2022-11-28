
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

    public void moveReleaseTime(int moveBy) {
        if (releaseTime + moveBy < closeTime) {
            releaseTime += moveBy;
        }
        else {
            int difference = releaseTime + moveBy - closeTime;
            releaseTime = openTime + difference;
        }
    }
}
