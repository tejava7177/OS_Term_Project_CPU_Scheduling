//public class Process {
//
//    private String id;
//    private int arrivalTime, serviceTime, priority;
//
//    private boolean visited;
//    private  double responseRatio;
//    private int remainingServiceTime;  // 남은 실행 시간
//    private int startTime = -1;  // 실행 시작 시간, 초기값 -1로 설정
//    private int finishTime;  // 실행 완료 시간
//    private int waitingTime;  // 대기 시간
//    private int turnaroundTime;  // 반환 시간
//
//
//    public Process(String id, int arrivalTime, int serviceTime, int priority) {
//        this.id = id;
//        this.arrivalTime = arrivalTime;
//        this.serviceTime = serviceTime;
//        this.remainingServiceTime = serviceTime;  // 초기 남은 실행 시간은 전체 서비스 시간과 같다
//        this.priority = priority;
//        this.visited = false;
//        this.responseRatio = 0.0;
//    }
//
//    // Getter와 Setter
//    public String  getId() {
//        return id;
//    }
//
//    public int getArrivalTime() {
//        return arrivalTime;
//    }
//
//    public int getServiceTime() {
//        return serviceTime;
//    }
//
//    public int getPriority() {
//        return priority;
//    }
//
//    public int getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(int startTime) {
//        this.startTime = startTime;
//    }
//
//    public int getFinishTime() {
//        return finishTime;
//    }
//
//    public void setFinishTime(int finishTime) {
//        this.finishTime = finishTime;
//    }
//
//    public int getWaitingTime() {
//        return waitingTime;
//    }
//
//    public void setWaitingTime(int waitingTime) {
//        this.waitingTime = waitingTime;
//    }
//
//    //SJF
//    public boolean isVisited(){
//        return visited;
//    }
//
//    public void setVisited(boolean visited){
//        this.visited = visited;
//    }
//
//    //HRN
//    public double getResponseRatio(){
//        return responseRatio;
//    }
//
//    public void setResponseRatio(double responseRatio){
//        this.responseRatio = responseRatio;
//    }
//
//    public int getTurnaroundTime() {
//        return turnaroundTime;
//    }
//
//    public void setTurnaroundTime(int turnaroundTime) {
//        this.turnaroundTime = turnaroundTime;
//    }
//
//
//    public int getRemainingServiceTime() {
//        return remainingServiceTime;
//    }
//
//    public void setRemainingServiceTime(int remainingServiceTime) {
//        this.remainingServiceTime = remainingServiceTime;
//    }
//}

import java.util.ArrayList;
import java.util.List;

public class Process {

    private String id;          //ID 문자열
    private int arrivalTime, serviceTime, priority;     //기본적으로 도착시간, 서비스시간, 우선순위

    private boolean visited;
    private double responseRatio;                       //응답률 HRN에 사용
    private int remainingServiceTime;                   // 남은 실행 시간
    private int startTime = -1;                         // 실행 시작 시간, 초기값 -1로 설정
    private int finishTime;                             // 실행 완료 시간
    private int waitingTime;                            // 대기 시간
    private int turnaroundTime;                         // 반환 시간



    private List<int[]> timeSlices;  // 각 슬라이스는 [시작 시간, 종료 시간, 상태]를 저장

    //객체 생성
    public Process(String id, int arrivalTime, int serviceTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.remainingServiceTime = serviceTime;  // 초기 남은 실행 시간 = 전체 서비스 시간과
        this.priority = priority;
        this.visited = false;
        this.responseRatio = 0.0;                   //응답률은 0 으로 초기화

        this.timeSlices = new ArrayList<>();
    }

    // Getter와 Setter
    public String getId() {
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

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public double getResponseRatio() {
        return responseRatio;
    }

    public void setResponseRatio(double responseRatio) {
        this.responseRatio = responseRatio;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getRemainingServiceTime() {
        return remainingServiceTime;
    }

    public void setRemainingServiceTime(int remainingServiceTime) {
        this.remainingServiceTime = remainingServiceTime;
    }


    public void addTimeSlice(int start, int end, int state) {
        timeSlices.add(new int[]{start, end, state});
    }


    public List<int[]> getTimeSlices() {
        return timeSlices;
    }

}
