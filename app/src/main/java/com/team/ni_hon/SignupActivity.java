package com.team.ni_hon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team.ni_hon.databinding.ActivitySignupBinding;
import com.team.ni_hon.model.Usuario;

public class SignupActivity extends AppCompatActivity {

    private String TAG ="SignUpActivity";
    private TextInputEditText username;
    private TextInputEditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button save;
    private ActivitySignupBinding bind;
    private DatabaseReference userRef=FirebaseDatabase.getInstance().getReference("Usuario");

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
                guardarUsuario();
            }
        });

        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Usuario user=snapshot.getValue(Usuario.class);
                if(user!=null){
                    Log.d(TAG,"usuario existe");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void guardarUsuario(){
        String name=username.getText().toString().trim();
        String correo=email.getText().toString().trim();
        String contrasenia=password.getText().toString().trim();
        String repcontrasenia=confirmPassword.getText().toString().trim();

        Usuario newUser=new Usuario(name,correo,contrasenia,0,0);
        userRef.push().setValue(newUser);
    }
}