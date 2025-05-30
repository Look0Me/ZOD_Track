package com.example.zodtrack.mechs;

public class Predator extends MechTemplate{
    public Predator() {
        name = "Хищник";
        alias = "Хищник";
        pfp = "mech_pred"; // путь к ресурсу, если будешь использовать через res — нужно будет по-другому
        hp = 11;

        ability = "Прицельный комплекс";
        ability_id = "targeting_array";
        ability_desc = "Когда Хищник атакует, вы можете бросить кубики атаки дважды и выбрать наилучший из двух бросков результат";
        ability_hp = 7;

        left_wep_dmg = 1;
        left_wep_rng = 7;
        left_wep_type = 0;
        left_wep_pic = "wep_pred_l";

        right_wep_dmg = 2;
        right_wep_rng = 11;
        right_wep_type = 0;
        right_wep_pic = "wep_pred_r";
    }
}
