import java.util.ArrayList;
import java.util.List;

public class VcRequestQueue {

    private final List<PendingRequest> pending = new ArrayList<>();

    public synchronized void add(PendingRequest request) {
        pending.add(request);
    }

    public synchronized void remove(PendingRequest request) {
        pending.remove(request);
    }

    public synchronized List<PendingRequest> getAll() {
        return new ArrayList<>(pending);
    }
}
