package com.team.ni_hon;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
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
    private LocalUser localUser;
    private ImageView icon;
    private TextView userEmail;
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
            add(new Lesson(1, "Hiragana", "Let's learn hiragana!", 3));
            add(new Lesson(2, "Katakana", "Let's learn katakana!", 1));
            add(new Lesson(3, "Lesson 3", "3 is bigger than 2", 1));
            add(new Lesson(4, "Lesson 4", "Sample text", 1));
            add(new Lesson(5, "Lesson 5", "I don't know what to write", 1));
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

        //Se usa para borrar la base de datos.
        //getApplicationContext().deleteDatabase(UserSQLiteHelper.DATABASE_NAME);

        recyclerView = bind.recycler;
        icon = bind.userImg;
        userEmail = bind.email;
        backgroundImage = bind.scrollImage;

        userDataBase = FirebaseFirestore.getInstance();
        userCollRef = userDataBase.collection("users");
        mAuth = FirebaseAuth.getInstance();

        getUserLevel(level -> {
            if (level != -1) {
                saveLevel(level);
                lessonAdapter = new MainLessonAdapter(this, lessons, level);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new MyLinearLayoutManager(this, MyLinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(lessonAdapter);
            } else {
                showErrorMenssage(R.string.dialogErrorMenssage, 0);
            }
        });

        assetManager = getAssets();
        localUser = new LocalUser();

        userSQLiteHelper = new UserSQLiteHelper(this);
        questionSQLiteHelper = new QuestionSQLiteHelper(this);
        optionsSQLiteHelper = new OptionsSQLiteHelper(this);


        setScrollableImage();

        initComponent();
    }

    public void initComponent() {
        loadUserBasicData();

        setUserQuestions();

        icon.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void saveLevel(int level){
        SharedPreferences pref=getSharedPreferences("UserSession",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("level",level);
        editor.apply();
    }

    public void setScrollableImage() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Obtener el desplazamiento horizontal actual del RecyclerView
                int horizontalScrollOffset = recyclerView.computeHorizontalScrollOffset();

                // Actualizar el desplazamiento de la imagen
                backgroundImage.setTranslationX(-horizontalScrollOffset);
            }
        });
    }

    public void setUserQuestions() {
        String questionsData = loadMyJsonFromAsset(assetManager, FILE);
        if (questionsData != null && !questionsData.isEmpty())
            localUser = MyJsonParser.parseJsonToObjects(questionsData);

        if (localUser != null) {
            localUser.setEmail(getUserEmail());

            if (userSQLiteHelper.VerifyIfEmailExist(getUserEmail())) {
                Log.d(TAG, "El usuario ya tiene las preguntas");
            }else {
                //Cargo las preguntas para el usuario.
                SQLiteDatabase db=userSQLiteHelper.getWritableDatabase();
                questionSQLiteHelper.onCreate(db);
                optionsSQLiteHelper.onCreate(db);

                userSQLiteHelper.addUser(getUserEmail());

                for(Question q:localUser.getQuestions()){
                    q.setEmail(getUserEmail());
                    questionSQLiteHelper.addQuestion(q);
                    for(Option o:q.getOptions()){
                        optionsSQLiteHelper.addOption(o);
                    }
                }
                db.close();
            }
        }
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

    public interface OnUserLevelListener {
        void onUserLevelObtained(int level);
    }

    public void getUserLevel(OnUserLevelListener listener) {
        Query query = userCollRef.whereEqualTo("email", getUserEmail());
        query.get().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful() && !task1.getResult().isEmpty()) {
                for (QueryDocumentSnapshot document : task1.getResult()) {
                    int level = document.getLong("level").intValue();
                    listener.onUserLevelObtained(level);
                    return;
                }
            }
            listener.onUserLevelObtained(-1);
        });
    }

    public void loadUserBasicData() {
        if (getUserEmail() != null)
            userEmail.setText(getUserEmail());

        Query query = userCollRef.whereEqualTo("email", getUserEmail());
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    int icon = document.getLong("icon").intValue();
                    setIcon(icon);
                } else {
                    Toast.makeText(this, "No se ha obtenido el Icono.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "No se ha obtenido el Icono.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setIcon(int icon) {
        switch (icon) {
            case 1:
                this.icon.setImageResource(R.drawable.moon_icon);
                break;
            case 2:
                this.icon.setImageResource(R.drawable.japan_icon);
                break;
            case 3:
                this.icon.setImageResource(R.drawable.yukata_icon);
                break;
            default:
                this.icon.setImageResource(R.drawable.user_icon_default);
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
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast toast = Toast.makeText(this, R.string.logoutText, Toast.LENGTH_LONG);
                toast.show();
                finish();
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

    public void deleteUserSesion(){
        //Borro la sesión del usuario guardado.
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove("token");
        editor.remove("pswd");
        editor.remove("google");
        editor.remove("level");
        editor.apply();
    }
}