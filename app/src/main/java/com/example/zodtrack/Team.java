package com.example.zodtrack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        private int dmg;    // полученный урон

        public TeamUnit(String mechID, String alias) {
            this.mechID = mechID;
            this.alias = alias;
            this.battleID = -1;
            this.status = 0;
            this.dmg = 0;
        }

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

        public void setDmg(int dmg) {
            this.dmg = dmg;
        }
    }
}

