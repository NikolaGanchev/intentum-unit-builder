package builder;

public class LockManager {
    private static final String LOCK_STATE = "const [isLocked%d, setIsLocked%d] = useState(true); \n";
    private int lastLock;
    private StringBuilder locks;

    public LockManager() {
        this.lastLock = 0;
        this.locks = new StringBuilder();
    }

    public int addLock() {
        this.lastLock++;
        this.locks.append(LOCK_STATE.formatted(this.lastLock, this.lastLock));
        return this.lastLock;
    }

    public String getLockState() {
        return locks.toString();
    }
}
