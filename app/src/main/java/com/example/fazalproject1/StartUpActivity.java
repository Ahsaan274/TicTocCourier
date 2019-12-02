package com.example.fazalproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import java.util.ArrayList;

public class StartUpActivity extends AppCompatActivity {

    Button member,visiter,aboutus;
    private static int SPLASH_TIME = 4000; //This is 4 seconds

    String Array[] = {"Member","Visiter","About"};
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        tv = findViewById(R.id.PressBtn);
        CircleMenu circleMenu = (CircleMenu) findViewById(R.id.circle);
        circleMenu.setMainMenu(Color.parseColor("#008577"),R.drawable.menu,R.drawable.menu)
                .addSubMenu(Color.parseColor("#008577"),R.drawable.member)
                .addSubMenu(Color.parseColor("#008577"),R.drawable.visitor)
                .addSubMenu(Color.parseColor("#008577"),R.drawable.about)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {

                        Toast.makeText(getApplicationContext(), "You Selected" + Array[index], Toast.LENGTH_SHORT).show();
                        if(index == 0){
                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);
                        }else if(index == 1){
                            Intent intent = new Intent(getApplicationContext(),VisiterSTR.class);
                            startActivity(intent);
                        }else if(index == 2){
                            Intent intent = new Intent(getApplicationContext(),AboutUsActivity.class);
                            startActivity(intent);
                        }

                    }
                });
    }
}
