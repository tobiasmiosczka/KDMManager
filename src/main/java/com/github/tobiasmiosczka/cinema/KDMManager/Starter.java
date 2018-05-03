package com.github.tobiasmiosczka.cinema.KDMManager;

import com.github.tobiasmiosczka.cinema.KDMManager.gui.Window;
import org.jdom2.JDOMException;

public class Starter {

    public static void main(String[] args) throws JDOMException {
        Window window = new Window();
        window.setVisible(true);
    }

}
