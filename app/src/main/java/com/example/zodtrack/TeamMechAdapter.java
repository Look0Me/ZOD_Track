package com.example.zodtrack;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zodtrack.mechs.MechTemplate;

import java.util.ArrayList;
import java.util.List;

public class TeamMechAdapter extends RecyclerView.Adapter<TeamMechAdapter.ViewHolder> {

    private final Context context;
    private final List<Team.TeamUnit> teamUnits;

    public TeamMechAdapter(Context context, List<Team.TeamUnit> teamUnits) {
        this.context = context;
        this.teamUnits = teamUnits;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.unit_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Team.TeamUnit teamUnit = teamUnits.get(position);
        MechTemplate mech = MechLibrary.getMechByID(teamUnit.getMechID());

        if (mech == null) return;

        holder.nameTitle.setText("Мех: " + mech.name);
        holder.aliasEdit.setText(teamUnit.getAlias());

        // Сохраняем alias при изменении
        holder.aliasEdit.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                teamUnit.setAlias(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Заполняем внутреннюю часть item_unit
        holder.mechAdapter.bindDataToView(holder.itemView, mech);
    }

    @Override
    public int getItemCount() {
        return teamUnits.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTitle;
        EditText aliasEdit;
        MechAdapter mechAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTitle = itemView.findViewById(R.id.mechNameTitle);
            aliasEdit = itemView.findViewById(R.id.aliasEditText);
            mechAdapter = new MechAdapter(itemView.getContext(), new ArrayList<>()) {
                public void bindDataToView(View rootView, MechTemplate mech) {
                    MechViewHolder vh = new MechViewHolder(rootView);
                    onBindViewHolder(vh, 0);
                    mechs.clear();
                    mechs.add(mech);
                }
            };
        }
    }
}