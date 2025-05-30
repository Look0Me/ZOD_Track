package com.example.zodtrack.mechs;

public class Doberman extends MechTemplate{
    public Doberman() {
        name = "Доберман";
        alias = "Доберман";
        pfp = "mech_dober"; // путь к ресурсу, если будешь использовать через res — нужно будет по-другому
        hp = 6;

        ability = "Ионный генератор";
        ability_id = "ion_generator";
        ability_desc = "Когда Доберман атакует из энергетического оружия, вместе с кубиками атаки бросьте символьный кубик: \n> - урон (x1) \n* - урон (x2) \nO - урон (x3)";
        ability_hp = 4;

        left_wep_dmg = 3;
        left_wep_rng = 1;
        left_wep_type = 2;
        left_wep_pic = "wep_dober_l";

        right_wep_dmg = 2;
        right_wep_rng = 7;
        right_wep_type = 1;
        right_wep_pic = "wep_dober_r";
    }
}
