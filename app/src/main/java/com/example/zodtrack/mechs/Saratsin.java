package com.example.zodtrack.mechs;

public class Saratsin extends MechTemplate{
    public Saratsin() {
        name = "Сарацин";
        alias = "Сарацин";
        pfp = "mech_sarat"; // путь к ресурсу, если будешь использовать через res — нужно будет по-другому
        hp = 10;

        ability = "Энергетический щит";
        ability_id = "energy_shield";
        ability_desc = "Если Сарацин атакован из энергетического оружия, бросьте символьный кубик: \n> - робот не пострадал \n(*/O) - энергетический щит не спас от повреждений";
        ability_hp = 7;

        left_wep_dmg = 2;
        left_wep_rng = 6;
        left_wep_type = 1;
        left_wep_pic = "wep_sarat_l";

        right_wep_dmg = 2;
        right_wep_rng = 6;
        right_wep_type = 1;
        right_wep_pic = "wep_sarat_r";
    }
}
