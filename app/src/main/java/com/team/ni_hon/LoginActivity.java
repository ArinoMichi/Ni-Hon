package com.team.ni_hon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.team.ni_hon.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText correo;
    private EditText contrasenia;
    private Button login;
    private Button sigup;
    private ActivityLoginBinding bind;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityLoginBinding.inflate(getLayoutInflater());
        View view=bind.getRoot();
        setContentView(view);

        correo=bind.editUsernameL;
        contrasenia=bind.editPasswordL;
        login=bind.buttonLogin;
        sigup=bind.buttonGoogle;

        Test();
    }

    public void Test(){

        login.setOnClickListener(v -> {
            if(correo.getText()!=null&&contrasenia.getText()!=null){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(correo.getText().toString()
                        ,contrasenia.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            ShowMensaje(true);
                        }else
                            ShowMensaje(false);
                    }
                });

            }
        });

        sigup.setOnClickListener(v -> {
            if(correo.getText()!=null&&contrasenia.getText()!=null){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo.getText().toString()
                        ,contrasenia.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            ShowMensaje(true);
                        }else
                            ShowMensaje(false);
                    }
                });
            }
        });
    }

    public void ShowMensaje(Boolean positivo){
        if(positivo)
            Toast.makeText(this,"usuario logeado",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
    }
}