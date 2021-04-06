package algrtm.core;

import algrtm.Algrtm;
import algrtm.core.recorder.Recorder;
import algrtm.variable.Flag;

import java.util.ArrayList;

public class Core {
    Algrtm algrtm;

    ArrayList<Integer> updaterates;
    int baseFrequency = 1;
    int updateCounter = 0;

    Flag interruptFlag;
    Thread coreThread;

    public Core(Algrtm algrtm) {
        this.algrtm = algrtm;
        updaterates = new ArrayList<>();
        interruptFlag = new Flag(false);
    }

    // Getters / Setters
    public int getBaseFrequency() {
        return baseFrequency;
    }

    public int getCurrentFrame() {
        return updateCounter;
    }

    public double getElapsedTime() {
        return (double) updateCounter / baseFrequency;
    }

    public int getElapsedTimeInt() {
        return updateCounter / baseFrequency;
    }

    // Core logic
    public void addUpdaterate(int updaterate) {
        updaterates.add(updaterate);
    }

    public void realtimeLoop() {
        // Count all updates per second. Resets every second
        int[] updateCounters = new int[updaterates.size()];

        // Loop delaying stuff
        double updateTick = 1000000000d / baseFrequency;
        double dUpdateTick = 0;
        double dSecTick = 0;
        long prevTime = System.nanoTime();

        // Update ticks relative to base frequency. Used for varying updaterates.
        float[] updateTicks = new float[updaterates.size()];
        for (int i = 0; i < updateTicks.length; i++)
            updateTicks[i] = (float) baseFrequency / updaterates.get(i);

        long updateTimer = 0;

        while (!interruptFlag.isSet()) {
            long crtTime = System.nanoTime();
            dUpdateTick += (crtTime - prevTime) / updateTick;
            dSecTick += (crtTime - prevTime) / 1000000000d;
            prevTime = crtTime;

            // One real second
            if (dSecTick >= 1d) {
                dSecTick--;
                updateTimer = 0;

//                System.out.println(updateCountersDecorator(updateCounters));
                algrtm.updateUPSCounters(updateCounters);
                updateCounters = new int[updaterates.size()];
            }

            // Logic update at base frequency
            if (dUpdateTick >= 1d) {
                dUpdateTick--;
                updateCounter++;

                // Look for other updates that has to happen
                for (int i = 0; i < updateCounters.length; i++) {
                    if (updateTimer - (updateTicks[i] * updateCounters[i]) >= 0f) {
                        updateCounters[i]++;
                        algrtm.updateDynamicLayer(i);
                    }
                }
                updateTimer++;

                algrtm.updateWindow();
            }
        }
    }

    public void recordingLoop(Recorder recorder) {
        // Count all updates per second. Resets every second
        int[] updateCounters = new int[updaterates.size()];

        // Loop delaying stuff
        double updateTick = 1000000000d / baseFrequency;
        double dUpdateTick = 0;
        double dSecTick = 0;
        long prevTime = System.nanoTime();

        // Update ticks relative to base frequency. Used for varying updaterates.
        float[] updateTicks = new float[updaterates.size()];
        for (int i = 0; i < updateTicks.length; i++)
            updateTicks[i] = (float) baseFrequency / updaterates.get(i);

        long updateTimer = 0;

        recorder.start();

        while (!interruptFlag.isSet()) {
            long crtTime = System.nanoTime();
            dUpdateTick += (crtTime - prevTime) / updateTick;
            dSecTick += (crtTime - prevTime) / 1000000000d;
            prevTime = crtTime;

            // One real second
            if (dSecTick >= 1d) {
                dSecTick--;
                updateTimer = 0;

                System.out.println(recorder.getBufferUsage(100) + "% buffer usage");

                algrtm.updateUPSCounters(updateCounters);
                updateCounters = new int[updaterates.size()];
            }

            // Logic update at base frequency
            if (dUpdateTick >= 1d) {
                dUpdateTick--;
                updateCounter++;

                // Look for other updates that has to happen
                for (int i = 0; i < updateCounters.length; i++) {
                    if (updateTimer - (updateTicks[i] * updateCounters[i]) >= 0f) {
                        updateCounters[i]++;
                        algrtm.updateDynamicLayer(i);
                    }
                }
                updateTimer++;

                algrtm.updateWindow();
                recordFrame(recorder);
            }
        }
    }

