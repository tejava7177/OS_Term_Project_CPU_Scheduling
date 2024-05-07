import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ProcessInputGUI extends JFrame {
    private List<Process> processes;
    private JTextField idField, arrivalTimeField, serviceTimeField, priorityField;
    private JButton addButton;
    private JTextArea processListDisplay;

    public ProcessInputGUI() {
        super("Process Input");
        processes = new ArrayList<>();

        setLayout(new GridLayout(6, 2, 10, 10)); // 6 rows, 2 columns, with spacing

        // Initialize and add components
        add(new JLabel("Process ID:"));
        idField = new JTextField(5);
        add(idField);

        add(new JLabel("Arrival Time:"));
        arrivalTimeField = new JTextField(5);
        add(arrivalTimeField);

        add(new JLabel("Service Time:"));
        serviceTimeField = new JTextField(5);
        add(serviceTimeField);

        add(new JLabel("Priority:"));
        priorityField = new JTextField(5);
        add(priorityField);

        addButton = new JButton("Add Process");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    int arrival = Integer.parseInt(arrivalTimeField.getText());
                    int service = Integer.parseInt(serviceTimeField.getText());
                    int priority = Integer.parseInt(priorityField.getText());
                    Process process = new Process(id, arrival, service, priority);
                    processes.add(process);
                    displayProcesses();
                    clearFields();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter valid numbers for all fields.");
                }
            }
        });
        add(addButton);

        processListDisplay = new JTextArea(10, 20);
        processListDisplay.setEditable(false);
        add(new JScrollPane(processListDisplay)); // Scroll pane for text area


        // 스케줄링 알고리즘 선택을 위한 콤보박스
        JComboBox<String> algorithmComboBox;
        JButton runAlgorithmButton;

// 콤보박스와 실행 버튼 초기화 및 추가
        algorithmComboBox = new JComboBox<>(new String[] {"FCFS", "SJF", "Non-preemptive Priority", "Preemptive Priority", "Round Robin", "SRT", "HRN"});
        add(algorithmComboBox); // GridLayout에 맞추어 추가

        runAlgorithmButton = new JButton("Run Selected Algorithm");
        runAlgorithmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
                executeAlgorithm(selectedAlgorithm);
            }
        });
        add(runAlgorithmButton); // GridLayout에 맞추어 추가


        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void displayProcesses() {
        StringBuilder sb = new StringBuilder();
        for (Process p : processes) {
            sb.append("ID: ").append(p.getId())
                    .append(", Arrival: ").append(p.getArrivalTime())
                    .append(", Service: ").append(p.getServiceTime())
                    .append(", Priority: ").append(p.getPriority()).append("\n");
        }
        processListDisplay.setText(sb.toString());
    }


    private void executeAlgorithm(String algorithm) {
        switch (algorithm) {
            case "FCFS":
                //executeFCFS();
                break;
            case "SJF":
                //executeSJF();
                break;
            case "Non-preemptive Priority":
                //executeNonPreemptivePriority();
                break;
            case "Preemptive Priority":
                //executePreemptivePriority();
                break;
            case "Round Robin":
                //executeRR();
                break;
            case "SRT":
                //executeSRT();
                break;
            case "HRN":
                //executeHRN();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Algorithm not implemented.");
        }
    }

    private void clearFields() {
        idField.setText("");
        arrivalTimeField.setText("");
        serviceTimeField.setText("");
        priorityField.setText("");
    }




    public static void main(String[] args) {
        new ProcessInputGUI();
    }

    static class Process {
        private int id, arrivalTime, serviceTime, priority;

        public Process(int id, int arrivalTime, int serviceTime, int priority) {
            this.id = id;
            this.arrivalTime = arrivalTime;
            this.serviceTime = serviceTime;
            this.priority = priority;
        }

        public int getId() {
            return id;
        }

        public int getArrivalTime() {
            return arrivalTime;
        }

        public int getServiceTime() {
            return serviceTime;
        }

        public int getPriority() {
            return priority;
        }
    }
}
