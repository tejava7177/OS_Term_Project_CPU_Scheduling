import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// HRN(Highest Response Ratio Next) 스케줄링 알고리즘을 실행하는 클래스
public class executeHRN {
    private List<Process> processes; // 프로세스 목록을 저장할 리스트
    private float avgWaitingTime = 0; // 평균 대기 시간
    private float avgTurnaroundTime = 0; // 평균 반환 시간

    // 생성자: 프로세스 리스트를 받아 객체 내부에 새로운 리스트로 복사하여 초기화
    public executeHRN(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
    }

    // HRN 스케줄링 알고리즘 실행 메서드
    public void run() {
        int currentTime = 0; // 현재 시간을 0으로 초기화
        float totalWaitingTime = 0; // 총 대기 시간
        float totalTurnaroundTime = 0; // 총 반환 시간
        int processedCount = 0; // 처리된 프로세스 수

        // 모든 프로세스가 처리될 때까지 반복
        while (processedCount < processes.size()) {
            // 현재 시간 이하로 도착한 프로세스의 응답 비율을 계산하여 설정
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && !process.isVisited()) {
                    // 응답 비율 = (대기 시간 + 서비스 시간) / 서비스 시간
                    process.setResponseRatio((currentTime - process.getArrivalTime() + process.getServiceTime()) / (double) process.getServiceTime());
                }
            }

            // 현재 시간 이하로 도착한 프로세스 중 응답 비율이 가장 높은 프로세스를 선택
            int finalCurrentTime = currentTime;
            Process currentProcess = processes.stream()
                    .filter(p -> !p.isVisited() && p.getArrivalTime() <= finalCurrentTime)
                    .max(Comparator.comparingDouble(Process::getResponseRatio))
                    .orElse(null);

            // 만약 현재 시간 이하로 도착한 프로세스가 없다면, 다음 도착할 프로세스의 도착 시간까지 현재 시간을 점프
            if (currentProcess == null) {
                currentTime = processes.stream()
                        .filter(p -> !p.isVisited())
                        .min(Comparator.comparingInt(Process::getArrivalTime))
                        .map(Process::getArrivalTime)
                        .orElse(currentTime);
                continue;
            }

            // 도착 시간 전이라면 도착 시간으로 시간을 조정
            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();
            }

            // 프로세스의 시작 시간, 종료 시간, 대기 시간 및 반환 시간 설정
            currentProcess.setStartTime(currentTime);
            currentProcess.addTimeSlice(currentTime, currentTime + currentProcess.getServiceTime(), 1); // 실행 구간 추가
            currentProcess.setFinishTime(currentTime + currentProcess.getServiceTime());
            currentProcess.setWaitingTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());
            currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());
            currentProcess.setVisited(true); // 프로세스가 실행되었음을 표시

            // 현재 시간을 서비스 시간만큼 증가시키고, 총 대기 시간과 총 반환 시간 업데이트
            currentTime += currentProcess.getServiceTime();
            totalWaitingTime += currentProcess.getWaitingTime();
            totalTurnaroundTime += currentProcess.getTurnaroundTime();
            processedCount++; // 처리된 프로세스 수 증가
        }

        // 평균 대기 시간과 평균 반환 시간 계산
        avgWaitingTime = totalWaitingTime / processes.size();
        avgTurnaroundTime = totalTurnaroundTime / processes.size();

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("HRN Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime));
    }
}
