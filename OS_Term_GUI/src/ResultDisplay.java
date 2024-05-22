import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ResultDisplay extends JFrame {
    public ResultDisplay(String title, List<Process> processes, float avgWaitingTime, float avgTurnaroundTime) {
        super(title);
        setSize(800, 600);  // 창의 크기를 설정
        setLocationRelativeTo(null);  // 창을 화면 중앙에 배치
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // 창 닫기 버튼을 누르면 창을 닫음
        setLayout(new BorderLayout());  // 레이아웃을 BorderLayout으로 설정

        // 테이블에 표시할 열 이름 설정
        String[] columnNames = {"Process ID", "Start Time", "Finish Time", "Waiting Time", "Turnaround Time"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);  // 테이블 모델 생성
        JTable table = new JTable(model);  // 테이블 생성
        JScrollPane scrollPane = new JScrollPane(table);  // 테이블을 스크롤 가능한 패널에 추가
        add(scrollPane, BorderLayout.CENTER);  // 테이블을 중앙에 배치

        // 테이블에 각 프로세스의 정보 추가
        for (Process process : processes) {
            model.addRow(new Object[]{
                    process.getId(),
                    process.getStartTime(),
                    process.getFinishTime(),
                    process.getWaitingTime(),
                    process.getTurnaroundTime()
            });
        }

        // 간트 차트 패널 추가
        GanttChartPanel ganttChartPanel = new GanttChartPanel(processes);
        JScrollPane ganttScrollPane = new JScrollPane(ganttChartPanel);  // 간트 차트를 스크롤 가능한 패널에 추가
        add(ganttScrollPane, BorderLayout.SOUTH);  // 간트 차트를 아래쪽에 배치

        // 평균 대기 시간과 반환 시간을 표시하는 패널 추가
        JPanel averagesPanel = new JPanel();  // 새로운 패널 생성
        averagesPanel.add(new JLabel("Average Waiting Time: " + String.format("%.2f", avgWaitingTime)));  // 평균 대기 시간 라벨 추가
        averagesPanel.add(new JLabel("Average Turnaround Time: " + String.format("%.2f", avgTurnaroundTime)));  // 평균 반환 시간 라벨 추가
        add(averagesPanel, BorderLayout.NORTH);  // 패널을 위쪽에 배치

        setVisible(true);  // 창을 표시
    }
}



class GanttChartPanel extends JPanel {
    private List<Process> processes;  // 프로세스 리스트
    private Set<Integer> timePoints;  // 간트 차트의 눈금을 표시할 시간 포인트
    private final int numTicks = 10;  // 기본 눈금 간격

    // 간트 차트 생성
    public GanttChartPanel(List<Process> processes) {
        this.processes = processes;
        this.timePoints = new TreeSet<>();  // 시간 포인트를 정렬된 집합으로 저장

        // 각 프로세스의 도착 시간과 시작 시간을 timePoints에 추가
        for (Process process : processes) {
            timePoints.add(process.getArrivalTime());
            timePoints.add(process.getStartTime());
            for (int[] slice : process.getTimeSlices()) {
                timePoints.add(slice[0]);
                timePoints.add(slice[1]);
            }
        }

        setPreferredSize(new Dimension(800, Math.max(100, processes.size() * 60)));  // 간트 차트의 크기 설정
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // JPanel의 기본 그리기 메서드 호출
        int width = getWidth();  // 패널의 너비
        int height = getHeight();  // 패널의 높이
        int barHeight = 40;  // 각 프로세스 바의 높이
        int yOffset = 20;  // Y축 오프셋

        int maxTime = timePoints.isEmpty() ? 0 : ((TreeSet<Integer>) timePoints).last();  // 최대 시간 포인트를 가져옴

        // 각 프로세스를 간트 차트에 그리기
        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            for (int[] slice : process.getTimeSlices()) {
                int start = slice[0];
                int end = slice[1];
                int state = slice[2];  // 상태: 0 - 대기, 1 - 실행

                int startX = timeToX(start, width, maxTime);  // 시작 시간을 X 좌표로 변환
                int endX = timeToX(end, width, maxTime);  // 종료 시간을 X 좌표로 변환

                g.setColor(state == 1 ? Color.BLUE : Color.GRAY);  // 상태에 따라 색상 설정
                g.fillRect(startX, yOffset + i * (barHeight + 20), endX - startX, barHeight);  // 바 그리기
                g.setColor(Color.BLACK);  // 글자 색상 설정
                g.drawString("P" + process.getId(), startX + 5, yOffset + i * (barHeight + 20) + 20);  // 프로세스 ID 그리기
            }
        }

        drawTicks(g, width, height, maxTime);  // 눈금 그리기
    }

    // 시간 값을 X 좌표로 변환하는 메서드
    private int timeToX(int time, int width, int maxTime) {
        return (int) ((time / (double) maxTime) * width);
    }

    // 눈금을 그리는 메서드
    private void drawTicks(Graphics g, int width, int height, int maxTime) {
        int tickSpacing = width / numTicks;  // 눈금 간격 계산
        int tickYPos = height - 30;  // 눈금의 Y 위치 설정

        // 기본 눈금 그리기
        for (int i = 0; i <= numTicks; i++) {
            int x = i * tickSpacing;
            int time = (int) ((i / (double) numTicks) * maxTime);
            g.drawLine(x, tickYPos, x, tickYPos + 10);
            g.drawString(String.valueOf(time), x - 5, tickYPos + 25);
        }

        // 의미 있는 시간 포인트에 눈금 추가
        for (int timePoint : timePoints) {
            int x = timeToX(timePoint, width, maxTime);
            g.drawLine(x, tickYPos, x, tickYPos + 10);
            g.drawString(String.valueOf(timePoint), x - 5, tickYPos + 25);
        }
    }
}
