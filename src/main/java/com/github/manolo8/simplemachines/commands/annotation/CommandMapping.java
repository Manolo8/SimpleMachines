package com.github.manolo8.simplemachines.commands.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Willian
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CommandMapping
{

    public String permission() default "machines.user";

    public String permissionMessage() default "NoPermission";

    public boolean defaultCommand() default false;

    public int[] args() default 1;

    public String command() default "default";
    
    public String superCommand() default "x1";
    
    public String usage() default "";

}
