package com.example.zodtrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TeamChoose extends AppCompatActivity {
    private Team selectedTeam;
    private FloatingActionButton btnFinish;
    private View selectedItemView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_choose);

        btnFinish = findViewById(R.id.btnFinish);
        btnFinish.setVisibility(View.GONE); // скрыта изначально

        loadTeamsFromPreferences();

        btnFinish.setOnClickListener(v -> {
            if (selectedTeam != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedTeamName", selectedTeam.getName());
                String serialized = serializeTeam(selectedTeam);
                resultIntent.putExtra("selectedTeamData", serialized); // <- Добавили сюда
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    public String serializeTeam(Team team) {
        StringBuilder sb = new StringBuilder();
        List<Team.TeamUnit> units = team.getUnits();

        for (int i = 0; i < units.size(); i++) {
            Team.TeamUnit unit = units.get(i);
            sb.append(unit.getMechID()).append("|").append(unit.getAlias());
            if (i != units.size() - 1) {
                sb.append(";");
            }
        }

        return sb.toString();
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

            itemView.setOnClickListener(v -> {
                if (selectedItemView != null) {
                    selectedItemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }

                // Установить фон для текущего
                selectedItemView = itemView;
                itemView.setBackgroundColor(Color.parseColor("#b0b0b0"));

                selectedTeam = team;
                btnFinish.setVisibility(View.VISIBLE);
            });

            teamList.addView(itemView);
        }
    }
}