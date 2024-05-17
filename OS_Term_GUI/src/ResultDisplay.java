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
    private final int numTicks = 10;

    public GanttChartPanel(List<Process> processes) {
        this.processes = processes;
        for (Process process : processes) {
            if (process.getFinishTime() > maxTime) {
                maxTime = process.getFinishTime();
            }
        }
        setPreferredSize(new Dimension(600, Math.max(100, processes.size() * 20 + 30)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int barHeight = Math.max(20, height / (processes.size() + 2));
        int xOffset = 10;

        drawTicks(g, width, height, xOffset);

        // 각 프로세스에 대해 막대 그리기
        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            int totalStartX = (int) ((process.getStartTime() / (double) maxTime) * (width - 2 * xOffset));
            int totalEndX = (int) ((process.getFinishTime() / (double) maxTime) * (width - 2 * xOffset));

            // 프로세스 실행 중 막대 (파란색)
            g.setColor(Color.BLUE);
            g.fillRect(xOffset + totalStartX, i * barHeight + 10, totalEndX - totalStartX, barHeight - 5);

            // 대기 중인 시간 막대 (초록색)
            if (process.getStartTime() > process.getArrivalTime()) {
                int waitEndX = (int) ((process.getStartTime() / (double) maxTime) * (width - 2 * xOffset));
                g.setColor(Color.GREEN);
                g.fillRect(xOffset + (int) ((process.getArrivalTime() / (double) maxTime) * (width - 2 * xOffset)), i * barHeight + 10, waitEndX - totalStartX, barHeight - 5);
            }

            // 프로세스 ID 표시
            g.setColor(Color.BLACK);
            g.drawString("P" + process.getId(), xOffset + totalStartX + 5, i * barHeight + barHeight / 2 + 15);
        }
    }

    private void drawTicks(Graphics g, int width, int height, int xOffset) {
        int tickSpacing = (width - 2 * xOffset) / numTicks;
        for (int i = 0; i <= numTicks; i++) {
            int x = xOffset + i * tickSpacing;
            g.drawLine(x, height - 30, x, height - 20);
            int time = (int)((i / (double) numTicks) * maxTime);
            g.drawString(String.valueOf(time), x - 5, height - 5);
        }
    }
}

