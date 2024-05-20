import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class executeNpP {
    private List<Process> processes;  // 프로세스 리스트
    private float avgWaitingTime = 0;  // 평균 대기 시간
    private float avgTurnaroundTime = 0;  // 평균 반환 시간

    public executeNpP(List<Process> processes) {
        // 생성자: 프로세스 리스트를 초기화
        this.processes = new ArrayList<>(processes);
    }

    public void run() {
        // 준비 큐: 우선순위 기준으로 정렬 (우선순위가 낮을수록 높은 우선순위)
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority));
        int currentTime = 0;  // 현재 시간
        float totalWaitingTime = 0;  // 총 대기 시간
        float totalTurnaroundTime = 0;  // 총 반환 시간
        int processedCount = 0;  // 처리된 프로세스 수

        // 모든 프로세스가 처리될 때까지 반복
        while (processedCount < processes.size()) {
            // 현재 시간에서 실행 가능한 프로세스를 준비 큐에 추가
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && !process.isVisited()) {
                    readyQueue.offer(process);
                    process.setVisited(true);  // 준비 큐에 추가되었음을 표시
                }
            }

            // 준비 큐가 비어있는 경우 다음 도착할 프로세스의 도착 시간까지 현재 시간을 점프
            if (readyQueue.isEmpty()) {
                currentTime = processes.stream()
                        .filter(p -> !p.isVisited())  // 아직 준비 큐에 추가되지 않은 프로세스들을 필터링
                        .min(Comparator.comparingInt(Process::getArrivalTime))  // 도착 시간이 가장 빠른 프로세스를 찾음
                        .map(Process::getArrivalTime)  // 찾은 프로세스의 도착 시간을 가져옴
                        .orElse(currentTime);  // 찾은 프로세스가 없다면 현재 시간 유지
                continue;
            }

            // 준비 큐에서 우선순위가 가장 높은 프로세스를 꺼냄
            Process currentProcess = readyQueue.poll();
            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();  // 도착 시간 전이라면 도착 시간으로 시간을 조정
            }

            // 프로세스 실행
            currentProcess.setStartTime(currentTime);  // 시작 시간 설정
            currentProcess.addTimeSlice(currentTime, currentTime + currentProcess.getServiceTime(), 1);  // 실행 구간 추가
            currentProcess.setFinishTime(currentTime + currentProcess.getServiceTime());  // 종료 시간 설정
            currentProcess.setWaitingTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());  // 대기 시간 계산
            currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());  // 반환 시간 계산

            currentTime += currentProcess.getServiceTime();  // 현재 시간을 업데이트
            totalWaitingTime += currentProcess.getWaitingTime();  // 총 대기 시간 업데이트
            totalTurnaroundTime += currentProcess.getTurnaroundTime();  // 총 반환 시간 업데이트
            processedCount++;  // 처리된 프로세스 수 증가
        }

        // 평균 대기 시간과 반환 시간 계산
        avgWaitingTime = totalWaitingTime / processes.size();
        avgTurnaroundTime = totalTurnaroundTime / processes.size();

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("Non-preemptive Priority Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime));
    }
}
