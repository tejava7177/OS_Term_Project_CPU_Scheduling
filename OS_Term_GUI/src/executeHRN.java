import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// HRN(Highest Response Ratio Next) 스케줄링 알고리즘을 실행하는 클래스
public class executeHRN {
    private List<Process> processes;                // 프로세스 목록 저장 리스트
    private float avgWaitingTime = 0;               // AWT
    private float avgTurnaroundTime = 0;            // ATT
    private float avgResponseTime = 0;              // ART

    // 생성자 : 프로세스 리스트를 새로운 리스트로 복사하여 초기화
    public executeHRN(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
        // 원래 입력 순서를 설정
        for (int i = 0; i < processes.size(); i++) {
            this.processes.get(i).setOriginalOrder(i);
        }
    }

    // HRN 스케줄링 알고리즘 실행 메서드
    // HRN 알고리즘은 응답 비율을 따져서 가장 효율적인 프로세스를 우선적으로 처리하는 방식이다.
    // 응답 비율 계산 = (대기시간 + 서비스 시간) / 서비스 시간
    public void run() {
        int currentTime = 0;                            // 현재 시간
        float totalWaitingTime = 0;                     // 총 대기 시간
        float totalTurnaroundTime = 0;                  // 총 반환 시간
        float totalResponseTime = 0;                    // 총 응답 시간
        int processedCount = 0;                         // 프로세스 수

        // 모든 프로세스가 처리될 때까지 반복
        while (processedCount < processes.size()) {
            // 현재 시간 이하로 도착한 프로세스의 응답 비율을 계산하여 설정
            for (Process process : processes) {
                // 현재 시간 이하로 도착했고 아직 실행되지 않은 프로세스인지 확인
                if (process.getArrivalTime() <= currentTime && !process.isVisited()) {
                    // 응답 비율 = (대기 시간 + 서비스 시간) / 서비스 시간  자료형은 소수 double
                    double responseRatio = (currentTime - process.getArrivalTime() + process.getServiceTime()) / (double) process.getServiceTime();
                    process.setResponseRatio(responseRatio);            // 응답률 저장
                }
            }

            // 현재 시간 이하로 도착한 프로세스 중 응답 비율이 가장 높은 프로세스를 선택
            Process currentProcess = null;  // 현재 처리할 프로세스를 저장할 변수
            double highestResponseRatio = -1;  // 가장 높은 응답 비율을 저장할 변수

            // 모든 프로세스를 확인하여 현재 시간 이하로 도착했고 아직 실행되지 않은 프로세스를 찾음
            for (Process process : processes) {
                if (!process.isVisited() && process.getArrivalTime() <= currentTime) {
                    // 응답 비율을 계산
                    double responseRatio = (currentTime - process.getArrivalTime() + process.getServiceTime()) / (double) process.getServiceTime();
                    // 가장 높은 응답 비율을 가진 프로세스를 선택
                    if (responseRatio > highestResponseRatio) {
                        highestResponseRatio = responseRatio;
                        currentProcess = process;
                    }
                }
            }

            // 만약 현재 시간 이하로 도착한 프로세스가 없다면, 다음 도착할 프로세스의 도착 시간까지 현재 시간을 넘김
            if (currentProcess == null) {
                int earliestArrivalTime = Integer.MAX_VALUE;  // 가장 이른 도착 시간을 저장할 변수

                // 아직 실행되지 않은 프로세스 중 가장 빨리 도착하는 프로세스를 찾음
                for (Process process : processes) {
                    if (!process.isVisited() && process.getArrivalTime() < earliestArrivalTime) {
                        earliestArrivalTime = process.getArrivalTime();
                    }
                }

                // 다음 도착할 프로세스의 도착 시간으로 현재 시간을 업데이트
                currentTime = earliestArrivalTime;
                continue; // 프로세스가 없다면 루프의 다음 반복으로 이동
            }

            // 도착 시간 전이라면 도착 시간으로 현재 시간을 조정
            if (currentTime < currentProcess.getArrivalTime()) {
                currentTime = currentProcess.getArrivalTime();
            }

            // 프로세스의 시작 시간, 종료 시간, 대기 시간, 반환 시간 및 응답 시간 설정
            currentProcess.setStartTime(currentTime);                                                                   // 프로세스 시작 시간 설정
            currentProcess.addTimeSlice(currentTime, currentTime + currentProcess.getServiceTime(), 1);      // 실행 구간 추가
            currentProcess.setFinishTime(currentTime + currentProcess.getServiceTime());                                // 프로세스 종료 시간 설정
            currentProcess.setWaitingTime(currentProcess.getStartTime() - currentProcess.getArrivalTime());             // 대기 시간 설정
            currentProcess.setTurnaroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());         // 반환 시간 설정
            currentProcess.setResponseTime(currentProcess.getWaitingTime() + 1);                                        // 응답 시간 = 대기 시간 + 1
            currentProcess.setVisited(true);                                                                // 프로세스가 실행되었음을 표시

            // 현재 시간을 서비스 시간만큼 증가
            currentTime += currentProcess.getServiceTime();

            //총 대기 시간과 총 반환 시간, 총 응답 시간 업데이트
            totalWaitingTime += currentProcess.getWaitingTime();
            totalTurnaroundTime += currentProcess.getTurnaroundTime();
            totalResponseTime += currentProcess.getResponseTime();
            processedCount++; // 처리된 프로세스 수 증가
        }

        // 평균 대기 시간과 평균 반환 시간 계산
        avgWaitingTime = totalWaitingTime / processes.size();
        avgTurnaroundTime = totalTurnaroundTime / processes.size();
        avgResponseTime = totalResponseTime / processes.size();


        // 원래 입력 순서대로 정렬하여 결과를 표시
        processes.sort(Comparator.comparingInt(Process::getOriginalOrder));

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("HRN Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime, avgResponseTime));
    }
}
