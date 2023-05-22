package com.team.ni_hon;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.team.ni_hon.databinding.ActivityLoginBinding;
import com.team.ni_hon.model.User;

import java.util.Objects;

public class LoginActivity extends NiHonActivity {

    private final String TAG="LoginActivity";
    private static final int RC_SIGN_IN = 123;
    private TextInputEditText email;
    private EditText password;
    private TextView welcome;
    private TextView welcomeJp;
    private TextView forgotPswd;
    private Button login;
    private Button loginGoogle;
    private ActivityLoginBinding bind;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView createAcc;
    private FirebaseAuth mAuth;
    private FirebaseFirestore userDataBase;
    private CollectionReference userCollRef;
    private Query query;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind=ActivityLoginBinding.inflate(getLayoutInflater());
        View view=bind.getRoot();
        setContentView(view);

        email =bind.editUsernameL;
        password =bind.editPasswordL;
        login=bind.buttonLogin;
        loginGoogle=bind.buttonGoogle;
        welcome=bind.welcome;
        welcomeJp=bind.welcomejp;
        createAcc=bind.createAcc;
        forgotPswd=bind.textForgotPassword;

        userDataBase=FirebaseFirestore.getInstance();
        userCollRef=userDataBase.collection("users");
        mAuth=FirebaseAuth.getInstance();

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        GetVersion();

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token != null) {
            ToMain();
            finish();
        }

        Test();
    }

    public void GetVersion(){
        if(getNightMode()){
            welcome.setTextColor(getResources().getColor(R.color.white));
            welcomeJp.setTextColor(getResources().getColor(R.color.white));
            createAcc.setTextColor(getResources().getColor(R.color.white));
            forgotPswd.setTextColor(getResources().getColor(R.color.white));
        }

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versioName=pInfo.versionName;
            setAppVersion(versioName);
        }catch (PackageManager.NameNotFoundException e){}
    }

    public void Test(){

        Intent intent=getIntent();
        String emailR=intent.getStringExtra("email");
        String passwdR=intent.getStringExtra("pswd");
        if(emailR!=null&&passwdR!=null){
            email.setText(emailR);
            password.setText(passwdR);

            try{
                Thread.sleep(3000);
                ToMain();
            }catch(InterruptedException e){}
        }

        login.setOnClickListener(v -> {
            if(!email.getText().toString().isEmpty()&&!password.getText().toString().isEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString().trim()
                        , password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            ShowMensaje(true);

                            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("token", email.getText().toString().trim());
                            editor.apply();

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
            GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            mGoogleSignInClient.signOut();
            Intent signIntent=mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signIntent,RC_SIGN_IN);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // Obtener resultado del inicio de sesión de Google
            try {
                Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account=task.getResult(ApiException.class);
                if(account!=null){
                    AuthCredential credential=GoogleAuthProvider.getCredential(account.getIdToken(),null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "signInWithCredential:success");

                                //Aquí obtengo los datos de la cuenta registrada de google a mi base de datos.
                                String emailGoogle=account.getEmail();
                                String usernameGoogle=account.getDisplayName();
                                String passwordG="*";
                                String GoogleUserId=mAuth.getCurrentUser().getUid();
                                User GoogleUser=new User(usernameGoogle,emailGoogle,passwordG,0,0);

                                query=userCollRef.whereEqualTo("email",emailGoogle);
                                query.get().addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()&& task1.getResult().isEmpty()){
                                        userCollRef.document(GoogleUserId)
                                                .set(GoogleUser)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task1) {
                                                        if(task1.isSuccessful()){
                                                            Log.d(TAG,"Se ha guardado el usuario Google con id: "+GoogleUserId);
                                                        }
                                                    }
                                                });
                                    }
                                });

                                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("token", account.getIdToken());
                                editor.apply();

                                ToMain();
                            }else{
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                ShowMensaje(false);
                            }
                        }
                    });
                }
            } catch (ApiException e) {
                Log.e(TAG,e.toString());
                ShowMensaje(false);
            }
        }else{
            Toast.makeText(this, R.string.errorLogInGoogle, Toast.LENGTH_SHORT).show();
        }
    }

    public void ShowMensaje(Boolean positivo){
        if(positivo)
            Toast.makeText(this,"usuario logeado",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
    }

    public void ToMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

}