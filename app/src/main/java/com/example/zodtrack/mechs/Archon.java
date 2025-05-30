package com.example.zodtrack.mechs;

public class Archon extends MechTemplate{
    public Archon() {
        name = "Архон";
        alias = "Архон";
        pfp = "mech_archon"; // путь к ресурсу, если будешь использовать через res — нужно будет по-другому
        hp = 8;

        ability = "Защитное поле";
        ability_id = "force_field";
        ability_desc = "Если Архон атакован, бросьте символьный кубик: \n> - защитное поле не помогло \n* - поле спасло от 1 повреждения \nO - робот не пострадал";
        ability_hp = 5;

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
