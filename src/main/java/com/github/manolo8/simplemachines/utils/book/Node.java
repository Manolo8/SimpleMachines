package com.github.manolo8.simplemachines.utils.book;

import java.util.Arrays;

public class Node {

    private String key;
    private String value;
    private String[] template;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String[] getTemplate() {
        return template;
    }

    public String getTemplateAsString() {
        StringBuilder builder = new StringBuilder();
        for (String string : template) builder.append(string).append("\n");
        return builder.toString().replaceAll("&", "§");
    }

    public void setTemplate(String[] template) {
        this.template = template;
    }
}
