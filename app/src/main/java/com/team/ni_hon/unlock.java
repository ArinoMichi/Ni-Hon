package com.team.ni_hon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class unlock extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);

        ImageView fondo = findViewById(R.id.fondo);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.unlocked);
        fondo.startAnimation(myanim);

        Glide.with(this)
                .load(R.drawable.unlo)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade(100))
                //.placeholder(new ColorDrawable(this.getResources().getColor(R.color.grey)))
                .into(fondo);


    Handler handler = new Handler();
        handler.postDelayed(new Runnable()
    {
        @Override
        public void run() {
        // Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        Intent intent = new Intent(unlock.this, MainActivity.class);
        startActivity(intent);
    }
    }, 3000);
}
}