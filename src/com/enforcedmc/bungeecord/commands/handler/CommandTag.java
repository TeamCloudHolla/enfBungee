package com.enforcedmc.bungeecord.commands.handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandTag {
	String usage() default "Usage of command.";
}