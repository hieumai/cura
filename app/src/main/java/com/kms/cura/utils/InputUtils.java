package com.kms.cura.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by linhtnvo on 5/30/2016.
 */
public class InputUtils {
    public static boolean isEmailValid(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(email);

        return (email.contains("@") && matcher.matches());
    }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.length() <= 16;
    }

    public static boolean isNotEmpty(String text) {
        String regex = "[a-zA-Z]+";
        return (Pattern.compile(regex).matcher(text).matches());
    }

    public static boolean isNameValid(String text) {
        String VIETNAMESE_DIACRITIC_CHARACTERS = "ẮẰẲẴẶĂẤẦẨẪẬÂÁÀÃẢẠĐẾỀỂỄỆÊÉÈẺẼẸÍÌỈĨỊỐỒỔỖỘÔỚỜỞỠỢƠÓÒÕỎỌỨỪỬỮỰƯÚÙỦŨỤÝỲỶỸỴ";
        String oneWord = "(?:[" + VIETNAMESE_DIACRITIC_CHARACTERS + "]|[A-Z])++";
        String regex = "(" + oneWord + ")+( " + oneWord + ")*";
        return (Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(text).matches());
    }

    public static String covertValidStringForSQL(String note) {
        return note.replace("\'", "\\\'");
    }
}
