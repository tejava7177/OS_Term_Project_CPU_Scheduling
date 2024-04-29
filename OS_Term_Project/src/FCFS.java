import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class FCFS {
    List<Process> processes;

    public FCFS(List<Process> processes) {
        // 도착 시간에 따라 프로세스 목록을 정렬합니다.
        this.processes = new ArrayList<>(processes);
        this.processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
    }

    public void run() {
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        System.out.println("PID | Arrival Time | Service Time | Start Time | Finish Time | Waiting Time | Turnaround Time");
        for (Process process : processes) {
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }
            int startTime = currentTime;
            int finishTime = startTime + process.serviceTime;
            int waitingTime = startTime - process.arrivalTime;
            int turnaroundTime = finishTime - process.arrivalTime;

            System.out.printf("%3d | %12d | %12d | %10d | %11d | %12d | %15d\n",
                    process.id, process.arrivalTime, process.serviceTime, startTime, finishTime, waitingTime, turnaroundTime);

            currentTime = finishTime;
            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;
        }

        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();

        System.out.printf("\nAverage Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}
