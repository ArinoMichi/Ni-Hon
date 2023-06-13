package com.team.ni_hon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.team.ni_hon.databinding.ActivityUnlockBinding;

public class unlock extends NiHonActivity {

    private ImageView fondo,tanuki;
    private Animation myanim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUnlockBinding bind=ActivityUnlockBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        fondo = bind.fondo;
        tanuki=bind.tanukiunlocked;

        SharedPreferences prefs = getSharedPreferences("PRACTICE", MODE_PRIVATE);
        int type = prefs.getInt("accessLevel", 1);

        myanim= AnimationUtils.loadAnimation(this,R.anim.unlocked);
        fondo.startAnimation(myanim);

        Glide.with(this)
                .load(R.drawable.unlo)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(100))
                //.placeholder(new ColorDrawable(this.getResources().getColor(R.color.grey)))
                .into(fondo);

        switch (type){
            case 2:
                tanuki.setImageResource(R.drawable.tanukiteen);
                break;
            case 4:
                tanuki.setImageResource(R.drawable.tanukiadult);
                break;
        }

    Handler handler = new Handler();
        handler.postDelayed(new Runnable()
    {
        @Override
        public void run() {
        // Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        Intent intent = new Intent(unlock.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    }, 3000);
}
}