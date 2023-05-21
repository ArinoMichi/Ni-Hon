package com.team.ni_hon.model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.team.ni_hon.MainActivity;
import com.team.ni_hon.NiHonActivity;
import com.team.ni_hon.R;
import com.team.ni_hon.databinding.ActivityUserInfoBinding;

public class UserInfoActivity extends NiHonActivity {

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
        showProgressDialog(R.string.load);

        Query query = usersRef.whereEqualTo("email", email);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                        String userName = document.getString("name");
                        String GoogleUserName= document.getString("nombre");
                        int icon = document.getLong("icon").intValue();
                        int level = document.getLong("level").intValue();
                        showUserData(userName,GoogleUserName,icon,level);
                    } else {
                        cancelProgressDialog();
                        Exception exception = task.getException();
                        if (exception != null) {
                            Log.e("Firestore", "Error: " + exception.getMessage());
                        }
                        showErrorMenssage(R.string.dialogErrorText,R.string.dialogErrorTitle);
                    }
                } else {
                    cancelProgressDialog();
                    Exception exception = task.getException();
                    if (exception != null) {
                        Log.e("Firestore", "Error: " + exception.getMessage());
                    }
                    showErrorMenssage(R.string.dialogErrorText,R.string.dialogErrorTitle);
                }
            }
        });

        Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });
    }

    public void showUserData(String name,String GoogleName,int icon,int level){
        cancelProgressDialog();
        if(name==null)
            userName.setText(GoogleName);
        else if(GoogleName==null)
            userName.setText(name);

        Level.setText(String.format(getResources().getString(R.string.levelIndicator),level));

        switch (icon){
            case R.drawable.moon_icon:
                Icon.setImageResource(R.drawable.moon_icon);
                break;
            case R.drawable.japan_icon:
                Icon.setImageResource(R.drawable.japan_icon);
                break;
            case R.drawable.yukata_icon:
                Icon.setImageResource(R.drawable.yukata_icon);
                break;
            default:
                Icon.setImageResource(R.drawable.user_icon_default);
                break;
        }
    }

    public void showMenu(){
        PopupMenu popupMenu = new PopupMenu(this, Icon, Gravity.END, 0, R.style.PopupMenuStyle);
        popupMenu.inflate(R.menu.icon_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.icon_default:
                    Icon.setImageResource(R.drawable.user_icon_default);
                    break;
                case R.id.icon_moon:
                    Icon.setImageResource(R.drawable.moon_icon);
                    break;
                case R.id.icon_japan:
                    Icon.setImageResource(R.drawable.japan_icon);
                    break;
                case R.id.icon_yukata:
                    Icon.setImageResource(R.drawable.yukata_icon);
                    break;
            }
            return false;
        });

        popupMenu.show();
    }

    @Override
    public void onBackPressed(){
        Intent intent=new Intent(UserInfoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}