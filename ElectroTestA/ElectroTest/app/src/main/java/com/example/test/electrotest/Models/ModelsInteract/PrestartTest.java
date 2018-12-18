package com.example.test.electrotest.Models.ModelsInteract;

public class PrestartTest {
    private int UserID;
    private int Ticket;

    public PrestartTest(int userID, int ticket) {
        UserID = userID;
        Ticket = ticket;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getTicket() {
        return Ticket;
    }

    public void setTicket(int ticket) {
        Ticket = ticket;
    }
}
