package com.example.zodtrack.mechs;

public class Mamont extends MechTemplate{
    public Mamont() {
        name = "Мамонт";
        alias = "Мамонт";
        pfp = "mech_mamont"; // путь к ресурсу, если будешь использовать через res — нужно будет по-другому
        hp = 8;

        ability = "Защитное поле";
        ability_id = "force_field";
        ability_desc = "Если мамонт атакован, бросьте символьный кубик: > - защитное поле не помогло; * - поле спасло от одного повреждения; робот не пострадал;";
        ability_hp = 6;

        left_wep_dmg = 3;
        left_wep_rng = 8;
        left_wep_type = 0;
        left_wep_pic = "wep_mini1_l";

        right_wep_dmg = 3;
        right_wep_rng = 8;
        right_wep_type = 0; 
        right_wep_pic = "wep_mini1_r";
    }
}
