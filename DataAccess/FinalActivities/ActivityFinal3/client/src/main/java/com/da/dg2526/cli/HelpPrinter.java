package com.da.dg2526.cli;

public class HelpPrinter {
    private static final String BOLD = "\033[1m";
    private static final String RESET = "\033[0m";
    private static final String CYAN = "\033[36m";
    // Just for visuals

    private static final String BC = BOLD + CYAN;
    private static final String COMMON = BC + "{studentId::String} {curseId::String}" + RESET;


    public static void printFullHelp() {
        System.out.println(BOLD + CYAN + "Options:" + RESET);
        printHelp();
        printAddHelp();
        printLendHelp();
        printReturnHelp();
        System.out.println();
    }


    public static void printHelp() {
        System.out.println("  -h | --help: Show this help");
    }

    public static void printAddHelp() {
        System.out.println("  -a | --add " + BC + "{file.xml}" + RESET + ": add the books in the XML file to the database");
    }

    public static void printReturnHelp() {
        System.out.println("  -r | --return " + COMMON + BC + "{isbn::String} {userId::String}" + RESET + ": return lent book into library.");
    }

    public static void printLendHelp() {
        System.out.println("  -l | --lend " + COMMON + BC + "{isbn::String} {userId::String}" + RESET + ": lend a book from a library");
    }

}
