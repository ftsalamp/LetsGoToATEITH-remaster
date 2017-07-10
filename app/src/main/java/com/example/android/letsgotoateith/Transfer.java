package com.example.android.letsgotoateith;

/**
 * Created by user on 7/10/17.
 */

class Transfer {

    private int people, day, time, direction, frequency, area;
    private String tranferId, userId;

    public Transfer() {
    }

    public Transfer(int people, int day, int time, int direction, int frequency, int area, String userId) {
        this.people = people;
        this.day = day;
        this.time = time;
        this.direction = direction;
        this.frequency = frequency;
        this.area = area;
        this.userId=userId;
    }

    public String toString(){
        return "people "+people+" day "+ day+" time "+time+" direction "+ direction+ " frequency "+ frequency +"area "+area;
    }

    public String getUserId() {
        return userId;
    }

    public int getPeople() {
        return people;
    }

    public int getDay() {
        return day;
    }

    public int getTime() {
        return time;
    }

    public int getDirection() {
        return direction;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getArea() {
        return area;
    }

    public String getTranferId() {
        return tranferId;
    }
}
