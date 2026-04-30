package org.activity.cli;

import java.util.Arrays;

public class CommandParser {
    public static ParsedCommand parse(String[] args) {

        if (args == null || args.length == 0) {
            return new ParsedCommand(Command.HELP, new String[0]);
        }

        Command command = Command.from(args[0]);

        String[] remainingArgs = Arrays.copyOfRange(args, 1, args.length);

        return new ParsedCommand(command, remainingArgs);
    }
}
