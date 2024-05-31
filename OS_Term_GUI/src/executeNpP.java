import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class executeNpP {
    private List<Process> processes;                    // 프로세스 리스트
    private float avgWaitingTime = 0;                   // 평균 대기 시간
    private float avgTurnaroundTime = 0;                // 평균 반환 시간
    private float avgResponseTime = 0;                  // 평균 응답 시간

    public executeNpP(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
        // 원래 입력 순서를 설정
        for (int i = 0; i < processes.size(); i++) {
            this.processes.get(i).setOriginalOrder(i);
        }
    }

    public void run() {

        // 준비 큐를 우선순위 기준으로 정렬 (숫자가 낮을수록 높은 우선순위)
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority));

        int currentTime = 0;            // 현재 시간
        float totalWaitingTime = 0;     // 총 대기 시간
        float totalTurnaroundTime = 0;  // 총 반환 시간
        float totalResponseTime = 0;    // 총 응답 시간
        int processedCount = 0;         // 처리된 프로세스 수

        // 모든 프로세스가 처리될 때까지 반복
        while (processedCount < processes.size()) {
            // 현재 시간에서 실행 가능한 프로세스를 준비 큐에 추가
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && !process.isVisited()) {
                    readyQueue.offer(process);
                    process.setVisited(true);  // 준비 큐에 추가되었음을 표시
                }
            }


            // 준비 큐가 비어있는 경우, 다음 도착할 프로세스의 도착 시간까지 현재 시간을 점프
            if (readyQueue.isEmpty()) {
                int earliestArrivalTime = Integer.MAX_VALUE;

                // 아직 방문하지 않은 프로세스 중에서 가장 이른 도착 시간을 찾음
                for (Process process : processes) {
                    if (!process.isVisited() && process.getArrivalTime() < earliestArrivalTime) {
                        earliestArrivalTime = process.getArrivalTime();
                    }
                }

                // 만약 유효한 최소 도착 시간이 발견되었다면, 현재 시간을 해당 도착 시간으로 설정
                if (earliestArrivalTime != Integer.MAX_VALUE) {
                    currentTime = earliestArrivalTime;
                }

                continue;
            }


            // 준비 큐에서 우선순위가 가장 높은 프로세스를 꺼냄
            Process currentProcess = readyQueue.poll();
            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();  // 도착 시간 전이라면 도착 시간으로 시간을 조정
            }

            // 프로세스 실행
            currentProcess.setStartTime(currentTime);                                                               // 시작 시간 설정
            currentProcess.addTimeSlice(currentTime, currentTime + currentProcess.getServiceTime(), 1);  // 실행 구간 추가
            currentProcess.setFinishTime(currentTime + currentProcess.getServiceTime());                            // 종료 시간 설정
            currentProcess.setWaitingTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());         // 대기 시간 계산
            currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());     // 반환 시간 계산
            currentProcess.setResponseTime(currentProcess.getWaitingTime() + 1);                                    // 응답 시간 = 대기 시간 + 1

            currentTime += currentProcess.getServiceTime();                     // 현재 시간을 업데이트
            totalWaitingTime += currentProcess.getWaitingTime();                // 총 대기 시간 업데이트
            totalTurnaroundTime += currentProcess.getTurnaroundTime();          // 총 반환 시간 업데이트
            totalResponseTime += currentProcess.getResponseTime();              // 총 응답 시간 업데이트
            processedCount++;                                                   // 처리된 프로세스 수 증가
        }

        // 평균 대기 시간, 반환 시간 및 응답 시간 계산
        avgWaitingTime = totalWaitingTime / processes.size();
        avgTurnaroundTime = totalTurnaroundTime / processes.size();
        avgResponseTime = totalResponseTime / processes.size();

        // 원래 입력 순서대로 정렬하여 결과를 표시
        processes.sort(Comparator.comparingInt(Process::getOriginalOrder));


        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("Non-preemptive Priority Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime, avgResponseTime));
    }
}
