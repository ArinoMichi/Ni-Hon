package com.team.ni_hon;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.team.ni_hon.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private final String TAG="LoginActivity";
    private static final int RC_SIGN_IN = 123;
    private TextInputEditText correo;
    private EditText contrasenia;
    private Button login;
    private Button loginGoogle;
    private ActivityLoginBinding bind;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView createAcc;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    private ActivityResultLauncher<Intent> mStartForResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                    try {
                        // Autenticar con Firebase
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account);
                    } catch (ApiException e) {
                        // Error al iniciar sesión con Google
                        Log.w(TAG, "Fallo en inicio de sesión con Google.", e);
                    }
                }
            });


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
        loginGoogle=bind.buttonGoogle;

        //Instancio registro con google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        createAcc=findViewById(R.id.createAcc);
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        Test();
    }

    public void Test(){

        login.setOnClickListener(v -> {
            if(!correo.getText().toString().isEmpty()&&!contrasenia.getText().toString().isEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(correo.getText().toString().trim()
                        ,contrasenia.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            ShowMensaje(true);
                            ToMain();
                        }else
                            ShowMensaje(false);
                    }
                });

            }else{
                Toast.makeText(this,"Campos vacios",Toast.LENGTH_LONG).show();
            }
        });

        loginGoogle.setOnClickListener(v -> {
            signInWithGoogle();
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        mStartForResultLauncher.launch(signInIntent);
    }


    public void ShowMensaje(Boolean positivo){
        if(positivo)
            Toast.makeText(this,"usuario logeado",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
    }

    public void ToMain(){
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión con éxito, actualizar la interfaz de usuario con la información del usuario conectado
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user==null){
                                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                                mAuth.createUserWithEmailAndPassword(acct.getEmail(), acct.getIdToken())
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "createUserWithEmailAndPassword:success");
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    ToMain();
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Fallo en inicio de sesión, mostrar mensaje de error al usuario
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Fallo en inicio de sesión.", Toast.LENGTH_SHORT).show();
                            // ...
                        }
                    }
                });
    }

}