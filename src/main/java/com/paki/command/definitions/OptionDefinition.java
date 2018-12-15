package com.paki.command.definitions;

import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Getter
public class OptionDefinition {
    private String name;
    private Optional<Set<String>> values;

    private OptionDefinition(String name, Set<String> values) {
        this.name = name;
        if (values == null || values.isEmpty()) {
            this.values = Optional.empty();
        } else {
            this.values = Optional.of(values);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OptionDefinition that = (OptionDefinition) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    public static class Builder {
        private String name;
        private Set<String> values;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withValue(String value) {
            if (this.values == null)
                this.values = new HashSet<>();
            this.values.add(value);
            return this;
        }

        public OptionDefinition build() {
            return new OptionDefinition(name, values);
        }
    }
}
