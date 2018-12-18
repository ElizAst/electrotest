package com.example.test.electrotest.ServerInteract;


/// Контекст пользователя
public class UserContextOperation {
    private static int userID;
    private static int testResultID;


    public static int getTestResultID() {
        return testResultID;
    }

    public static void setTestResultID(int testResultID) {
        UserContextOperation.testResultID = testResultID;
    }

    public static int getUserID() {
        return userID;
    }

    public static void setUserID(int userID) {
        UserContextOperation.userID = userID;
    }
}
