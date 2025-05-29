package com.example.zodtrack;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Remote extends AppCompatActivity {
    TextView mechNameTitle, aliasText, hp, actionPnt, wepLused, wepRused;
    Button wepL, wepR, move, turn, endTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_remote);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialWork();
        exqListeners();
    }

    private void initialWork() {
        mechNameTitle = findViewById(R.id.mechNameTitle);
        aliasText = findViewById(R.id.aliasText);
        hp = findViewById(R.id.hp);
        actionPnt = findViewById(R.id.actionPoints);
        wepLused = findViewById(R.id.wepLused);
        wepRused = findViewById(R.id.wepRused);
        wepL = findViewById(R.id.wepL);
        wepR = findViewById(R.id.wepR);
        move = findViewById(R.id.move);
        turn = findViewById(R.id.turn);
        endTurn = findViewById(R.id.endTurn);



    }

    private void exqListeners() {

    }
}