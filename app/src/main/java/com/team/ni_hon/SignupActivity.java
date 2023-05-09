package com.team.ni_hon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.team.ni_hon.databinding.ActivitySignupBinding;
import com.team.ni_hon.model.User;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends NiHonActivity {

    private String TAG ="SignUpActivity";
    private TextInputEditText username;
    private TextInputEditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button save;
    private ActivitySignupBinding bind;
    private FirebaseAuth myLoginAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore userDataBase=FirebaseFirestore.getInstance();
    private CollectionReference userCollRef=userDataBase.collection("users");
    private Query query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivitySignupBinding.inflate(getLayoutInflater());
        View view=bind.getRoot();
        setContentView(view);

        username=bind.editUsername;
        email=bind.editEmail;
        password=bind.editPassword;
        confirmPassword=bind.editRepeatPassword;
        save=bind.buttonSignUp;

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveNewUser();
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void saveNewUser() throws IllegalAccessException {
        String name=username.getText().toString().trim();
        String emails=email.getText().toString().trim();
        String pswd=password.getText().toString().trim();
        String rpswd=confirmPassword.getText().toString().trim();

        if(name.isEmpty()||emails.isEmpty()||pswd.isEmpty()||rpswd.isEmpty()){
            Toast.makeText(this,"Hay campos vacios",Toast.LENGTH_LONG).show();
        }else if(!pswd.equals(rpswd)){
            Toast.makeText(this,"Las contraseñas introducidas no coinciden",Toast.LENGTH_LONG).show();
        }else if (pswd.length()<6){
            Toast.makeText(this,"La contraseña debe no puede ser inferior a 6 digitos",Toast.LENGTH_LONG).show();
        }else{
            User newUser=new User(name,emails,pswd,0,0);

            if(newUser!=null) {
                Map<String,Object> UserToAdd=convertObjectToMap(newUser);

                query=userCollRef.whereEqualTo("email",emails);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()&&task.getResult().isEmpty()){
                            myLoginAuth.createUserWithEmailAndPassword(newUser.getEmail(),newUser.getPassword()).addOnCompleteListener(mtask->{
                                if(mtask.isSuccessful()){
                                    String userId = myLoginAuth.getCurrentUser().getUid();
                                    userCollRef.document(userId)
                                            .set(UserToAdd)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot added with ID: " +userId);
                                                    mostrarMensaje(true);
                                                    ToLogin(emails,pswd);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                }
                                            });
                                }
                            });
                        }else{
                            mostrarMensaje(false);
                        }
                    }
                });



            }else
                Toast.makeText(this,"Se produjo un error inesperado",Toast.LENGTH_LONG).show();
        }

    }

    public void mostrarMensaje(boolean positivo){
        if(positivo)
            Toast.makeText(this,"Usuario registrado correctamente",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"El correo ya tiene una cuenta asociada",Toast.LENGTH_LONG).show();
    }

    private void ToLogin(String emails, String pswd) {
        Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
        intent.putExtra("email",emails);
        intent.putExtra("pswd",pswd);
        startActivity(intent);
    }

}