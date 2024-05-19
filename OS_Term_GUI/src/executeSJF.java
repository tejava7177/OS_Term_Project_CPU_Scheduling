//import javax.swing.*;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.PriorityQueue;
//
//public class executeSJF {
//    private List<Process> processes;
//    private float avgWaitingTime = 0;
//    private float avgTurnaroundTime = 0;
//
//    private PriorityQueue<Process> readyQueue;
//
//    public executeSJF(List<Process> processes) {
//        this.processes = new ArrayList<>(processes);
//        this.readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getArrivalTime));
//    }
//
//    public void run() {
//        // 프로세스 리스트를 도착 시간에 따라 오름차순으로 정렬
//
//
//        // 서비스 시간을 기준으로 하는 우선순위 큐를 생성하여 실행 준비 큐로 사용
//        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getServiceTime));
//
//        int currentTime = 0;                            // 현재 시간을 0으로 초기화
//        float totalWaitingTime = 0;                     // 총 대기 시간
//        float totalTurnaroundTime = 0;                  // 총 반환 시간
//
//        int processedCount = 0;                         // 처리된 프로세스 수
//
//        // 모든 프로세스가 처리될 때까지 반복
//        while (processedCount < processes.size()) {
//
//            for (Process process : processes) {
//                if (process.getArrivalTime() <= currentTime && !process.isVisited()) {
//                    readyQueue.offer(process);                       // 현재 시간 이하로 도착한 모든 프로세스를 준비 큐에 추가
//                    process.setVisited(true);                       // 프로세스가 준비 큐에 추가되었음을 표시
//                }
//            }
//
//            // 준비 큐가 비어있다면, 다음 도착할 프로세스의 도착 시간까지 현재 시간을 점프
//            if (readyQueue.isEmpty()) {
//                currentTime = processes.stream()
//                        .filter(p -> !p.isVisited())            // 아직 준비 큐에 추가되지 않은 프로세스들을 필터리
//                        .min(Comparator.comparingInt(Process::getArrivalTime))  //도착시간이 가장 빠른 프로세스를 찾음
//                        .map(Process::getArrivalTime)            // 찾은 프로세스의 도착 시간을 가져옴
//                        .orElse(currentTime);                    // 찾은 프로세스가 없다면 현재 시간 유지
//                continue;               //반복
//            }
//
//            // 준비 큐에서 서비스 시간이 가장 짧은 프로세스를 꺼내어 실행
//            Process currentProcess = readyQueue.poll();
//
//
//            if (currentTime < currentProcess.getArrivalTime()) {
//                currentTime = currentProcess.getArrivalTime();  // 도착 시간 전이라면 도착 시간으로 시간을 조정
//            }
//
//
//            currentProcess.setStartTime(currentTime);                                                           // 시작 시간 설정
//            currentProcess.setFinishTime(currentTime + currentProcess.getServiceTime());                             // 종료 시간 설정
//            currentProcess.setWaitingTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());         // 대기 시간 계산
//            currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());     // 반환 시간 계산
//
//            currentTime += currentProcess.getServiceTime();                     // 현재 시간을 종료 시간으로 업데이트
//            totalWaitingTime += currentProcess.getWaitingTime();                // 총 대기 시간 업데이트
//            totalTurnaroundTime += currentProcess.getTurnaroundTime();          // 총 반환 시간 업데이트
//            processedCount++;                                                   // 처리된 프로세스 수 증가
//        }
//
//        // 평균 시간 계산
//        avgWaitingTime = totalWaitingTime / processes.size();
//        avgTurnaroundTime = totalTurnaroundTime / processes.size();
//
//        // 결과를 GUI 창으로 보여줌
//        SwingUtilities.invokeLater(() -> new ResultDisplay("SJF Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime));
//    }
//
//    public float getAvgWaitingTime() {
//        return avgWaitingTime;
//    }
//
//    public float getAvgTurnaroundTime() {
//        return avgTurnaroundTime;
//    }
//}


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
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getServiceTime));
        int currentTime = 0;
        float totalWaitingTime = 0;
        float totalTurnaroundTime = 0;
        int processedCount = 0;

        while (processedCount < processes.size()) {
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && !process.isVisited()) {
                    readyQueue.offer(process);
                    process.setVisited(true);
                }
            }

            if (readyQueue.isEmpty()) {
                currentTime = processes.stream()
                        .filter(p -> !p.isVisited())
                        .min(Comparator.comparingInt(Process::getArrivalTime))
                        .map(Process::getArrivalTime)
                        .orElse(currentTime);
                continue;
            }

            Process currentProcess = readyQueue.poll();
            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();
            }

            currentProcess.setStartTime(currentTime);
            currentProcess.addTimeSlice(currentTime, currentTime + currentProcess.getServiceTime(), 1); // 실행 구간 추가
            currentProcess.setFinishTime(currentTime + currentProcess.getServiceTime());
            currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime());
            currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());

            currentTime += currentProcess.getServiceTime();
            totalWaitingTime += currentProcess.getWaitingTime();
            totalTurnaroundTime += currentProcess.getTurnaroundTime();
            processedCount++;
        }

        avgWaitingTime = totalWaitingTime / processes.size();
        avgTurnaroundTime = totalTurnaroundTime / processes.size();

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("SJF Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime));
    }

    public float getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public float getAvgTurnaroundTime() {
        return avgTurnaroundTime;
    }
}
