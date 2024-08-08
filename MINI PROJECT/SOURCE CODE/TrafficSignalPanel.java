package com.mycompany.smarttrafficsignal;

import java.awt.*;
import javax.swing.*;

public class TrafficSignalPanel extends JPanel {
    private String lightColor = "OFF";
    private String intersectionName;
    private JLabel label;

    public TrafficSignalPanel(TrafficData data) {
        this.intersectionName = data.getIntersectionName();
        this.label = new JLabel(intersectionName + " - " + lightColor);
        setPreferredSize(new Dimension(200, 150));
        setLayout(new BorderLayout());
        add(label, BorderLayout.NORTH);
    }

    public void setLight(String color) {
        this.lightColor = color;
        label.setText(intersectionName + " - " + lightColor);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int lightDiameter = 40;
        int lightX = getWidth() / 2 - lightDiameter / 2;
        int lightY = 50;

        // Draw the border of the traffic light
        g2d.setColor(Color.BLACK);
        g2d.fillRoundRect(lightX - 10, lightY - 10, lightDiameter + 20, lightDiameter * 3 + 40, 20, 20);

        drawLight(g2d, lightX, lightY, lightDiameter, "RED");
        drawLight(g2d, lightX, lightY + 50, lightDiameter, "YELLOW");
        drawLight(g2d, lightX, lightY + 100, lightDiameter, "GREEN");
    }

    private void drawLight(Graphics2D g2d, int x, int y, int diameter, String color) {
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x, y, diameter, diameter);

        if ("RED".equals(lightColor) && color.equals("RED")) {
            g2d.setColor(Color.RED);
        } else if ("YELLOW".equals(lightColor) && color.equals("YELLOW")) {
            g2d.setColor(Color.YELLOW);
        } else if ("GREEN".equals(lightColor) && color.equals("GREEN")) {
            g2d.setColor(Color.GREEN);
        } else {
            g2d.setColor(Color.GRAY); // Default color for lights that are off
        }

        g2d.fillOval(x + 2, y + 2, diameter - 4, diameter - 4); // Draw a smaller circle to simulate the light
    }
}
