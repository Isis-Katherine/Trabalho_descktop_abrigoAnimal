package utils;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class MenuPainel extends JPanel {

    public MenuPainel() {

        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        GradientPaint gp = new GradientPaint(
                0,
                0,
                new Color(76,175,80),

                0,
                getHeight(),

                new Color(56,142,60)
        );

        g2.setPaint(gp);

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                30,
                30
        );
    }
}