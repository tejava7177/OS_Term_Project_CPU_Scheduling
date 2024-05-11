import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class executeSJF {
    private List<Process> processes;
    private float avgWaitingTime = 0;
    private float avgTurnaroundTime = 0;

    public executeSJF(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
    }

    public void run() {
        // 도착 시간으로 프로세스를 정렬
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        // 실행 준비 큐 생성 (서비스 시간 기준으로 정렬)
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getServiceTime));

        int currentTime = 0;
        float totalWaitingTime = 0;
        float totalTurnaroundTime = 0;
        int processedCount = 0;

        while (processedCount < processes.size()) {
            // 현재 시간에서 실행할 수 있는 프로세스를 준비 큐에 추가
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && !process.isVisited()) {
                    readyQueue.offer(process);
                    process.setVisited(true); // 프로세스가 준비 큐에 추가됐음을 표시
                }
            }

            // 준비 큐가 비어있다면, 다음 프로세스의 도착 시간까지 시간을 점프
            if (readyQueue.isEmpty()) {
                currentTime = processes.stream().filter(p -> !p.isVisited())
                        .min(Comparator.comparingInt(Process::getArrivalTime))
                        .map(Process::getArrivalTime)
                        .orElse(currentTime);
                continue;
            }

            // 준비 큐에서 프로세스를 꺼내 실행
            Process currentProcess = readyQueue.poll();
            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();
            }
            currentProcess.setStartTime(currentTime);
            currentProcess.setFinishTime(currentTime + currentProcess.getServiceTime());
            currentProcess.setWaitingTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());
            currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());

            currentTime += currentProcess.getServiceTime();
            totalWaitingTime += currentProcess.getWaitingTime();
            totalTurnaroundTime += currentProcess.getTurnaroundTime();
            processedCount++;
        }

        // 평균 시간 계산
        avgWaitingTime = totalWaitingTime / processes.size();
        avgTurnaroundTime = totalTurnaroundTime / processes.size();

        // 결과를 새 창에서 보여줌
        SwingUtilities.invokeLater(() -> new ResultDisplay("SJF Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime));
    }

    public float getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public float getAvgTurnaroundTime() {
        return avgTurnaroundTime;
    }
}
