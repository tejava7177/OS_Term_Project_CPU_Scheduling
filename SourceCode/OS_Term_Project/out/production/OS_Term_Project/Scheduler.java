import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Scheduler {
    public static void main(String[] args) {  // main 메서드는 소문자로 시작해야 합니다.
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 10, 3));
        processes.add(new Process(2, 1, 28, 2));
        processes.add(new Process(3, 2, 6, 4));
        processes.add(new Process(4, 3, 4, 1));
        processes.add(new Process(5, 4, 14, 2));

        // NonPreemptivePriority 스케줄링 알고리즘 실행
        NonPreemptivePriority npPriority = new NonPreemptivePriority(processes);
        npPriority.run();

        // PreemptivePriority 스케줄링 알고리즘 실행
        PreemptivePriority pp = new PreemptivePriority(processes);
        pp.run();
    }
}

class Process {
    int id;             // 프로세스 ID
    int arrivalTime;    // 도착 시간
    int serviceTime;    // 서비스 시간 (실행 시간)
    int priority;       // 우선순위

    int initialServiceTime; // 초기 서비스 시간 (생성 시의 서비스 시간)

    int startTime = -1;
    int finishTime = -1;
    int waitingTime = 0;

    public Process(int id, int arrivalTime, int serviceTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.initialServiceTime = serviceTime; // 초기 서비스 시간 설정
        this.priority = priority;
    }

    // Getter 메서드 추가
    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int getPriority() {
        return priority;
    }

    // 우선순위를 계산하는 responseRatio(응답률)
    public double responseRatio(int currentTime) {
        if (startTime == -1) {  // 아직 시작하지 않았을 경우
            int timeWaited = currentTime - arrivalTime;
            return (double) (timeWaited + serviceTime) / serviceTime;
        }
        return -1;  // 이미 처리된 프로세스
    }
}