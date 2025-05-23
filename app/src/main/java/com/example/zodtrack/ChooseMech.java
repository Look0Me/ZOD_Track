package com.example.zodtrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zodtrack.mechs.MechTemplate;

import java.util.ArrayList;
import java.util.List;

public class ChooseMech extends AppCompatActivity {

    RecyclerView recyclerView;
    List<MechTemplate> mechList = MechLibrary.loadMechs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mech);

        recyclerView = findViewById(R.id.chooseMechContainer);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ChooseMechAdapter adapter = new ChooseMechAdapter(this, mechList, mech -> {
            // например, возвращаем выбранного меха обратно в TeamCreate
            Intent resultIntent = new Intent();
            resultIntent.putExtra("mechName", mech.name);
            setResult(RESULT_OK, resultIntent);
            finish();
        });



        recyclerView.setAdapter(adapter);
    }
}

