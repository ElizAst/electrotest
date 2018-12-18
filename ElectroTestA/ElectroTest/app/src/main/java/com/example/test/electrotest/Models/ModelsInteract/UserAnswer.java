package com.example.test.electrotest.Models.ModelsInteract;

public class UserAnswer {
    private int UserID;
    private int QuestionID;
    private int AnswerID;
    private int TestResult;


    public UserAnswer(int userID, int questionID, int answerID, int testResult) {
        UserID = userID;
        QuestionID = questionID;
        AnswerID = answerID;
        TestResult = testResult;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getQuestionID() {
        return QuestionID;
    }

    public void setQuestionID(int questionID) {
        QuestionID = questionID;
    }

    public int getAnswerID() {
        return AnswerID;
    }

    public void setAnswerID(int answerID) {
        AnswerID = answerID;
    }

    public int getTestResult() {
        return TestResult;
    }

    public void setTestResult(int testResult) {
        TestResult = testResult;
    }
}
