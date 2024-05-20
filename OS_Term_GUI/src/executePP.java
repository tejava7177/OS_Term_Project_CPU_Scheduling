import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class executePP {
    private List<Process> processes;  // 프로세스 리스트
    private PriorityQueue<Process> readyQueue;  // 준비 큐 (우선순위 기준)
    private List<Process> completedProcesses = new ArrayList<>();  // 완료된 프로세스를 저장할 리스트
    private float avgWaitingTime = 0;  // 평균 대기 시간
    private float avgTurnaroundTime = 0;  // 평균 반환 시간

    public executePP(List<Process> processes) {
        // 생성자: 프로세스 리스트를 초기화하고 준비 큐를 우선순위 기준으로 설정
        this.processes = new ArrayList<>(processes);
        this.readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority));
    }

    public void run() {
        int currentTime = 0;  // 현재 시간
        float totalWaitingTime = 0;  // 총 대기 시간
        float totalTurnaroundTime = 0;  // 총 반환 시간
        Process currentProcess = null;  // 현재 실행 중인 프로세스

        // 모든 프로세스가 완료될 때까지 반복
        while (!processes.isEmpty() || !readyQueue.isEmpty() || currentProcess != null) {
            int finalCurrentTime = currentTime;

            // 현재 시간에 도착한 모든 프로세스를 준비 큐에 추가
            processes.removeIf(process -> {
                if (process.getArrivalTime() <= finalCurrentTime) {
                    readyQueue.offer(process);
                    return true;
                }
                return false;
            });

            // 현재 프로세스가 없거나, 준비 큐의 최우선 프로세스가 현재 프로세스보다 높은 우선순위인 경우
            if (currentProcess == null || (!readyQueue.isEmpty() && readyQueue.peek().getPriority() < currentProcess.getPriority())) {
                // 현재 프로세스를 준비 큐에 다시 추가하고 대기 구간 추가
                if (currentProcess != null && currentProcess.getRemainingServiceTime() > 0) {
                    currentProcess.addTimeSlice(currentTime - 1, currentTime, 0); // 대기 구간 추가
                    readyQueue.offer(currentProcess);
                }
                // 준비 큐에서 우선순위가 가장 높은 프로세스를 가져옴
                currentProcess = readyQueue.poll();
                if (currentProcess.getStartTime() == -1) {
                    currentProcess.setStartTime(currentTime);  // 처음 실행되는 경우 시작 시간 설정
                }
                currentProcess.addTimeSlice(currentTime, currentTime + 1, 1); // 실행 구간 추가
            } else if (currentProcess != null) {
                // 현재 프로세스를 계속 실행
                currentProcess.addTimeSlice(currentTime - 1, currentTime, 1); // 실행 구간 추가
            }

            currentTime++;  // 현재 시간 증가

            // 현재 프로세스를 1단위 시간 실행
            if (currentProcess != null) {
                currentProcess.setRemainingServiceTime(currentProcess.getRemainingServiceTime() - 1);  // 남은 실행 시간 감소
                if (currentProcess.getRemainingServiceTime() == 0) {
                    // 현재 프로세스가 완료된 경우
                    currentProcess.setFinishTime(currentTime);  // 종료 시간 설정
                    currentProcess.setTurnaroundTime(currentTime - currentProcess.getArrivalTime());  // 반환 시간 계산
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getServiceTime());  // 대기 시간 계산
                    completedProcesses.add(currentProcess);  // 완료된 프로세스 리스트에 추가
                    currentProcess = null;  // 현재 프로세스 초기화
                }
            }
        }

        // 평균 대기 시간 및 반환 시간 계산
        for (Process p : completedProcesses) {
            totalWaitingTime += p.getWaitingTime();
            totalTurnaroundTime += p.getTurnaroundTime();
        }
        avgWaitingTime = totalWaitingTime / completedProcesses.size();
        avgTurnaroundTime = totalTurnaroundTime / completedProcesses.size();

        // 결과를 GUI로 표시
        SwingUtilities.invokeLater(() -> new ResultDisplay("Preemptive Priority Scheduling Results", completedProcesses, avgWaitingTime, avgTurnaroundTime));
    }
}
