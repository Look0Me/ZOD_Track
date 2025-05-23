package com.example.zodtrack.mechs;


public class MechTemplate {
    public String name;                 //Название меха
    public String alias;                //Псевдоним в отряде
    public String pfp;                  //Изображение меха
    public int hp = 0;                  //Прочность меха

    public String ability;              //Боевая система
    public String ability_id;           //Айди боевой системы
    public String ability_desc;         //Описание боевой системы
    public int ability_hp = 0;          //Минимальная прочность меха, при которой работает боевая система

    public int left_wep_dmg = 0;        //Мощность левого оружия
    public int left_wep_rng = 0;        //Дальность левого оружия
    public int left_wep_type = 0;       //Тип левого оружия (0 - пулевой, 1 - энергетический, 2 - ближний)
    public String left_wep_pic;         //Изображение левого оружия

    public int right_wep_dmg = 0;        //Мощность правого оружия
    public int right_wep_rng = 0;        //Дальность правого оружия
    public int right_wep_type = 0;       //Тип правого оружия
    public String right_wep_pic;         //Изображение правого оружия


    public String getName() {
        return name;
    }
}
