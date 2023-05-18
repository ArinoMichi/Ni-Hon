package com.team.ni_hon.model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.team.ni_hon.R;
import com.team.ni_hon.databinding.ActivityUserInfoBinding;

public class UserInfoActivity extends AppCompatActivity {

    private final String TAG="UserInfoActivity";
    private TextView userName;
    private String email;
    private TextView Level;
    private ImageView Icon;
    FirebaseFirestore myDB;
    CollectionReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUserInfoBinding bind=ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(bind.getRoot());

        userName=bind.UserName;
        Level=bind.level;
        Icon=bind.UserIcon;

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        email=sharedPreferences.getString("token",null);

        myDB= FirebaseFirestore.getInstance();
        usersRef= myDB.collection("users");

        if(email!=null){
            initComponent();
        }
    }

    public void initComponent(){
        Query query = usersRef.whereEqualTo("email", email);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        String userName = document.getString("name");
                        int icon = document.getLong("icon").intValue();
                        int level = document.getLong("level").intValue();
                        showUserData(userName,icon,level);
                    } else {
                        Exception exception = task.getException();
                        if (exception != null) {
                            Log.e("Firestore", "Error: " + exception.getMessage());
                        }
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception != null) {
                        Log.e("Firestore", "Error: " + exception.getMessage());
                    }
                }
            }
        });
    }

    public void showUserData(String name,int icon,int level){
        userName.setText(name);
        Level.setText(String.valueOf(level));
        if(icon!=0){
            Icon.setImageResource(R.drawable.sakura);
        }else{
            Icon.setImageResource(R.drawable.user_icon_default);
        }
    }
}