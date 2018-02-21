package com.github.manolo8.simplemachines.utils.replace;

public class Content {

    private String key;
    private String def;
    private Object value;

    public Content(String key, String def, Object value) {
        this.key = key;
        this.def = def;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Content setKey(String key) {
        this.key = key;
        return this;
    }

    public String getDef() {
        return def;
    }

    public Content setDef(String def) {
        this.def = def;
        return this;
    }

    public Object getValue() {
        if (value == null || value == "") return 0;
        return value;
    }

    public Content setValue(Object value) {
        this.value = value;
        return this;
    }
}
