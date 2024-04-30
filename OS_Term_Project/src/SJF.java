import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;

public class SJF {
    private List<Process> processQueue;

    public SJF(List<Process> incomingProcesses) {
        processQueue = new ArrayList<>(incomingProcesses);
        // 도착 시간으로 초기 정렬을 하여 준비 큐를 만듭니다.
        processQueue.sort(Comparator.comparingInt(proc -> proc.getArrivalTime()));
    }

    public void run() {
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getServiceTime));
        int currentTime = 0;
        int accumulatedWaitTime = 0;
        int accumulatedTurnaroundTime = 0;

        int processIndex = 0; // 이 인덱스는 전체 프로세스 목록을 추적합니다.

        System.out.println("PID | Arrival Time | Service Time | Start Time | Finish Time | Waiting Time | Turnaround Time");

        while (processIndex < processQueue.size() || !readyQueue.isEmpty()) {
            // 현재 시간에 맞춰 준비 큐에 프로세스를 추가합니다.
            while (processIndex < processQueue.size() && processQueue.get(processIndex).getArrivalTime() <= currentTime) {
                readyQueue.add(processQueue.get(processIndex));
                processIndex++;
            }

            if (readyQueue.isEmpty()) {
                // 다음 프로세스의 도착을 기다립니다.
                currentTime = processQueue.get(processIndex).getArrivalTime();
                continue;
            }

            Process currentProcess = readyQueue.poll(); // 가장 짧은 서비스 시간을 가진 프로세스 선택
            int startTime = currentTime;
            int finishTime = startTime + currentProcess.getServiceTime();
            int waitTime = startTime - currentProcess.getArrivalTime();
            int turnaroundTime = finishTime - currentProcess.getArrivalTime();

            System.out.printf("%3d | %12d | %12d | %10d | %11d | %12d | %15d\n",
                    currentProcess.getId(), currentProcess.getArrivalTime(), currentProcess.getServiceTime(), startTime, finishTime, waitTime, turnaroundTime);

            currentTime = finishTime; // 현재 시간 업데이트
            accumulatedWaitTime += waitTime; // 총 대기 시간 업데이트
            accumulatedTurnaroundTime += turnaroundTime; // 총 반환 시간 업데이트
        }

        // 평균 대기 시간과 평균 반환 시간을 계산하여 출력합니다.
        double averageWaitingTime = (double) accumulatedWaitTime / processQueue.size();
        double averageTurnaroundTime = (double) accumulatedTurnaroundTime / processQueue.size();

        System.out.printf("\nAverage Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}
