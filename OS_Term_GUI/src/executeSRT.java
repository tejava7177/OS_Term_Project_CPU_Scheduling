import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class executeSRT {
    private List<Process> processes;
    private int timeSlice;
    private float avgWaitingTime = 0;
    private float avgTurnaroundTime = 0;

    public executeSRT(List<Process> processes, int timeSlice) {
        this.processes = new ArrayList<>(processes);
        this.timeSlice = timeSlice;
    }

    public void run() {
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getRemainingServiceTime).thenComparingInt(Process::getArrivalTime)
        );
        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        // 모든 프로세스의 도착 시간에 따라 정렬
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        while (!completedProcesses.containsAll(processes)) {
            // 현재 시간에서 실행 가능한 프로세스를 readyQueue에 추가
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && !completedProcesses.contains(p) && !readyQueue.contains(p)) {
                    p.setRemainingServiceTime(p.getServiceTime()); // 초기 남은 시간 설정
                    readyQueue.add(p);
                }
            }

            Process currentProcess = readyQueue.poll();

            if (currentProcess != null) {
                if (currentProcess.getStartTime() == -1) {
                    currentProcess.setStartTime(currentTime);
                }

                int executionTime = Math.min(currentProcess.getRemainingServiceTime(), timeSlice);
                currentTime += executionTime;
                currentProcess.setRemainingServiceTime(currentProcess.getRemainingServiceTime() - executionTime);

                if (currentProcess.getRemainingServiceTime() == 0) {
                    currentProcess.setFinishTime(currentTime);
                    completedProcesses.add(currentProcess);
                    totalWaitingTime += currentProcess.getFinishTime() - currentProcess.getArrivalTime() - currentProcess.getServiceTime();
                    totalTurnaroundTime += currentProcess.getFinishTime() - currentProcess.getArrivalTime();
                } else {
                    readyQueue.add(currentProcess);
                }
            } else {
                currentTime++;  // 아무 프로세스도 실행되지 않으면 시간을 증가시켜 다음 가능한 프로세스를 대기
            }
        }

        // 평균 대기 시간과 반환 시간 계산
        avgWaitingTime = (float) totalWaitingTime / processes.size();
        avgTurnaroundTime = (float) totalTurnaroundTime / processes.size();

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("SRT Scheduling Results", completedProcesses, avgWaitingTime, avgTurnaroundTime));
    }
}


