public class Bus {
    private String carNumber;
    private long peopleDown;
    private long peopleUp;

    public Bus() {}

    public Bus(String carNumber, long peopleDown, long peopleUp) {
        setCarNumber(carNumber);
        setDown(peopleDown);
        setUp(peopleUp);
        //setNumber(number);
    }
    public void setCarNumber(String carNumber){
        this.carNumber = carNumber;
    }
    public void setDown(long peopleDown){
        this.peopleDown = peopleDown;
    }
    public void setUp(long peopleUp){this.peopleUp = peopleUp;}


    public String getCarNumber() { return carNumber; }
    public long getDown()
    {
        return peopleDown;
    }
    public long getUp()
    {
        return peopleUp;
    }
}
