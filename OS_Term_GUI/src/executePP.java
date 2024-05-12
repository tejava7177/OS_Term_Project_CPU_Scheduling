//import javax.swing.*;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.PriorityQueue;
//
//public class executePP {
//    private List<Process> processes;
//    private float avgWaitingTime = 0;
//    private float avgTurnaroundTime = 0;
//
//    public executePP(List<Process> processes) {
//        this.processes = new ArrayList<>(processes);
//    }
//
//    public void run() {
//        // 우선순위 큐를 사용하여 프로세스 관리
//        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
//                Comparator.comparingInt(Process::getPriority)
//                        .thenComparingInt(Process::getArrivalTime)
//        );
//
//        int currentTime = 0;
//        float totalWaitingTime = 0;
//        float totalTurnaroundTime = 0;
//        Process currentProcess = null;
//
//        while (!processes.isEmpty() || currentProcess != null || !readyQueue.isEmpty()) {
//            // 현재 시간에 도착하는 모든 프로세스를 준비 큐에 추가
//            int finalCurrentTime = currentTime;
//            processes.removeIf(process -> {
//                if (process.getArrivalTime() <= finalCurrentTime) {
//                    readyQueue.add(process);
//                    return true;
//                }
//                return false;
//            });
//
//            if (currentProcess != null && !currentProcess.isVisited()) {
//                // 현재 프로세스가 완료되지 않았다면 준비 큐로 다시 추가
//                readyQueue.add(currentProcess);
//            }
//
//            // 준비 큐에서 다음 프로세스 선택
//            currentProcess = readyQueue.poll();
//            if (currentProcess == null) {
//                currentTime++;
//                continue;
//            }
//
//            // 프로세스 실행
//            if (currentTime < currentProcess.getArrivalTime()) {
//                currentTime = currentProcess.getArrivalTime();
//            }
//            currentProcess.setStartTime(currentTime);
//            int executionTime = Math.min(currentProcess.getServiceTime(), currentProcess.getRemainingServiceTime());
//            currentProcess.setRemainingServiceTime(currentProcess.getRemainingServiceTime() - executionTime);
//            currentTime += executionTime;
//
//            // 프로세스 완료 처리
//            if (currentProcess.getRemainingServiceTime() <= 0) {
//                currentProcess.setFinishTime(currentTime);
//                currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());
//                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getServiceTime());
//                totalWaitingTime += currentProcess.getWaitingTime();
//                totalTurnaroundTime += currentProcess.getTurnaroundTime();
//                currentProcess = null; // 현재 프로세스를 완료 처리
//            }
//        }
//
//        // 평균 시간 계산
//        int totalProcesses = processes.size();
//        avgWaitingTime = totalWaitingTime / totalProcesses;
//        avgTurnaroundTime = totalTurnaroundTime / totalProcesses;
//
//        // 결과를 새 창에서 보여줌
//        SwingUtilities.invokeLater(() -> new ResultDisplay("Preemptive Priority Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime));
//    }
//}

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.List;

public class executePP {
    private List<Process> processes;
    private PriorityQueue<Process> readyQueue;
    private List<Process> completedProcesses = new ArrayList<>();

    public executePP(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
        this.readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority));
    }

    public void run() {
        int currentTime = 0;
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
                    readyQueue.offer(currentProcess);
                }
                currentProcess = readyQueue.poll();
                if (currentProcess.getStartTime() == -1) {
                    currentProcess.setStartTime(currentTime);
                }
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

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> displayResults());
    }

    private void displayResults() {
        // GUI를 통해 결과를 표시하는 ResultDisplay 클래스 호출
        new ResultDisplay("Preemptive Priority Scheduling Results", completedProcesses,
                calculateAverageWaitingTime(), calculateAverageTurnaroundTime());
    }

    private float calculateAverageWaitingTime() {
        float totalWaitingTime = 0;
        for (Process p : completedProcesses) {
            totalWaitingTime += p.getWaitingTime();
        }
        return totalWaitingTime / completedProcesses.size();
    }

    private float calculateAverageTurnaroundTime() {
        float totalTurnaroundTime = 0;
        for (Process p : completedProcesses) {
            totalTurnaroundTime += p.getTurnaroundTime();
        }
        return totalTurnaroundTime / completedProcesses.size();
    }
}

