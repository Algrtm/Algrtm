package algrtm.core.recorder;

import algrtm.math.Calc;
import algrtm.variable.Flag;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Recorder {
    int bufferSize;
    String outFolder;
    BlockingQueue<IndexedImage> imageQueue;
    Thread[] bufferingThreads;
    int maxThreads;
    Runnable bufferRunnable;

    boolean INTERRUPT;

    public Recorder(int bufferSize, int concurrentThreads, int maxThreads, String outFolder) {
        this.bufferSize = bufferSize;
        this.outFolder = outFolder;
        imageQueue = new ArrayBlockingQueue<>(bufferSize);
        bufferingThreads = new Thread[concurrentThreads];
        this.maxThreads = maxThreads;

        bufferRunnable = () -> {
            IndexedImage indexedImage;
            try {
                while (!INTERRUPT) {
                    if ((indexedImage = imageQueue.poll()) != null)
                        ImageIO.write(indexedImage.image, "png", new File(outFolder + File.separator + indexedImage.index + ".png"));
                }
            } catch(IOException e) {
                System.err.println("Writing error");
                System.exit(1);
            }
            Thread.currentThread().interrupt();
        };

        for (int t = 0; t < bufferingThreads.length; t++)
            bufferingThreads[t] = new Thread(bufferRunnable);
    }

    public void start() {
        new File(outFolder).mkdirs();
        INTERRUPT = false;
        for (Thread thread : bufferingThreads)
            thread.start();
    }

    public void stop() {
        INTERRUPT = true;
        boolean threadsComplete;
        do {
            threadsComplete = true;
            for (Thread thread : bufferingThreads) {
                if (thread.isAlive()) {
                    threadsComplete = false;
                    break;
                }
            }
        } while (!threadsComplete);
    }

    public void waitForBuffer(boolean log) {
        // Stop current threads
        stop();

        // Expand writing threads
        bufferingThreads = new Thread[maxThreads];
        for (int t = 0; t < bufferingThreads.length; t++)
            bufferingThreads[t] = new Thread(bufferRunnable);

        // Start new threads
        start();

        // Wait
        long timer = System.currentTimeMillis();
        while (!imageQueue.isEmpty()) {
            if (System.currentTimeMillis() - timer >= 1000) {
                timer = System.currentTimeMillis();
                if (log)
                    System.out.println(getBufferUsage(100) + "% left");
            }
        }

        // Complete
        stop();
    }

    public void reset() {
        //TODO
    }

    // Frame buffer
    public void queue(BufferedImage image, int index) throws InterruptedException {
        imageQueue.put(new IndexedImage(image, index));
    }

    public int getBufferSize() {
        return bufferSize;
    }

    // Misc
    public int getThreadsCount() {
        return bufferingThreads.length;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public String getOutFolder() {
        return outFolder;
    }

    public double getBufferUsage(int precision) {
        return Calc.percent(imageQueue.size(), bufferSize, precision);
    }

}
