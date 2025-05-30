package com.example.zodtrack.mechs;

public class Barbarian extends MechTemplate{
    public Barbarian() {
        name = "Варвар";
        alias = "Варвар";
        pfp = "mech_barbar"; // путь к ресурсу, если будешь использовать через res — нужно будет по-другому
        hp = 12;

        ability = "Устройство маскировки";
        ability_id = "cloaking_device";
        ability_desc = "Если Варвар атакован из пулевого оружия, бросьте символьный кубик: \n> - робот не пострадал \n(*/O) - маскировка не спасла от повреждений";
        ability_hp = 6;

        left_wep_dmg = 4;
        left_wep_rng = 8;
        left_wep_type = 0;
        left_wep_pic = "wep_barbar_l";

        right_wep_dmg = 3;
        right_wep_rng = 1;
        right_wep_type = 2;
        right_wep_pic = "wep_barbar_r";
    }
}
