import java.util.HashSet;

public class ResourceManager {

    public static HashSet<Resource> allResources = new HashSet<>();

    public static Resource createResource(String name, int openTime, int closeTime) {
        Resource resource = new Resource(name, openTime, closeTime);
        allResources.add(resource);
        return resource;
    }

    public static boolean resourceIsUnused(Resource resource) {
        for (OperationLot lot : LotManager.allLots)
            for (Operation operation : lot.getLot())
                if (operation.requiredResources().contains(resource) && !operation.resourceReceived(resource)) {
                    return false;
                }
        return true;
    }

    public static void removeResource(Resource resource) {
        allResources.remove(resource);
    }
}
