
import java.util.ArrayList;
import java.util.List;

public class Process {

    private String id;                                  //ID 문자열
    private int arrivalTime, serviceTime, priority;     //기본적으로 도착시간, 서비스시간, 우선순위

    private boolean visited;                            //방문 여부 확인
    private double responseRatio;                       //응답률 HRN에 사용
    private int remainingServiceTime;                   // 남은 실행 시간
    private int startTime = -1;                         // 실행 시작 시간, 초기값 -1로 설정
    private int finishTime;                             // 실행 완료 시간
    private int waitingTime;                            // 대기 시간
    private int turnaroundTime;                         // 반환 시간
    private int responseTime;                           //응답 시간
    private int originalOrder;                          // 입력 순서

    private List<int[]> timeSave;  // 각 슬라이스는 [시작 시간, 종료 시간, 상태]를 저장 (간트 차트 그리기 위해 저장)

    //객체 생성
    public Process(String id, int arrivalTime, int serviceTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.remainingServiceTime = serviceTime;    // 초기 남은 실행 시간 = 전체 서비스 시간
        this.priority = priority;
        this.visited = false;                       // 방문여부 초기화
        this.responseRatio = 0.0;                   //응답률은 0 으로 초기화


        this.timeSave = new ArrayList<>();          // 타임 정보 저장 리스트 초기화
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

    public void setResponseTime(int responseTime) { this.responseTime = responseTime; }

    public int getResponseTime() { return responseTime; }

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


    // 프로세스의 시작, 종료, 상태를 저장한다.
    public void addTimeSlice(int start, int end, int state) {
        timeSave.add(new int[]{start, end, state});
    }
    public List<int[]> gettimeSave() {
        return timeSave;
    }


    public void setOriginalOrder(int order) {
        this.originalOrder = order;
    }

    public int getOriginalOrder() {
        return originalOrder;
    }

}
