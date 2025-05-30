package com.example.zodtrack.mechs;

public class Titan extends MechTemplate{
    public Titan() {
        name = "Титан";
        alias = "Титан";
        pfp = "mech_titan"; // путь к ресурсу, если будешь использовать через res — нужно будет по-другому
        hp = 10;

        ability = "Устройство маскировки";
        ability_id = "cloaking_device";
        ability_desc = "Если Титан атакован из пулевого оружия, бросьте символьный кубик: \n> - робот не пострадал \n(*/O) - маскировка не спасла от повреждений";
        ability_hp = 6;

        left_wep_dmg = 2;
        left_wep_rng = 12;
        left_wep_type = 1;
        left_wep_pic = "wep_titan_l";

        right_wep_dmg = 2;
        right_wep_rng = 11;
        right_wep_type = 0;
        right_wep_pic = "wep_titan_r";
    }
}
