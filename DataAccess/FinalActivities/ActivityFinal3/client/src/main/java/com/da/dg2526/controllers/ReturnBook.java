package com.da.dg2526.controllers;

import com.da.dg2526.cli.ParsedCommand;
import com.da.dg2526.restapi.RestApiConnection;

public class ReturnBook {

    private final RestApiConnection connection;

    public ReturnBook(RestApiConnection conn) {
        this.connection = conn;
    }

    public void returnBook(ParsedCommand parsed) {
    }
}
