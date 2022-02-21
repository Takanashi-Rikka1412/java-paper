package net.pojo;

public class Order {
    private String orderID;
    private String userID;
    private int state;
    private String nTime;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public int getState() {
        return state;
    }

    public String getnTime() {
        return nTime;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setnTime(String nTime) {
        this.nTime = nTime;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setState(int state) {
        this.state = state;
    }

}
