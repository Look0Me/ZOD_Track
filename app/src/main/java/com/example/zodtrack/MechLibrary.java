package com.example.zodtrack;

import com.example.zodtrack.mechs.Archon;
import com.example.zodtrack.mechs.Barbarian;
import com.example.zodtrack.mechs.Doberman;
import com.example.zodtrack.mechs.Golem;
import com.example.zodtrack.mechs.Goliath;
import com.example.zodtrack.mechs.Kles;
import com.example.zodtrack.mechs.Mamont;
import com.example.zodtrack.mechs.MechTemplate;
import com.example.zodtrack.mechs.Minotavr;
import com.example.zodtrack.mechs.Predator;
import com.example.zodtrack.mechs.Saratsin;
import com.example.zodtrack.mechs.Titan;
import com.example.zodtrack.mechs.Werewolf;

import java.util.ArrayList;
import java.util.List;

public class MechLibrary {
    public static List<MechTemplate> loadMechs() {
        List<MechTemplate> mechs = new ArrayList<>();

        // Добавь сюда всех своих мехов
        mechs.add(new Werewolf());
        mechs.add(new Predator());
        mechs.add(new Kles());
        mechs.add(new Titan());
        mechs.add(new Saratsin());
        mechs.add(new Barbarian());
        mechs.add(new Mamont());
        mechs.add(new Archon());
        mechs.add(new Minotavr());
        mechs.add(new Goliath());
        mechs.add(new Doberman());
        mechs.add(new Golem());
        // Добавляй остальных по аналогии

        return mechs;
    }

    public static MechTemplate getMechByID(String id) {
        for (MechTemplate mech : loadMechs()) {
            if (mech.name.equals(id)) {
                return mech;
            }
        }
        return null; // если не найден
    }
}