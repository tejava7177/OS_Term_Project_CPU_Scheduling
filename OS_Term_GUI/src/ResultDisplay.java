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
            maxTime = Math.max(maxTime, process.getFinishTime());
        }
        setPreferredSize(new Dimension(600, Math.max(100, processes.size() * 50)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = getWidth();
        int height = getHeight();
        int barHeight = 40;
        int yOffset = 10;

        for (int i = 0; i < processes.size(); i++) {
            Process process = processes.get(i);
            for (int[] slice : process.getTimeSlices()) {
                int start = slice[0];
                int end = slice[1];
                int state = slice[2];  // 상태: 0 - 대기, 1 - 실행

                int startX = (int) ((start / (double) maxTime) * width);
                int endX = (int) ((end / (double) maxTime) * width);
                //g.setColor(state == 1 ? Color.BLUE : Color.GRAY);
                if (state == 1){
                    g.setColor(Color.BLUE);
                }
                g.fillRect(startX, yOffset + i * (barHeight + 10), endX - startX, barHeight);

                g.setColor(Color.BLACK);
                g.drawString("P" + process.getId(), startX + 5, yOffset + i * (barHeight + 10) + 20);
            }

        }

        drawTicks(g, width, height, 10);
    }

    private void drawTicks(Graphics g, int width, int height, int xOffset) {
        int tickSpacing = (width - 2 * xOffset) / numTicks;
        for (int i = 0; i <= numTicks; i++) {
            int x = xOffset + i * tickSpacing;
            g.drawLine(x, height - 30, x, height - 20);
            int time = (int) ((i / (double) numTicks) * maxTime);
            g.drawString(String.valueOf(time), x - 5, height - 5);
        }
    }
}






