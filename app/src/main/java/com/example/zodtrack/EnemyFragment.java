package com.example.zodtrack;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.zodtrack.mechs.MechTemplate;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class EnemyFragment extends Fragment {
    private TextView teamNameTextView;
    private String teamName, teamList;

    private LinearLayout mechListContainer;

    private boolean imported = false;
    private boolean details = true;


    private Team enemyTeam;

    public EnemyFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enemy, container, false);

        teamNameTextView = view.findViewById(R.id.teamName);
        mechListContainer = view.findViewById(R.id.mechListContainer);
        startImportWatcher();

        return view;
    }

    private void startImportWatcher() {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                if (!imported && MainActivity.pendingEnemyTeamName != null && MainActivity.pendingEnemyTeamList != null) {
                    // Применяем данные
                    setTeamName(MainActivity.pendingEnemyTeamName);
                    setTeamList(MainActivity.pendingEnemyTeamList); // если есть такой метод

                    buildTeam();

                    imported = true;

                    displayTeam();

                    // Очищаем статические данные
                    MainActivity.pendingEnemyTeamName = null;
                    MainActivity.pendingEnemyTeamList = null;
                } else if (!imported) {
                    // Повторная проверка через 1 секунду
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(checkRunnable);
    }

    public void setDetail(boolean checked){
        details = checked;
        displayTeam();
    }

    private void displayTeam() {
        mechListContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (Team.TeamUnit unit : enemyTeam.getUnits()) {

            View cardView = inflater.inflate(R.layout.item_enemy_mech, mechListContainer, false);

            TextView mechNameTitle = cardView.findViewById(R.id.mechNameTitle);
            TextView aliasText = cardView.findViewById(R.id.aliasEditText);
            TextView destroyed = cardView.findViewById(R.id.destroyed);
            TextView hp = cardView.findViewById(R.id.currentHP);

            mechNameTitle.setText("Мех: " + unit.getMechID());
            aliasText.setText(unit.getAlias());

            if (unit.getStatus()!=2)
            {
                hp.setText(unit.getMax_hp()-unit.getDmg() +"/"+ unit.getMax_hp());//Установка хп
                destroyed.setVisibility(View.GONE);
            }
            else
            {
                hp.setVisibility(View.GONE);
            }

            if (details)
            {// Заполняем include через MechViewBinder
                MechTemplate mech = MechLibrary.getMechByID(unit.getMechID());
                View included = cardView.findViewById(R.id.included);
                MechViewBinder.bind(included, mech, getContext());
            }
            else
            {
                View included = cardView.findViewById(R.id.included);
                if (included != null) {
                    ((ViewGroup) included.getParent()).removeView(included);
                }
            }

            mechListContainer.addView(cardView);


        }
    }

    public void setTeamName(String name) {
        teamNameTextView.setText(name);
        teamName = name;
    }

    private void setTeamList(String TeamList) {
        teamList = TeamList;
    }

    private void buildTeam() {
        List<Team.TeamUnit> units = new ArrayList<>();

        String[] unitStrings = teamList.split(";");
        for (String unitString : unitStrings) {
            String[] parts = unitString.split("\\|");
            if (parts.length == 2) {
                String mechID = parts[0];
                String alias = parts[1];
                units.add(new Team.TeamUnit(mechID, alias));
            }
        }

        enemyTeam = new Team(teamName,units);
        enemyTeam.assignBattleIDs();

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.setEnemySize(enemyTeam.getUnits().size());  // или другое значение
            activity.checkSizes();
        }
    }

    public void incBattleIDs(int host_max)
    {
        enemyTeam.incBattleIDs(host_max);
    }

    public Team getTeam(){
        return enemyTeam;
    }
}
