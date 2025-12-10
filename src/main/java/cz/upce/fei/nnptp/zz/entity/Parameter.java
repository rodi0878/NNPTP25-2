package cz.upce.fei.nnptp.zz.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final transient List<Predicate<T>> validators = new ArrayList<>();

    /**
     * Default constructor.
     * Initializes a new parameter without a value and assigns default validators.
     */
    public Parameter() {
        assignDefaultValidators();
    }

    /**
     * Constructs a new parameter with the specified value.
     * Default validators are assigned and the value is validated.
     *
     * @param value the value to store in this parameter
     * @throws IllegalArgumentException if validation fails
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
     * The new value is validated before being assigned.
     *
     * @param value the new value to assign
     * @throws IllegalArgumentException if validation fails
     */
    public void setValue(T value) {
        validate(value);
        this.value = value;
    }

    /**
     * Adds a custom validator to this parameter.
     * Validators are checked whenever a new value is assigned.
     *
     * @param validator a predicate representing the validation rule
     */
    public void addValidator(Predicate<T> validator) {
        Objects.requireNonNull(validator, "validator must not be null");
        validators.add(validator);
        validateCurrentValue();
    }

    /**
     * Validates the provided value using all assigned validators.
     *
     * @param value the value to validate
     * @throws IllegalArgumentException if any validator fails
     */
    private void validate(T value) {
        for (Predicate<T> validator : validators) {
            if (!validator.test(value)) {
                throw new IllegalArgumentException(
                        "Validation failed for value: " + value);
            }
        }
    }

    /**
     * Assigns the default validation rules for this parameter.
     * Currently, ensures that the value is not null.
     */
    private void assignDefaultValidators() {
        validators.add(Objects::nonNull);
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

    /** Validates the current internal value after adding new validators. */
    private void validateCurrentValue() {
        if (value != null) {
            validate(value);
        }
    }

    /**
     * Compares this parameter to another object for equality.
     * Two Parameter instances are considered equal if they are of the same type
     * and their underlying values are equal according to {@link Objects#equals(Object, Object)}.
     *
     * @param o the object to compare with this parameter
     * @return {@code true} if the specified object is equal to this parameter; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Parameter<?> that = (Parameter<?>) o;
        return Objects.equals(value, that.value);
    }

    /**
     * Returns a hash code value for this parameter.
     * The hash code is based solely on the underlying value to be consistent
     * with the implementation of {@link #equals(Object)}.
     *
     * @return the hash code value for this parameter
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Returns a string representation of this parameter.
     * The returned string contains the simple class name and the current value
     * stored in this parameter, which is useful for logging and debugging.
     *
     * @return a string representation of this parameter
     */
    @Override
    public String toString() {
        return "Parameter{" +
                "value=" + value +
                '}';
    }

}
