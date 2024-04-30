import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 10, 3));
        processes.add(new Process(2, 1, 28, 2));
        processes.add(new Process(3, 2, 6, 4));
        processes.add(new Process(4,3,4,1));
        processes.add(new Process(5,4,14,2));

        FCFS fcfs = new FCFS(processes);
        fcfs.run();

        SJF sjf = new SJF(processes);
        sjf.run();


    }
}



class Process {
    int id;             // 프로세스 ID
    int arrivalTime;    // 도착 시간
    int serviceTime;    // 서비스 시간 (실행 시간)
    int priority;       // 우선순위

    public Process(int id, int arrivalTime, int serviceTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
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

}