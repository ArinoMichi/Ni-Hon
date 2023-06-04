package com.team.ni_hon;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.team.ni_hon.databinding.ActivityUserInfoBinding;
import com.team.ni_hon.local_data_base.OptionsSQLiteHelper;
import com.team.ni_hon.local_data_base.QuestionSQLiteHelper;
import com.team.ni_hon.local_data_base.UserSQLiteHelper;

import java.util.List;

public class UserInfoActivity extends NiHonActivity {

    private final String TAG = "UserInfoActivity";
    private TextView userName, userLevel, tanukiStatus, userScore;
    private String email;
    private ImageView Icon, delete;
    private CircularProgressBar progressBar;
    private static FirebaseFirestore myDB;
    private static CollectionReference usersRef;
    private static Context context;
    private static UserSQLiteHelper userSQLiteHelper;
    private static QuestionSQLiteHelper questionSQLiteHelper;
    private static OptionsSQLiteHelper optionsSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUserInfoBinding bind = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        context = UserInfoActivity.this;
        userName = bind.UserName;
        Icon = bind.UserIcon;
        userLevel = bind.level;
        delete = bind.deleteCount;
        progressBar = bind.progressBar;
        tanukiStatus = bind.tanuki;
        userScore = bind.score;


        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        email = sharedPreferences.getString("token", null);

        myDB = FirebaseFirestore.getInstance();
        usersRef = myDB.collection("users");

        userSQLiteHelper = new UserSQLiteHelper(this);
        questionSQLiteHelper = new QuestionSQLiteHelper(this);
        optionsSQLiteHelper = new OptionsSQLiteHelper(this);

