package com.htp.avia_booking.service.validator;

import com.htp.avia_booking.domain.objects.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginValidator implements ValidatorInterface<User> {

        private static final ValidatorInterface<User> instance = new LoginValidator();

    private LoginValidator(){}

        public static ValidatorInterface<User> getInstance(){
            return instance;
        }

        private static final String REGULAR_EXP_LOGIN = "[a-zA-Z]{2,30}";
        private static final String REGULAR_EXP_PASSWORD = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";

        private static final Pattern patternLogin = Pattern.compile(REGULAR_EXP_LOGIN);
        private static final Pattern patternPassword = Pattern.compile(REGULAR_EXP_PASSWORD);

        @Override
        public boolean isValid(User user) {
            String login = String.valueOf(user.getLogin());
            String password = String.valueOf(user.getPassword());

            Matcher matcherLogin = patternLogin.matcher(login);
            Matcher matcherPassword = patternPassword.matcher(password);

            return matcherLogin.matches() &
                    matcherPassword.matches();
        }
}
