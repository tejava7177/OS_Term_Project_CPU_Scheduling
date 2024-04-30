import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class RoundRobin {
    List<Process> processes;
    int quantum;  // 시간 할당량

    public RoundRobin(List<Process> processes, int quantum) {
        this.processes = new ArrayList<>(processes);
        this.quantum = quantum;
    }

    public void run() {
        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        // 프로세스를 큐에 추가
        queue.addAll(processes);

        // 결과 표 출력을 위한 헤더
        System.out.println("PID | Arrival Time | Service Time | Start Time | Finish Time | Waiting Time | Turnaround Time");

        while (!queue.isEmpty()) {
            Process current = queue.poll();

            // 프로세스가 시작한 적이 없으면 현재 시간이 시작 시간이 됩니다.
            if (current.startTime == -1) {
                current.startTime = Math.max(currentTime, current.arrivalTime);
            }

            // 실행할 시간 계산 (남은 서비스 시간과 할당량 중 작은 값)
            int timeToRun = Math.min(current.serviceTime, quantum);

            // 프로세스 실행 업데이트
            current.serviceTime -= timeToRun;
            currentTime += timeToRun;

            // 프로세스가 완료되지 않았으면 큐에 다시 추가
            if (current.serviceTime > 0) {
                queue.offer(current);
            } else {
                // 프로세스 완료 처리
                current.finishTime = currentTime;
                current.waitingTime = current.finishTime - current.arrivalTime - (current.serviceTime + timeToRun);
                totalWaitingTime += current.waitingTime;
                totalTurnaroundTime += current.finishTime - current.arrivalTime;

                // 결과 출력
                System.out.printf("%3d | %12d | %12d | %10d | %11d | %12d | %15d\n",
                        current.id, current.arrivalTime, current.serviceTime + timeToRun, current.startTime, current.finishTime, current.waitingTime, current.finishTime - current.arrivalTime);
            }
        }

        // 평균 대기 시간과 평균 반환 시간 출력
        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        System.out.printf("Average Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}
