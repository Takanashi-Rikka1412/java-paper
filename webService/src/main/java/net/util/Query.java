package net.util;

public class Query {
    private String orderNumber;
    private String userID;
    private int orderState;
    private String time;

    public void setTime(String time) {
        this.time = time;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTime() {
        return time;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public int getOrderState() {
        return orderState;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
