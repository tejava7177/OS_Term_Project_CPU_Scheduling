import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

class SRT {
    List<Process> processes;
    int quantum;  // 시간 할당량

    public SRT(List<Process> processes, int quantum) {
        this.processes = new ArrayList<>(processes);
        this.quantum = quantum;
    }

    public void run() {
        // 프로세스를 도착 시간 순으로 정렬
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));

        // 준비 큐 생성
        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getServiceTime));
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        // 결과 출력을 위한 헤더
        System.out.println("PID | Arrival Time | Service Time | Start Time | Finish Time | Waiting Time | Turnaround Time");

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).getArrivalTime() <= currentTime) {
                readyQueue.offer(processes.remove(0));
            }

            if (readyQueue.isEmpty()) {
                currentTime = processes.get(0).getArrivalTime();
                continue;
            }

            Process current = readyQueue.poll();

            // 타임 슬라이스 또는 남은 서비스 시간 중 더 작은 값을 선택
            int timeToRun = Math.min(current.getServiceTime(), quantum);

            // 처음 시작되는 프로세스인 경우 시작 시간 설정
            if (current.startTime == -1) {
                current.startTime = currentTime;
            }

            // 서비스 시간 업데이트 및 시간 진행
            current.serviceTime -= timeToRun;
            currentTime += timeToRun;

            // 프로세스가 아직 완료되지 않은 경우
            if (current.serviceTime > 0) {
                readyQueue.offer(current); // 프로세스를 다시 큐에 추가
            } else {
                // 프로세스 완료 처리
                current.finishTime = currentTime;
                current.waitingTime = currentTime - current.arrivalTime - (current.finishTime - current.startTime);
                totalWaitingTime += current.waitingTime;
                totalTurnaroundTime += current.finishTime - current.arrivalTime;

                // 결과 출력
                System.out.printf("%3d | %12d | %12d | %10d | %11d | %12d | %15d\n",
                        current.getId(), current.getArrivalTime(), current.getServiceTime() + timeToRun, current.startTime, current.finishTime, current.waitingTime, current.finishTime - current.arrivalTime);
            }
        }

        // 평균 대기 시간과 평균 반환 시간 계산 및 출력
        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();
        System.out.printf("Average Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}
