package module_2_core._51_enum_advanced;

public class EnumSingletonDemo {

    // Effective Java Item 3: prefer ENUM SINGLETON
    //  - Thread-safe miễn phí (class init đảm bảo)
    //  - Serialization-safe (deserialize không sinh instance mới)
    //  - Reflection cũng không phá được (newInstance() throws)

    public enum DBPool {
        INSTANCE;

        private int activeConnections = 0;

        public synchronized int acquire() { return ++activeConnections; }
        public synchronized int release() { return --activeConnections; }
        public int active() { return activeConnections; }
    }

    public static void main(String[] args) {
        DBPool.INSTANCE.acquire();
        DBPool.INSTANCE.acquire();
        System.out.println("active = " + DBPool.INSTANCE.active());
        DBPool.INSTANCE.release();
        System.out.println("active = " + DBPool.INSTANCE.active());
    }
}
