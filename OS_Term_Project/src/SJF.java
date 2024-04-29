import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

class SJF {
    List<Process> processes;

    public SJF(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
    }

    public void run() {
        // 프로세스를 도착 시간 순으로 정렬
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        // 처리할 프로세스를 저장할 우선순위 큐
        PriorityQueue<Process> queue = new PriorityQueue<>(Comparator.comparingInt(p -> p.serviceTime));

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        int index = 0;

        System.out.println("PID | Arrival Time | Service Time | Start Time | Finish Time | Waiting Time | Turnaround Time");


        //핵심 코드
        // 모든 프로세스가 처리될 때까지 또는 우선순위 큐가 비어있지 않을 때까지 반복
        while (index < processes.size() || !queue.isEmpty()) {
            // 현재 시간 이하로 도착한 프로세스들을 우선순위 큐에 추가
            while (index < processes.size() && processes.get(index).arrivalTime <= currentTime) {
                queue.add(processes.get(index)); // 프로세스를 우선순위 큐에 추가
                index++; // 다음 프로세스로 인덱스 이동
            }

            // 우선순위 큐가 비어있다면, 다음 프로세스가 도착할 때까지 시간을 뛰어넘음
            if (queue.isEmpty()) {
                currentTime = processes.get(index).arrivalTime; // 현재 시간을 다음 프로세스의 도착 시간으로 설정
                continue; // 다음 반복으로 넘어감
            }

            Process process = queue.poll();
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
