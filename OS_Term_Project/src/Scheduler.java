import java.util.ArrayList;
import java.util.List;


public class Scheduler {
    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 0, 10, 3));
        processes.add(new Process(2, 1, 28, 2));
        processes.add(new Process(3, 2, 6, 4));
        processes.add(new Process(4,3,4,1));
        processes.add(new Process(5,4,14,2));

//        FCFS fcfs = new FCFS(processes);
//        fcfs.run();
//
//        SJF sjf = new SJF(processes);
//        sjf.run();
//
        HRN hrn = new HRN(processes);
        hrn.run();
//
//        RoundRobin rr = new RoundRobin(processes, 2);  // 시간 할당량을 5로 설정
//        rr.run();
//
//        SRT srt = new SRT(processes, 2); // 시간 할당량을 2로 설정
//        srt.run();
    }
}



class Process {
    int id;             // 프로세스 ID
    int arrivalTime;    // 도착 시간
    int serviceTime;    // 서비스 시간 (실행 시간)
    int priority;       // 우선순위



    int startTime = -1;
    int finishTime = -1;
    int waitingTime = 0;


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


    //우선순위를 계산하는 responseRatio(응답률)
    public double responseRatio(int currentTime) {
        if (startTime == -1) {  // 아직 시작하지 않았을 경우
            int timeWaited = currentTime - arrivalTime;
            return (double) (timeWaited + serviceTime) / serviceTime;
        }
        return -1;  // 이미 처리된 프로세스
    }



}