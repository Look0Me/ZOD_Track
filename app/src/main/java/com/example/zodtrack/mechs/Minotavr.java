package com.example.zodtrack.mechs;

public class Minotavr extends MechTemplate{
    public Minotavr() {
        name = "Минотавр";
        alias = "Минотавр";
        pfp = "mech_minot"; // путь к ресурсу, если будешь использовать через res — нужно будет по-другому
        hp = 8;

        ability = "Тепловой сканер";
        ability_id = "heat_scan";
        ability_desc = "Когда Минотавр атакует, то вместе с кубиками атаки бросьте символьный кубик: \n> - урон (x1) \n* - урон (x2) \nO - урон (x3)";
        ability_hp = 6;

        left_wep_dmg = 3;
        left_wep_rng = 8;
        left_wep_type = 0;
        left_wep_pic = "wep_mini_l";

        right_wep_dmg = 3;
        right_wep_rng = 8;
        right_wep_type = 0;
        right_wep_pic = "wep_mini_r";
    }

}
