package com.example.test.electrotest.Presenters;

/// Класс оболочка для синглетонов
public class BuilderPresenter {

    private static TestPresenter testPresenter;

    public static TestPresenter getTestPresenter() {
        if (testPresenter == null)
            testPresenter = new TestPresenter();
        return testPresenter;
    }
}
