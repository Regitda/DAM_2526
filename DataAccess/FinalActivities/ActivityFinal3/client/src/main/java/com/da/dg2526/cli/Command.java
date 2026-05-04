package com.da.dg2526.cli;

public enum Command {
    HELP("--h","--help"),
    ADD("--a","--add"),
    LEND("--l","--lend"),
    RETURN("--r","--return"),
    UNKNOWN("","");

    private final String shortOpt;
    private final String longOpt;

    Command(String shortOpt, String longOpt) {
        this.shortOpt = shortOpt;
        this.longOpt = longOpt;
    }

    public static Command from(String arg) {
        for (Command c : values()) {
            if (c.shortOpt.equals(arg) || c.longOpt.equals(arg)) {
                return c;
            }
        }
        return UNKNOWN;
    }
}
