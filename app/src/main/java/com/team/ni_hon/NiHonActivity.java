package com.team.ni_hon;

import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class NiHonActivity extends AppCompatActivity {
    private final String TAG="NiHonActivity";
    private static String AppVersion;
    private static boolean nightMode=false;

    public static <T> Map<String, Object> convertObjectToMap(T object) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue = field.get(object);
            map.put(fieldName, fieldValue);
        }
        return map;
    }

    public void setAppVersion(String version){
        this.AppVersion=version;
    }

    public String getAppVersion(){
        return AppVersion;
    }

    public void setNightMode(boolean activated){
        this.nightMode=activated;
    }
    public boolean getNightMode(){
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                // Modo nocturno ya activado
                nightMode=true;
                break;
            case Configuration.UI_MODE_NIGHT_NO:
                // Modo nocturno no activado.
                nightMode=false;
                break;
        }
        return nightMode;
    }
}
