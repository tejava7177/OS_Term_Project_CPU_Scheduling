import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

// FCFS(First Come First Serve) 스케줄링 알고리즘을 실행하는 클래스
public class executeFCFS {
    private List<Process> processes;                // 프로세스 목록을 저장할 리스트
    private float avgWaitingTime = 0;               // 평균 대기 시간
    private float avgTurnaroundTime = 0;            // 평균 반환 시간

    // 프로세스 리스트를 받아 객체 내부에 새로운 리스트로 복사하여 초기화
    public executeFCFS(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
    }

    // FCFS 스케줄링 알고리즘 실행 메서드
    public void run() {
        int currentTime = 0;                // 현재 시간, 시작 시 0부터 시작
        float totalWaitingTime = 0;         // 총 대기 시간
        float totalTurnaroundTime = 0;      // 총 반환 시간

        // 모든 프로세스에 대해 반복
        for (Process process : processes) {
            // 프로세스 도착 시간이 현재 시간보다 크면 현재 시간을 도착 시간으로 업데이트
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }


            process.setStartTime(currentTime);                                                         // 프로세스의 시작 시간 설정
            process.addTimeSlice(currentTime, currentTime + process.getServiceTime(), 1);   // 프로세스 실행 시간을 시각화하기 위한 시간 구간 추가


            currentTime += process.getServiceTime();                                                  // 현재 시간을 서비스 시간만큼 증가
            process.setFinishTime(currentTime);                                                       // 프로세스의 종료 시간 설정
            process.setTurnaroundTime(currentTime - process.getArrivalTime());                        // 반환 시간과 대기 시간 계산
            process.setWaitingTime(process.getTurnaroundTime() - process.getServiceTime());

            // 총 대기 시간과 총 반환 시간 누적합 계산 업데이트
            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        // 평균 대기 시간과 평균 반환 시간 계산
        avgWaitingTime = totalWaitingTime / processes.size();
        avgTurnaroundTime = totalTurnaroundTime / processes.size();

        // 결과를 그래픽 사용자 인터페이스(GUI)로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("FCFS Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime));
    }
}
