package com.github.tobiasmiosczka.cinema.KDMManager.gui;

public interface IUpdate {
    void onUpdateEmailBox(int current, int total, String host);
    void onUpdateEmailLoading(int current, int total);
    void onUpdateSending(int current, int total);
    void onDone();
}