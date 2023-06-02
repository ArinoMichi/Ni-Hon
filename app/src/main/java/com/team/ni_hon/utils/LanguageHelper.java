package com.team.ni_hon.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageHelper {
    public static void setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        // Guardar el idioma seleccionado en las preferencias compartidas
        SharedPreferences preferences = context.getSharedPreferences("Config",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", languageCode);
        editor.apply();
    }

    public static String getLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Config",Context.MODE_PRIVATE);
        String language = preferences.getString("language", null);

        //Si no hay idioma guardado uso el del sistema
        if (language==null) {
            language = Locale.getDefault().getLanguage();
        }

        return language;
    }
}
