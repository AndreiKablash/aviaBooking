package com.htp.avia_booking.service.validator;

import com.htp.avia_booking.domain.objects.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationValidator implements ValidatorInterface<User> {

    private static final ValidatorInterface<User> instance = new RegistrationValidator();

    private RegistrationValidator(){}

    public static ValidatorInterface<User> getInstance(){
        return instance;
    }

    private static final String REGULAR_EXP_NAME = "[a-zA-Z]{2,20}"; // name from document id by Roman alphabet
    private static final String REGULAR_EXP_SURNAME = "[a-zA-Z]{2,30}"; //surname from document id by Roman alphabet
    private static final String REGULAR_EXP_DOCUMENT_ID = "^(?!^0+$)[a-zA-Z0-9]{3,20}$";
    private static final String REGULAR_EXP_EMAIL = "([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}";
    private static final String REGULAR_EXP_LOGIN = "[a-zA-Z]{2,30}";
    private static final String REGULAR_EXP_PASSWORD = "(?=^.{8,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
    private static final String REGULAR_EXP_PHONE = "\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$";

    private static final Pattern patternName = Pattern.compile(REGULAR_EXP_NAME);
    private static final Pattern patternSurname = Pattern.compile(REGULAR_EXP_SURNAME);
    private static final Pattern patternDocumentId = Pattern.compile(REGULAR_EXP_DOCUMENT_ID);
    private static final Pattern patternEmail = Pattern.compile(REGULAR_EXP_EMAIL);
    private static final Pattern patternLogin = Pattern.compile(REGULAR_EXP_LOGIN);
    private static final Pattern patternPassword = Pattern.compile(REGULAR_EXP_PASSWORD);
    private static final Pattern patternPhone = Pattern.compile(REGULAR_EXP_PHONE);

    @Override
    public boolean isValid(User user) {
        String name = user.getName();
        String surname = user.getSurname();
        String documentId = user.getDocumentId();
        String email = user.getEmail();
        String login = user.getLogin();
        String password = user.getPassword();
        String phone = user.getPhone();

        if(name.trim().isEmpty() || surname.trim().isEmpty() || documentId.trim().isEmpty() || email.trim().isEmpty()||
              login.trim().isEmpty()||  password.isEmpty() || phone.trim().isEmpty()){
            return false;
        } else {
            Matcher matcherName = patternName.matcher(name);
            Matcher matcherSurname = patternSurname.matcher(surname);
            Matcher matcherDocumentId = patternDocumentId.matcher(documentId);
            Matcher matcherLogin = patternLogin.matcher(login);
            Matcher matcherPassword = patternPassword.matcher(password);
            Matcher matcherEmail = patternEmail.matcher(email);
            Matcher matcherPhone = patternPhone.matcher(phone);

            return  matcherName.matches() &
                    matcherSurname.matches() &
                    matcherDocumentId.matches() &
                    matcherLogin.matches() &
                    matcherPassword.matches() &
                    matcherEmail.matches() &
                    matcherPhone.matches();
        }
    }
}
