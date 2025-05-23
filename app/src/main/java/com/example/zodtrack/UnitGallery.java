package com.example.zodtrack;

import android.os.Bundle;

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

public class UnitGallery extends AppCompatActivity {

    RecyclerView recyclerView;
    List<MechTemplate> mechList = MechLibrary.loadMechs();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_gallery);

        recyclerView = findViewById(R.id.recyclerViewMechs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new MechAdapter(this, mechList));
    }
}
