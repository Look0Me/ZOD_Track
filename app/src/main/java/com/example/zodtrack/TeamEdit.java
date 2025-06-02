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

import java.lang.reflect.Type;
import java.util.List;

public class TeamEdit extends AppCompatActivity {
    FloatingActionButton btnAddUnit, btnFinish;
    EditText editTeamName;
    Button btnDel;
    Team team;
    LinearLayout mechListContainer;
    CheckBox detailCheckbox;
    String oldName;
    private static final int REQUEST_CHOOSE_MECH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_team_edit);
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
        btnDel=findViewById(R.id.btnDel);
        editTeamName=findViewById(R.id.editTeamName);
        mechListContainer = findViewById(R.id.mechListContainer);
        detailCheckbox = findViewById(R.id.detail);

        // Подсчёт сохранённых команд
        SharedPreferences prefs = getSharedPreferences("teams_storage", MODE_PRIVATE);
        int n = prefs.getAll().size();

        team = (Team) getIntent().getSerializableExtra("team");
        oldName = team.getName();
        editTeamName.setText(oldName);

    }

    private void exqListener() {


        btnAddUnit.setOnClickListener(v -> {
            Intent intent = new Intent(TeamEdit.this, ChooseMech.class);
            startActivityForResult(intent, REQUEST_CHOOSE_MECH);
        });


        btnFinish.setOnClickListener(v -> {
            team.setName(editTeamName.getText().toString().trim());
            finishAndSave();
        });

        detailCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> updateUI(isChecked));

        btnDel.setOnClickListener(v -> {
            if (team == null || team.getName() == null || team.getName().isEmpty()) {
                finish(); // если имя не задано — просто закрываем
                return;
            }

            SharedPreferences prefs = getSharedPreferences("teams_storage", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            // Удаление самой команды
            editor.remove(team.getName());

            // Удаление имени из списка
            String ordered = prefs.getString("ordered_team_names", "[]");
            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>() {}.getType();
            List<String> orderedNames = gson.fromJson(ordered, listType);

            orderedNames.remove(team.getName());
            editor.putString("ordered_team_names", gson.toJson(orderedNames));

            editor.apply();

            // Возврат на экран списка
            finish();
        });
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
        SharedPreferences prefs = getSharedPreferences("teams_storage", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();

        // Получаем новое имя от пользователя
        String newName = team.getName(); // предполагаем, что team.name обновлён пользователем в интерфейсе
        team.assignBattleIDs();

        // Загружаем текущий список имён
        String orderedJson = prefs.getString("ordered_team_names", "[]");
        Type listType = new TypeToken<List<String>>() {}.getType();
        List<String> orderedNames = gson.fromJson(orderedJson, listType);

        // Если имя изменилось — удалить старую запись
        if (!newName.equals(oldName)) {
            editor.remove(oldName);
            int index = orderedNames.indexOf(oldName);
            if (index != -1) {
                orderedNames.set(index, newName); // заменяем имя в списке
            }
        }

        // Сохраняем обновлённую команду
        String json = gson.toJson(team);
        editor.putString(newName, json);
        editor.putString("ordered_team_names", gson.toJson(orderedNames));
        editor.apply();

        finish(); // закрываем активити
    }




}