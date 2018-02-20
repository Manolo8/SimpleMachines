package com.github.manolo8.simplemachines.commands;

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

    public CommandController(Object object) {
        this.methods = new ArrayList<>();
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
            cs.sendMessage("§cOnly players can use this command");
            return true;
        }

        Player player = (Player) cs;

        String command = cmnd.getName().toLowerCase();

        if (args.length == 0) {
            return false;
        }

        for (Method method : methods) {
            CommandMapping annotation = method.getAnnotation(CommandMapping.class);

            if (!annotation.superCommand().equals(command)) {
                continue;
            }

            if (!annotation.command().equals(args[0].toLowerCase())) continue;

            if (!player.hasPermission(annotation.permission())) {
                player.sendMessage(annotation.permissionMessage());
                return true;
            }

            if (!ArrayUtils.contains(annotation.args(), args.length)) {
                player.sendMessage(annotation.usage());
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
            return true;
        }
        return true;
    }
}
