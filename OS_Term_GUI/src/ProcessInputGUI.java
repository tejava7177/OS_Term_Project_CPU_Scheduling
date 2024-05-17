import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ProcessInputGUI extends JFrame {
    private List<Process> processes;
    private JTextField idField, arrivalTimeField, serviceTimeField, priorityField;
    private JButton addButton, deleteButton, runAlgorithmButton;
    private JTextArea processListDisplay;
    private JComboBox<String> algorithmComboBox;

    private int timeSlice; // 타임슬라이스를 저장할 변수 추가

    public ProcessInputGUI() {
        super("Process Input");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new GridLayout(7, 2, 10, 10));  // Adjust grid layout rows for additional components

        // Initialize components
        initializeComponents();
        addComponentsToFrame();
        setVisible(true);
    }

    private void initializeComponents() {
        processes = new ArrayList<>();

        idField = new JTextField(5);
        arrivalTimeField = new JTextField(5);
        serviceTimeField = new JTextField(5);
        priorityField = new JTextField(5);
        processListDisplay = new JTextArea(10, 20);
        processListDisplay.setEditable(false);

        addButton = new JButton("Add Process");
        addButton.addActionListener(this::addProcessAction);

        deleteButton = new JButton("Delete Process");
        deleteButton.addActionListener(this::deleteProcessAction);

        algorithmComboBox = new JComboBox<>(new String[]{"FCFS", "SJF", "Non-preemptive Priority", "Preemptive Priority", "Round Robin", "SRT", "HRN"});
        runAlgorithmButton = new JButton("Run Selected Algorithm");
        runAlgorithmButton.addActionListener(this::runSelectedAlgorithm);

    }

    private void addComponentsToFrame() {
        add(new JLabel("Process ID:"));
        add(idField);
        add(new JLabel("Arrival Time:"));
        add(arrivalTimeField);
        add(new JLabel("Service Time:"));
        add(serviceTimeField);
        add(new JLabel("Priority(default 0):"));
        add(priorityField);
        add(deleteButton);
        add(addButton);
        add(algorithmComboBox);
        add(new JScrollPane(processListDisplay));
        add(runAlgorithmButton, Component.CENTER_ALIGNMENT);
    }

    private void addProcessAction(ActionEvent e) {
        try {
            String id = idField.getText();
            int arrival = Integer.parseInt(arrivalTimeField.getText());
            int service = Integer.parseInt(serviceTimeField.getText());
            int priority;

            String priorityText = priorityField.getText();
            if (priorityText.isEmpty()) {
                priority = 0; // Priority 필드가 비어있으면 기본값 0 사용
            } else {
                priority = Integer.parseInt(priorityField.getText());
            }

            Process process = new Process(id, arrival, service, priority);
            processes.add(process);
            displayProcesses();
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.");
        }
    }

    private void deleteProcessAction(ActionEvent e){
        processes.clear();
        processListDisplay.setText("");
    }



    private void displayProcesses() {
        StringBuilder sb = new StringBuilder();
        for (Process process : processes) {
            sb.append("ID: ").append(process.getId())
                    .append(", Arrival: ").append(process.getArrivalTime())
                    .append(", Service: ").append(process.getServiceTime())
                    .append(", Priority: ").append(process.getPriority()).append("\n");
        }
        processListDisplay.setText(sb.toString());
    }


    private void runSelectedAlgorithm(ActionEvent e) {
        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
        if("Round Robin".equals(selectedAlgorithm) || "SRT".equals(selectedAlgorithm)){
            String input = JOptionPane.showInputDialog(this, "Enter Time Slice", "Time slice Input", JOptionPane.QUESTION_MESSAGE);
            try{
                timeSlice = Integer.parseInt(input);

            }
            catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this, "Invalid time Slice, Please enter a valid number");
            }
        }
        executeAlgorithm(selectedAlgorithm);
    }

    private void clearFields() {
        idField.setText("");
        arrivalTimeField.setText("");
        serviceTimeField.setText("");
        priorityField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProcessInputGUI::new);
    }


    private void executeAlgorithm(String algorithm) {
        switch (algorithm) {
            case "FCFS":
                executeFCFS fcfs = new executeFCFS(processes);
                fcfs.run(); // FCFS 알고리즘을 실행합니다.
                break;


            case "SJF":
                executeSJF sjf = new executeSJF(processes);
                sjf.run();
                break;

            case "Non-preemptive Priority":
                executeNpP npp= new executeNpP(processes);
                npp.run();
                break;

            case "HRN":
                executeHRN hrn= new executeHRN(processes);
                hrn.run();
                break;

            case "Preemptive Priority":
                executePP pp = new executePP(processes);
                pp.run();
                break;

            case "Round Robin":

                executeRR rr = new executeRR(processes, timeSlice);
                rr.run();
                break;

        }
    }
}
