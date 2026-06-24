package utils;

import javax.swing.*;
import java.awt.*;

public class MenuBotao extends JButton {

    public MenuBotao(String texto, String caminhoIcone) {

        super(texto);

        // ÍCONE seguro
        if (caminhoIcone != null) {
            java.net.URL url = getClass().getResource(caminhoIcone);
            if (url != null) {
                ImageIcon icone = new ImageIcon(url);
                setIcon(icone);
            }
        }

        // TAMANHO
        setMaximumSize(new Dimension(200, 50));

        // TEXTO
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 18));

        // ESTILO
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setHorizontalAlignment(SwingConstants.LEFT);
        setIconTextGap(15);
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        // COR DO BOTÃO (hover e clique)
        if (getModel().isPressed()) {
            g2.setColor(new Color(56, 142, 60));   // mais escuro ao clicar
        } else if (getModel().isRollover()) {
            g2.setColor(new Color(102, 187, 106)); // mais claro ao passar o mouse
        } else {
            g2.setColor(new Color(76, 175, 80));   // cor padrão
        }

        // BOTÃO ARREDONDADO
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        g2.dispose();

        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // sem borda
    }
}