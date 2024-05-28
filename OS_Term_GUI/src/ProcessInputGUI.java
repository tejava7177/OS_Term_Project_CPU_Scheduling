import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ProcessInputGUI extends JFrame {
    private List<Process> processes;                //프로세스 정보 리스트
    // GUI 구성요소
    private JTextField idField, arrivalTimeField, serviceTimeField, priorityField;      //ID, 도착시간, 서비스시간, 우선순위 입력 필드
    private JButton addButton, deleteButton, runAlgorithmButton;                        //프로세스 추가, 삭제, 구동 버튼
    private JTextArea processListDisplay;                                               //프로세스 정보 리스트 출력
    private JComboBox<String> algorithmComboBox;                                        //프로세스 선택

    private int timeSlice;                                                              // 타임슬라이스를 저장할 변수 추가

    public ProcessInputGUI() {
        super("Process Input");                         //윈도우 제목
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);      // X 버튼을 누르면 종료
        setSize(600, 400);                      //크기 설정
        setLayout(new GridLayout(7, 2, 10, 10));  // 그리드 레이아웃 설정 7행 2열 간격 10px

        // 초기화
        initializeComponents();         //GUI 컴포넌트 초기화 함수 호출
        addComponentsToFrame();         //요소를 프레임에 배치
        setVisible(true);               //화면에 표시
    }


    private void initializeComponents() {
        processes = new ArrayList<>();  // 리스트에 프로세스의 ID, 도착 시간, 서비스 시간, 우선 순위 등의 데이터를 저장

        // 크기 설정
        idField = new JTextField(5);  // Process ID 입력 칸
        arrivalTimeField = new JTextField(5);  // 도착 시간 입력 칸
        serviceTimeField = new JTextField(5);  // 서비스 시간 입력 칸
        priorityField = new JTextField(5);  // 우선순위 입력 칸
        processListDisplay = new JTextArea(10, 20);  // 입력된 프로세스 출력 칸
        processListDisplay.setEditable(false);  // 수정 불가능하게 설정

        addButton = new JButton("Add Process");  // Process 데이터 입력 버튼
        addButton.addActionListener(this::addProcessClick);  // 추가 이벤트 처리

        deleteButton = new JButton("Delete Process");  // Process 데이터 삭제 버튼
        deleteButton.addActionListener(this::deleteProcessClick);  // 삭제 이벤트 처리

        // 알고리즘을 선택할 수 있는 ComboBox 생성
        algorithmComboBox = new JComboBox<>(new String[]{"FCFS", "SJF", "Non-preemptive Priority", "Preemptive Priority", "Round Robin", "SRT", "HRN"});
        runAlgorithmButton = new JButton("Run Selected Algorithm");  // 알고리즘 실행 버튼
        runAlgorithmButton.addActionListener(this::runSelectedAlgorithm);  // 알고리즘 실행 이벤트 처리
    }

    // 각 컴포넌트를 순서대로 추가하여 GUI를 구성한다.
    private void addComponentsToFrame() {
        add(new JLabel("Process ID:"));  // Process ID 레이블 추가
        add(idField);  // Process ID 입력 칸 추가
        add(new JLabel("Arrival Time:"));  // 도착 시간 레이블 추가
        add(arrivalTimeField);  // 도착 시간 입력 칸 추가
        add(new JLabel("Service Time:"));  // 서비스 시간 레이블 추가
        add(serviceTimeField);  // 서비스 시간 입력 칸 추가
        add(new JLabel("Priority (default 0):"));  // 우선순위 레이블 추가
        add(priorityField);  // 우선순위 입력 칸 추가
        add(deleteButton);  // 프로세스 삭제 버튼 추가
        add(addButton);  // 프로세스 추가 버튼 추가
        add(algorithmComboBox);  // 알고리즘 선택 ComboBox 추가
        add(new JScrollPane(processListDisplay));  // 프로세스 리스트 출력 칸 추가 (스크롤 가능)
        add(runAlgorithmButton);  // 알고리즘 실행 버튼을 가운데 정렬하여 추가
    }

    //프로세스 데이터를 추가하는 함수
    private void addProcessClick(ActionEvent e) {
        try {
            String id = idField.getText();              // Process ID 는 문자열로 받아옴
            int arrival = Integer.parseInt(arrivalTimeField.getText());     // 정수형으로 사용자에게 입력 받음.
            int service = Integer.parseInt(serviceTimeField.getText());     // 정수형으로 사용자에게 입력 받음.
            int priority;                                                   // 우선순위 변수

            String priorityText = priorityField.getText();
            if (priorityText.isEmpty()) {                       // 비어있음 = 입력을 하지 않음
                priority = 0;                                   // Priority 필드가 비어있으면 기본값 0 사용
            } else {
                priority = Integer.parseInt(priorityField.getText());       //입력을 하면 해당 데이터로 우선순위 설정
            }

            Process process = new Process(id, arrival, service, priority);      //객체 생성
            processes.add(process);                                             //입력받은 process 데이터를 객체에 담음.
            displayProcesses();                                                 //데이터를 GUI 환경에 보여줌.
            clearFields();                                                      //다시 입력 받기 위해서 textfields 비우.
        } catch (NumberFormatException ex) {                        //올바른 자료형(정수형)이 아닌 경우 출력
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for all fields.");
        }
    }

    // 삭제 버튼을 눌렀을 경우
    private void deleteProcessClick(ActionEvent e){
        processes.clear();                      //객체 삭제
        processListDisplay.setText("");         //프로세스 데이터 정보 리스트 출력 textfields 비우기
    }



    // 프로세스 데이터 리스트를 보여주는 textfield
    private void displayProcesses() {
        StringBuilder sb = new StringBuilder();         //StringBuilder 객체 생성
        // 데이터터 출력
        for (Process process : processes) {
            sb.append("ID: ").append(process.getId())
                    .append(", Arrival: ").append(process.getArrivalTime())
                    .append(", Service: ").append(process.getServiceTime())
                    .append(", Priority: ").append(process.getPriority()).append("\n");
        }
        processListDisplay.setText(sb.toString());      //목록 업데이트
    }



    //
    private void runSelectedAlgorithm(ActionEvent e) {
        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();    //선택한 알고리즘을 문자열로 가져옴
        // timeSlice 가 필요한 알고리즘은 추가로 사용자에게 입력 받는다. (RR, SRT)
        if("Round Robin".equals(selectedAlgorithm) || "SRT".equals(selectedAlgorithm)){
            String input = JOptionPane.showInputDialog
                    (this, "Time Slice 입력 :", "Time slice Input", JOptionPane.QUESTION_MESSAGE);
            try{
                timeSlice = Integer.parseInt(input);        //timeSlice 변수에 저장

            }
            catch (NumberFormatException ex){       //정수형이 아니라면 오류 출력
                JOptionPane.showMessageDialog(this, "정수형을 입력해주세요");
            }
        }
        executeAlgorithm(selectedAlgorithm);        //선택 된 알고리즘과 일치하는 알고리즘 실행
    }


    // textfield 빈 칸으로 변경하는 함수
    private void clearFields() {
        idField.setText("");
        arrivalTimeField.setText("");
        serviceTimeField.setText("");
        priorityField.setText("");
    }



    // comboBox 에서 선택된 알고리즘과 Run Selected Algorithm 버튼이 눌리면 조건문 실행
    private void executeAlgorithm(String algorithm) {
        switch (algorithm) {
            case "FCFS":
                executeFCFS fcfs = new executeFCFS(processes);
                fcfs.run();
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
                executeRR rr = new executeRR(processes, timeSlice);     //사용자에게 입력 받은 timeSlice
                rr.run();
                break;

            case "SRT":
                executeSRT srt = new executeSRT(processes, timeSlice);  //사용자에게 입력 받은 timeSlice
                srt.run();
                break;
        }
    }
}
