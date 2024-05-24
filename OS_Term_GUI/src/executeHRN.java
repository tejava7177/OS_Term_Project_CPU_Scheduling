import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// HRN(Highest Response Ratio Next) 스케줄링 알고리즘을 실행하는 클래스
public class executeHRN {
    private List<Process> processes;
    private float avgWaitingTime = 0;
    private float avgTurnaroundTime = 0;


    public executeHRN(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
    }


    public void run() {
        int currentTime = 0;                            // 현재 시간을 추적
        float totalWaitingTime = 0;                     // 총 대기 시간
        float totalTurnaroundTime = 0;                  // 총 반환 시간
        int processedCount = 0;                         // 처리된 프로세스 수

        // 모든 프로세스가 처리될 때까지 반복
        while (processedCount < processes.size()) {
            // 현재 시간 이하로 도착한 프로세스의 응답 비율을 계산하여 설정
            for (Process process : processes) {
                if (process.getArrivalTime() <= currentTime && !process.isVisited()) {
                    // 응답 비율 = (대기 시간 + 서비스 시간) / 서비스 시간
                    double responseRatio = (currentTime - process.getArrivalTime() + process.getServiceTime()) / (double) process.getServiceTime();
                    process.setResponseRatio(responseRatio);
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
                continue; // 프로세스가 없다면 루프의 다음 반복으로 이동
            }

            // 도착 시간 전이라면 도착 시간으로 현재 시간을 조정
            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();
            }

            // 프로세스의 시작 시간, 종료 시간, 대기 시간 및 반환 시간을 설정
            currentProcess.setStartTime(currentTime);                                                                   // 프로세스 시작 시간 설정
            currentProcess.addTimeSlice(currentTime, currentTime + currentProcess.getServiceTime(), 1);      // 실행 구간 추가
            currentProcess.setFinishTime(currentTime + currentProcess.getServiceTime());                                // 프로세스 종료 시간 설정
            currentProcess.setWaitingTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());             // 대기 시간 설정
            currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());         // 반환 시간 설정
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
