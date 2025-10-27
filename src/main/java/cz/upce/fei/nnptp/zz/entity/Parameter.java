package cz.upce.fei.nnptp.zz.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Parameter<T> {

    private T value;
    private final List<Predicate<T>> validators = new ArrayList<>();

    public Parameter() {
        assignDefaultValidators();
    }

    public Parameter(T value) {
        assignDefaultValidators();
        validate(value);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        validate(value);
        this.value = value;
    }

    public void addValidator(Predicate<T> validator) {
        validators.add(validator);
    }

    private void validate(T value) {
        for (Predicate<T> validator : validators) {
            if (!validator.test(value)) {
                throw new IllegalArgumentException(
                        "Validation failed for value: " + value);
            }
        }
    }

    private void assignDefaultValidators() {
        validators.add(v -> v != null);
    }

    public static class StandardizedParameters {
        public static final String TITLE = "title";
        public static final String EXPIRATION_DATETIME = "expiration-datetime";
        public static final String WEBSITE = "website";
        public static final String DESCRIPTION = "description";

    }

    // TODO: add support for validation rules
}
