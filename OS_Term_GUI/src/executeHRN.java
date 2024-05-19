//import javax.swing.*;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//public class executeHRN {
//    private List<Process> processes;
//    private float avgWaitingTime = 0;
//    private float avgTurnaroundTime = 0;
//
//    public executeHRN(List<Process> processes) {
//        this.processes = new ArrayList<>(processes);
//    }
//
//    public void run() {
//        int currentTime = 0;
//        float totalWaitingTime = 0;
//        float totalTurnaroundTime = 0;
//
//        List<Process> completedProcesses = new ArrayList<>();
//
//        while (!processes.isEmpty()) {
//            List<Process> availableProcesses = new ArrayList<>();
//            for (Process process : processes) {
//                if (process.getArrivalTime() <= currentTime) {
//                    availableProcesses.add(process);
//                }
//            }
//
//            if (availableProcesses.isEmpty()) {
//                currentTime++;
//                continue;
//            }
//
//            for (Process process : availableProcesses) {
//                double waitingTime = currentTime - process.getArrivalTime();
//                double responseRatio = (waitingTime + process.getServiceTime()) / (double) process.getServiceTime();
//                process.setResponseRatio(responseRatio);
//            }
//
//            Process nextProcess = availableProcesses.stream()
//                    .max(Comparator.comparingDouble(Process::getResponseRatio))
//                    .orElse(null);
//
//            assert nextProcess != null;
//            nextProcess.setStartTime(currentTime);
//            nextProcess.setFinishTime(currentTime + nextProcess.getServiceTime());
//            nextProcess.setWaitingTime(nextProcess.getStartTime() - nextProcess.getArrivalTime());
//            nextProcess.setTurnaroundTime(nextProcess.getFinishTime() - nextProcess.getArrivalTime());
//
//            processes.remove(nextProcess);
//            completedProcesses.add(nextProcess);
//
//            currentTime = nextProcess.getFinishTime();
//
//            totalWaitingTime += nextProcess.getWaitingTime();
//            totalTurnaroundTime += nextProcess.getTurnaroundTime();
//        }
//
//        avgWaitingTime = totalWaitingTime / completedProcesses.size();
//        avgTurnaroundTime = totalTurnaroundTime / completedProcesses.size();
//
//
//        SwingUtilities.invokeLater(() -> new ResultDisplay("HRN Scheduling Results", completedProcesses, avgWaitingTime, avgTurnaroundTime));
//    }
//}

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class executeHRN {
    private List<Process> processes;
    private float avgWaitingTime = 0;
    private float avgTurnaroundTime = 0;

    public executeHRN(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
    }

    public void run() {
        int currentTime = 0;
        float totalWaitingTime = 0;
        float totalTurnaroundTime = 0;
        int processedCount = 0;

        while (processedCount < processes.size()) {
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && !process.isVisited()) {
                    process.setResponseRatio((currentTime - process.getArrivalTime() + process.getServiceTime()) / (double) process.getServiceTime());
                }
            }

            int finalCurrentTime = currentTime;
            Process currentProcess = processes.stream()
                    .filter(p -> !p.isVisited() && p.getArrivalTime() <= finalCurrentTime)
                    .max(Comparator.comparingDouble(Process::getResponseRatio))
                    .orElse(null);

            if (currentProcess == null) {
                currentTime = processes.stream()
                        .filter(p -> !p.isVisited())
                        .min(Comparator.comparingInt(Process::getArrivalTime))
                        .map(Process::getArrivalTime)
                        .orElse(currentTime);
                continue;
            }

            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();
            }

            currentProcess.setStartTime(currentTime);
            currentProcess.addTimeSlice(currentTime, currentTime + currentProcess.getServiceTime(), 1); // 실행 구간 추가
            currentProcess.setFinishTime(currentTime + currentProcess.getServiceTime());
            currentProcess.setWaitingTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());
            currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());
            currentProcess.setVisited(true);

            currentTime += currentProcess.getServiceTime();
            totalWaitingTime += currentProcess.getWaitingTime();
            totalTurnaroundTime += currentProcess.getTurnaroundTime();
            processedCount++;
        }

        avgWaitingTime = totalWaitingTime / processes.size();
        avgTurnaroundTime = totalTurnaroundTime / processes.size();

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("HRN Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime));
    }
}
