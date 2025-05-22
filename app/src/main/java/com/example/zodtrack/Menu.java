package com.example.zodtrack;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Menu extends AppCompatActivity {
    Button btnConnect, btnTeams, btnNewTeam, btnUnits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialWork();
        exqListener();
    }

    private void initialWork() {
        btnConnect=findViewById(R.id.btnConnect);
        btnTeams=findViewById(R.id.btnTeams);
        btnNewTeam=findViewById(R.id.btnNewTeam);
        btnUnits=findViewById(R.id.btnUnits);
    }

    private void exqListener() {

    }


}