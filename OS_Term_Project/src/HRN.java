import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class HRN {
    List<Process> processes;

    public HRN(List<Process> processes) {
        this.processes = new ArrayList<>(processes);
        // 도착 시간으로 초기 정렬
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
    }



    public void run() {
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int processesCompleted = 0;

        // 결과 표 출력을 위한 헤더
        System.out.println("PID | Arrival Time | Service Time | Start Time | Finish Time | Waiting Time | Turnaround Time");

        while (processesCompleted < processes.size()) {
            Process nextProcess = null;
            double highestRatio = 0;

            // 최고 응답률을 가진 프로세스 찾기
            for (Process process : processes) {
                if (process.startTime == -1 && process.arrivalTime <= currentTime) {
                    double ratio = process.responseRatio(currentTime);
                    if (ratio > highestRatio) {
                        highestRatio = ratio;
                        nextProcess = process;
                    }
                }
            }

            // 아무 프로세스도 선택되지 않았을 경우 (큐가 비어 있을 때)
            if (nextProcess == null) {
                currentTime++;
                continue;
            }

            // 프로세스 실행
            nextProcess.startTime = currentTime;
            nextProcess.finishTime = nextProcess.startTime + nextProcess.serviceTime;
            nextProcess.waitingTime = nextProcess.startTime - nextProcess.arrivalTime;
            nextProcess.finishTime = nextProcess.startTime + nextProcess.serviceTime;

            // 결과 출력
            System.out.printf("%3d | %12d | %12d | %10d | %11d | %12d | %15d\n",
                    nextProcess.id, nextProcess.arrivalTime, nextProcess.serviceTime, nextProcess.startTime, nextProcess.finishTime, nextProcess.waitingTime, nextProcess.finishTime - nextProcess.arrivalTime);



            // 시간 및 완료 처리
            currentTime = nextProcess.finishTime;
            processesCompleted++;
            totalWaitingTime += nextProcess.waitingTime;
            totalTurnaroundTime += nextProcess.finishTime - nextProcess.arrivalTime;
        }

        double averageWaitingTime = (double) totalWaitingTime / processes.size();
        double averageTurnaroundTime = (double) totalTurnaroundTime / processes.size();

        System.out.printf("Average Waiting Time: %.2f\n", averageWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
    }
}

