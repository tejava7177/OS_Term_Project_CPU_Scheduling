import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

class NonPreemptivePriority {
    List<Process> processes;

    public NonPreemptivePriority(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
    }

    public void run() {
        // 프로세스를 도착 시간과 우선순위에 따라 정렬
        processes.sort(Comparator.comparingInt(Process::getArrivalTime)
                .thenComparingInt(Process::getPriority));

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.println("PID | Arrival Time | Service Time | Priority | Start Time | Finish Time | Waiting Time | Turnaround Time");

        for (Process process : processes) {
            // 도착 시간이 현재 시간보다 큰 경우, CPU가 유휴 상태로 있어야 함
            if (currentTime < process.getArrivalTime()) {
                currentTime = process.getArrivalTime();
            }

            // 프로세스 시작 처리
            process.startTime = currentTime;
            process.finishTime = process.startTime + process.getServiceTime();
            process.waitingTime = process.startTime - process.getArrivalTime();
            process.finishTime = process.startTime + process.getServiceTime();

            // 결과 출력
            System.out.printf("%3d  | %12d | %12d | %8d | %10d | %11d | %12d | %15d\n",
                    process.getId(), process.getArrivalTime(), process.getServiceTime(), process.getPriority(),
                    process.startTime, process.finishTime, process.waitingTime, process.finishTime - process.getArrivalTime());

            // 시간 및 완료 처리
            currentTime = process.finishTime;
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.finishTime - process.getArrivalTime();
        }

        // 평균 대기 시간과 반환 시간 출력
        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();

        System.out.printf("\nAverage Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}
