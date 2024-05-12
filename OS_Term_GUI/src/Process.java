public class Process {
    private int id, arrivalTime, serviceTime, priority;

    private boolean visited;
    private  double responseRatio;
    private int remainingServiceTime;  // 남은 실행 시간
    private int startTime = -1;  // 실행 시작 시간, 초기값 -1로 설정
    private int finishTime;  // 실행 완료 시간
    private int waitingTime;  // 대기 시간
    private int turnaroundTime;  // 반환 시간


    public Process(int id, int arrivalTime, int serviceTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.remainingServiceTime = serviceTime;  // 초기 남은 실행 시간은 전체 서비스 시간과 같다
        this.priority = priority;
        this.visited = false;
        this.responseRatio = 0.0;
    }

    // Getter와 Setter
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

    //SJF
    public boolean isVisited(){
        return visited;
    }

    public void setVisited(boolean visited){
        this.visited = visited;
    }

    //HRN
    public double getResponseRatio(){
        return responseRatio;
    }

    public void setResponseRatio(double responseRatio){
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
}
