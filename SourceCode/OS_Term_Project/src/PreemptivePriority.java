import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;

class PreemptivePriority {
    List<Process> processes;

    public PreemptivePriority(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
        // 프로세스를 도착 시간 순으로 정렬
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
    }

    public void run() {
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(
                Comparator.comparingInt(Process::getPriority).thenComparingInt(Process::getArrivalTime)
        );

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        Process currentProcess = null;

        System.out.println("PID | Arrival Time | Service Time | Priority | Start Time | Finish Time | Waiting Time | Turnaround Time");

        while (!processes.isEmpty() || !readyQueue.isEmpty() || currentProcess != null) {
            while (!processes.isEmpty() && processes.get(0).getArrivalTime() <= currentTime) {
                Process process = processes.remove(0);
                readyQueue.offer(process);
                // Check for preemption
                if (currentProcess != null && process.getPriority() < currentProcess.getPriority()) {
                    // Preempt current process
                    currentProcess.serviceTime -= (currentTime - currentProcess.startTime);
                    readyQueue.offer(currentProcess);
                    currentProcess = null; // Clear current process
                }
            }

            if (currentProcess == null && !readyQueue.isEmpty()) {
                currentProcess = readyQueue.poll();
                if (currentProcess.startTime == -1) {
                    currentProcess.startTime = currentTime;
                }
            }

            if (currentProcess != null) {
                // Execute the current process for one unit of time
                currentTime++;
                currentProcess.serviceTime--;

                if (currentProcess.serviceTime == 0) {
                    // Process completes
                    currentProcess.finishTime = currentTime;
                    currentProcess.waitingTime = currentProcess.finishTime - currentProcess.arrivalTime - (currentProcess.initialServiceTime - currentProcess.serviceTime);
                    totalWaitingTime += currentProcess.waitingTime;
                    totalTurnaroundTime += currentProcess.finishTime - currentProcess.arrivalTime;

                    System.out.printf("%3d  | %12d | %12d | %8d | %10d | %11d | %12d | %15d\n",
                            currentProcess.getId(), currentProcess.getArrivalTime(), currentProcess.initialServiceTime, currentProcess.getPriority(),
                            currentProcess.startTime, currentProcess.finishTime, currentProcess.waitingTime, currentProcess.finishTime - currentProcess.getArrivalTime());

                    currentProcess = null; // Clear current process
                }
            } else {
                currentTime++; // Advance time if no process is running
            }
        }

        // Calculate and print averages
        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();

        System.out.printf("\nAverage Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}
