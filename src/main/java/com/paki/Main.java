package com.paki;

import com.paki.parser.Parser;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser();
        parser.parseCommand(args);
    }
}
