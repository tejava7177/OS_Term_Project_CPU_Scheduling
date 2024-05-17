import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class executeNpP {
    private List<Process> processes;
    private float avgWaitingTime = 0;
    private float avgTurnaroundTime = 0;

    public executeNpP(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
    }

    public void run() {
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        int currentTime = 0;
        List<Process> completedProcesses = new ArrayList<>();
        List<Process> readyQueue = new ArrayList<>();

        // 처음에 도착하는 프로세스들을 준비 큐에 추가
        int index = 0;
        while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
            readyQueue.add(processes.get(index));
            index++;
        }

        // 우선순위에 따라 프로세스 선택 및 실행
        while (!readyQueue.isEmpty()) {
            // 우선순위가 가장 높은(숫자가 가장 낮은) 프로세스를 선택
            Process currentProcess = readyQueue.stream()
                    .min(Comparator.comparingInt(Process::getPriority))
                    .orElse(null);

            // 프로세스 실행
            if (currentProcess != null) {
                readyQueue.remove(currentProcess);
                if (currentTime < currentProcess.getArrivalTime()) {
                    currentTime = currentProcess.getArrivalTime();
                }
                currentProcess.setStartTime(currentTime);
                currentProcess.setFinishTime(currentTime + currentProcess.getServiceTime());
                currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());
                currentTime += currentProcess.getServiceTime();
                completedProcesses.add(currentProcess);

                // 다음 도착하는 프로세스를 준비 큐에 추가
                while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
                    readyQueue.add(processes.get(index));
                    index++;
                }
            }
        }

        // 평균 대기 시간 및 반환 시간 계산
        float totalWaitingTime = 0;
        float totalTurnaroundTime = 0;
        for (Process p : completedProcesses) {
            totalWaitingTime += p.getWaitingTime();
            totalTurnaroundTime += p.getTurnaroundTime();
        }
        avgWaitingTime = totalWaitingTime / completedProcesses.size();
        avgTurnaroundTime = totalTurnaroundTime / completedProcesses.size();

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("Non-Preemptive Priority Scheduling Results", completedProcesses, avgWaitingTime, avgTurnaroundTime));
    }
}
