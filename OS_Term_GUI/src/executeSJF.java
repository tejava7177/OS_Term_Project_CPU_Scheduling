import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

// SJF(Shortest Job First) 스케줄링 알고리즘을 실행하는 클래스
public class executeSJF {
    private List<Process> processes; // 프로세스 목록을 저장할 리스트
    private float avgWaitingTime = 0; // 평균 대기 시간
    private float avgTurnaroundTime = 0; // 평균 반환 시간
    private float avgResponseTime = 0; // 평균 응답 시간

    // 프로세스 리스트를 받아 객체 내부에 새로운 리스트로 복사하여 초기화
    public executeSJF(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
        // 원래 입력 순서를 설정
        for (int i = 0; i < processes.size(); i++) {
            this.processes.get(i).setOriginalOrder(i);
        }
    }

    // SJF 스케줄링 알고리즘 실행 메서드
    public void run() {
        // 서비스 시간을 기준으로 하는 우선순위 큐를 초기화
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getServiceTime));

        int currentTime = 0;                        // 현재 시간을 0으로 초기화
        float totalWaitingTime = 0;                 // 총 대기 시간
        float totalTurnaroundTime = 0;              // 총 반환 시간
        float totalResponseTime = 0;                // 총 응답 시간
        int processedCount = 0;                     // 처리된 프로세스 수

        // 모든 프로세스가 처리될 때까지 반복
        while (processedCount < processes.size()) {
            // 현재 시간 이하로 도착한 프로세스를 준비 큐에 추가
            for (Process process : processes) {
                //// 현재 시간보다 일찍 도착하고 아직 방문하지 않은 프로세스 찾기
                if (process.getArrivalTime() <= currentTime && !process.isVisited()) {
                    readyQueue.offer(process);
                    process.setVisited(true); // 프로세스 방문 했다는 표시
                }
            }

            // 준비 큐가 비어 있다면, 다음 도착할 프로세스의 도착 시간까지 현재 시간을 넘김
            if (readyQueue.isEmpty()) {
                int minArrivalTime = Integer.MAX_VALUE;

                // 아직 방문하지 않은 프로세스 중에서 최소값 찾기
                for (Process process : processes) {
                    if (!process.isVisited() && process.getArrivalTime() < minArrivalTime) {
                        minArrivalTime = process.getArrivalTime();
                    }
                }
                // 만약 유효한 최소 도착 시간이 발견되었다면, 현재 시간을 해당 도착 시간으로 설정.
                if (minArrivalTime != Integer.MAX_VALUE) {
                    currentTime = minArrivalTime;
                }
                continue;
            }


            // 준비 큐에서 서비스 시간이 가장 짧은 프로세스를 꺼내어 실행
            Process currentProcess = readyQueue.poll();
            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();
            }


            // 프로세스의 시작 시간, 종료 시간, 대기 시간, 반환 시간 및 응답 시간 설정
            currentProcess.setStartTime(currentTime);
            currentProcess.addTimeSlice(currentTime, currentTime + currentProcess.getServiceTime(), 1); // 실행 구간 추가
            currentProcess.setFinishTime(currentTime + currentProcess.getServiceTime());
            currentProcess.setWaitingTime(currentTime - currentProcess.getArrivalTime());
            currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());
            currentProcess.setResponseTime(currentProcess.getWaitingTime() + 1);

            // 현재 시간을 서비스 시간만큼 증가시키고, 총 대기 시간과 총 반환 시간, 총 응답 시간 업데이트
            currentTime += currentProcess.getServiceTime();
            totalWaitingTime += currentProcess.getWaitingTime();
            totalTurnaroundTime += currentProcess.getTurnaroundTime();
            totalResponseTime += currentProcess.getResponseTime();
            processedCount++; // 처리된 프로세스 수 증가
        }

        // 평균 대기 시간, 평균 반환 시간 및 평균 응답 시간 계산
        avgWaitingTime = totalWaitingTime / processes.size();
        avgTurnaroundTime = totalTurnaroundTime / processes.size();
        avgResponseTime = totalResponseTime / processes.size();

        // 원래 입력 순서대로 정렬하여 결과를 표시
        processes.sort(Comparator.comparingInt(Process::getOriginalOrder));

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("SJF Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime, avgResponseTime));
    }
}
