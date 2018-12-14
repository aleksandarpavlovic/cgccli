package com.paki.command;

import java.util.ArrayList;
import java.util.List;

public class DefaultOptionsHolder implements OptionsHolder {

    private List<Option> options = new ArrayList<>();

    @Override
    public List<Option> getOptions() {
        return options;
    }

    @Override
    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public void addAllOptions(List<Option> options) {
        options.addAll(options);
    }
}
