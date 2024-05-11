import javax.swing.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class executeFCFS {
    private List<Process> processes;
    private float avgWaitingTime = 0; // 클래스 멤버 변수로 선언
    private float avgTurnaroundTime = 0; // 클래스 멤버 변수로 선언

    public executeFCFS(List<Process> processes) {
        this.processes = processes;
    }

    public void run() {
        // 도착 시간 순으로 프로세스를 정렬합니다.
        Collections.sort(processes, Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0; // 현재 시간을 추적합니다.
        float totalWaitingTime = 0;
        float totalTurnaroundTime = 0;

        for (Process process : processes) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime(); // 프로세스 도착까지 시간을 뛰어넘습니다.
            }
            process.setStartTime(currentTime);
            process.setFinishTime(currentTime + process.getServiceTime());
            process.setWaitingTime(process.getStartTime() - process.getArrivalTime());
            process.setTurnaroundTime(process.getFinishTime() - process.getArrivalTime());

            currentTime = process.getFinishTime(); // 현재 시간을 갱신합니다.
            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        // 평균 시간 계산
        avgWaitingTime = totalWaitingTime / processes.size();
        avgTurnaroundTime = totalTurnaroundTime / processes.size();

        // 결과를 새 창에서 보여줍니다.
        SwingUtilities.invokeLater(() -> new ResultDisplay("FCFS Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime));
    }

    public float getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public float getAvgTurnaroundTime() {
        return avgTurnaroundTime;
    }
}
