package com.paki.command;

import java.util.List;

public interface OptionsHolder {
    List<Option> getOptions();
    void addOption(Option option);
    void addAllOptions(List<Option> options);
}
