import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class executePP {
    private List<Process> processes;
    private PriorityQueue<Process> readyQueue;
    private List<Process> completedProcesses = new ArrayList<>();
    private float avgWaitingTime = 0;
    private float avgTurnaroundTime = 0;

    public executePP(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
        this.readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority));
    }

    public void run() {
        int currentTime = 0;
        float totalWaitingTime = 0;
        float totalTurnaroundTime = 0;
        Process currentProcess = null;

        while (!processes.isEmpty() || !readyQueue.isEmpty() || currentProcess != null) {
            int finalCurrentTime = currentTime;
            processes.removeIf(process -> {
                if (process.getArrivalTime() <= finalCurrentTime) {
                    readyQueue.offer(process);
                    return true;
                }
                return false;
            });

            if (currentProcess == null || (!readyQueue.isEmpty() && readyQueue.peek().getPriority() < currentProcess.getPriority())) {
                if (currentProcess != null && currentProcess.getRemainingServiceTime() > 0) {
                    currentProcess.addTimeSlice(currentTime - 1, currentTime, 0); // 대기 구간 추가
                    readyQueue.offer(currentProcess);
                }
                currentProcess = readyQueue.poll();
                if (currentProcess.getStartTime() == -1) {
                    currentProcess.setStartTime(currentTime);
                }
                currentProcess.addTimeSlice(currentTime, currentTime + 1, 1); // 실행 구간 추가
            } else if (currentProcess != null) {
                currentProcess.addTimeSlice(currentTime - 1, currentTime, 1); // 실행 구간 추가
            }

            currentTime++;
            if (currentProcess != null) {
                currentProcess.setRemainingServiceTime(currentProcess.getRemainingServiceTime() - 1);
                if (currentProcess.getRemainingServiceTime() == 0) {
                    currentProcess.setFinishTime(currentTime);
                    currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getServiceTime());
                    completedProcesses.add(currentProcess);
                    currentProcess = null;
                }
            }
        }

        // 평균 대기 시간 및 반환 시간 계산
        for (Process p : completedProcesses) {
            totalWaitingTime += p.getWaitingTime();
            totalTurnaroundTime += p.getTurnaroundTime();
        }
        avgWaitingTime = totalWaitingTime / completedProcesses.size();
        avgTurnaroundTime = totalTurnaroundTime / completedProcesses.size();

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("Preemptive Priority Scheduling Results", completedProcesses, avgWaitingTime, avgTurnaroundTime));
    }
}
