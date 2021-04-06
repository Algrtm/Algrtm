package algrtm.core.window;

import algrtm.math.Calc;
import algrtm.variable.Variable;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

public class VarPanel extends JPanel {
    JLabel varNameLabel;
    ValueInputField valueInputField, minValueInputField, maxValueInputField;
    JSlider slider;

    int sliderResolution = 100;

    /*  Layout setup
     *  0|       Name         |
     *  1|       Value        |
     *  2| Min | Slider | Max |
     *  3| -----Separator-----|
     *           * * *
     */
    public VarPanel(Variable variable) {
        this.setBackground(WindowUtil.BACKGROUND);
        this.setForeground(WindowUtil.FOREGROUND);
        UIManager.put("ToolTip.font", new FontUIResource(Font.MONOSPACED, Font.BOLD, 14));
        UIManager.put("ToolTip.background", Color.WHITE);
        UIManager.put("ToolTip.foreground", Color.BLACK);

        // Name
        varNameLabel = WindowUtil.decoratedLabel(new JLabel(variable.getName()));

        // Current value
        valueInputField = WindowUtil.decoratedInputField(
                new ValueInputField(
                        variable.getValue(),
                        variable::setValue));

        // Minimum slider value
        minValueInputField = WindowUtil.decoratedInputField(
                new ValueInputField(
                        variable.getMin(),
                        newValue -> variable.setValue(
                                Math.max(variable.getValue(), newValue))));

        // Maximum slider value
        maxValueInputField = WindowUtil.decoratedInputField(
                new ValueInputField(
                        variable.getMax(),
                        newValue -> variable.setValue(
                                Math.min(variable.getValue(), newValue))));

        // Slider
        slider = new JSlider(
                0, sliderResolution,
                (int) Calc.mapClamp(
                        variable.getValue(),
                        minValueInputField.getValue(), maxValueInputField.getValue(),
                        0, sliderResolution)
        );
        slider.addChangeListener(e -> {
            variable.setValue(Calc.map(
                    slider.getValue(),
                    slider.getMinimum(), slider.getMaximum(),
                    minValueInputField.getValue(), maxValueInputField.getValue()));
            valueInputField.setValue(variable.getValue());
        });
        slider.setBackground(WindowUtil.BACKGROUND);

        // Variable change listener
        variable.addChangeListener((oldValue, newValue) -> {
            valueInputField.setValue(newValue);
            if (newValue < minValueInputField.getValue())
                minValueInputField.setValue(newValue);
            if (newValue > maxValueInputField.getValue())
                maxValueInputField.setValue(newValue);
//            slider.setValue((int) Calc.map(
//                    newValue,
//                    minValueInputField.getValue(), maxValueInputField.getValue(),
//                    slider.getMinimum(), slider.getMaximum()));
        });

        // Separator
        JSeparator separator = new JSeparator();
        separator.setBorder(WindowUtil.customBorder(WindowUtil.FOREGROUND));

        //Labels
        JLabel nameLabel = WindowUtil.decoratedLabel(new JLabel("Name:"));
        JLabel valueLabel = WindowUtil.decoratedLabel(new JLabel("Value:"));
        JLabel minLabel = WindowUtil.decoratedLabel(new JLabel("Min value:"));
        JLabel maxLabel = WindowUtil.decoratedLabel(new JLabel("Max value:"));

        JLabel TEST = WindowUtil.decoratedLabel(new JLabel("TEST"));

        // Adding to panel
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(nameLabel)
                                .addComponent(valueLabel)
                                .addComponent(minLabel)
                                .addComponent(maxLabel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(varNameLabel)
                                .addComponent(valueInputField)
                                .addComponent(minValueInputField)
                                .addComponent(maxValueInputField)))
                .addComponent(slider)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(nameLabel)
                                        .addComponent(varNameLabel))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(valueLabel)
                                        .addComponent(valueInputField))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(minLabel)
                                        .addComponent(minValueInputField))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(maxLabel)
                                        .addComponent(maxValueInputField))))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(slider))
        );
    }
}
