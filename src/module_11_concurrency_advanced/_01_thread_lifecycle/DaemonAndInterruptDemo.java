package module_11_concurrency_advanced._01_thread_lifecycle;

public class DaemonAndInterruptDemo {

    public static void main(String[] args) throws Exception {
        // DAEMON: thread "phục vụ" — JVM thoát khi mọi non-daemon kết thúc.
        // GC, finalizer, JIT compiler đều chạy trong daemon thread.
        Thread daemon = new Thread(() -> {
            while (true) {
                try { Thread.sleep(100); } catch (InterruptedException e) { return; }
                System.out.println("[daemon] tick");
            }
        });
        daemon.setDaemon(true);   // PHẢI gọi TRƯỚC start()
        daemon.start();

        Thread user = new Thread(() -> {
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            System.out.println("[user] done");
        });
        user.start();
        user.join();

        System.out.println("main exits — daemon will be killed by JVM");
        // JVM thoát ngay sau dòng này; daemon không in thêm.

        // INTERRUPT là cách HỢP TÁC để dừng thread.
        Thread worker = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(100);
                    System.out.println("[worker] working...");
                } catch (InterruptedException e) {
                    // Quan trọng: sleep/wait BẮT InterruptedException và CLEAR flag.
                    // Phải set lại flag để vòng while detect.
                    Thread.currentThread().interrupt();
                    System.out.println("[worker] received interrupt");
                }
            }
            System.out.println("[worker] exiting");
        });
        worker.start();
        Thread.sleep(350);
        worker.interrupt();
        worker.join();
    }
}
