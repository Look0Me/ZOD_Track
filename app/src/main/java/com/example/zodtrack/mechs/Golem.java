package com.example.zodtrack.mechs;

public class Golem extends MechTemplate{
    public Golem() {
        name = "Голем";
        alias = "Голем";
        pfp = "mech_golem"; // путь к ресурсу, если будешь использовать через res — нужно будет по-другому
        hp = 6;

        ability = "Система перехватного огня";
        ability_id = "intercept_fire";
        ability_desc = "Если Голем атакован из пулевого оружия, бросьте символьный кубик: \n> - робот не пострадал \n(*/O) - система не спасла от повреждений";
        ability_hp = 3;

        left_wep_dmg = 2;
        left_wep_rng = 1;
        left_wep_type = 2;
        left_wep_pic = "wep_golem_l";

        right_wep_dmg = 3;
        right_wep_rng = 8;
        right_wep_type = 1;
        right_wep_pic = "wep_golem_r";
    }
}
