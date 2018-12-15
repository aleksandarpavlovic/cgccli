package com.paki.command;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DefaultOptionsHolder implements OptionsHolder {

    private Set<Option> options = new HashSet<>();

    @Override
    public Set<Option> getOptions() {
        return options;
    }

    @Override
    public void addOption(Option option) {
        this.options.add(option);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultOptionsHolder that = (DefaultOptionsHolder) o;
        return Objects.equals(options, that.options);
    }

    @Override
    public int hashCode() {

        return Objects.hash(options);
    }

    @Override
    public void addAllOptions(Set<Option> options) {
        this.options.addAll(options);

    }
}
