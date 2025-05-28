package com.example.zodtrack;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.zodtrack.mechs.MechTemplate;

public class MechViewBinder {

    public static void bind(View itemView, MechTemplate mech, Context context) {
        bind(itemView, mech, context, 0); // значение по умолчанию = 0
    }

    public static void bind(View itemView, MechTemplate mech, Context context, int dmg) {
        TextView name = itemView.findViewById(R.id.mechName);
        TextView abilityName = itemView.findViewById(R.id.abilityName);
        TextView abilityDesc = itemView.findViewById(R.id.abilityDescription);
        TextView LdmgVal = itemView.findViewById(R.id.LdmgVal);
        TextView LrangVal = itemView.findViewById(R.id.LrangVal);
        TextView RdmgVal = itemView.findViewById(R.id.RdmgVal);
        TextView RrangVal = itemView.findViewById(R.id.RrangVal);
        ImageView imageMech = itemView.findViewById(R.id.imageMech);
        ImageView imageL = itemView.findViewById(R.id.imageL);
        ImageView imageR = itemView.findViewById(R.id.imageR);
        ImageView wpnLtype = itemView.findViewById(R.id.wpnLtype);
        ImageView wpnRtype = itemView.findViewById(R.id.wpnRtype);
        LinearLayout hpBar = itemView.findViewById(R.id.hpBar);

        name.setText(mech.name);
        abilityName.setText(mech.ability);
        abilityDesc.setText(mech.ability_desc);
        LdmgVal.setText(String.valueOf(mech.left_wep_dmg));
        LrangVal.setText(String.valueOf(mech.left_wep_rng));
        RdmgVal.setText(String.valueOf(mech.right_wep_dmg));
        RrangVal.setText(String.valueOf(mech.right_wep_rng));

        int mechImgId = context.getResources().getIdentifier(mech.pfp, "drawable", context.getPackageName());
        int leftImgId = context.getResources().getIdentifier(mech.left_wep_pic, "drawable", context.getPackageName());
        int rightImgId = context.getResources().getIdentifier(mech.right_wep_pic, "drawable", context.getPackageName());

        imageMech.setImageResource(mechImgId != 0 ? mechImgId : R.drawable.mech_blank);
        imageL.setImageResource(leftImgId != 0 ? leftImgId : R.drawable.wep_blank_r);
        imageR.setImageResource(rightImgId != 0 ? rightImgId : R.drawable.wep_blank_r);

        switch (mech.right_wep_type) {
            case 1:
                wpnRtype.setImageResource(R.drawable.dmg_energy);
                break;
            case 2:
                wpnRtype.setImageResource(R.drawable.dmg_melee);
                break;
            default:
                wpnRtype.setImageResource(R.drawable.dmg_bullet);
        }

        switch (mech.left_wep_type) {
            case 1:
                wpnLtype.setImageResource(R.drawable.dmg_energy);
                break;
            case 2:
                wpnLtype.setImageResource(R.drawable.dmg_melee);
                break;
            default:
                wpnLtype.setImageResource(R.drawable.dmg_bullet);
        }

        hpBar.removeAllViews();
        for (int i = mech.hp; i >= 0; i--) {
            TextView hpCell = new TextView(context);
            hpCell.setText(String.valueOf(i));
            hpCell.setTextSize(12);
            hpCell.setPadding(6, 2, 6, 2);
            hpCell.setTextColor(Color.WHITE);


            if (i == 0) {
                hpCell.setBackground(ContextCompat.getDrawable(context, R.drawable.hp_cell_red));
            }else if (dmg>0)
            {
                hpCell.setBackground(ContextCompat.getDrawable(context, R.drawable.hp_cell_gray));
                dmg--;
            }
            else if (i < mech.ability_hp) {
                hpCell.setBackground(ContextCompat.getDrawable(context, R.drawable.hp_cell_yellow));
            } else {
                hpCell.setBackground(ContextCompat.getDrawable(context, R.drawable.hp_cell_green));
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 0, 0, 0);
            hpCell.setLayoutParams(params);

            hpBar.addView(hpCell);
        }
    }
}
