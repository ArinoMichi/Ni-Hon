package com.team.ni_hon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import com.team.ni_hon.databinding.ActivitySettingsBinding;
import com.team.ni_hon.utils.LanguageHelper;

import java.util.Locale;

public class SettingsActivity extends NiHonActivity {

    private final String TAG="SettingActivity";
    private TextView version;
    private Button language;
    private Switch nightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding bind=ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        version=bind.versionApp;
        language=bind.lang;
        nightMode=bind.switchbutton;

        version.setText(getAppVersion());

        //Establece el lenguaje actual del app.
        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
        //Muestra el lenguaje actual del app.
        showActualLanguage();
        //Muestra si la app está en modo nocturno.
        showIfNighModeActive();

        initComponent();
    }

    public void initComponent(){

        //OPCION: SELECCIONAR IDIOMA.
        language.setOnClickListener(v -> showLanguageMenu());

        //OPCION: SELECCIONAR MODO NOCHE.
        nightMode.setOnClickListener(v -> {
            if (nightMode.isChecked()) {
                // Cambiar a modo nocturno
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
                setNightMode(true);

                saveConfig(null,String.valueOf(true));
            } else {
                // Cambiar a modo normal
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
                setNightMode(false);

                saveConfig(null,String.valueOf(false));
            }
        });
    }

    public void showIfNighModeActive() {
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                // Modo nocturno ya activado
                nightMode.setChecked(true);
                setNightMode(true);
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                // Modo nocturno no activado.
                nightMode.setChecked(false);
                setNightMode(false);
                break;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                // No se especificó un modo de noche.
                nightMode.setChecked(false);
                break;
        }
    }

    public void setLanguage(String languageCode) {
        saveConfig(languageCode,null);

        showActualLanguage();
    }

    @SuppressLint("NonConstantResourceId")
    public void showLanguageMenu(){
        PopupMenu popupMenu = new PopupMenu(this, language, Gravity.END, 0, R.style.PopupMenuStyle);
        popupMenu.inflate(R.menu.language_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.action_english:
                    setLanguage("en");
                    break;
                case R.id.action_spanish:
                    setLanguage("es");
                    break;
                case R.id.action_chinese:
                    setLanguage("zh");
                    break;
            }
            return false;
        });

        popupMenu.show();
    }

    public void showActualLanguage(){
        SharedPreferences pref=getSharedPreferences("Config", MODE_PRIVATE);

        String ActualLang=pref.getString("language",null);

        if(ActualLang==null) {
           ActualLang = getCurrentLanguage(this);
        }
        switch (ActualLang) {
            case "en":
                language.setText(R.string.en);
                break;
            case "es":
                language.setText(R.string.es);
                break;
            case "zh":
                language.setText(R.string.ch);
                break;
        }
    }

    public static String getCurrentLanguage(Context context) {
        Configuration config = context.getResources().getConfiguration();
        Locale currentLocale = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            currentLocale = config.getLocales().get(0);
        } else {
            currentLocale = config.locale;
        }

        return currentLocale.getLanguage();
    }

    public void saveConfig(String lang,String night){
        SharedPreferences sharedPreferences = getSharedPreferences("Config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(lang!=null) {
            editor.putString("language", lang);
            LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
            recreate();
        }
        if(night!=null)
            editor.putBoolean("nightMode", Boolean.valueOf(night));
        editor.apply();
    }

    @Override
    public void onBackPressed(){
        showErrorMenssage(R.string.dialogSettingText,R.string.dialogSettingTitle);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
    }
}