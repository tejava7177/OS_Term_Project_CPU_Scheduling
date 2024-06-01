import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.TreeSet;

public class ResultDisplay extends JFrame {
    public ResultDisplay(String title, List<Process> processes, float avgWaitingTime, float avgTurnaroundTime, float avgResponseTime) {
        super(title);                                       // 창의 제목 설정
        setSize(800, 600);                      // 창의 크기 설정
        setLocationRelativeTo(null);                        // 창을 화면 중앙에 배치
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // 창 닫기 버튼을 누르면 창을 닫음
        setLayout(new BorderLayout());                      // 레이아웃을 BorderLayout으로 설정

        // 테이블에 표시할 열 이름 설정
        String[] columns = {"Process ID", "Start Time", "Finish Time", "Waiting Time", "Turnaround Time", "Response Time"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);   // 테이블 모델 생성
        JTable table = new JTable(model);                                       // 테이블 생성
        JScrollPane scrollPane = new JScrollPane(table);                        // 테이블을 스크롤 가능한 패널에 추가
        add(scrollPane, BorderLayout.CENTER);                                   // 테이블을 중앙에 배치

        // 테이블에 각 프로세스의 정보 추가
        for (Process process : processes) {
            model.addRow(new Object[]{
                    process.getId(),
                    process.getStartTime(),
                    process.getFinishTime(),
                    process.getWaitingTime(),
                    process.getTurnaroundTime(),
                    process.getResponseTime()
            });
        }
        // 간트 차트 패널 추가
        GanttChartPanel ganttChartPanel = new GanttChartPanel(processes);
        add(ganttChartPanel, BorderLayout.SOUTH);                       // 간트 차트를 아래쪽에 배치

        // 평균 대기 시간, 반환 시간, 응답 시간을 표시하는 패널 추가
        JPanel averagesPanel = new JPanel();                             // 새로운 패널 생성
        averagesPanel.add(new JLabel("Average Waiting Time: " + String.format("%.2f", avgWaitingTime)));  // AWT
        averagesPanel.add(new JLabel("Average Turnaround Time: " + String.format("%.2f", avgTurnaroundTime)));  // ATT
        averagesPanel.add(new JLabel("Average Response Time: " + String.format("%.2f", avgResponseTime)));  // ART
        add(averagesPanel, BorderLayout.NORTH);  // 패널을 위쪽에 배치

        setVisible(true);                                                // 창을 표시
    }

    public class GanttChartPanel extends JPanel {
        private List<Process> processes;
        private TreeSet<Integer> timePoints;

        public GanttChartPanel(List<Process> processes) {
            this.processes = processes;
            this.timePoints = new TreeSet<>();  // 시간 포인트를 정렬된 집합으로 저장

            // 각 프로세스의 도착 시간과 시작 시간을 timePoints에 추가
            for (Process process : processes) {
                timePoints.add(process.getArrivalTime());  // 프로세스의 도착 시간을 시간 포인트에 추가
                timePoints.add(process.getStartTime());  // 프로세스의 시작 시간을 시간 포인트에 추가

                // 프로세스의 각 실행 슬라이스의 시작 시간과 종료 시간을 timePoints에 추가
                for (int[] slice : process.gettimeSave()) {
                    timePoints.add(slice[0]);  // 슬라이스의 시작 시간 추가
                    timePoints.add(slice[1]);  // 슬라이스의 종료 시간 추가
                }
            }

            setPreferredSize(new Dimension(800, Math.max(100, processes.size() * 60 + 80)));  // 간트 차트의 크기를 동적으로 설정
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);  // JPanel의 기본 그리기 메서드 호출
            int width = getWidth();  // 패널의 너비
            int height = getHeight();  // 패널의 높이
            int barHeight = 40;  // 각 프로세스 바의 높이
            int yOffset = 20;  // Y축 오프셋
            int tickHeight = 40; // 눈금 높이

            int maxTime = timePoints.isEmpty() ? 0 : timePoints.last();  // 최대 시간 포인트를 가져옴

            // 각 프로세스를 간트 차트에 그리기
            for (int i = 0; i < processes.size(); i++) {
                Process process = processes.get(i);
                boolean isFirstSlice = true; // 첫 번째 슬라이스 여부를 추적하는 변수

                for (int[] slice : process.gettimeSave()) {
                    int start = slice[0];
                    int end = slice[1];
                    int state = slice[2];  // 상태: 0 - 대기, 1 - 실행

                    int startX = timeToX(start, width, maxTime);  // 시작 시간을 X 좌표로 변환
                    int endX = timeToX(end, width, maxTime);  // 종료 시간을 X 좌표로 변환

                    g.setColor(Color.BLUE);  // 상태에 따라 색상 설정
                    g.fillRect(startX, yOffset + i * (barHeight + 20), endX - startX, barHeight);  // 바 그리기

                    g.setColor(Color.BLACK);  // 글자 색상 설정
                    if (isFirstSlice) {
                        g.drawString("Process :" + process.getId(), startX + 5, yOffset + i * (barHeight + 20) + 20);  // 프로세스 ID 그리기
                        isFirstSlice = false; // 첫 번째 슬라이스 후에는 ID를 그리지 않도록 설정
                    }
                }
            }

            drawTicks(g, width, height, maxTime, yOffset + processes.size() * (barHeight + 20));  // 눈금 그리기
        }

        // 시간 값을 X 좌표로 변환하는 메서드
        private int timeToX(int time, int width, int maxTime) {
            return (int) ((time / (double) maxTime) * width);
        }

        // 눈금 그리기 메서드 추가
        private void drawTicks(Graphics g, int width, int height, int maxTime, int yPosition) {
            g.setColor(Color.RED);
            int tickInterval = 5;
            for (int i = 0; i <= maxTime; i += tickInterval) {
                int x = timeToX(i, width, maxTime);
                g.drawLine(x, yPosition, x, yPosition + 10); // 눈금의 위치를 조정하여 간트 차트와 겹치지 않도록 함
                g.drawString(String.valueOf(i), x - 5, yPosition + 20); // 눈금 글자의 위치를 조정
            }
        }
    }
}
