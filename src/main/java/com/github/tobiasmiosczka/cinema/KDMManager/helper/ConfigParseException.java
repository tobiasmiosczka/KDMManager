package com.github.tobiasmiosczka.cinema.KDMManager.helper;

import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

public class ConfigParseException extends Throwable {

    private final List<String> stack = new LinkedList<>();

    public ConfigParseException(Element element, String name) {
        stack.add(name);
        while (element != null) {
            stack.add(element.getName());
            element = element.getParentElement();
        }
    }

    @Override
    public String getMessage() {
        return stack.stream().reduce("", (a, b) -> "<" + b + "> " + a);
    }
}
