package com.meowser.meowtapper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class StatAdapter extends RecyclerView.Adapter<StatAdapter.CardViewHolder> {

    private Context context;
    private ArrayList<Stat> listStat;

    public StatAdapter(Context context, ArrayList<Stat> list) {
        this.context = context;
        this.listStat = list;
    }

    public ArrayList<Stat> getListStat() {
        return listStat;
    }

    public void setListStat(ArrayList<Stat> listStat) {
        this.listStat = listStat;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_stat, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.statName.setText(getListStat().get(position).getStatTitle());
        if (getListStat().get(position).getStatTitle().equals("Total Achievements")){
            holder.statNumber.setText(String.format("%s/10", NumberFormat.getInstance().format(getListStat().get(position).getStatNumber())));
        } else{
            holder.statNumber.setText(String.format("%s", NumberFormat.getInstance().format(getListStat().get(position).getStatNumber())));
        }
    }

    @Override
    public int getItemCount() {
        return getListStat().size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        TextView statName, statNumber;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            statName = itemView.findViewById(R.id.stat_title);
            statNumber = itemView.findViewById(R.id.stat_title_number);
        }
    }

}
