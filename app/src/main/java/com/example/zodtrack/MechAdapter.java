package com.example.zodtrack;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zodtrack.mechs.MechTemplate;

import java.util.List;

public class MechAdapter extends RecyclerView.Adapter<MechAdapter.MechViewHolder> {

    protected final List<MechTemplate> mechs;
    protected final Context context;

    public MechAdapter(Context context, List<MechTemplate> mechs) {
        this.context = context;
        this.mechs = mechs;
    }

    @NonNull
    @Override
    public MechViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_unit, parent, false);
        return new MechViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MechViewHolder holder, int position) {
        Log.d("DEBUG", "Binding position: " + position + " / total: " + mechs.size());
        MechTemplate mech = mechs.get(position);
        holder.name.setText(mech.name);
        holder.abilityName.setText(mech.ability);
        holder.abilityDesc.setText(mech.ability_desc);
        holder.LdmgVal.setText(String.valueOf(mech.left_wep_dmg));
        holder.LrangVal.setText(String.valueOf(mech.left_wep_rng));
        holder.RdmgVal.setText(String.valueOf(mech.right_wep_dmg));
        holder.RrangVal.setText(String.valueOf(mech.right_wep_rng));





        int mechImgId = context.getResources().getIdentifier(mech.pfp, "drawable", context.getPackageName());
        int leftImgId = context.getResources().getIdentifier(mech.left_wep_pic, "drawable", context.getPackageName());
        int rightImgId = context.getResources().getIdentifier(mech.right_wep_pic, "drawable", context.getPackageName());

        holder.imageMech.setImageResource(mechImgId != 0 ? mechImgId : R.drawable.mech_blank);
        holder.imageL.setImageResource(leftImgId != 0 ? leftImgId : R.drawable.wep_blank_r);
        holder.imageR.setImageResource(rightImgId != 0 ? rightImgId : R.drawable.wep_blank_r);

        switch(mech.right_wep_type) {       //Правое оружие
            case 1:
                holder.wpnRtype.setImageResource(R.drawable.dmg_energy);
                break;
            case 2:
                holder.wpnRtype.setImageResource(R.drawable.dmg_melee);
                break;
            default:
                holder.wpnRtype.setImageResource(R.drawable.dmg_bullet);
        }

        switch(mech.left_wep_type) {       //Левое оружие
            case 1:
                holder.wpnLtype.setImageResource(R.drawable.dmg_energy);
                break;
            case 2:
                holder.wpnLtype.setImageResource(R.drawable.dmg_melee);
                break;
            default:
                holder.wpnLtype.setImageResource(R.drawable.dmg_bullet);
        }

        holder.hpBar.removeAllViews();

        for (int i = mech.hp; i >= 0; i--) {
            TextView hpCell = new TextView(context);
            hpCell.setText(String.valueOf(i));
            hpCell.setTextSize(12);
            hpCell.setPadding(6, 2, 6, 2);
            hpCell.setTextColor(Color.WHITE);

            // Выбор фона по условиям
            if (i == 0) {
                hpCell.setBackground(ContextCompat.getDrawable(context, R.drawable.hp_cell_red));
            } else if (i < mech.ability_hp) {
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

            holder.hpBar.addView(hpCell);
        }

    }

    @Override
    public int getItemCount() {
        Log.d("DEBUG", "Item count: " + mechs.size());
        return mechs.size();
    }

    static class MechViewHolder extends RecyclerView.ViewHolder {
        TextView name, abilityName, abilityDesc, LdmgVal, LrangVal, RdmgVal, RrangVal;
        ImageView imageMech, imageL, imageR, wpnRtype, wpnLtype;
        LinearLayout hpBar;


        public MechViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.mechName);
            abilityName = itemView.findViewById(R.id.abilityName);
            abilityDesc = itemView.findViewById(R.id.abilityDescription);
            LdmgVal = itemView.findViewById(R.id.LdmgVal);
            LrangVal = itemView.findViewById(R.id.LrangVal);
            RdmgVal = itemView.findViewById(R.id.RdmgVal);
            RrangVal = itemView.findViewById(R.id.RrangVal);
            imageMech = itemView.findViewById(R.id.imageMech);
            imageL = itemView.findViewById(R.id.imageL);
            imageR = itemView.findViewById(R.id.imageR);
            wpnLtype = itemView.findViewById(R.id.wpnLtype);
            wpnRtype = itemView.findViewById(R.id.wpnRtype);
            hpBar = itemView.findViewById(R.id.hpBar);

        }
    }
}
