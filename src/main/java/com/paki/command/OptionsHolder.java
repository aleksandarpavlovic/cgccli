package com.paki.command;

import java.util.Set;

public interface OptionsHolder {
    Set<Option> getOptions();
    void addOption(Option option);
    void addAllOptions(Set<Option> options);
}
