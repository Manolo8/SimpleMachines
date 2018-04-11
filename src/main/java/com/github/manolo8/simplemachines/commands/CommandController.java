package com.github.manolo8.simplemachines.commands;

import com.github.manolo8.simplemachines.Language;
import com.github.manolo8.simplemachines.commands.annotation.CommandMapping;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Willian
 */
public class CommandController implements CommandExecutor {

    private final Object commands;
    private final List<Method> methods;
    private final Language language;

    public CommandController(Object object, Language language) {
        this.methods = new ArrayList<>();
        this.language = language;
        this.commands = object;

        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(CommandMapping.class)) {
                methods.add(method);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(language.getString("command.only.players"));
            return true;
        }

        Player player = (Player) cs;

        String command = cmnd.getName().toLowerCase();

        if (args.length == 0) {
            sendHelp(player, command);
            return true;
        }

        for (Method method : methods) {
            CommandMapping annotation = method.getAnnotation(CommandMapping.class);

            if (!annotation.command().equals(command)) {
                continue;
            }

            String subCommandLang = language.getString(annotation.subCommand());

            if (!subCommandLang.equals(args[0].toLowerCase())) {
                continue;
            }

            if (!player.hasPermission(annotation.permission())) {
                player.sendMessage(language.getString("command.no.permission"));
                return true;
            }

            if (!ArrayUtils.contains(annotation.args(), args.length)) {
                player.sendMessage(language.getString(annotation.usage()));
                return true;
            }

            try {
                Object object = method.invoke(commands, player, args);
                if (object instanceof Boolean) return (Boolean) object;
                return true;
            } catch (Exception e) {
                player.sendMessage("§cAn internal error occurred");
                e.printStackTrace();
            }
        }
        sendHelp(player, command);
        return true;
    }

    public void sendHelp(Player player, String command) {
        StringBuilder builder = new StringBuilder();

        for (Method method : methods) {
            CommandMapping mapping = method.getAnnotation(CommandMapping.class);

            if (command.equals(mapping.command()))
                builder.append(language.getString(mapping.usage())).append("\n");
        }

        player.sendMessage(builder.toString());
    }
}
