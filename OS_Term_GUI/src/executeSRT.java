import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class executeSRT {
    private List<Process> processes;                        // 프로세스 리스트
    private int timeSlice;                                  // 타임 슬라이스
    private float avgWaitingTime = 0;                       // 평균 대기 시간
    private float avgTurnaroundTime = 0;                    // 평균 반환 시간

    public executeSRT(List<Process> processes, int timeSlice) {
        // 생성자: 프로세스 리스트와 타임 슬라이스를 초기화
        this.processes = new ArrayList<>(processes);
        this.timeSlice = timeSlice;
    }

    public void run() {
        // 준비 큐: 남은 실행 시간 기준으로 정렬 (도착 시간이 같으면 도착 시간 기준)
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getRemainingServiceTime).thenComparingInt(Process::getArrivalTime)
        );
        List<Process> completedProcesses = new ArrayList<>();   // 완료된 프로세스를 저장할 리스트
        int currentTime = 0;                                    // 현재 시간
        int totalWaitingTime = 0;                               // 총 대기 시간
        int totalTurnaroundTime = 0;                            // 총 반환 시간

        // 모든 프로세스를 도착 시간 기준으로 정렬
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        // 모든 프로세스가 완료될 때까지 반복
        while (!completedProcesses.containsAll(processes)) {
            // 현재 시간에서 실행 가능한 프로세스를 준비 큐에 추가
            for (Process p : processes) {
                if (p.getArrivalTime() <= currentTime && !completedProcesses.contains(p) && !readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
            }

            Process currentProcess = readyQueue.poll();         // 준비 큐에서 남은 실행 시간이 가장 적은 프로세스를 꺼냄

            if (currentProcess != null) {

                if (currentProcess.getStartTime() == -1) {
                    currentProcess.setStartTime(currentTime);   // 프로세스가 처음 실행되는 경우 시작 시간을 설정
                }

                // 타임 슬라이스 또는 남은 실행 시간 중 작은 값을 실행 시간으로 설정
                int executionTime = Math.min(currentProcess.getRemainingServiceTime(), timeSlice);
                currentProcess.addTimeSlice(currentTime, currentTime + executionTime, 1);                 // 실행 구간 추가
                currentTime += executionTime;                                                                       // 현재 시간을 업데이트
                currentProcess.setRemainingServiceTime(currentProcess.getRemainingServiceTime() - executionTime);  // 남은 실행 시간을 업데이트

                // 프로세스가 완료된 경우
                if (currentProcess.getRemainingServiceTime() == 0) {
                    currentProcess.setFinishTime(currentTime);                                          // 종료 시간 설정
                    currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());    // 반환 시간 계산
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getServiceTime());  // 대기 시간 계산
                    completedProcesses.add(currentProcess);                                             // 완료된 프로세스 리스트에 추가
                    totalWaitingTime += currentProcess.getWaitingTime();                                // 총 대기 시간 업데이트
                    totalTurnaroundTime += currentProcess.getTurnaroundTime();                          // 총 반환 시간 업데이트
                } else {
                    readyQueue.add(currentProcess);                                // 프로세스가 완료되지 않은 경우 다시 준비 큐에 추가
                }
            } else {
                currentTime++;                       // 실행할 프로세스가 없는 경우 시간을 증가시켜 다음 가능한 프로세스를 대기
            }
        }

        // 평균 대기 시간과 반환 시간 계산
        avgWaitingTime = (float) totalWaitingTime / processes.size();
        avgTurnaroundTime = (float) totalTurnaroundTime / processes.size();

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("SRT Scheduling Results", completedProcesses, avgWaitingTime, avgTurnaroundTime));
    }
}
