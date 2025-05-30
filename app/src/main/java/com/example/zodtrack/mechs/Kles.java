package com.example.zodtrack.mechs;

public class Kles extends MechTemplate{
    public Kles() {
        name = "Клещ";
        alias = "Клещ";
        pfp = "mech_kles"; // путь к ресурсу, если будешь использовать через res — нужно будет по-другому
        hp = 9;

        ability = "Ионный генератор";
        ability_id = "ion_generator";
        ability_desc = "Когда Клещ атакует из энергетического оружия, вместе с кубиками атаки бросьте символьный кубик: \n> - урон (x1) \n* - урон (x2) \nO - урон (x3)";
        ability_hp = 6;

        left_wep_dmg = 3;
        left_wep_rng = 1;
        left_wep_type = 2;
        left_wep_pic = "wep_kles_l";

        right_wep_dmg = 3;
        right_wep_rng = 9;
        right_wep_type = 1;
        right_wep_pic = "wep_kles_r";
    }
}
