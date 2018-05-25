package com.github.tobiasmiosczka.cinema.KDMManager;

import com.github.tobiasmiosczka.cinema.KDMManager.gui.LoadingWindow;
import com.github.tobiasmiosczka.cinema.KDMManager.gui.Window;

public class Starter {

    public static void main(String[] args) {
        IUpdateProgress iUpdateProgress = new LoadingWindow();
        IUpdateGui iUpdateGui = new Window();
        Program program = new Program(iUpdateGui, iUpdateProgress, "config.xml");
        ((Window) iUpdateGui).setProgram(program);
    }

}
