package com.example.zodtrack;

import com.example.zodtrack.mechs.Mamont;
import com.example.zodtrack.mechs.MechTemplate;
import com.example.zodtrack.mechs.Minotavr;

import java.util.ArrayList;
import java.util.List;

public class MechLibrary {
    public static List<MechTemplate> loadMechs() {
        List<MechTemplate> mechs = new ArrayList<>();

        // Добавь сюда всех своих мехов
        mechs.add(new Mamont());
        mechs.add(new Minotavr());
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