/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.upce.fei.nnptp.zz.entity;

import java.time.LocalDateTime;

/**
 * @author Roman
 */
public class Parameter {

    public static class StandardizedParameters {
        public static final String TITLE = "title";
        public static final String EXPIRATION_DATETIME = "expiration-datetime";
        public static final String WEBSITE = "website";
        public static final String DESCRIPTION = "description";

    }

    public boolean isValid() {
        return true;
    }

    // TODO: add support for validation rules

    public static class TextParameter extends Parameter {
        private String value;

        public TextParameter(String value) {
            this.value = value;
        }

        public TextParameter() {
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public boolean isValid() {
            return value != null && !value.trim().isEmpty();
        }
    }

    public static class DateTimeParameter extends Parameter {
        private LocalDateTime value;

        public DateTimeParameter() {
        }

        public DateTimeParameter(LocalDateTime value) {
            this.value = value;
        }

        public LocalDateTime getValue() {
            return value;
        }

        public void setValue(LocalDateTime value) {
            this.value = value;
        }

        @Override
        public boolean isValid() {
            return value != null && !value.isBefore(LocalDateTime.now());
        }

    }

    public static class PasswordParameter extends Parameter {
        private String password;

        public PasswordParameter() {
        }

        public PasswordParameter(String password) {
            this.password = password;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public boolean isValid() {
            if (password == null) return false;
            if (password.length() < 8) return false;
            if (!password.matches(".*[A-Z].*")) return false;
            if (!password.matches(".*[a-z].*")) return false;
            if (!password.matches(".*\\d.*")) return false;
            return true;
        }


    }
}
