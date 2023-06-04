public class BusCurrent {
    private String carNo;
    private long currentNum;
//    private int carNumberPartition;


    public BusCurrent() {}

    public BusCurrent(String carNo, long currentNum) {
        setCarNo(carNo);
        setCurrentNum(currentNum);
        //setNumber(number);
    }
    public void setCarNo(String carNo){
        this.carNo = carNo;
    }
    public void setCurrentNum(long currentNum){
        this.currentNum = currentNum;
    }


    public String getCarNo() { return carNo; }
    public long getCurrentNum()
    {
        return currentNum;
    }
}
