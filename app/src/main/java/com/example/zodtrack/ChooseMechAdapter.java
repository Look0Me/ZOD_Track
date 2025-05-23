package com.example.zodtrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.zodtrack.mechs.MechTemplate;

import java.util.List;

public class ChooseMechAdapter extends MechAdapter {

    public interface OnAddClickListener {
        void onAddClick(MechTemplate mech);
    }

    private final OnAddClickListener listener;

    public ChooseMechAdapter(Context context, List<MechTemplate> mechs, OnAddClickListener listener) {
        super(context, mechs);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MechViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_choose_mech, parent, false);
        return new MechViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MechViewHolder holder, int position) {
        super.onBindViewHolder(holder, position); // наполняет данные как в UnitGallery

        MechTemplate mech = mechs.get(position);

        Button buttonAdd = holder.itemView.findViewById(R.id.buttonAddMech);
        buttonAdd.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddClick(mech);
            }
        });
    }
}