package com.team.ni_hon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.team.ni_hon.databinding.ActivityAboutBinding;

public class About extends NiHonActivity {

    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding bind=ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        version=bind.version;

        version.setText(getAppVersion());
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(About.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}