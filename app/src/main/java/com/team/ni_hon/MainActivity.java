package com.team.ni_hon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.team.ni_hon.main_recycler.MainLesson;
import com.team.ni_hon.main_recycler.MainLessonAdapter;
import com.team.ni_hon.main_recycler.MyLinearLayoutManager;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private final String TAG="MainActivity";

    private static final int RC_SIGN_IN = 123;

    public static ArrayList<MainLesson> lessons = new ArrayList<MainLesson>(){
        {
            add(new MainLesson(1, "Hiragana", "Let's learn hiragana!"));
            add(new MainLesson(2, "Katakana", "Let's learn katakana!"));
            add(new MainLesson(3, "Lesson 3", "3 is bigger than 2"));
            add(new MainLesson(4, "Lesson 4", "Sample text"));
            add(new MainLesson(5, "Lesson 5", "I don't know what to write"));
        }
    };

    private MainLessonAdapter lessonAdapter;

    private static RecyclerView recyclerView;

    // private static ConstraintLayout popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(null);

        recyclerView = findViewById(R.id.recycler);

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
        if (id == R.id.LogOut) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            Toast toast = Toast.makeText(this, "Cerrando sesion...", Toast.LENGTH_LONG);
            toast.show();
        }
        if (id == R.id.about) {
            //Intent intent = new Intent(this, AboutActivity.class);
            //startActivity(intent);
        }
        if (id == R.id.Settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}