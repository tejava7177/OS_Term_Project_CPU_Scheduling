import javax.swing.*;
import java.util.*;

public class executeRR {
    private List<Process> processes;    // 프로세스 리스트
    private int timeSlice;              // 타임 슬라이스
    private float avgWaitingTime = 0;   // 평균 대기 시간
    private float avgTurnaroundTime = 0;  // 평균 반환 시간
    private float avgResponseTime = 0;  // 평균 응답 시간

    public executeRR(List<Process> processes, int timeSlice) {
        // 생성자: 프로세스 리스트와 타임 슬라이스를 초기화
        this.processes = new ArrayList<>(processes);
        this.timeSlice = timeSlice;
        // 원래 입력 순서를 설정
        for (int i = 0; i < processes.size(); i++) {
            this.processes.get(i).setOriginalOrder(i);
        }
    }

    public void run() {
        Queue<Process> queue = new LinkedList<>();  // 프로세스를 저장할 큐
        List<Process> completedProcesses = new ArrayList<>();  // 완료된 프로세스를 저장할 리스트
        int currentTime = 0;  // 현재 시간
        int totalWaitingTime = 0;  // 총 대기 시간
        int totalTurnaroundTime = 0;  // 총 반환 시간
        int totalResponseTime = 0;  // 총 응답 시간

        // 프로세스를 도착 시간 기준으로 정렬
        processes.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        int index = 0;  // 프로세스 리스트 인덱스

        // 초기 큐 로딩: 도착 시간이 현재 시간 이하인 프로세스를 큐에 추가
        while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
            queue.add(processes.get(index));
            index++;
        }

        // 큐가 빌 때까지 반복
        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();  // 큐에서 프로세스를 꺼냄
            if (currentProcess.getStartTime() == -1) {
                currentProcess.setStartTime(currentTime);  // 프로세스 시작 시간 설정

            }

            int executionTime = Math.min(currentProcess.getRemainingServiceTime(), timeSlice);  // 실행 시간 결정
            currentProcess.addTimeSlice(currentTime, currentTime + executionTime, 1);  // 실행 구간 추가
            currentTime += executionTime;  // 현재 시간 업데이트
            currentProcess.setRemainingServiceTime(currentProcess.getRemainingServiceTime() - executionTime);  // 남은 실행 시간 업데이트

            // 다음 프로세스가 도착할 때까지 시간을 뛰어넘기
            while (index < processes.size() && processes.get(index).getArrivalTime() <= currentTime) {
                queue.add(processes.get(index));
                index++;
            }

            // 프로세스가 완료되지 않은 경우
            if (currentProcess.getRemainingServiceTime() > 0) {
                queue.add(currentProcess);  // 남은 시간이 있다면 큐의 끝으로
                currentProcess.addTimeSlice(currentTime - executionTime, currentTime, 0);  // 대기 구간 추가
            } else {
                // 프로세스가 완료된 경우
                currentProcess.setFinishTime(currentTime);  // 종료 시간 설정
                currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());  // 반환 시간 계산
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getServiceTime());  // 대기 시간 계산
                currentProcess.setResponseTime(currentProcess.getWaitingTime() + 1);  // 응답 시간 = 대기 시간 + 1
                completedProcesses.add(currentProcess);  // 완료된 프로세스 리스트에 추가
            }
        }

        // 평균 대기 시간, 반환 시간 및 응답 시간 계산
        for (Process p : completedProcesses) {
            totalWaitingTime += p.getWaitingTime();
            totalTurnaroundTime += p.getTurnaroundTime();
            totalResponseTime += p.getResponseTime();
        }
        avgWaitingTime = (float) totalWaitingTime / completedProcesses.size();
        avgTurnaroundTime = (float) totalTurnaroundTime / completedProcesses.size();
        avgResponseTime = (float) totalResponseTime / completedProcesses.size();

        // 원래 입력 순서대로 정렬하여 결과를 표시
        completedProcesses.sort(Comparator.comparingInt(Process::getOriginalOrder));

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("Round Robin Scheduling Results", completedProcesses, avgWaitingTime, avgTurnaroundTime, avgResponseTime));
    }
}