        if (email != null) {
            initComponent();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initComponent() {
        showProgressDialog(R.string.load);

        buttonEffects();

        loadUserData();

        delete.setOnClickListener(v -> showErrorMenssage(R.string.dialogDeleteMensage, R.string.dialogDeleteTitle));

        Icon.setOnClickListener(v -> showIconMenu());
    }

    public void loadUserData() {
        Query query = usersRef.whereEqualTo("email", email);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                    String userName = document.getString("name");
                    String GoogleUserName = document.getString("nombre");
                    int icon = document.getLong("icon").intValue();
                    int level = document.getLong("level").intValue();

                    showUserData(userName, GoogleUserName, icon, level);
                } else {
                    cancelProgressDialog();
                    Exception exception = task.getException();
                    if (exception != null) {
                        Log.e("Firestore", "Error: " + exception.getMessage());
                    }
                    showErrorMenssage(R.string.dialogErrorText
                            , R.string.dialogErrorTitle);
                }
            } else {
                cancelProgressDialog();
                Exception exception = task.getException();
                if (exception != null) {
                    Log.e("Firestore", "Error: " + exception.getMessage());
                }
                showErrorMenssage(R.string.dialogErrorText, R.string.dialogErrorTitle);
            }
        });
    }

    public void showUserData(String name, String GoogleName, int icon, int level) {
        cancelProgressDialog();
        if (name == null)
            userName.setText(GoogleName);
        else if (GoogleName == null)
            userName.setText(name);

        userLevel.append(String.valueOf(level));
        progressBar.setProgress((level - 1) * 10);

        //donde está el 0 será el valor de la racha de aciertos más alta. (por defecto siempre es 0)
        int points = (level + (0)) * 69;
        if (points > 2000) {
            String htmlText = "<font color='#F6B300'>" + points + "</font>"; //Golden
            Spanned resultScore = Html.fromHtml(htmlText);
            userScore.append(resultScore);
        } else if (points < 2000 && points > 1000) {
            String htmlText = "<font color='#12F000'>" + points + "</font>"; //Verde
            Spanned resultScore = Html.fromHtml(htmlText);
            userScore.append(resultScore);
        } else {
            String htmlText = "<font color='#B4B4AF'>" + points + "</font>"; //Gris
            Spanned resultScore = Html.fromHtml(htmlText);
            userScore.append(resultScore);
        }

        String htmlText;
        Spanned spannedText;

        switch (level) {
            case 0:
                htmlText = "<font color='#B4B4AF'>" + getString(R.string.egg) + "</font>"; //Gris
                spannedText = Html.fromHtml(htmlText);
                break;
            case 1:
            case 2:
                htmlText = "<font color='#FF0000'>" + getString(R.string.bad) + "</font>"; //Rojo
                spannedText = Html.fromHtml(htmlText);
                break;
            case 3:
            case 4:
                htmlText = "<font color='#12F000'>" + getString(R.string.medium) + "</font>"; //Verde
                spannedText = Html.fromHtml(htmlText);
                break;
            case 5:
                htmlText = "<font color='#F6B300'>" + getString(R.string.great) + "</font>"; //Golden
                spannedText = Html.fromHtml(htmlText);
                break;
            default:
                htmlText = "<font color='#B054F8'>" + getString(R.string.missing) + "</font>"; //Violet
                spannedText = Html.fromHtml(htmlText);
                break;
        }
        tanukiStatus.append(spannedText);

        switch (icon) {
            case 1:
                Icon.setImageResource(R.drawable.moon_icon);
                break;
            case 2:
                Icon.setImageResource(R.drawable.japan_icon);
                break;
            case 3:
                Icon.setImageResource(R.drawable.yukata_icon);
                break;
            default:
                Icon.setImageResource(R.drawable.user_icon_default);
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    public void showIconMenu() {
        PopupMenu popupMenu = new PopupMenu(this, Icon, Gravity.END, 0, R.style.PopupMenuStyle);
        popupMenu.inflate(R.menu.icon_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.icon_default:
                    Icon.setImageResource(R.drawable.user_icon_default);
                    saveIcon(0);
                    break;
                case R.id.icon_moon:
                    Icon.setImageResource(R.drawable.moon_icon);
                    saveIcon(1);
                    break;
                case R.id.icon_japan:
                    Icon.setImageResource(R.drawable.japan_icon);
                    saveIcon(2);
                    break;
                case R.id.icon_yukata:
                    Icon.setImageResource(R.drawable.yukata_icon);
                    saveIcon(3);
                    break;
            }
            return false;
        });

        popupMenu.show();
    }

    public void saveIcon(int icon) {
        Query query = usersRef.whereEqualTo("email", getUserEmail());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String documentId = document.getId();
                    usersRef.document(documentId).update("icon", icon)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, "Se ha guardo el Icono", Toast.LENGTH_SHORT).show();
                                recreate();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "No se ha podido guardar el Icono", Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                Toast.makeText(context, "No se ha podido guardar el Icono", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void buttonEffects() {
        delete.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint({"ClickableViewAccessibility", "UseCompatLoadingForDrawables"})
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    delete.setBackground(getDrawable(R.drawable.circular_shape));
                    delete.setScaleX(0.9f);
                    delete.setScaleY(0.9f);
                } else {
                    delete.setBackground(getDrawable(R.drawable.button));
                    delete.setScaleX(1.0f);
                    delete.setScaleY(1.0f);
                }
                return false;
            }
        });
    }

    public static void DeleteUser() {
        if (getGoogleToken() == null)
            DeleteEmailUser();
        else
            DeleteGoogleUser();

        DeleteLocalData();
    }

    private static void DeleteLocalData() {
        //Elimino todas las preguntas asociadas a esta cuenta. (en local).
        userSQLiteHelper.deleteByEmail(getUserEmail());

        questionSQLiteHelper.deleteByEmail(getUserEmail());

        optionsSQLiteHelper.deleteOptionsByIdQuestions(
                questionSQLiteHelper.getIdQuestionsByEmail(getUserEmail()));
    }

    private static void DeleteGoogleUser() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, gso);

        googleSignInClient.signOut()
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // El usuario se ha deslogueado exitosamente de Google
                        // Ahora puedes eliminar el usuario de Firebase Authentication
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // El usuario se eliminó exitosamente de Firebase Authentication
                                                DeleteUserFromDataBase(getUserEmail());
                                            } else {
                                                // Error al eliminar el usuario de Firebase Authentication
                                                ErrorMensage();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private static void DeleteEmailUser() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(getUserEmail(), getUserPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if (user != null) {
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // El usuario se eliminó exitosamente de Firebase Authentication
                                                    // Ahora puedes eliminarlo de la base de datos (Cloud Firestore)
                                                    DeleteUserFromDataBase(getUserEmail());
                                                } else {
                                                    // Error al eliminar el usuario de Firebase Authentication
                                                    ErrorMensage();
                                                }
                                            }
                                        });
                            }
                        } else {
                            // Error al autenticar al usuario
                        }
                    }
                });
    }

    public static void DeleteUserFromDataBase(String email) {
        Query query = usersRef.whereEqualTo("email", email);
        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                        }
                        sayGoodbye();
                    } else {
                        ErrorMensage();
                    }
                });
    }

    public static void sayGoodbye() {
        //Borro la sesión del usuario guardado.
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove("token");
        editor.remove("pswd");
        editor.remove("google");
        editor.remove("level");
        editor.apply();

        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_alertdialog, null);
        Button cancel = dialogView.findViewById(R.id.button_ok);
        TextView text = dialogView.findViewById(R.id.dialog_text);
        TextView titleD = dialogView.findViewById(R.id.text_title);

        text.setText(R.string.bye_message);
        titleD.setText(R.string.bye_title);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        cancel.setOnClickListener(v -> {
            alertDialog.dismiss();
            ((Activity) context).finish();
            //terminar el proceso actual y salir.
            System.exit(0);
        });
    }

    public static void ErrorMensage() {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_alertdialog, null);
        Button cancel = dialogView.findViewById(R.id.button_ok);
        TextView text = dialogView.findViewById(R.id.dialog_text);
        TextView titleD = dialogView.findViewById(R.id.text_title);

        text.setText("Error inesperado");
        titleD.setText("No se ha podido borrar el usuario");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        cancel.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}