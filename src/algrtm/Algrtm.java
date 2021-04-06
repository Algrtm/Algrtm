package algrtm;

import algrtm.core.Core;
import algrtm.core.renderer.Renderer;
import algrtm.core.window.VarPanel;
import algrtm.core.window.WindowUtil;
import algrtm.core.recorder.Recorder;
import algrtm.layers.DynamicLayer;
import algrtm.layers.StaticLayer;
import algrtm.variable.Flag;
import algrtm.variable.Variable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

public class Algrtm {
    Window window;
    Renderer renderer;
    Core core;
    ResetAction resetAction;
    ArrayList<Variable> variables;

    Flag completionFlag;

    // Recording
    Recorder recorder;

    /**
     * Constructs and sets up the canvas and frame.
     * @param canvasWidth canvas width
     * @param canvasHeight canvas height
     */
    public Algrtm(int canvasWidth, int canvasHeight, boolean keepWindowScale) {
        int width, height;
        if (keepWindowScale) {
            width = canvasWidth;
            height = canvasHeight;
        } else {
            width = 1000;
            height = 1000;
            if (canvasWidth != canvasHeight) {
                double aspectRatio = (double) canvasWidth / canvasHeight;
                if (aspectRatio > 1)
                    height = (int) (width / aspectRatio);
                else
                    width = (int) (height / aspectRatio);
            }
        }
        window = new Window(width, height);

        renderer = new Renderer(this, canvasWidth, canvasHeight);
        core = new Core(this);
        variables = new ArrayList<>();
    }
    public Algrtm(int canvasWidth, int canvasHeight) {
        this(canvasWidth, canvasHeight, false);
    }

    /**
     * Returns canvas width.
     * @return canvas width
     */
    public int getWidth() {
        return renderer.getRenderWidth();
    }

    /**
     * Returns canvas height.
     * @return canvas height
     */
    public int getHeight() {
        return renderer.getRenderHeight();
    }

    /**
     * Creates a new transparent layer on the canvas. Layer is drawn and refreshed with set updaterate.
     * @param updaterate layer refreshrate
     * @param dynamicLayer layer object
     */
    public void addDynamicLayer(int updaterate, DynamicLayer dynamicLayer) {
        core.addUpdaterate(updaterate);
        renderer.addDynamicLayer(dynamicLayer);
    }
    public void addStaticLayer(double width, double height, StaticLayer staticLayer) {
        renderer.addStaticLayer(width, height, staticLayer);
    }

    public void show(Flag completionFlag) {
        this.completionFlag = completionFlag;
        window.show(variables);
        renderer.updateAllStaticLayers();
        updateWindow();
    }
    public void show() {
        show(new Flag(false));
    }

    public void setResetAction(ResetAction resetAction) {
        this.resetAction = resetAction;
    }

    public void reset() {
        stopCore();
        renderer.reset();
        if (resetAction == null)
            return;
        resetAction.reset();
    }

    public void stop() {
        window.stop();
        renderer.dispose();
        core.dispose();
    }

    public Flag getCompletionFlag() {
        return completionFlag;
    }

    // Variables panel
    public void addVariable(Variable variable) {
        variables.add(variable);
        variable.addChangeListener((oldValue, newValue) -> {
            renderer.updateAllStaticLayers();
            updateWindow();
        });
    }

    public void addVariables(Variable... variables) {
        for (Variable variable : variables)
            addVariable(variable);
    }

    // Core access
    void startCore() {
        core.start();
    }

    void stopCore() {
        core.stop();
    }

    void disposeCore() {
        core.dispose();
    }

    public int getBaseFrequency() {
        return core.getBaseFrequency();
    }

    public int getFrameCounter() {
        return core.getCurrentFrame();
    }

    public double getElapsedTime() {
        return core.getElapsedTime();
    }

    public int getElapsedTimeInt() {
        return core.getElapsedTimeInt();
    }

    // Renderer access
    public void updateDynamicLayer(int layer) {
        renderer.updateDynamicLayer(layer);
    }

    public void updateStaticLayers() {
        renderer.updateAllStaticLayers();
    }

    public BufferedImage getFrame() {
        return renderer.getImageRender();
    }

    public Graphics2D getLayerGraphics(int layer) {
        return renderer.getLayerGraphics(layer);
    }

    public void setBackground(Color color) {
        renderer.setBackground(color);
    }

    public Color getColor(int layer, int x, int y) {
        return renderer.getColor(layer, x, y);
    }

    public void setColor(int layer, int x, int y, Color color) {
        renderer.setRGB(layer, x, y, color.getRGB());
    }

    // Window access
    public void updateWindow() {
        renderer.updateFrame();
        window.update(renderer.getImageRender());
    }

    public void updateUPSCounters(int[] counters) {
        window.updateUPSCounters(counters);
    }

    public void setDebugText(String text) {
        window.updateDebugText(text);
    }

    // Recording
    public void setRecorder(int bufferSize, int concurrentThreads, int maxThreads, String outFolder) {
        this.recorder = new Recorder(bufferSize, concurrentThreads, maxThreads, outFolder);
    }

