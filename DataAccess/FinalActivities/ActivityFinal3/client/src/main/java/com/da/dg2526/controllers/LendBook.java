package com.da.dg2526.controllers;

import com.da.dg2526.cli.ParsedCommand;
import com.da.dg2526.restapi.RestApiConnection;

public class LendBook {
    private final RestApiConnection connection;

    public LendBook(RestApiConnection conn) {
        this.connection = conn;
    }

    public void lendBook(ParsedCommand parsed) {
    }
}
