package algrtm.core.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ValueInputField extends JTextField {
    double value;
    Color valid, invalid;

    public ValueInputField(double value, ValueInputListener valueListener, Color valid, Color invalid) {
        this.value = value;
//        String valueText = String.valueOf(value);
//        setText(valueText.substring(0, Math.min(9, valueText.length())));
        setText(String.valueOf(value));
        this.valid = valid;
        this.invalid = invalid;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (validValue(getText())) {
                        double value = setValue(textToValue(getText()));
                        valueListener.valueChanged(value);
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                dynamicError(getText() + e.getKeyChar());
            }
        });
    }
    public ValueInputField(double value, ValueInputListener valueListener) {
        this(value, valueListener, WindowUtil.FOREGROUND, WindowUtil.ERROR);
    }

    // Getters / Setters
    public double getValue() {
        return value;
    }

    public double setValue(double value) {
        this.value = value;
        setText(String.valueOf(value));
        return value;
    }

    // Misc
    public boolean inputIsValid() {
        return validValue(getText());
    }

    // Validating and error checking
    private boolean validValue(String text) {
        boolean valid = true;
        try {
            Double.parseDouble(text);
        } catch (NumberFormatException exception) {
            valid = false;
        }
        return valid;
    }

    private double textToValue(String text) {
        double value = Double.NaN;
        try {
            value = Double.parseDouble(text);
        } catch (NumberFormatException ignore) {
        }
        return value;
    }

    private void dynamicError(String text) {
        if (validValue(text))
            setBorder(WindowUtil.customBorder(valid));
        else
            setBorder(WindowUtil.customBorder(invalid));
    }
}
