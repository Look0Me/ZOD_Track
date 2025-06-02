package com.example.zodtrack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
    private TextView teamNameTextView, wepLused, wepRused, hp, aliasText, mechNameTitle, actionPt;
    private ConstraintLayout teamView;
    private LinearLayout remoteView;
    private Button wepL, wepR, move, turn, endTurn;
    private String teamName;
    private LinearLayout mechListContainer;
    View included;
    private Team currentTeam;
    List<Team.TeamUnit> enemyUnits;
    private int actPt = 0;

    private boolean details = true;
    private boolean yourTurn = false;
    private boolean enemyOut = false;

    private int toastSleep = 100;

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

                            // Подгружаем новую разметку с пультом

                            remoteView.setVisibility(View.VISIBLE);
                            teamView.setVisibility(View.GONE);
                            remoteRefresh(unit);

                            wepL.setOnClickListener(wv -> {
                                openChooseTarget(unit.getBattleID(), 1);//Левая пушка = 1
                                wepL.setVisibility(View.GONE);
                                wepLused.setVisibility(View.VISIBLE);
                            });

                            wepR.setOnClickListener(wv -> {
                                openChooseTarget(unit.getBattleID(), 0);//Правая пушка = 0
                                wepR.setVisibility(View.GONE);
                                wepRused.setVisibility(View.VISIBLE);
                            });

                            move.setOnClickListener(wv -> {
                                Toast.makeText(getContext(), "Мех переместился", Toast.LENGTH_SHORT).show();
                                checkAP();
                            });

                            turn.setOnClickListener(wv -> {
                                Toast.makeText(getContext(), "Мех повернулся на 90°", Toast.LENGTH_SHORT).show();
                                checkAP();
                            });

                            endTurn.setOnClickListener(ev -> {
                                // Возвращаем предыдущую разметку (fragment_player)
                                hideRemote();
                            });

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

    private void checkAP() {
        actPt--;
        actionPt.setText("Очки действий: " + actPt);
        if (actPt==0)
        {
            hideRemote();
        }
    }

    private void hideRemote() {
        remoteView.setVisibility(View.GONE);
        teamView.setVisibility(View.VISIBLE);

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
//                //Конец обработки передачи хода
    }

    private void remoteRefresh(Team.TeamUnit unit) {
        wepL.setVisibility(View.VISIBLE);
        wepR.setVisibility(View.VISIBLE);
        wepLused.setVisibility(View.GONE);
        wepRused.setVisibility(View.GONE);

        actPt = 1 + (int)(Math.random()*6);//Симуляция шестигранного дайса
        actionPt.setText("Очки действий: " + actPt);
        Toast.makeText(getContext(), "Очки действий: " + actPt, Toast.LENGTH_SHORT).show();

        mechNameTitle.setText("Мех: " + unit.getMechID());
        aliasText.setText(unit.getAlias());
        if (unit.getStatus()!=2)
        {
            hp.setText(unit.getMax_hp()-unit.getDmg() +"/"+ unit.getMax_hp());//Установка хп
        }

        MechTemplate mech = MechLibrary.getMechByID(unit.getMechID());

        MechViewBinder.bind(included, mech, getContext(), unit.getDmg());


    }

    private void openChooseTarget(int battleID, int side) {
        MainActivity activity = (MainActivity) requireActivity();
        enemyUnits = activity.getEnemyList();
        List<Team.TeamUnit> yourUnits = currentTeam.getUnits();

        Intent intent = new Intent(getContext(), ChooseTarget.class);
        intent.putExtra("enemyUnits", new ArrayList<>(enemyUnits));
        intent.putExtra("yourUnits", new ArrayList<>(yourUnits));
        intent.putExtra("thisUnit", battleID);
        intent.putExtra("weaponSide", side);
        startActivityForResult(intent, REQUEST_TARGET);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        teamNameTextView = view.findViewById(R.id.teamName);
        mechListContainer = view.findViewById(R.id.mechListContainer);
        wepL = view.findViewById(R.id.wepL);
        wepR = view.findViewById(R.id.wepR);
        move = view.findViewById(R.id.move);
        turn = view.findViewById(R.id.turn);
        endTurn = view.findViewById(R.id.endTurn);
        remoteView = view.findViewById(R.id.remoteView);
        teamView = view.findViewById(R.id.teamView);
        wepLused = view.findViewById(R.id.wepLused);
        wepRused = view.findViewById(R.id.wepRused);
        mechNameTitle = view.findViewById(R.id.mechNameTitle);
        aliasText = view.findViewById(R.id.aliasText);
        hp = view.findViewById(R.id.hp);
        included = view.findViewById(R.id.included);
        actionPt = view.findViewById(R.id.actionPoints);

        remoteView.setVisibility(View.GONE);

        return view;
    }

    public void incBattleIDs(int host_max)
    {
        currentTeam.incBattleIDs(host_max);
    }

    public int calcDmg(Team.TeamUnit attacker, int side, Team.TeamUnit target)
    {
        int dmg = 0;
        int wep_dmg = 0;
        int wep_rng = 0;
        int wep_type = 0;
        int[] dice;

        String attackerName = attacker.getMechID();
        String targetName = target.getMechID();
        String atkSys = "";
        String targetSys = "";

        if (attacker.getBSstatus())//Получение системы атакующего
            atkSys = MechLibrary.getMechByID(attackerName).ability_id;
        if (target.getBSstatus())//Получение системы цели
            targetSys = MechLibrary.getMechByID(targetName).ability_id;

        if (side == 0)//правое оружие
        {
            wep_dmg = MechLibrary.getMechByID(attackerName).right_wep_dmg;
            wep_rng = MechLibrary.getMechByID(attackerName).right_wep_rng;
            wep_type = MechLibrary.getMechByID(attackerName).right_wep_type;
        }
        else //Левое оружие
        {
            wep_dmg = MechLibrary.getMechByID(attackerName).left_wep_dmg;
            wep_rng = MechLibrary.getMechByID(attackerName).left_wep_rng;
            wep_type = MechLibrary.getMechByID(attackerName).left_wep_type;
        }

        dice = new int[wep_dmg];//Количество дайсов
        String toastStr = "Кости:";

        for (int i = 0; i < wep_dmg; i++)//Бросок костей и расчёт урона
        {
            dice[i] = 1 + (int)(Math.random()*6);//Симуляция шестигранного дайса
            if (dice[i]>3){toastStr += " !"+dice[i]+"!";}
            else {toastStr += " "+dice[i];}
            if (dice[i]>3){dmg++;}
        }



        if (atkSys == "targeting_array")//Реализация Прицельного комплекса
        {
            String tmpToastStr = "Кости:";
            int tmpdmg = 0;
            int[] tmpdice = new int[wep_dmg];//Количество дайсов
            for (int i = 0; i < wep_dmg; i++)//Бросок костей
            {
                tmpdice[i] = 1 + (int)(Math.random()*6);//Симуляция шестигранного дайса
                if (tmpdice[i]>3){tmpToastStr += " !"+tmpdice[i]+"!";}
                else {tmpToastStr += " "+tmpdice[i];}
                if (tmpdice[i]>3){tmpdmg++;}
            }

            if (tmpdmg>dmg)
            {
                dmg = tmpdmg;
                toastStr = tmpToastStr;
                Toast.makeText(getContext(), "Прицельный комплекс улучшил атаку!", Toast.LENGTH_SHORT).show();//Уведомление об успехе БС
            }
            else
            {

                Toast.makeText(getContext(), "Прицельный комплекс не повлиял на атаку!", Toast.LENGTH_SHORT).show();//Уведомление об неудаче БС
            }

        }

        Toast.makeText(getContext(), toastStr, Toast.LENGTH_SHORT).show();//Вывод дайсов

        if (atkSys == "ion_generator" && wep_type == 1)//Реализация Ионного генератора
        {
            switch (getSymbol())
            {
                case 1://Урон остался прежним
                    Toast.makeText(getContext(), "Ионный генератор не повлиял на атаку", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    dmg *= 2;
                    Toast.makeText(getContext(), "Ионный генератор удвоил силу атаки!", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    dmg *= 3;
                    Toast.makeText(getContext(), "Ионный генератор утроил силу атаки!!!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        if (atkSys == "heat_scan")//Реализация Теплового сканера
        {
            switch (getSymbol())
            {
                case 1://Урон остался прежним
                    Toast.makeText(getContext(), "Тепловой сканер не повлиял на атаку", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    dmg *= 2;
                    Toast.makeText(getContext(), "Тепловой сканер удвоил силу атаки!", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    dmg *= 3;
                    Toast.makeText(getContext(), "Тепловой сканер утроил силу атаки!!!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        if (targetSys == "cloaking_device" && wep_type == 0)//Реализация устройства маскировки
        {
            if (getSymbol() == 1)
            {
                dmg = 0;
                Toast.makeText(getContext(), "Маскировка скрыла от атаки!!!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "Маскировка не спалса от повреждений", Toast.LENGTH_SHORT).show();
            }
        }

        if (targetSys == "intercept_fire" && wep_type == 0)//Реализация системы перехватного огня
        {
            if (getSymbol() == 1)
            {
                dmg = 0;
                Toast.makeText(getContext(), "Перехватный огонь спас от атаки!!!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "Перехватный огонь не помог", Toast.LENGTH_SHORT).show();
            }
        }

        if (targetSys == "energy_shield" && wep_type == 1)//Реализация энергетического щита
        {
            if (getSymbol() == 1)
            {
                dmg = 0;
                Toast.makeText(getContext(), "Энергетический щит рассеял атаку!!!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(), "Энергетический щит не помог", Toast.LENGTH_SHORT).show();
            }
        }

        if (targetSys == "force_field")//Реализация защитного поля
        {
            switch (getSymbol())
            {
                case 1:
                    Toast.makeText(getContext(), "Защитное поле не помогло", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    dmg--;
                    Toast.makeText(getContext(), "Защитное поле спасло от 1 повреждения!", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    dmg = 0;
                    Toast.makeText(getContext(), "Защитное поле ликвидировало атаку!!!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        if (side == 0)
        {
            Toast.makeText(getContext(), "Урон справа: " + dmg, Toast.LENGTH_SHORT).show();//Вывод урона
        }
        else
        {
            Toast.makeText(getContext(), "Урон слева: " + dmg, Toast.LENGTH_SHORT).show();//Вывод урона
        }
        return dmg;
    }

    private int getSymbol()
    {
        int value = 1 + (int)(Math.random()*6);//Симуляция шестигранного дайса
        int symbol=0;

        if (value >0 && value <=4)
        {
            symbol = 1;
//            Toast.makeText(getContext(), "Символьный кубик: >", Toast.LENGTH_SHORT).show();//Вывод урона
        }
        else if (value > 3 && value <=6)
        {
            symbol = 2;
//            Toast.makeText(getContext(), "Символьный кубик: *", Toast.LENGTH_SHORT).show();//Вывод урона
        }
        else
        {
            symbol = 3;
//            Toast.makeText(getContext(), "Символьный кубик: O", Toast.LENGTH_SHORT).show();//Вывод урона
        }
        return symbol;
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
            int side = data.getIntExtra("wepSide", -1);
            if (targetBattleID != -1) {

                Log.d("PlayerFragment", "Выбран цель с battleID = " + targetBattleID);

                List<Team.TeamUnit> yourUnits = currentTeam.getUnits();
                int id1 = yourUnits.get(0).getBattleID();
                int youSize = 0;
                int enemySize = 0;
                String targetName;

                youSize = yourUnits.size();
                enemySize = enemyUnits.size();
                Team.TeamUnit target;

                if (id1 == 1)//Хост
                {
                    if (targetBattleID>youSize)
                    {
                        target = findUnitByBattleID(enemyUnits, targetBattleID);
                    }
                    else
                    {
                        target = findUnitByBattleID(yourUnits, targetBattleID);
                    }
                }
                else//Клиент
                {
                    if (targetBattleID>enemySize)
                    {
                        target = findUnitByBattleID(yourUnits, targetBattleID);
                    }
                    else
                    {
                        target = findUnitByBattleID(enemyUnits, targetBattleID);
                    }
                }


                int dmg = calcDmg(findUnitByBattleID(yourUnits, atkBattleID), side, target);//Расчёт урона

                ((MainActivity) requireActivity()).dealDmg(atkBattleID, dmg, targetBattleID, true);
                //Конец обработки урона


                checkAP();

                displayTeam();

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
                if (dmg > 0)
                    Toast.makeText(getContext(), unit.getAlias() + " получил " + dmg + " урона.", Toast.LENGTH_SHORT).show();
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
            if (tmpunit.getBSstatus() && (tmpunit.getMax_hp() - tmpunit.getDmg() < MechLibrary.getMechByID(tmpunit.getMechID()).ability_hp))
            {
                tmpunit.setBSstatus(false);
                Toast.makeText(getContext(), tmpunit.getAlias() + ": боевая система вышла из строя", Toast.LENGTH_SHORT).show();
            }
            if (tmpunit.getDmg() >= tmpunit.getMax_hp()){ tmpunit.setStatus(2);}
        }
        yourTurn = false;
        enemyOut = false;
        displayTeam();
    }


}
