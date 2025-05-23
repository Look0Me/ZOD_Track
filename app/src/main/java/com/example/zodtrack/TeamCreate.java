package com.example.zodtrack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zodtrack.mechs.MechTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TeamCreate extends AppCompatActivity {
    FloatingActionButton btnAddUnit, btnFinish;
    EditText editTeamName;
    Team team;
    LinearLayout mechListContainer;
    CheckBox detailCheckbox;
    private static final int REQUEST_CHOOSE_MECH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_team_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        initialWork();
        exqListener();
        updateUI(((CheckBox) findViewById(R.id.detail)).isChecked());

    }

    private void initialWork() {
        btnAddUnit=findViewById(R.id.btnAddUnit);
        btnFinish=findViewById(R.id.btnFinish);
        editTeamName=findViewById(R.id.editTeamName);
        mechListContainer = findViewById(R.id.mechListContainer);
        detailCheckbox = findViewById(R.id.detail);

        // Подсчёт сохранённых команд
        SharedPreferences prefs = getSharedPreferences("teams_storage", MODE_PRIVATE);
        int n = prefs.getAll().size();

        // Установка имени по умолчанию
        editTeamName.setText("Отряд-" + (n));

        team = (Team) getIntent().getSerializableExtra("team");

    }

    private void exqListener() {


        btnAddUnit.setOnClickListener(v -> {
            Intent intent = new Intent(TeamCreate.this, ChooseMech.class);
            startActivityForResult(intent, REQUEST_CHOOSE_MECH);
        });


        btnFinish.setOnClickListener(v -> finishAndSave());

        detailCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> updateUI(isChecked));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_MECH && resultCode == RESULT_OK) {
            if (data != null) {
                String mechName = data.getStringExtra("mechName");
                Log.d("TeamCreate", "Получен мех: " + mechName);

                // найти мех по имени
                MechTemplate selectedMech = null;
                for (MechTemplate mech : MechLibrary.loadMechs()) {
                    if (mech.name.equals(mechName)) {
                        selectedMech = mech;
                        break;
                    }
                }

                if (selectedMech != null) {
                    // Генерация alias
                    String baseAlias = selectedMech.name;
                    int count = 1;
                    for (Team.TeamUnit unit : team.getUnits()) {
                        if (unit.getMechID().equals(selectedMech.name)) {
                            count++;
                        }
                    }
                    String alias = baseAlias + "-" + count;

                    team.addUnit(selectedMech.name, alias); // добавляем меха с псевдонимом
                    Log.d("TeamCreate", "Добавлен в отряд: " + selectedMech.name + " с псевдонимом " + alias);
                    updateUI(((CheckBox) findViewById(R.id.detail)).isChecked());

                }
            }
        }

    }

    private void updateUI(boolean showDetails) {
        mechListContainer.removeAllViews();

        for (Team.TeamUnit unit : team.getUnits()) {
            View cardView = getLayoutInflater().inflate(R.layout.unit_card, mechListContainer, false);

            TextView mechNameTitle = cardView.findViewById(R.id.mechNameTitle);
            EditText aliasEditText = cardView.findViewById(R.id.aliasEditText);
            Button btnDel = cardView.findViewById(R.id.btnDel);
            btnDel.setOnClickListener(v -> {
                // Удалить этот юнит
                team.getUnits().remove(unit);
                updateUI(((CheckBox) findViewById(R.id.detail)).isChecked()); // обновить UI после удаления
            });

            mechNameTitle.setText("Мех: " + unit.getMechID());
            aliasEditText.setText(unit.getAlias());

            aliasEditText.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    unit.setAlias(s.toString());
                }
                @Override public void afterTextChanged(Editable s) {}
            });




            if (showDetails) {
                MechTemplate mech = MechLibrary.getMechByID(unit.getMechID());
                View itemUnitView = cardView.findViewById(R.id.included);
                MechViewBinder.bind(itemUnitView, mech, this);
            } else {
                // Удаляем include, если не нужно показывать
                View included = cardView.findViewById(R.id.included);
                if (included != null) {
                    ((ViewGroup) included.getParent()).removeView(included);
                }
            }

            mechListContainer.addView(cardView);
        }
    }


    private void finishAndSave() {
        String teamName = editTeamName.getText().toString().trim();

        if (teamName.isEmpty()) return;

        SharedPreferences prefs = getSharedPreferences("teams_storage", MODE_PRIVATE);
        if (prefs.contains(teamName)) {
            Toast.makeText(this, "Команда с таким названием уже существует", Toast.LENGTH_SHORT).show();
            return;
        }

        team = new Team(teamName, team.getUnits());

        Gson gson = new Gson();
        String json = gson.toJson(team);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(teamName, json);

        // Получаем текущий список имён и добавляем новое имя
        String ordered = prefs.getString("ordered_team_names", "[]");
        List<String> orderedNames = gson.fromJson(ordered, new TypeToken<List<String>>() {}.getType());
        orderedNames.add(teamName);
        editor.putString("ordered_team_names", gson.toJson(orderedNames));

        editor.apply();
        finish();
    }



}