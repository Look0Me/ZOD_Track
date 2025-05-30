package com.example.zodtrack.mechs;

public class Werewolf extends MechTemplate{
    public Werewolf() {
        name = "Вервольф";
        alias = "Вервольф";
        pfp = "mech_were"; // путь к ресурсу, если будешь использовать через res — нужно будет по-другому
        hp = 11;

        ability = "Прицельный комплекс";
        ability_id = "targeting_array";
        ability_desc = "Когда Вервольф атакует, вы можете бросить кубики атаки дважды и выбрать наилучший из двух бросков результат";
        ability_hp = 8;

        left_wep_dmg = 1;
        left_wep_rng = 7;
        left_wep_type = 0;
        left_wep_pic = "wep_were_l";

        right_wep_dmg = 3;
        right_wep_rng = 8;
        right_wep_type = 1;
        right_wep_pic = "wep_were_r";
    }
}
