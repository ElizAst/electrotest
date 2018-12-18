package com.example.test.electrotest.Models.ModelsInteract;

public class IsStartTest {

    public IsStartTest(boolean isStart, int testID, int userID) {
        IsStart = isStart;
        TestID = testID;
        UserID = userID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public boolean isStart() {
        return IsStart;
    }

    public void setStart(boolean start) {
        IsStart = start;
    }

    public int getTestID() {
        return TestID;
    }

    public void setTestID(int testID) {
        TestID = testID;
    }

    private boolean IsStart;
    private int TestID;
    private int UserID;
}
