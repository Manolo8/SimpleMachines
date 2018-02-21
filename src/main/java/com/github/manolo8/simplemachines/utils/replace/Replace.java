package com.github.manolo8.simplemachines.utils.replace;

import java.util.ArrayList;
import java.util.List;

public class Replace {

    private String template;
    private List<Content> contents;

    public Replace(String template) {
        this.template = template;
        this.contents = new ArrayList<>();
    }

    public boolean hasKey(String key) {
        for (Content content : contents) if (content.getKey().equals(key)) return true;
        return false;
    }

    public Replace setTemplate(String template) {
        this.template = template;
        compile();
        return this;
    }

    public Replace setValue(String key, Object value) {
        for (Content content : contents) {
            if (content.getKey().equals(key))
                content.setValue(value);
        }
        return this;
    }

    public Replace compile() {
        contents.clear();

        StringBuilder mainBuilder = new StringBuilder();

        for (int i = 0; i < template.length(); i++) {
            char c = template.charAt(i);
            if (c == '{') {
                StringBuilder builder = new StringBuilder();
                while (i < template.length()) {
                    i++;
                    c = template.charAt(i);
                    if (c == '}') break;
                    builder.append(c);
                }
                contents.add(new Content(builder.toString(), mainBuilder.toString(), ""));
                mainBuilder = new StringBuilder();
                continue;
            }
            mainBuilder.append(c);
        }
        contents.add(new Content("end", mainBuilder.toString(), ";"));
        //Libera a memória
        template = null;
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();

        for (Content content : contents) builder.append(content.getDef()).append(content.getValue());

        return builder.toString();
    }

    @Override
    public String toString() {
        return build();
    }
}
