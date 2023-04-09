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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText correo;
    private EditText contrasenia;
    private Button login;
    private Button sigup;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correo=findViewById(R.id.email);
        contrasenia=findViewById(R.id.password);
        login=findViewById(R.id.send);
        sigup=findViewById(R.id.get);

        Test();
    }

    public void Test(){

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        sigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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