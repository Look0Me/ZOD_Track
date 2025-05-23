package com.example.zodtrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class TeamsOverview extends AppCompatActivity {
    FloatingActionButton newTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teams_overview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        initialWork();
        exqListener();
    }

    private void initialWork() {
        newTeam=findViewById(R.id.newTeam);

    }

    private void exqListener() {
        newTeam.setOnClickListener(v -> {
            Team team = new Team(""); // пока без имени
            Intent intent = new Intent(TeamsOverview.this, TeamCreate.class);
            intent.putExtra("team", team);
            startActivity(intent);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTeamsFromPreferences();
    }

    private void loadTeamsFromPreferences() {
        SharedPreferences prefs = getSharedPreferences("teams_storage", MODE_PRIVATE);
        Gson gson = new Gson();

        String ordered = prefs.getString("ordered_team_names", "[]");
        List<String> orderedNames = gson.fromJson(ordered, new TypeToken<List<String>>() {}.getType());

        LinearLayout teamList = findViewById(R.id.teamListContainer);
        teamList.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(this);
        Type teamType = new TypeToken<Team>() {}.getType();

        for (String teamNameStr : orderedNames) {
            String json = prefs.getString(teamNameStr, null);
            if (json == null) continue;

            Team team = gson.fromJson(json, teamType);

            View itemView = inflater.inflate(R.layout.item_team, teamList, false);
            TextView teamName = itemView.findViewById(R.id.teamName);
            TextView teamCnt = itemView.findViewById(R.id.teamCnt);

            teamName.setText(team.getName());
            teamCnt.setText("x" + team.getUnits().size());

            // Переход при клике
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(TeamsOverview.this, TeamEdit.class); // или TeamEdit.class
                intent.putExtra("team", team);
                startActivity(intent);
            });

            teamList.addView(itemView);
        }
    }

}