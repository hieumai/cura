package com.kms.cura.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by duyhnguyen on 6/23/2016.
 */
public class Countries {

    public static ArrayList<String> getAllCountries() {
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        return countries;
    }
}