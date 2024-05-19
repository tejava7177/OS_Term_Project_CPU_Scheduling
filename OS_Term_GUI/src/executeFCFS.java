//import javax.swing.*;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
//public class executeFCFS {
//    private List<Process> processes;                // process 객체 리스트를 받음
//    private float avgWaitingTime = 0;               // AWT 변수를 클래스 멤버 변수로 선언
//    private float avgTurnaroundTime = 0;            // ATT 변수를 클래스 멤버 변수로 선언
//
//    public executeFCFS(List<Process> processes) {
//        this.processes = processes;
//    }
//
//    //알고리즘 실행
//    public void run() {
//
//        // 도착 시간 순으로 프로세스를 정렬
//        Collections.sort(processes, Comparator.comparingInt(Process::getArrivalTime));
//
//        int currentTime = 0;                // 현재 시간 변수 생성
//        float totalWaitingTime = 0;         // 총 대기 시간
//        float totalTurnaroundTime = 0;      // 총 실행 시긴
//
//        for (Process process : processes) {
//            // 시뮬레이션에서 현재 시간이 프로세스의 도착 시간보다 작은 경우, 도착 시간까지 시간을 이동
//            if (currentTime < process.getArrivalTime()) {
//                currentTime = process.getArrivalTime();
//            }
//
//            process.setStartTime(currentTime);                                                // 프로세스의 시작 시간을 현재 시간으로 설정
//            process.setFinishTime(currentTime + process.getServiceTime());                    // 프로세스의 종료 시간을 시작 시간 + 서비스 시간으로 설정
//            process.setWaitingTime(process.getStartTime() - process.getArrivalTime());       // 대기 시간을 계산: 시작 시간 - 도착 시간
//            process.setTurnaroundTime(process.getFinishTime() - process.getArrivalTime());  // 반환 시간을 계산: 종료 시간 - 도착 시간
//
//            // 프로세스의 종료 시간을 현재 시간으로 업데이트
//            currentTime = process.getFinishTime();
//
//            totalWaitingTime += process.getWaitingTime();               // 총 대기 시간 대기 시간 누적
//            totalTurnaroundTime += process.getTurnaroundTime();         // 총 반환 시간 반환 시간 누적
//        }
//
//        // 평균 시간 계산
//        avgWaitingTime = totalWaitingTime / processes.size();           // AWT 계산 후 저장
//        avgTurnaroundTime = totalTurnaroundTime / processes.size();     // ATT 계산 후 저장
//
//        // 결과를 GUI 환경에 출력.
//        SwingUtilities.invokeLater(() -> new ResultDisplay("FCFS Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime));
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
import java.util.List;

public class executeFCFS {
    private List<Process> processes;
    private float avgWaitingTime = 0;
    private float avgTurnaroundTime = 0;

    public executeFCFS(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
    }

    public void run() {
        int currentTime = 0;
        float totalWaitingTime = 0;
        float totalTurnaroundTime = 0;

        for (Process process : processes) {
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }

            process.setStartTime(currentTime);
            process.addTimeSlice(currentTime, currentTime + process.getServiceTime(), 1); // 실행 구간 추가

            currentTime += process.getServiceTime();
            process.setFinishTime(currentTime);
            process.setTurnaroundTime(currentTime - process.getArrivalTime());
            process.setWaitingTime(process.getTurnaroundTime() - process.getServiceTime());

            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        avgWaitingTime = totalWaitingTime / processes.size();
        avgTurnaroundTime = totalTurnaroundTime / processes.size();

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("FCFS Scheduling Results", processes, avgWaitingTime, avgTurnaroundTime));
    }
}
