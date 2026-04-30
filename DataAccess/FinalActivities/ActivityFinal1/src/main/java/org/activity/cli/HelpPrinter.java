package org.activity.cli;

public class HelpPrinter {
    private static final String BOLD = "\033[1m";
    private static final String RESET = "\033[0m";
    private static final String CYAN = "\033[36m";
    // Just for visuals

    private static final String BC = BOLD + CYAN;
    private static final String COMMON = BC+"{studentId::String} {curseId::String}"+RESET;


    public static void printFullHelp() {
        System.out.println(BOLD+CYAN+"Options:"+RESET);
        printHelp();
        printAddHelp();
        printEnrolHelp();
        printPrintHelp();
        printQualifyHelp();
        System.out.println();
    }


    public static void printHelp() {
        System.out.println("  -h | --help: Show this help");
    }

    public static void printAddHelp() {
        System.out.println("  -a | --add "+BC+"{file.xml}"+RESET+": add the student in the XML file to the database");
    }
    public static void printEnrolHelp() {
        System.out.println("  -e | --enroll "+COMMON+BC+"{year::String}"+RESET+": enroll a student into a course");
    }
    public static void printPrintHelp() {
        System.out.println("  -p | --print "+COMMON+": show the scores of a student in a course");
    }
    public static void printQualifyHelp() {
        System.out.println("  -q | --qualify "+COMMON+": introduce the scores obtained by the student in the course");
    }

}
