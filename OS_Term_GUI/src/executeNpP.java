import javax.swing.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class executeNpP {
    private List<Process> processes;
    private float avgWaitingTime = 0;
    private float avgTurnaroundTime = 0;

    public executeNpP(List<Process> processes) {
        this.processes = processes;
    }

    public void run() {
        // 도착 시간과 우선순위에 따라 프로세스를 정렬합니다.
        Collections.sort(processes, (p1, p2) -> {
            if (p1.getArrivalTime() == p2.getArrivalTime()) {
                return Integer.compare(p1.getPriority(), p2.getPriority());
            }
            return Integer.compare(p1.getArrivalTime(), p2.getArrivalTime());
        });

        int currentTime = 0;
        float totalWaitingTime = 0;
        float totalTurnaroundTime = 0;

        for (Process process : processes) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }
            process.setStartTime(currentTime);
            process.setFinishTime(currentTime + process.getServiceTime());
            process.setWaitingTime(process.getStartTime() - process.getArrivalTime());
            process.setTurnaroundTime(process.getFinishTime() - process.getArrivalTime());

            currentTime = process.getFinishTime();
            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        // 평균 시간 계산
        avgWaitingTime = totalWaitingTime / processes.size();
        avgTurnaroundTime = totalTurnaroundTime / processes.size();

        // 결과를 새 창에서 보여줍니다.
        SwingUtilities.invokeLater(() -> new ResultDisplay("Non-preemptive Priority Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime));
    }

    public float getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public float getAvgTurnaroundTime() {
        return avgTurnaroundTime;
    }
}
