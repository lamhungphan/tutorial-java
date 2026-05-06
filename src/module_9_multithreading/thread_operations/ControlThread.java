package module_9_multithreading.thread_operations;

public class ControlThread implements Runnable {

    private Thread thread;
    private boolean paused;
    private boolean stopped;

    public ControlThread(String name) {
        this.thread = new Thread(this, name);
        this.paused = false;
        this.stopped = false;
    }
    public String getName(){
        return this.thread.getName();
    }

    public void start() {
        if (!this.thread.isAlive()) {
            this.thread.start();
        }
    }

    public void setPriority(int level) {
        this.thread.setPriority(level);
    }

    public void join() throws InterruptedException {
        this.thread.join();
    }

    public void pause() {
        this.paused = true;
    }

    public void resumse() {
        this.paused = false;
        notify();
    }

    public void stop() {
        this.stopped = true;
        this.paused = false;
        notify();
    }

    @Override
    public void run() {

        System.out.println("Luong " + thread.getName() + " bat dau chay");

        try {
            for (int i = 0; i < 1000; i++) {
                System.out.print(i + " ");
                if ((i % 10) == 0) {
                    System.out.println("");
                    Thread.sleep(200);
                }
                synchronized (this) {
                    while (paused) {
                        this.wait();
                    }
                    if (stopped) {
                        break;
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Luong da bi ngat");
        }
    }
}
