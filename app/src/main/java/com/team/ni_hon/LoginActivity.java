package com.team.ni_hon;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.team.ni_hon.utils.LanguageHelper;

import java.util.Objects;

public class LoginActivity extends NiHonActivity {

    private final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 123;
    private TextInputEditText email;
    private TextInputEditText password;
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
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        setContentView(view);

        email = bind.editUsernameL;
        password = bind.editPasswordL;
        login = bind.buttonLogin;
        loginGoogle = bind.buttonGoogle;
        welcome = bind.welcome;
        welcomeJp = bind.welcomejp;
        createAcc = bind.createAcc;
        forgotPswd = bind.textForgotPassword;

        userDataBase = FirebaseFirestore.getInstance();
        userCollRef = userDataBase.collection("users");
        mAuth = FirebaseAuth.getInstance();

        //Recoge la version del app.
        GetVersion();
        //Establece el idioma del app.
        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
        //Inicia sesión para usuario de nuevo registro.
        initSesionForNewUser();
        //Iniciar sesión para usuario con sesión guardado.
        initSesionBySaveSesion();

        initComponent();
    }

    public void initComponent() {

        //OPCION: CREAR CUENTA.
        createAcc.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
        });

        //OPCION: RESTABLECER CONTRASEÑA.
        forgotPswd.setOnClickListener(v -> showDialog());

        //OPCION: INICIAR SESION CON CORREO Y CONTRASEÑA.
        login.setOnClickListener(v -> {
            showProgressDialog(R.string.verify);

            if (!email.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString().trim()
                        , password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            ShowMensaje(true);

                            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("token", email.getText().toString().trim());
                            editor.putString("pswd", password.getText().toString().trim());
                            editor.apply();

                            ToMain();
                        } else
                            ShowMensaje(false);
                    }
                });

            } else {
                cancelProgressDialog();
                Toast.makeText(this, R.string.empty_fields, Toast.LENGTH_LONG).show();
            }
        });

        //OPCION: INICIAR SESION CON CUENTA GOOGLE.
        loginGoogle.setOnClickListener(v -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            mGoogleSignInClient.signOut();
            Intent signIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signIntent, RC_SIGN_IN);
        });
    }

    public void initSesionBySaveSesion() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String pswd = sharedPreferences.getString("pswd", null);
        String google = sharedPreferences.getString("google", null);

        if (token != null) {
            if (pswd == null && google != null) {
                setGoogleToken(google);
                setUserSession(token, null);
            } else
                setUserSession(token, pswd);

            ToMain();
            finish();
        }
    }

    public void initSesionForNewUser() {
        Intent intent = getIntent();
        String emailR = intent.getStringExtra("email");
        String passwdR = intent.getStringExtra("pswd");
        if (emailR != null && passwdR != null) {
            email.setText(emailR);
            password.setText(passwdR);

            try {
                Thread.sleep(3000);
                ToMain();
            } catch (InterruptedException e) {
            }
        }
    }

    public void GetVersion() {
        if (getNightMode()) {
            welcome.setTextColor(getResources().getColor(R.color.white));
            welcomeJp.setTextColor(getResources().getColor(R.color.white));
            createAcc.setTextColor(getResources().getColor(R.color.white));
            forgotPswd.setTextColor(getResources().getColor(R.color.white));
        }

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versioName = pInfo.versionName;
            setAppVersion(versioName);
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showProgressDialog(R.string.verify);

        if (requestCode == RC_SIGN_IN) {
            // Obtener resultado del inicio de sesión de Google
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithCredential:success");

                                //Aquí obtengo los datos de la cuenta registrada de google a mi base de datos.
                                String emailGoogle = account.getEmail();
                                String usernameGoogle = account.getDisplayName();
                                String passwordG = "*";
                                String GoogleUserId = mAuth.getCurrentUser().getUid();
                                User GoogleUser = new User(usernameGoogle, emailGoogle, passwordG, 0, 1);

                                query = userCollRef.whereEqualTo("email", emailGoogle);
                                query.get().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful() && task1.getResult().isEmpty()) {
                                        userCollRef.document(GoogleUserId)
                                                .set(GoogleUser)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task1) {
                                                        if (task1.isSuccessful()) {
                                                            Log.d(TAG, "Se ha guardado el usuario Google con id: " + GoogleUserId);
                                                        }
                                                    }
                                                });
                                    }
                                });

                                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString("token", account.getEmail());
                                editor.putString("google", account.getIdToken());
                                editor.apply();

                                setGoogleToken(account.getIdToken());
                                setUserSession(account.getEmail(), null);

                                ToMain();
                            } else {
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                ShowMensaje(false);
                            }
                        }
                    });
                }
            } catch (ApiException e) {
                Log.e(TAG, e.toString());
                ShowMensaje(false);
            }
        } else {
            Toast.makeText(this, R.string.errorLogInGoogle, Toast.LENGTH_SHORT).show();
        }
    }

    public void showDialog() {

        View dialogView = LayoutInflater.from(this).inflate(R.layout.resetpswd_alertdialog, null);
        Button accept = dialogView.findViewById(R.id.button_rok);
        Button dismiss = dialogView.findViewById(R.id.button_rno);
        EditText remail = dialogView.findViewById(R.id.resetPswd_email);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendEmail = remail.getText().toString().trim();
                if (!sendEmail.isEmpty()) {
                    SendResetEmail(sendEmail);
                } else {
                    remail.setHintTextColor(getColor(android.R.color.holo_red_light));
                    remail.setHint(R.string.dialogEmptEmail);
                }
            }
        });

        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    public void ShowMensaje(@NonNull Boolean positivo) {
        if (positivo) {
            Toast.makeText(this, R.string.logged, Toast.LENGTH_LONG).show();
        } else {
            cancelProgressDialog();
            showErrorMenssage(R.string.dialogNotExistText, R.string.dialogNotExistTitle);
        }
    }

    public void SendResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                alertDialog.dismiss();
                Toast.makeText(this, R.string.checkEmail, Toast.LENGTH_SHORT).show();
            } else {
                alertDialog.dismiss();
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ToMain() {
        cancelProgressDialog();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));
    }
}