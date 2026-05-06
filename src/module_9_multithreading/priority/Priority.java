package module_9_multithreading.priority;

public class Priority implements Runnable {


    Thread thread;
    static boolean stop = false;
    static String currentName;

    public Priority(String name) {
        thread = new Thread(this, name);
        currentName = name;
    }

    @Override
    public void run() {
        System.out.println(thread.getName() + "Starting...");
        int count = 0;
        do {
            count++;
            if (currentName.compareTo(thread.getName()) != 0) {
                currentName = thread.getName();
                System.out.println("Thread is running " + currentName);
            }
        } while (stop == false && count < 10000000);
        stop = true;
        System.out.println(thread.getName() + " finished with count:" + String.valueOf(count));
    }

    public void start() {
        this.thread.start();
    }

    public void join() throws InterruptedException {
        this.thread.join();
    }
}