    private void setup() {
        interruptFlag.set(false);

        // Find base frequency for relative calculations
        baseFrequency = 0;
        for (int rate : updaterates) {
            if (rate > baseFrequency)
                baseFrequency = rate;
        }
    }

    public void start() {
        setup();
        coreThread = new Thread(this::realtimeLoop);
        coreThread.start();
    }

    public void startRecording(Recorder recorder) {
        setup();
//        coreThread = new Thread(() -> fastRecordingLoop(recorder));
        coreThread = new Thread(() -> recordingLoop(recorder));
        coreThread.start();
    }

    public void stop() {
        interruptFlag.set(true);
        if (coreThread != null) {
            if (coreThread.isAlive())
                coreThread.interrupt();
        }
    }

    public void dispose() {
        stop();
        try {
            coreThread.interrupt();
        } catch (Exception ignore) {}
    }

    void recordFrame(Recorder recorder) {
        // Queue frame
        try {
            recorder.queue(algrtm.getFrame(), updateCounter);
        } catch (InterruptedException e) {
            System.err.println("Queue error at frame " + updateCounter);
            System.exit(1);
        }

        // De-queue buffer
        if (recorder.getBufferUsage(100) >= 99d) {
            int bufferSize = recorder.getBufferSize();
            int threads = recorder.getThreadsCount();
            String outFolder = recorder.getOutFolder();

            System.out.println("Buffering...");
            recorder.waitForBuffer(true);
            System.out.println("Resuming");

            recorder = new Recorder(bufferSize, threads, recorder.getMaxThreads(), outFolder);
            recorder.start();
            // TODO recorder.reset();
        }

        // Finishing up
        if (algrtm.getCompletionFlag().isSet()) {
            System.out.println("Waiting for buffer to complete...");
            recorder.waitForBuffer(true);
            System.out.println("Complete");
            stop();
        }
    }
}

/*
// Fast recording (not base frequency dependant)
    public void fastRecordingLoop(Recorder recorder) {
        // Counts all updates per second. Resets every second
        int[] updateCounters = new int[updaterates.size()];
        // Counts all updates per virtual second. Resets every iterations
        int[] counters = new int[updateCounters.length];

        float[] updateTicks = new float[updaterates.size()];
        for (int i = 0; i < updateTicks.length; i++)
            updateTicks[i] = (float) baseFrequency / updaterates.get(i);

        long windowTimer = System.currentTimeMillis();
        long secondsTimer = System.currentTimeMillis();
        long virtualTimer = 0;

        recorder.start();

        while (!interruptFlag.isSet()) {
            // 60 FPS window update (16.66 ms)
            if (System.currentTimeMillis() - windowTimer >= 17) {
                windowTimer = System.currentTimeMillis();
                algrtm.updateWindow();
            }

            // One real second
            if (System.currentTimeMillis() - secondsTimer >= 1000) {
                secondsTimer = System.currentTimeMillis();

                System.out.println(recorder.getBufferUsage(100) + "% buffer usage");
                updateCounters = new int[updaterates.size()];
            }

            // One virtual second
            if (virtualTimer - baseFrequency == 1) {
                virtualTimer = 0;
                for (int i = 0; i < counters.length; i++)
                    updateCounters[i] += counters[i];
                counters = new int[updateCounters.length];
            }

            // Logic update
            for (int i = 0; i < updateCounters.length; i++) {
                if (virtualTimer - (updateTicks[i] * (counters[i]+1)) >= 0f) {
                    counters[i]++;
                    algrtm.updateLayer(i);
                }
            }

            recordFrame(recorder);

            virtualTimer++;
            updateCounter++;
        }
    }
 */
