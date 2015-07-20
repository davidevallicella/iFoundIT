package com.cellasoft.ifoundit.utils;

import android.widget.EditText;
import com.pipl.api.data.Utils;

import java.util.regex.Pattern;

public class Validation {

    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{7}";
    private static final String COUNTRY_STATE_REGEX = "^[a-zA-Z]{2}$";
    private static final String NAME_REGEX = ".{2,}";
    private static final String NUMERIC_REGEX = "[0-9]+";

    // Error Messages
    private static final String REQUIRED_MSG = "required";
    private static final String EMAIL_MSG = "invalid email";
    private static final String PHONE_MSG = "###-#######";
    private static final String NAME_MSG = "min 2 char";
    private static final String COUNTRY_MSG = "invalid country";
    private static final String STATE_MSG = "invalid state";
    private static final String NUMERIC_MSG = "invalid number";

    // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }

    public static boolean isName(EditText editText, boolean required) {
        return isValid(editText, NAME_REGEX, NAME_MSG, required);
    }

    public static boolean isNumeric(EditText editText, boolean required) {
        return isValid(editText, NUMERIC_REGEX, NAME_MSG, required);
    }

    public static boolean isCountry(EditText editText, boolean required) {
        if (isValid(editText, COUNTRY_STATE_REGEX, COUNTRY_MSG, required)) {
            if (Utils.COUNTRIES.containsKey(editText.getText().toString().trim().toUpperCase())) {
                return true;
            } else {
                editText.setError(COUNTRY_MSG);
            }
        }

        return false;
    }

    public static boolean isState(EditText editText, String country, boolean required) {
        String state = editText.getText().toString().trim().toUpperCase();
        if (required || state.length() > 0) {
            if (Utils.STATES.containsKey(country.trim().toUpperCase())) {
                if (Utils.STATES.get(country.trim().toUpperCase()).containsKey(state)) {
                    return true;
                } else {
                    editText.setError(STATE_MSG);
                }
            }
        } else { // required or empty
            return true;
        }

        return false;
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and
        if (required) {
            //editText is blank, so return false
            if (!hasText(editText)) {
                return false;
            }

            // pattern doesn't match so returning false
            if (!Pattern.matches(regex, text)) {
                editText.setError(errMsg);
                return false;
            }
        }

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }
}