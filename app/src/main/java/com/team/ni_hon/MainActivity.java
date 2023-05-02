package com.team.ni_hon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.team.ni_hon.recycler.Lesson;
import com.team.ni_hon.recycler.LessonAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Lesson> lessons;
    private LessonAdapter lessonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(null);

        RecyclerView recyclerView = findViewById(R.id.recycler);

        // move this code to database
        lessons = new ArrayList<>();
        for(int i=0; i<5; i++){
            lessons.add(new Lesson(i+1, Integer.toString(i+1)));
        }

        lessonAdapter = new LessonAdapter(lessons);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(lessonAdapter);

        lessonAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pulsado", Toast.LENGTH_LONG).show();
            }
        });
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