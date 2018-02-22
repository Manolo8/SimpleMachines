package com.github.manolo8.simplemachines;

import com.github.manolo8.simplemachines.utils.replace.Replace;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author Willian
 */
public class Language {

    private Properties prop;
    private HashMap<String, Replace> replaces;
    private HashMap<String, String> strings;

    public Language(Plugin plugin, Config config) {
        replaces = new HashMap<>();
        strings = new HashMap<>();
        prop = new Properties();
        InputStream is = null;

        try {

            is = plugin.getResource(config.getString("LANGUAGE", "PT-BR") + ".properties");

            if (is.available() == 0) {
                is = plugin.getResource("PT-BR.properties");
            }
            prop.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Replace getReplacer(String key) {
        Replace replace = this.replaces.get(key);
        if (replace != null) return replace;

        String template = this.prop.getProperty(key);
        if (template == null) return new Replace("MESSAGE " + key + " NOT FOUND").compile();
        replace = new Replace(template).compile();
        replaces.put(key, replace);
        return replace;
    }

    public String getString(String key) {
        String string = this.strings.get(key);
        if (string != null) return string;

        string = this.prop.getProperty(key);
        this.strings.put(key, string);
        if (string == null) return "MESSAGE " + key + " NOT FOUND";
        return string;
    }
}
