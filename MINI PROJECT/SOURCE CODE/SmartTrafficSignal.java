import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmartTrafficSignal extends JFrame {
    private JTextField vehicleCountField;
    private JTextField averageSpeedField;
    private JTextField intersectionNameField;
    private JPanel signalPanel;
    private List<IntersectionData> intersectionDataList;

    public SmartTrafficSignal() {
        setTitle("Smart Traffic Signal System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        intersectionDataList = new ArrayList<>();

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        inputPanel.add(new JLabel("Vehicle Count:"));
        vehicleCountField = new JTextField();
        inputPanel.add(vehicleCountField);

        inputPanel.add(new JLabel("Average Speed:"));
        averageSpeedField = new JTextField();
        inputPanel.add(averageSpeedField);

        inputPanel.add(new JLabel("Intersection Name:"));
        intersectionNameField = new JTextField();
        inputPanel.add(intersectionNameField);

        JButton addDataButton = new JButton("Add Data");
        addDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addIntersectionData();
            }
        });
        inputPanel.add(addDataButton);

        JButton generateSignalButton = new JButton("Generate Traffic Signal");
        generateSignalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateTrafficSignals();
            }
        });
        inputPanel.add(generateSignalButton);

        JButton generateReportButton = new JButton("Generate Report");
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        inputPanel.add(generateReportButton);

        add(inputPanel, BorderLayout.NORTH);

        signalPanel = new JPanel();
        signalPanel.setLayout(new GridLayout(2, 2, 10, 10)); // Adjusted for a 2x2 grid
        add(signalPanel, BorderLayout.CENTER);

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        add(new JScrollPane(reportArea), BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addIntersectionData() {
        try {
            int vehicleCount = Integer.parseInt(vehicleCountField.getText());
            double averageSpeed = Double.parseDouble(averageSpeedField.getText());
            String intersectionName = intersectionNameField.getText();

            if (intersectionName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Intersection Name cannot be empty.");
                return;
            }

            intersectionDataList.add(new IntersectionData(intersectionName, vehicleCount, averageSpeed));
            vehicleCountField.setText("");
            averageSpeedField.setText("");
            intersectionNameField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Vehicle Count and Average Speed.");
        }
    }

    private void generateTrafficSignals() {
        if (intersectionDataList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data available to generate traffic signals.");
            return;
        }

        intersectionDataList.sort((a, b) -> {
            int cmp = Integer.compare(b.getVehicleCount(), a.getVehicleCount());
            if (cmp == 0) {
                return Double.compare(b.getAverageSpeed(), a.getAverageSpeed());
            }
            return cmp;
        });

        Random random = new Random();

        signalPanel.removeAll();

        IntersectionData maxIntersection = intersectionDataList.get(0);
        IntersectionData secondMaxIntersection = null;
        if (intersectionDataList.size() > 1) {
            secondMaxIntersection = intersectionDataList.get(1);
        }

        List<IntersectionData> maxIntersections = new ArrayList<>();
        for (IntersectionData data : intersectionDataList) {
            if (data.getVehicleCount() == maxIntersection.getVehicleCount()) {
                maxIntersections.add(data);
            }
        }

        if (maxIntersections.size() > 1) {
            maxIntersection = maxIntersections.get(random.nextInt(maxIntersections.size()));
        }

        for (IntersectionData data : intersectionDataList) {
            JPanel intersectionPanel = new JPanel(new BorderLayout());
            intersectionPanel.setBorder(BorderFactory.createTitledBorder(data.getIntersectionName()));
            
            TrafficSignal signal = new TrafficSignal();
            if (data == maxIntersection) {
                signal.setGreen();
            } else if (data == secondMaxIntersection) {
                signal.setYellow();
            } else {
                signal.setRed();
            }

            intersectionPanel.add(signal, BorderLayout.CENTER);
            intersectionPanel.add(new JLabel("Vehicle Count: " + data.getVehicleCount()), BorderLayout.NORTH);
            intersectionPanel.add(new JLabel("Avg Speed: " + data.getAverageSpeed()), BorderLayout.SOUTH);
            
            signalPanel.add(intersectionPanel);
        }

        revalidate();
        repaint();
    }

    private void generateReport() {
        JTextArea reportArea = (JTextArea) ((JScrollPane) getContentPane().getComponent(2)).getViewport().getView();
        reportArea.setText("Traffic Report:\n");
        for (IntersectionData data : intersectionDataList) {
            reportArea.append("Intersection: " + data.getIntersectionName() + ", Vehicle Count: " + data.getVehicleCount() + ", Average Speed: " + data.getAverageSpeed() + "\n");
        }
    }

    public static void main(String[] args) {
        new SmartTrafficSignal();
    }
}

class IntersectionData {
    private String intersectionName;
    private int vehicleCount;
    private double averageSpeed;

    public IntersectionData(String intersectionName, int vehicleCount, double averageSpeed) {
        this.intersectionName = intersectionName;
        this.vehicleCount = vehicleCount;
        this.averageSpeed = averageSpeed;
    }

    public String getIntersectionName() {
        return intersectionName;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }
}

class TrafficSignal extends JPanel {
    private JLabel redLight;
    private JLabel yellowLight;
    private JLabel greenLight;

    public TrafficSignal() {
        setLayout(new GridLayout(3, 1));
        redLight = createLight(Color.RED);
        yellowLight = createLight(Color.YELLOW);
        greenLight = createLight(Color.GREEN);
        add(redLight);
        add(yellowLight);
        add(greenLight);
        setRed();
    }

    private JLabel createLight(Color color) {
        JLabel label = new JLabel();
        label.setOpaque(true);
        label.setBackground(Color.DARK_GRAY);
        label.setPreferredSize(new Dimension(50, 50));
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        return label;
    }

    public void setRed() {
        redLight.setBackground(Color.RED);
        yellowLight.setBackground(Color.DARK_GRAY);
        greenLight.setBackground(Color.DARK_GRAY);
    }

    public void setYellow() {
        redLight.setBackground(Color.DARK_GRAY);
        yellowLight.setBackground(Color.YELLOW);
        greenLight.setBackground(Color.DARK_GRAY);
    }

    public void setGreen() {
        redLight.setBackground(Color.DARK_GRAY);
        yellowLight.setBackground(Color.DARK_GRAY);
        greenLight.setBackground(Color.GREEN);
    }
}
