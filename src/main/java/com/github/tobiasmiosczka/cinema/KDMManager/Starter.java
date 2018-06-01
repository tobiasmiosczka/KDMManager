package com.github.tobiasmiosczka.cinema.KDMManager;

import com.github.tobiasmiosczka.cinema.KDMManager.gui.ProgressWindow;
import com.github.tobiasmiosczka.cinema.KDMManager.gui.Window;

public class Starter {

    public static void main(String[] args) {
        IUpdateProgress iUpdateProgress = new ProgressWindow();
        Window iUpdateGui = new Window();
        Program program = new Program(iUpdateGui, iUpdateProgress, "config.xml");
        iUpdateGui.setProgram(program);
    }

}