    void startRecording() {
        reset();
        core.startRecording(recorder);
    }

    class Window {
        int displayWidth, displayHeight;
        JFrame mainFrame;
        JPanel renderPanel, sidePanel, infoPanel;
        BufferedImage imageBuffer;

        // Info panel
        JLabel UPSLabel, debugLabel;
        JButton startCoreButton, stopCoreButton, resetButton, startRecordingButton;

        Window(int width, int height) {
            this.displayWidth = width;
            this.displayHeight = height;

            imageBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            renderPanel = new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (imageBuffer == null)
                        return;

                    // Drawing final image
                    g.drawImage(imageBuffer, 0, 0, width, height, null);
                }
            };

            renderPanel.setPreferredSize(new Dimension(width, height));

            // Info panel stuff
            UPSLabel = WindowUtil.decoratedLabel(new JLabel(""));
            debugLabel = WindowUtil.decoratedLabel(new JLabel(""));

            startCoreButton = WindowUtil.decoratedButton(new JButton("Start"));
            startCoreButton.addActionListener(e -> {
                if (startCoreButton.isEnabled()) {
                    startCoreButton.setEnabled(false);
                    stopCoreButton.setEnabled(true);
                    startCore();
                }
            });

            stopCoreButton = WindowUtil.decoratedButton(new JButton("Pause"));
            stopCoreButton.setEnabled(false);
            stopCoreButton.addActionListener(e -> {
                if (stopCoreButton.isEnabled()) {
                    stopCoreButton.setEnabled(false);
                    startCoreButton.setEnabled(true);
                    stopCore();
                }
            });

            resetButton = WindowUtil.decoratedButton(new JButton("Reset"));
            resetButton.addActionListener(e -> {
                stopCoreButton.setEnabled(false);
                startCoreButton.setEnabled(true);
                reset();
            });

            startRecordingButton = WindowUtil.decoratedButton(new JButton("Start recording"));
            startRecordingButton.addActionListener(e -> {
                startRecordingButton.setEnabled(false);
                startCoreButton.setEnabled(false);
                stopCoreButton.setEnabled(false);
                resetButton.setEnabled(false);
                startRecording();
                startRecordingButton.setEnabled(true);
                startCoreButton.setEnabled(true);
                resetButton.setEnabled(true);
            });

            // Panels
            infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(WindowUtil.BACKGROUND);
            infoPanel.add(UPSLabel);
            infoPanel.add(debugLabel);
            infoPanel.add(startCoreButton);
            infoPanel.add(stopCoreButton);
            infoPanel.add(resetButton);
            infoPanel.add(startRecordingButton);

            sidePanel = new JPanel();
            sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
            sidePanel.setBackground(WindowUtil.BACKGROUND);
            sidePanel.add(infoPanel);
            sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Frame
            mainFrame = new JFrame("Algrtm");
            mainFrame.setResizable(false);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setBackground(WindowUtil.BACKGROUND);
        }

        void show(ArrayList<Variable> variables) {
            if (recorder == null)
                startRecordingButton.setEnabled(false);

            if (variables.size() != 0)
                addVariables(variables);

            mainFrame.add(renderPanel, BorderLayout.CENTER);
            mainFrame.add(sidePanel, BorderLayout.EAST);
            mainFrame.pack();

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            mainFrame.setLocation(
                    (dim.width - mainFrame.getWidth()) / 2,
                    (dim.height - mainFrame.getHeight()) / 2);

//            SwingUtilities.invokeLater(() -> mainFrame.setVisible(true));
            mainFrame.setVisible(true);
        }

        void stop() {
            mainFrame.dispose();
        }

        void update(BufferedImage imageRender) {
            Graphics2D buffer = imageBuffer.createGraphics();
            buffer.drawImage(imageRender, 0, 0, displayWidth, displayHeight, null);
            renderPanel.repaint();
            buffer.dispose();
        }

        // Info panel
        void updateUPSCounters(int[] UPSCounters) {
            StringBuilder outString = new StringBuilder();
            for (int i = 0; i < UPSCounters.length; i++) {
                outString.append(UPSCounters[i]);
                if (i < UPSCounters.length - 1)
                    outString.append(", ");
            }
            UPSLabel.setText(outString.toString() + " UPS");
        }

        void updateDebugText(String text) {
            debugLabel.setText(text);
        }

        // Variables
        void addVariables(ArrayList<Variable> variables) {
            JPanel varPanel = new JPanel();
            varPanel.setLayout(new BoxLayout(varPanel, BoxLayout.Y_AXIS));
            for (Variable variable : variables)
                varPanel.add(new VarPanel(variable));
            varPanel.add(Box.createVerticalGlue());

            JScrollPane scrollPane = new JScrollPane(varPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.getVerticalScrollBar().setUnitIncrement(8);
            scrollPane.setPreferredSize(new Dimension(300, 800));
            sidePanel.add(scrollPane);
        }
    }
}
