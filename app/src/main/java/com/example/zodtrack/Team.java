package com.example.zodtrack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team implements Serializable {
    private String name;
    private List<TeamUnit> units;

    public Team(String name) {
        this.name = name;
        this.units = new ArrayList<>();
    }

    public Team(String name, List<TeamUnit> units) {
        this.name = name;
        this.units = new ArrayList<>(units); // копируем список, чтобы избежать внешних изменений
    }

    public int getTeamSize(){
        return units.size();
    }

    public void assignBattleIDs() {
        for (int i = 0; i < units.size(); i++) {
            units.get(i).setBattleID(i + 1);
        }
    }

    public void incBattleIDs(int host_max)
    {
        for (int i = 0; i < units.size(); i++) {
            units.get(i).setBattleID(units.get(i).getBattleID()+host_max);
        }
    }

    public String getName() {
        return name;
    }

    public List<TeamUnit> getUnits() {
        return units;
    }

    public void setUnits(List<TeamUnit> units) {
        this.units = units;
    }

    // Добавляем меха без псевдонима
    public void addUnit(String mechID, String alias) {
        units.add(new TeamUnit(mechID, alias));
    }

    public void setName(String name) {
        this.name = name;
    }

    // Вложенный static-класс TeamUnit (аналог UnitEntry)
    public static class TeamUnit implements Serializable {
        private String mechID;
        private String alias;
        private int battleID;
        private int status; // 0 - не активирован, 1 - активирован, 2 - уничтожен
        private int max_hp;
        private int dmg;
        private boolean BSstatus=true;// полученный урон

        public TeamUnit(String mechID, String alias) {
            this.mechID = mechID;
            this.alias = alias;
            this.battleID = -1;
            this.status = 0;
            this.dmg = 0;
            this.BSstatus = true;

            setMax_hp(Objects.requireNonNull(MechLibrary.getMechByID(mechID)).hp);
        }

        public void setBSstatus(boolean BSstatus) {
            this.BSstatus = BSstatus;
        }

        public boolean getBSstatus() {
            return BSstatus;
        }

        public void setMax_hp(int max_hp){this.max_hp = max_hp; }

        public int getMax_hp() {return max_hp;}

        public String getMechID() {
            return mechID;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public int getBattleID() {
            return battleID;
        }

        public void setBattleID(int battleID) {
            this.battleID = battleID;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getDmg() {
            return dmg;
        }

        public void addDmg(int dmg)
        {
            this.dmg += dmg;
            if (this.dmg<0)
                this.dmg = 0;
        }


    }
}

