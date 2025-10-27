package cz.upce.fei.nnptp.zz.entity;

import java.time.LocalDateTime;

/**
 * @author Roman
 */
public class Parameter<T> {

    private T value;

    public Parameter() {
    }

    public Parameter(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public static class StandardizedParameters {
        public static final String TITLE = "title";
        public static final String EXPIRATION_DATETIME = "expiration-datetime";
        public static final String WEBSITE = "website";
        public static final String DESCRIPTION = "description";

    }

    // TODO: add support for validation rules
}
