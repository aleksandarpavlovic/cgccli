package com.paki.command;

public class HelpCommand implements Command {

    @Override
    public String identifier() {
        return "help";
    }

    private HelpCommand(){}

    public static HelpCommand instance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final HelpCommand instance = new HelpCommand();
    }
}
