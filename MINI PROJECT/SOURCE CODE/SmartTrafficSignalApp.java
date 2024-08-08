package com.mycompany.smarttrafficsignal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SmartTrafficSignalApp extends JFrame {
    private JTextField vehicleCountField;
    private JTextField averageSpeedField;
    private JTextField intersectionNameField;
    private JPanel signalPanel;
    private JTextArea reportArea;
    private List<TrafficData> trafficDataList;

    public SmartTrafficSignalApp() {
        setTitle("Smart Traffic Signal System");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        trafficDataList = new ArrayList<>();

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Input Traffic Data"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;

        inputPanel.add(new JLabel("Vehicle Count:"), gbc);
        vehicleCountField = new JTextField(10);
        gbc.gridx = 1;
        inputPanel.add(vehicleCountField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Average Speed:"), gbc);
        averageSpeedField = new JTextField(10);
        gbc.gridx = 1;
        inputPanel.add(averageSpeedField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Intersection Name:"), gbc);
        intersectionNameField = new JTextField(10);
        gbc.gridx = 1;
        inputPanel.add(intersectionNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton addButton = new JButton("Add Data");
        inputPanel.add(addButton, gbc);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTrafficData();
            }
        });

        gbc.gridy = 4;
        JButton generateSignalButton = new JButton("Generate Traffic Signal");
        inputPanel.add(generateSignalButton, gbc);
        generateSignalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateTrafficSignal();
            }
        });

        gbc.gridy = 5;
        JButton reportButton = new JButton("Generate Report");
        inputPanel.add(reportButton, gbc);
        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });

        add(inputPanel, BorderLayout.NORTH);

        // Signal panel
        signalPanel = new JPanel(new GridLayout(0, 2));
        signalPanel.setBorder(BorderFactory.createTitledBorder("Traffic Signals"));
        add(signalPanel, BorderLayout.CENTER);

        // Report area
        reportArea = new JTextArea(10, 30);
        reportArea.setEditable(false);
        add(new JScrollPane(reportArea), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addTrafficData() {
        try {
            int vehicleCount = Integer.parseInt(vehicleCountField.getText().trim());
            double averageSpeed = Double.parseDouble(averageSpeedField.getText().trim());
            String intersectionName = intersectionNameField.getText().trim();

            TrafficData data = new TrafficData(vehicleCount, averageSpeed, intersectionName);
            trafficDataList.add(data);

            // Clear input fields
            vehicleCountField.setText("");
            averageSpeedField.setText("");
            intersectionNameField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for vehicle count and average speed.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateTrafficSignal() {
        signalPanel.removeAll();

        if (trafficDataList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No traffic data available.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<TrafficData> sortedDataList = new ArrayList<>(trafficDataList);
        sortedDataList.sort((d1, d2) -> Integer.compare(d2.getVehicleCount(), d1.getVehicleCount()));

        TrafficData greenIntersection = null;
        TrafficData yellowIntersection = null;
        Random rand = new Random();

        for (TrafficData data : sortedDataList) {
            if (greenIntersection == null && data.getVehicleCount() > 50) {
                greenIntersection = data;
            } else if (yellowIntersection == null && data.getVehicleCount() > 20) {
                yellowIntersection = data;
            } else {
                break;
            }
        }

        if (greenIntersection == null && !sortedDataList.isEmpty() && sortedDataList.get(0).getVehicleCount() > 20) {
            greenIntersection = sortedDataList.get(0);
        }

        for (TrafficData data : trafficDataList) {
            String signalColor = "RED";

            if (data.equals(greenIntersection)) {
                signalColor = "GREEN";
            } else if (data.equals(yellowIntersection)) {
                signalColor = "YELLOW";
            }

            TrafficSignalPanel panel = new TrafficSignalPanel(data);
            panel.setLight(signalColor);
            signalPanel.add(panel);
        }

        signalPanel.revalidate();
        signalPanel.repaint();
    }

    private void generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("Traffic Report:\n");
        for (TrafficData data : trafficDataList) {
            report.append(data).append("\n");
        }
        reportArea.setText(report.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SmartTrafficSignalApp::new);
    }
}
