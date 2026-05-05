package com.da.dg2526;

import com.da.dg2526.cli.CommandParser;
import com.da.dg2526.cli.HelpPrinter;
import com.da.dg2526.controllers.*;
import com.da.dg2526.restapi.RestApiConnection;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        var parsed = CommandParser.parse(args);
        var conn = new RestApiConnection("http://localhost:8080/", "restapi");

        switch (parsed.command()) {
            case HELP:
                HelpPrinter.printFullHelp();
                break;

            case ADD:
                var addNewBookController = new AddNewBook(conn);
                addNewBookController.addFromXml(parsed);
                break;

            case RETURN:
                var enrollmentController = new ReturnBook(conn);
                enrollmentController.returnBook(parsed);
                break;

            case LEND:
                var lendBookController = new LendBook(conn);
                lendBookController.lendBook(parsed);
                break;

            default:
                System.out.println("Unknown command");
                HelpPrinter.printHelp();
        }
    }
}