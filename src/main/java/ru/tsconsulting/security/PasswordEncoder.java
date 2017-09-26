package ru.tsconsulting.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder extends BCryptPasswordEncoder {
    private static PasswordEncoder instance;
    private PasswordEncoder(){};

    public static PasswordEncoder getInstance() {
        if(instance == null) {
            instance = new PasswordEncoder();
        }
        return instance;
    }

}
