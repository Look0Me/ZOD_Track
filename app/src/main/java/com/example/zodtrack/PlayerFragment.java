package com.example.zodtrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zodtrack.mechs.MechTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PlayerFragment extends Fragment {
    private TextView teamNameTextView;
    private String teamName;
    private LinearLayout mechListContainer;
    private Team currentTeam;
    List<Team.TeamUnit> enemyUnits;

    private boolean details = true;
    private boolean yourTurn = false;
    private boolean enemyOut = false;

    private static final int REQUEST_TARGET = 1234;

    public PlayerFragment() {}

    public void setTeamName(String teamName) {
        this.teamName = teamName;
        if (teamNameTextView != null) {
            teamNameTextView.setText(teamName);
        }

        if (mechListContainer != null) {
            loadAndDisplayTeam(teamName);
        }
    }

    private void loadAndDisplayTeam(String teamName) {
        mechListContainer.removeAllViews(); // Очистка

        // Загружаем команду
        SharedPreferences prefs = requireActivity().getSharedPreferences("teams_storage", Context.MODE_PRIVATE);
        String json = prefs.getString(teamName, null);
        if (json == null) return;

        Gson gson = new Gson();
        Type teamType = new TypeToken<Team>() {}.getType();
        currentTeam = gson.fromJson(json, teamType);
        currentTeam.assignBattleIDs();

        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            activity.setYouSize(currentTeam.getUnits().size());  // или другое значение
            activity.checkSizes();
        }
        displayTeam();
    }

    public void setDetail(boolean checked){
        details = checked;
        displayTeam();
    }


    private void displayTeam() {
        mechListContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (Team.TeamUnit unit : currentTeam.getUnits()) {

            View cardView = inflater.inflate(R.layout.item_your_mech, mechListContainer, false);

            TextView mechNameTitle = cardView.findViewById(R.id.mechNameTitle);
            TextView aliasText = cardView.findViewById(R.id.aliasEditText);
            TextView activated = cardView.findViewById(R.id.activated);
            TextView destroyed = cardView.findViewById(R.id.destroyed);
            TextView hp = cardView.findViewById(R.id.currentHP);
            Button btnActivate = cardView.findViewById(R.id.btnDel); // Используем существующую кнопку

//            if (unit.getDmg()>= unit.getMax_hp())//ВРЕМЕННАЯ РЕАЛИЗАЦИЯ
//            {
//                unit.setStatus(2);
//            }


            mechNameTitle.setText("Мех: " + unit.getMechID());
            aliasText.setText(unit.getAlias());
            if (unit.getStatus()!=2)
            {
                hp.setText(unit.getMax_hp()-unit.getDmg() +"/"+ unit.getMax_hp());//Установка хп
            }

            switch (unit.getStatus())
            {
                case 0://Мех ещё не активирован
                    activated.setVisibility(View.GONE);
                    destroyed.setVisibility(View.GONE);
                    // Назначаем действие кнопки

                    if (yourTurn)
                    {
                        btnActivate.setOnClickListener(v -> {
                            Toast.makeText(getContext(), "Мех " + unit.getAlias() + " активирован", Toast.LENGTH_SHORT).show();
                            unit.setStatus(1);

                            MainActivity activity = (MainActivity) requireActivity();
                            enemyUnits = activity.getEnemyList();
                            List<Team.TeamUnit> yourUnits = currentTeam.getUnits();

                            Intent intent = new Intent(getContext(), ChooseTarget.class);
                            intent.putExtra("enemyUnits", new ArrayList<>(enemyUnits));
                            intent.putExtra("yourUnits", new ArrayList<>(yourUnits));
                            intent.putExtra("thisUnit", unit.getBattleID());
                            startActivityForResult(intent, REQUEST_TARGET);

                        });
                    }
                    else {btnActivate.setVisibility(View.GONE);}
                    break;
                case 1: //Мех активирован
                    btnActivate.setVisibility(View.GONE);
                    destroyed.setVisibility(View.GONE);
                    break;
                case 2: //Мех уничтожен
                    btnActivate.setVisibility(View.GONE);
                    activated.setVisibility(View.GONE);
                    hp.setVisibility(View.GONE);
                    break;

            }

            if (details)
            {// Заполняем include через MechViewBinder
                MechTemplate mech = MechLibrary.getMechByID(unit.getMechID());
                View included = cardView.findViewById(R.id.included);
                MechViewBinder.bind(included, mech, getContext(), unit.getDmg());
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        teamNameTextView = view.findViewById(R.id.teamName);
        mechListContainer = view.findViewById(R.id.mechListContainer);

        return view;
    }

    public void incBattleIDs(int host_max)
    {
        currentTeam.incBattleIDs(host_max);
    }

    public int calcDmg(String attacker, int side, String target)
    {
        int dmg = 0;
        int wep_dmg = 0;
        int wep_rng = 0;
        int wep_type = 0;
        int[] dice;

        if (side == 0)//правое оружие
        {
            wep_dmg = MechLibrary.getMechByID(attacker).right_wep_dmg;
            wep_rng = MechLibrary.getMechByID(attacker).right_wep_rng;
            wep_type = MechLibrary.getMechByID(attacker).right_wep_type;
        }
        else //Левое оружие
        {
            wep_dmg = MechLibrary.getMechByID(attacker).left_wep_dmg;
            wep_rng = MechLibrary.getMechByID(attacker).left_wep_rng;
            wep_type = MechLibrary.getMechByID(attacker).left_wep_type;
        }

        dice = new int[wep_dmg];//Количество дайсов

        String toastStr = "Кости:";



        for (int i = 0; i < wep_dmg; i++)//Бросок костей
        {
            dice[i] = 1 + (int)(Math.random()*6);//Симуляция шестигранного дайса
            if (dice[i]>3){toastStr += " !"+dice[i]+"!";}
            else {toastStr += " "+dice[i];}
        }

        Toast.makeText(getContext(), toastStr, Toast.LENGTH_SHORT).show();//Вывод дайсов

        for (int i = 0; i < wep_dmg; i++)//Расчёт урона
        {
            if (dice[i]>3){dmg++;}
        }

        //TODO: обработка систем


        Toast.makeText(getContext(), "Урон: " + dmg, Toast.LENGTH_SHORT).show();//Вывод урона
        return dmg;
    }

    public Team getTeam(){
        return currentTeam;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TARGET && resultCode == Activity.RESULT_OK && data != null) {

            //Начало обработки урона
            int targetBattleID = data.getIntExtra("targetBattleID", -1);
            int atkBattleID = data.getIntExtra("atkBattleID", -1);
            if (targetBattleID != -1) {

                Log.d("PlayerFragment", "Выбран цель с battleID = " + targetBattleID);

                List<Team.TeamUnit> yourUnits = currentTeam.getUnits();
                int id1 = yourUnits.get(0).getBattleID();
                int youSize = 0;
                int enemySize = 0;
                String targetName;

                youSize = yourUnits.size();
                enemySize = enemyUnits.size();

                String atkName = findUnitByBattleID(yourUnits, atkBattleID).getMechID();
                int side = 0;//Правая пушка по умолчанию

                if (id1 == 1)//Хост
                {
                    if (targetBattleID>youSize)
                    {
                        targetName = findUnitByBattleID(enemyUnits, targetBattleID).getMechID();
                    }
                    else
                    {
                        targetName = findUnitByBattleID(yourUnits, targetBattleID).getMechID();
                    }
                }
                else//Клиент
                {
                    if (targetBattleID>enemySize)
                    {
                        targetName = findUnitByBattleID(yourUnits, targetBattleID).getMechID();
                    }
                    else
                    {
                        targetName = findUnitByBattleID(enemyUnits, targetBattleID).getMechID();
                    }
                }


                int dmg = calcDmg(atkName, side, targetName);//Расчёт урона

                ((MainActivity) requireActivity()).dealDmg(atkBattleID, dmg, targetBattleID, true);
                //Конец обработки урона


                //Начало обработки передачи хода
                MainActivity activity = (MainActivity) requireActivity();
                List<Team.TeamUnit> youUnits = currentTeam.getUnits();

                int inactive = 0;
                for (Team.TeamUnit tmpunit : youUnits)
                {
                    if (tmpunit.getStatus()==0){ inactive++;}
                }

                if (inactive==0 && enemyOut) {activity.initNewRound();}
                else if (inactive==0) {activity.letFinish();}
                else if (enemyOut) {}
                else
                {
                    activity.endTurn();
                    yourTurn = false;//Ход заканчивается при активации меха
                }

                displayTeam();
                //Конец обработки передачи хода

            }
        }
    }

    public Team.TeamUnit findUnitByBattleID(List<Team.TeamUnit> units, int BattleID) {
        for (Team.TeamUnit unit : units) {
            if (unit.getBattleID() == BattleID) {
                return unit;
            }
        }
        return null; // Если не найден
    }

    public void assignDmg(int dmg, int battleID)
    {
        for (Team.TeamUnit unit : currentTeam.getUnits())
        {
            if (unit.getBattleID()==battleID)
            {
                unit.addDmg(dmg);
                displayTeam();
                break;
            }
        }
    }

    public void setTurn(boolean yourTurn)
    {
        this.yourTurn = yourTurn;
        displayTeam();
    }

    public void setEnemyOut(boolean enemyOut)
    {
        this.enemyOut = enemyOut;
    }

    public void newRound()
    {
        for (Team.TeamUnit tmpunit : currentTeam.getUnits())
        {
            if (tmpunit.getStatus()==1){ tmpunit.setStatus(0);}
            if (tmpunit.getDmg() >= tmpunit.getMax_hp()){ tmpunit.setStatus(2);}
        }
        yourTurn = false;
        enemyOut = false;
    }


}
