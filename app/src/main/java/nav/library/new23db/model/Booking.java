package nav.library.new23db.model;

import java.io.Serializable;

/**
 * Created by abhin on 4/21/2017.
 */

public class Booking implements Serializable {

    private long bookingID;
    private String bookinDate;
    private String empID;
    private String roomID;
    private String startTime;
    private String endTime;
    private String isReserved;

    public Booking() {
    }

    public long getBookingID() {
        return bookingID;
    }

    public String getBookinDate() {
        return bookinDate;
    }

    public String getEmpID() {
        return empID;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getIsReserved() {
        return isReserved;
    }

    public void setBookingID(long bookingID) {

        this.bookingID = bookingID;
    }

    public void setBookinDate(String bookinDate) {
        this.bookinDate = bookinDate;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setIsReserved(String isReserved) {
        this.isReserved = isReserved;
    }
}
