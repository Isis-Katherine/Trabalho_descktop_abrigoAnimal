package utils;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class BotaoEditar extends JButton {

    public BotaoEditar() {

        super();
        // Carrega e redimensiona o ícone de forma segura
        java.net.URL url = getClass().getResource("/icons/editar.png");
        if (url != null) {
            ImageIcon icone = new ImageIcon(url);
            Image img = icone.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            setIcon(new ImageIcon(img));
        } else {
            setText("✏");
        }

        setFont(new Font("Segoe UI", Font.BOLD, 11));
        setForeground(Color.WHITE);
        setBackground(new Color(25, 118, 210)); // azul
        setBorder(new EmptyBorder(5, 10, 5, 10));
        setFocusPainted(false);
        setOpaque(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}