package com.example.test.electrotest.Presenters;

// Класс для презентеров
public abstract class ICreatablePresenter<T> {
    public abstract void onCreate(T v);

    protected T activity;

    public void setView(T v) {
        this.activity = v;
    }

    public T getView() {
        return activity;
    }
}
