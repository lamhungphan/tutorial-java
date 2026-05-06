package module_9_multithreading.priority;

public class PriorityDemo {

    public static void main(String[] args) {
        Priority priorityHigher = new Priority("Higher priority");
        Priority priorityLower = new Priority("Lower priority");

        priorityHigher.thread.setPriority(Thread.NORM_PRIORITY + 2);
        priorityLower.thread.setPriority(Thread.MIN_PRIORITY + 1);

        priorityLower.start();
        priorityHigher.start();
        try {
            priorityHigher.join();
            priorityLower.join();
        } catch (InterruptedException e) {
            System.out.println("Main-thread is interrupted");
        }
    }
}
