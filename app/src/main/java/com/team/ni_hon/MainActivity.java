package com.team.ni_hon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.team.ni_hon.databinding.ActivityMainBinding;
import com.team.ni_hon.main_recycler.Lesson;
import com.team.ni_hon.main_recycler.MainLessonAdapter;
import com.team.ni_hon.main_recycler.MyLinearLayoutManager;
import com.team.ni_hon.model.UserInfoActivity;
import com.team.ni_hon.utils.LanguageHelper;

import java.util.ArrayList;


public class MainActivity extends NiHonActivity {

    private final String TAG="MainActivity";

    private static final int RC_SIGN_IN = 123;

    public static ArrayList<Lesson> lessons = new ArrayList<Lesson>(){
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
        ActivityMainBinding bind=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());
        setTitle(null);

        LanguageHelper.setLocale(this, LanguageHelper.getLanguage(this));

        recyclerView = bind.recycler;

        lessonAdapter = new MainLessonAdapter(this, lessons);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new MyLinearLayoutManager(this, MyLinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(lessonAdapter);
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

        switch(id) {
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
            case R.id.user:
                Intent intentUser =new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intentUser);
                finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }


}