package algrtm.parallel;

public class Parallel {
    Task task;
    int taskLength;
//    Thread[] threads;

    public Parallel(Task task, int taskLength) {
        this.task = task;
        this.taskLength = taskLength;
    }
    public Parallel(Task task) {
        this(task, -1);
    }

    public void setTaskLength(int taskLength) {
        this.taskLength = taskLength;
    }

    public int getTaskLength() {
        return taskLength;
    }

    public void start(int threadsCount) {
        if (taskLength <= 0)
            throw new IllegalArgumentException("Task length less or equal 0");

        Thread[] threads = new Thread[threadsCount];
        for (int t = 0; t < threads.length; t++) {
            int startIndex = taskLength * t / threads.length;
            int endIndex = taskLength * (t+1) / threads.length;
            threads[t] = new Thread(() -> {
                for (int index = startIndex; index < endIndex; index++) {
                    task.passIndex(index);
                }
            });
        }

        for (Thread thread : threads)
            thread.start();

        try {
            for (Thread thread : threads)
                thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
