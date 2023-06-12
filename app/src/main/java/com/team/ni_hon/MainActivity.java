package com.team.ni_hon;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.team.ni_hon.databinding.ActivityMainBinding;
import com.team.ni_hon.local_data_base.OptionsSQLiteHelper;
import com.team.ni_hon.local_data_base.QuestionSQLiteHelper;
import com.team.ni_hon.local_data_base.UserSQLiteHelper;
import com.team.ni_hon.local_data_base.database_models.LocalUser;
import com.team.ni_hon.local_data_base.database_models.Option;
import com.team.ni_hon.local_data_base.database_models.Question;
import com.team.ni_hon.main_recycler.Lesson;
import com.team.ni_hon.main_recycler.MainLessonAdapter;
import com.team.ni_hon.main_recycler.MyLinearLayoutManager;
import com.team.ni_hon.utils.LanguageHelper;
import com.team.ni_hon.utils.MyJsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class MainActivity extends NiHonActivity {

    private final String TAG = "MainActivity";

    private static final int RC_SIGN_IN = 123;
    private static final String FILE = "preguntas.json";
    private String LoginEmail;
    private LocalUser localUser;
    private ImageView icon;
    private TextView userName;
    ImageView backgroundImage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore userDataBase;
    private CollectionReference userCollRef;
    private AssetManager assetManager;
    private UserSQLiteHelper userSQLiteHelper;
    private QuestionSQLiteHelper questionSQLiteHelper;
    private OptionsSQLiteHelper optionsSQLiteHelper;

    public static ArrayList<Lesson> lessons = new ArrayList<Lesson>() {
        {
            add(new Lesson(1, "Hiragana 1", "あ い う え お", 6));
            add(new Lesson(2, "Hiragana 2", "か き く け こ", 6));
            add(new Lesson(3, "Hiragana 3", "さ し す せ そ", 6));
            add(new Lesson(4, "Hiragana 4", "た ち つ て と", 6));
            add(new Lesson(5, "Hiragana 5", "な に ぬ ね の", 6));
        }
    };

    private MainLessonAdapter lessonAdapter;
    private static RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding bind = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        setTitle(null);

        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));

        recyclerView = bind.recycler;
        icon = bind.userImg;
        userName = bind.email;

        userDataBase = FirebaseFirestore.getInstance();
        userCollRef = userDataBase.collection("users");
        mAuth = FirebaseAuth.getInstance();

        SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        LoginEmail=pref.getString("token",null);

        assetManager = getAssets();
        localUser = new LocalUser();

        userSQLiteHelper = new UserSQLiteHelper(this);
        questionSQLiteHelper = new QuestionSQLiteHelper(this);
        optionsSQLiteHelper = new OptionsSQLiteHelper(this);

        //Se usa para borrar la base de datos.
        //getApplicationContext().deleteDatabase(UserSQLiteHelper.DATABASE_NAME);

        //Crear las tablas si no existen:
        createLocalTable();

        initComponent();
    }

    public void initComponent() {
        showProgressDialog(R.string.load);

        loadUserBasicData();

        setUserQuestions();

        icon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
            startActivity(intent);
            finish();
        });

        getUserLevel();
    }

    public void getUserLevel() {
        Query query = userCollRef.whereEqualTo("email", LoginEmail);
        query.get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful() && !task1.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task1.getResult()) {
                    int level = document.getLong("level").intValue();
                    saveLevel(level);

                    lessonAdapter = new MainLessonAdapter(this, lessons, level);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new MyLinearLayoutManager(this, MyLinearLayoutManager.HORIZONTAL, false));
                    recyclerView.setAdapter(lessonAdapter);
                    recyclerView.scrollToPosition(level - 1);

                    cancelProgressDialog();
                    return;
                }

            }else{
                cancelProgressDialog();
                deleteUserSesion();
                Toast.makeText(this, R.string.dialogSettingTitle, Toast.LENGTH_SHORT).show();
                restartApp();
            }
        });
    }

    public void saveLevel(int level) {
        SharedPreferences pref = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("level", level);
        editor.apply();
    }

    public void setUserQuestions() {
        String questionsData = loadMyJsonFromAsset(assetManager, FILE);
        if (questionsData != null && !questionsData.isEmpty())
            localUser = MyJsonParser.parseJsonToObjects(questionsData);

        try {
            localUser.setEmail(LoginEmail);

            if (userSQLiteHelper.VerifyIfEmailExist(LoginEmail)) {
                Log.d(TAG, "El usuario ya tiene las preguntas");
            } else {
                //Cargo las preguntas para el usuario.
                userSQLiteHelper.addUser(LoginEmail);

                for (Question q : localUser.getQuestions()) {
                    if (q != null) {
                        q.setEmail(LoginEmail);
                        questionSQLiteHelper.addQuestion(q);

                        if (q.getOptions() != null) {
                            for (Option o : q.getOptions()) {
                                if (o != null) {
                                    optionsSQLiteHelper.addOption(o);
                                }
                            }
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
        }
    }

    public void createLocalTable() {
        SQLiteDatabase db = userSQLiteHelper.getWritableDatabase();
        if (!isTableExists(db, UserSQLiteHelper.TABLE_NAME)) {
            userSQLiteHelper.onCreate(db);
        }

        if (!isTableExists(db, QuestionSQLiteHelper.TABLE_NAME)) {
            questionSQLiteHelper.onCreate(db);
        }

        if (!isTableExists(db, OptionsSQLiteHelper.TABLE_NAME)) {
            optionsSQLiteHelper.onCreate(db);
        }
        db.close();
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        String query = "SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = ?";
        Cursor cursor = db.rawQuery(query, new String[]{tableName});
        boolean tableExists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) {
            cursor.close();
        }
        return tableExists;
    }

    private String loadMyJsonFromAsset(@NonNull AssetManager assetManager, String fileName) {
        try {
            InputStream inputStream = assetManager.open(fileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadUserBasicData() {
        if (LoginEmail != null) {

            Query query = userCollRef.whereEqualTo("email", LoginEmail);
            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        int icon = document.getLong("icon").intValue();
                        String name = document.getString("name");
                        if(name==null){
                            name=document.getString("nombre");
                        }
                        setIcon(icon);
                        userName.setText(name);
                    }
                }
            });
        }
    }

    public void setIcon(int icon) {
        switch (icon) {
            case 1:
                this.icon.setImageResource(R.drawable.user_icon2);
                break;
            case 2:
                this.icon.setImageResource(R.drawable.user_icon3);
                break;
            case 3:
                this.icon.setImageResource(R.drawable.user_icon4);
                break;
            default:
                this.icon.setImageResource(R.drawable.user_icon1);
                break;
        }
    }

    public static RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.LogOut:

                deleteUserSesion();

                //Finaliza la pantalla y se vuelve al login.
                Toast toast = Toast.makeText(this, R.string.logoutText, Toast.LENGTH_LONG);
                toast.show();
                remove();
                break;
            case R.id.about:
                Intent intentAbout = new Intent(MainActivity.this, About.class);
                startActivity(intentAbout);
                finish();
                break;
            case R.id.Settings:
                Intent intentSetting = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentSetting);
                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void deleteUserSesion() {
        //Borro la sesión del usuario guardado.
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove("token");
        editor.remove("pswd");
        editor.remove("google");
        editor.remove("level");
        editor.apply();
    }

    public void remove(){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}