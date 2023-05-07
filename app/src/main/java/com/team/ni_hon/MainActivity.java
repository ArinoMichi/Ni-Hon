package com.team.ni_hon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.team.ni_hon.recycler.Lesson;
import com.team.ni_hon.recycler.LessonAdapter;
import com.team.ni_hon.recycler.MyLinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class MainActivity extends AppCompatActivity {

    private final String TAG="MainActivity";

    private static final int RC_SIGN_IN = 123;

    public static ArrayList<Lesson> lessons = new ArrayList<Lesson>(){
        {
            add(new Lesson(1, "Hiragana", "Let's learn hiragana!"));
            add(new Lesson(2, "Katakana", "Let's learn katakana!"));
            add(new Lesson(3, "Lesson 3", "3 is bigger than 2"));
            add(new Lesson(4, "Lesson 4", "Sample text"));
            add(new Lesson(5, "Lesson 5", "I don't know what to write"));
        }
    };

    private LessonAdapter lessonAdapter;

    private static RecyclerView recyclerView;
    // private static ConstraintLayout popup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(null);

        recyclerView = findViewById(R.id.recycler);

        lessonAdapter = new LessonAdapter(this, lessons);
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