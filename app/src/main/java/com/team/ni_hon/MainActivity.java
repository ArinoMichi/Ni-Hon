package com.team.ni_hon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.team.ni_hon.databinding.ActivityMainBinding;
import com.team.ni_hon.main_recycler.Lesson;
import com.team.ni_hon.main_recycler.MainLessonAdapter;
import com.team.ni_hon.main_recycler.MyLinearLayoutManager;
import com.team.ni_hon.model.UserInfoActivity;
import com.team.ni_hon.utils.LanguageHelper;

import java.util.ArrayList;


public class MainActivity extends NiHonActivity {

    private final String TAG = "MainActivity";

    private static final int RC_SIGN_IN = 123;
    private ImageView icon;
    private TextView userEmail;
    ImageView backgroundImage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore userDataBase;
    private CollectionReference userCollRef;
    private Query query;

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

        recyclerView = bind.recycler;
        icon = bind.userImg;
        userEmail = bind.email;
        backgroundImage = bind.scrollImage;

        getUserLevel(level -> {
            if (level != -1) {
                lessonAdapter = new MainLessonAdapter(this, lessons, level);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new MyLinearLayoutManager(this, MyLinearLayoutManager.HORIZONTAL, false));
                recyclerView.setAdapter(lessonAdapter);
            } else {
                showErrorMenssage(R.string.dialogErrorMenssage, 0);
            }
        });

        setScrollableImage();

        initComponent();
    }

    public void initComponent() {
        loadUserBasicData();

        icon.setOnClickListener(v -> {
            Log.d(TAG, "Se ha clickado el icono");
            Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void setScrollableImage(){
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

    public interface OnUserLevelListener {
        void onUserLevelObtained(int level);
    }

    public void getUserLevel(OnUserLevelListener listener) {
        userDataBase = FirebaseFirestore.getInstance();
        userCollRef = userDataBase.collection("users");
        mAuth = FirebaseAuth.getInstance();
        query = userCollRef.whereEqualTo("email", getUserEmail());
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

        //loadUserIcon();
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
                //Borro la sesión del usuario guardado.
                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.remove("token");
                editor.remove("pswd");
                editor.remove("google");
                editor.apply();

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


}