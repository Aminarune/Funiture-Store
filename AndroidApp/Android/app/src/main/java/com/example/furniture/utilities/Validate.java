package com.example.furniture.utilities;

import android.util.Patterns;
import android.widget.EditText;


import java.util.Random;
import java.util.regex.*;

public class Validate {

    // Function to validate the password.
    public static boolean isValidPassword(String pass)
    {


        // Regex to check valid password.
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{5,8}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the password is empty
        // return false
        if (pass == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given password
        // and regular expression.
        Matcher m = p.matcher(pass);

        // Return if the password
        // matched the ReGex
        return m.matches();
    }


    public static boolean emailValidator(String etMail) {
        // extract the entered data from the EditText

        // Android offers the inbuilt patterns which the entered
        // data from the EditText field needs to be compared with
        // In this case the the entered data needs to compared with
        // the EMAIL_ADDRESS, which is implemented same below
        if (!etMail.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(etMail).matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidPhone(String phone){

        String regex= "^\\d{10}$";

        Pattern pattern = Pattern.compile(regex);


        if(phone == null){
            return false;
        }

        Matcher matcher = pattern.matcher(phone);

        return matcher.matches();
    }


    public static boolean isValidUsername(String name)
    {
        // Regex to check valid username.
        String regex = "^[A-Za-z]\\w{3,20}$";

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);

        // If the username is empty
        // return false
        if (name == null) {
            return false;
        }

        // Pattern class contains matcher() method
        // to find matching between given username
        // and regular expression.
        Matcher m = p.matcher(name);

        // Return if the username
        // matched the ReGex
        return m.matches();
    }


}
