package algrtm.core.window;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class WindowUtil {
    public static Color BACKGROUND = new Color(30, 30, 30);
    public static Color FOREGROUND = Color.WHITE;
    public static Color ERROR = Color.RED;

    public static ValueInputField decoratedInputField(ValueInputField inputField, String tip, Color background, Color foreground) {
        inputField.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 20));
        inputField.setBackground(background);
        inputField.setForeground(foreground);
        inputField.setBorder(customBorder(foreground));
        inputField.setCaretColor(foreground);
        inputField.setToolTipText(tip);
        return inputField;
    }
    public static ValueInputField decoratedInputField(ValueInputField inputField) {
        return decoratedInputField(inputField, null, BACKGROUND, FOREGROUND);
    }

    public static JLabel decoratedLabel(JLabel label, Color background, Color foreground) {
        label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        label.setBackground(background);
        label.setForeground(foreground);
        return label;
    }
    public static JLabel decoratedLabel(JLabel label) {
        return decoratedLabel(label, BACKGROUND, FOREGROUND);
    }

    public static JButton decoratedButton(JButton button, Color background, Color foreground) {
        button.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        button.setBackground(background);
        button.setForeground(foreground);
        return button;
    }
    public static JButton decoratedButton(JButton button) {
        return decoratedButton(button, BACKGROUND, FOREGROUND);
    }

    public static Border customBorder(int top, int left, int bottom, int right, Color color) {
        return BorderFactory.createMatteBorder(top, left, bottom, right, color);
    }

    public static Border customBorder(Color color) {
        return customBorder(2, 2, 2, 2, color);
    }
}
