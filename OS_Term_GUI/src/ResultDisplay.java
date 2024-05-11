import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class ResultDisplay extends JFrame {
    public ResultDisplay(String title, List<Process> processes, float avgWaitingTime, float avgTurnaroundTime) {
        super(title);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 기존 테이블 설정 코드
        String[] columnNames = {"Process ID", "Start Time", "Finish Time", "Waiting Time", "Turnaround Time"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

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
        add(ganttChartPanel, BorderLayout.SOUTH);

        // 평균 값 표시를 위한 Panel
        JPanel averagesPanel = new JPanel();
        averagesPanel.add(new JLabel("Average Waiting Time: " + String.format("%.2f", avgWaitingTime)));
        averagesPanel.add(new JLabel("Average Turnaround Time: " + String.format("%.2f", avgTurnaroundTime)));
        add(averagesPanel, BorderLayout.NORTH);

        setVisible(true);
    }
}

class GanttChartPanel extends JPanel {
    private List<Process> processes;
    private int maxTime = 0;
    private final int numTicks = 10; // 눈금의 수

    public GanttChartPanel(List<Process> processes) {
        this.processes = processes;
        for (Process process : processes) {
            if (process.getFinishTime() > maxTime) {
                maxTime = process.getFinishTime();
            }
        }
        setPreferredSize(new Dimension(600, 100));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int barHeight = height / (processes.size() + 1);
        int xOffset = 10;

        // 간트 차트 눈금 그리기
        drawTicks(g, width, height, xOffset);

        // 프로세스 바와 대기 시간 그리기
        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            int startWaitX = (int) ((process.getArrivalTime() / (double) maxTime) * (width - 2 * xOffset));
            int startX = (int) ((process.getStartTime() / (double) maxTime) * (width - 2 * xOffset));
            int endX = (int) ((process.getFinishTime() / (double) maxTime) * (width - 2 * xOffset));
            int waitWidth = startX - startWaitX;
            int barWidth = endX - startX;

            // 대기 시간 그리기 (회색)
            g.setColor(Color.GRAY);
            g.fillRect(xOffset + startWaitX, i * barHeight, waitWidth, barHeight - 5);

            // 실행 시간 그리기 (파란색)
            g.setColor(new Color(100, 150, 255));
            g.fillRect(xOffset + startX, i * barHeight, barWidth, barHeight - 5);

            g.setColor(Color.BLACK);
            g.drawString("P" + process.getId(), xOffset + startX + 5, i * barHeight + barHeight / 2);
        }
    }

    private void drawTicks(Graphics g, int width, int height, int xOffset) {
        g.setColor(Color.BLACK);
        int tickSpacing = (width - 2 * xOffset) / numTicks;
        for (int i = 0; i <= numTicks; i++) {
            int x = xOffset + i * tickSpacing;
            g.drawLine(x, 0, x, height);
            int time = (int)((i / (double) numTicks) * maxTime);
            g.drawString(String.valueOf(time), x - 5, height - 5);
        }
    }
}

