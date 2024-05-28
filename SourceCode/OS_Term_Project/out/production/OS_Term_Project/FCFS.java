import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class FCFS {
    List<Process> processes;        //Main 에서 Process 리스트를 받아옵니다.

    public FCFS(List<Process> processes) {
        // 도착 시간에 따라 프로세스 목록을 정렬합니다.
        this.processes = new ArrayList<>(processes);
        this.processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
    }

    public void run() {
        int currentTime = 0;        // 현재 시간을 나타내는 변수
        int totalWaitingTime = 0;   // 프로세스의 총 대기시간의 합
        int totalTurnaroundTime = 0; // 프로세스의 총 반환 시간 합


        System.out.println("PID | Arrival Time | Service Time | Start Time | Finish Time | Waiting Time | Turnaround Time");

        // 각 프로세스에 대해 스케줄링을 수행 반복
        for (Process process : processes) {
            // 현재 시간이 프로세스 도착 시간보다 작으면 현재 시간을 도착 시간으로 조정
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime;
            }

            // 프로세스 시작 시간 = 현재 시간 = 0
            int startTime = currentTime;

            // 프로세스 완료 시간 = 시작 시간 + 서비스 시간
            int finishTime = startTime + process.serviceTime;

            // 대기 시간 = 시작 시간 - 도착 시간
            int waitingTime = startTime - process.arrivalTime;

            // 반환 시간 = 완료 시간 - 도착 시간
            int turnaroundTime = finishTime - process.arrivalTime;

            // 결과 출력
            System.out.printf("%3d | %12d | %12d | %10d | %11d | %12d | %15d\n",
                    process.id, process.arrivalTime, process.serviceTime, startTime, finishTime, waitingTime, turnaroundTime);


            // 다음 프로세스를 위해 현재 시간 업데이트
            currentTime = finishTime;


            // 총 대기 시간과 총 반환 시간을 업데이트
            totalWaitingTime += waitingTime;
            totalTurnaroundTime += turnaroundTime;
        }

        // 평균 대기 시간과 평균 반환 시간 계산 및 출력, 소수점을 위한 double 자료형
        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();

        //소수 둘째자리까지 출력
        System.out.printf("\nAverage Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }

}
