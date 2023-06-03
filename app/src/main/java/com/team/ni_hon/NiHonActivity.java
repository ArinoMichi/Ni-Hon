package com.team.ni_hon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
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
    private static String userEmail,userPassword,googleToken;

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

    public void setGoogleToken(String token){
        this.googleToken=token;
    }

    public void setUserSession(String email,String password){
        this.userEmail=email;
        this.userPassword=password;
    }
    public static String getUserEmail(){return userEmail;}

    public static String getUserPassword(){return userPassword;}

    public static String getGoogleToken(){return googleToken;}

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

    public void restartApp() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

        // Finalizar el proceso actual
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void showErrorMenssage(int mensage,int title){
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_alertdialog, null);
        Button cancel=dialogView.findViewById(R.id.button_ok);
        Button dismiss=dialogView.findViewById(R.id.button_no);
        TextView text=dialogView.findViewById(R.id.dialog_text);
        TextView titleD=dialogView.findViewById(R.id.text_title);

        text.setText(mensage);
        titleD.setText(title);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        switch(title){
            case R.string.dialogSettingTitle:
                dismiss.setVisibility(dialogView.VISIBLE);
                alertDialog.setCancelable(true);
                cancel.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    restartApp();
                });

                dismiss.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    Intent intent=new Intent(NiHonActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                });
                break;
            case R.string.dialogDeleteTitle:
                dismiss.setVisibility(View.VISIBLE);
                cancel.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    UserInfoActivity.DeleteUser();
                });

                dismiss.setOnClickListener(v -> alertDialog.dismiss());
                break;
            default:
                cancel.setOnClickListener(v -> {
                    Log.d(TAG, "He entrado");
                    alertDialog.dismiss();
                    if (title == R.string.dialogErrorTitle)
                        onBackPressed();
                });
                break;
        }
    }
}
