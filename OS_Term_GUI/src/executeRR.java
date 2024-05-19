import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class executeRR {
    private List<Process> processes;
    private int timeSlice;
    private float avgWaitingTime = 0;
    private float avgTurnaroundTime = 0;

    public executeRR(List<Process> processes, int timeSlice) {
        this.processes = new ArrayList<>(processes);
        this.timeSlice = timeSlice;
    }

    public void run() {
        Queue<Process> queue = new LinkedList<>();
        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        processes.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        int index = 0;

        // 초기 큐 로딩
        while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
            queue.add(processes.get(index));
            index++;
        }

        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();
            if (currentProcess.getStartTime() == -1) {
                currentProcess.setStartTime(currentTime);
            }

            int executionTime = Math.min(currentProcess.getRemainingServiceTime(), timeSlice);
            currentProcess.addTimeSlice(currentTime, currentTime + executionTime, 1);  // 실행 구간 추가
            currentTime += executionTime;
            currentProcess.setRemainingServiceTime(currentProcess.getRemainingServiceTime() - executionTime);

            // 다음 프로세스가 도착할 때까지 시간을 뛰어넘기
            while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
                queue.add(processes.get(index));
                index++;
            }

            if (currentProcess.getRemainingServiceTime() > 0) {
                queue.add(currentProcess);  // 남은 시간이 있다면 큐의 끝으로
                currentProcess.addTimeSlice(currentTime - executionTime, currentTime, 0);  // 대기 구간 추가
            } else {
                currentProcess.setFinishTime(currentTime);
                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getServiceTime());
                completedProcesses.add(currentProcess);
            }
        }

        // 평균 대기 시간과 반환 시간 계산
        for (Process p : completedProcesses) {
            totalWaitingTime += p.getWaitingTime();
            totalTurnaroundTime += p.getTurnaroundTime();
        }
        avgWaitingTime = (float) totalWaitingTime / completedProcesses.size();
        avgTurnaroundTime = (float) totalTurnaroundTime / completedProcesses.size();

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("Round Robin Scheduling Results", completedProcesses, avgWaitingTime, avgTurnaroundTime));
    }
}
