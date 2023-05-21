package com.team.ni_hon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class NiHonActivity extends AppCompatActivity {
    private final String TAG="NiHonActivity";
    private static String AppVersion;
    private static boolean nightMode=false;
    private ProgressDialog progressDialog;

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

    public void showProgressDialog(int text) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(text));

        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        // Cambiar el color del spinner
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.pink), PorterDuff.Mode.SRC_IN);
        progressDialog.setIndeterminateDrawable(progressBar.getIndeterminateDrawable());

        progressDialog.show();
        progressDialog.setCancelable(false);
    }

    public void cancelProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void showErrorMenssage(int mensage,int title){
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alertdialog, null);
        Button cancel=dialogView.findViewById(R.id.button_ok);
        TextView text=dialogView.findViewById(R.id.dialog_text);
        TextView titleD=dialogView.findViewById(R.id.text_title);

        text.setText(mensage);
        titleD.setText(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"He entrado");
                alertDialog.dismiss();
                if(title==R.string.dialogErrorTitle)
                    onBackPressed();
            }
        });

    }
}
