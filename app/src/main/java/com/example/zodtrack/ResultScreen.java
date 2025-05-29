package com.example.zodtrack;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultScreen extends AppCompatActivity {

    TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        result = findViewById(R.id.result);

        int res = 0;

        Intent intent = getIntent();
        res = (int) intent.getIntExtra("game_result",0);

        switch (res)
        {
            case 1://Ничья
                result.setText("Ничья");
                result.setTextColor(Color.parseColor("#FFC107"));
                break;
            case 2://Проигрыш
                result.setText("Вы проиграли!");
                result.setTextColor(Color.parseColor("#F44336"));
                break;
            case 3://Победа
                result.setText("Вы победили!");
                result.setTextColor(Color.parseColor("#4CAF50"));
                break;
        }

        setResult(RESULT_OK); // Сообщаем, что пора закрывать MainActivity
    }
}