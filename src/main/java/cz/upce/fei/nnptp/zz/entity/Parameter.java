package cz.upce.fei.nnptp.zz.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Generic class representing a parameter that holds a value of any type.
 * <p>
 * Used for storing information such as titles, descriptions, websites, or expiration dates
 * within password entries.
 * </p>
 *
 * @param <T> the type of the value stored in this parameter
 */
public class Parameter<T> {

    private T value;
    private final List<Predicate<T>> validators = new ArrayList<>();

    /**
     * Default constructor.
     * Initializes a new parameter without a value.
     */
    public Parameter() {
        assignDefaultValidators();
    }

    /**
     * Constructs a new parameter with the specified value.
     *
     * @param value the value to store in this parameter
     */
    public Parameter(T value) {
        assignDefaultValidators();
        validate(value);
        this.value = value;
    }

    /**
     * Returns the value stored in this parameter.
     *
     * @return the current value
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the value of this parameter.
     *
     * @param value the new value to assign
     */
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

    /**
     * Contains standardized parameter names used across the application.
     * <p>
     * Examples include title, description, website, and expiration date.
     * </p>
     */
    public static class StandardizedParameters {
        public static final String TITLE = "title";
        public static final String EXPIRATION_DATETIME = "expiration-datetime";
        public static final String WEBSITE = "website";
        public static final String DESCRIPTION = "description";

    }

    // TODO: add support for validation rules
}
