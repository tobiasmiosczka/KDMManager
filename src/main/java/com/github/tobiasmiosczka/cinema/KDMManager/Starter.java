package com.github.tobiasmiosczka.cinema.KDMManager;

import com.github.tobiasmiosczka.cinema.KDMManager.gui.Window;
import org.jdom2.JDOMException;

import java.io.IOException;

public class Starter {

    public static void main(String[] args) throws IOException, JDOMException {
        Window window = new Window();
        window.setVisible(true);
    }

}
