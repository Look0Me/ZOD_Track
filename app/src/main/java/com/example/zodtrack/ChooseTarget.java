package com.example.zodtrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zodtrack.mechs.MechTemplate;

import java.util.List;

public class ChooseTarget extends AppCompatActivity {


    LinearLayout yourListContainer;
    LinearLayout enemyListContainer;
    CheckBox detailCheckbox;

    List<Team.TeamUnit> enemyUnits, yourUnits;
    int thisID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_target);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        enemyUnits = (List<Team.TeamUnit>) intent.getSerializableExtra("enemyUnits");
        yourUnits = (List<Team.TeamUnit>) intent.getSerializableExtra("yourUnits");
        thisID = (int) intent.getSerializableExtra("thisUnit");

        initialWork();
        exqListeners();




        updateUI(((CheckBox) findViewById(R.id.detail)).isChecked());

    }

    private void initialWork() {
        yourListContainer = findViewById(R.id.yourListContainer);
        enemyListContainer = findViewById(R.id.enemyListContainer);
        detailCheckbox = findViewById(R.id.detail);
    }

    private void exqListeners() {
        detailCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> updateUI(isChecked));


    }

    private void updateUI(boolean isChecked) {
        displayTeamCards(yourUnits, yourListContainer, isChecked);
        displayTeamCards(enemyUnits, enemyListContainer, isChecked);
    }

    private void displayTeamCards(List<Team.TeamUnit> units, LinearLayout container, boolean ShowDetails) {
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Team.TeamUnit unit : units) {
            if (unit.getBattleID() == thisID){continue;}
            if (unit.getStatus() == 2){continue;}

            View cardView = inflater.inflate(R.layout.item_target_mech, container, false);

            TextView mechNameTitle = cardView.findViewById(R.id.mechNameTitle);
            TextView aliasText = cardView.findViewById(R.id.aliasEditText);
            TextView hp = cardView.findViewById(R.id.currentHP);
            Button btnAtk = cardView.findViewById(R.id.btnAtk);

            btnAtk.setOnClickListener(v -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("targetBattleID", unit.getBattleID());
                resultIntent.putExtra("atkBattleID", thisID);
                setResult(RESULT_OK, resultIntent);
                finish(); // Закрываем активити
            });

            mechNameTitle.setText("Мех: " + unit.getMechID());
            aliasText.setText(unit.getAlias());

            if (unit.getStatus() != 2) {
                hp.setText((unit.getMax_hp() - unit.getDmg()) + "/" + unit.getMax_hp());
            }


            if (ShowDetails) {
                MechTemplate mech = MechLibrary.getMechByID(unit.getMechID());
                View included = cardView.findViewById(R.id.included);
                MechViewBinder.bind(included, mech, this);
            } else {
                View included = cardView.findViewById(R.id.included);
                if (included != null) {
                    ((ViewGroup) included.getParent()).removeView(included);
                }
            }

            container.addView(cardView);
        }
    }





}