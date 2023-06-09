package com.meowser.meowtapper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.CardViewHolder> {

    private Context context;
    private ArrayList<Achievement> listAchievement;

    public AchievementAdapter(Context context, ArrayList<Achievement> listAchievement) {
        this.context = context;
        this.listAchievement = listAchievement;
    }

    public ArrayList<Achievement> getListAchievement() {
        return listAchievement;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_achievement, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.ImgLeft.setImageResource(getListAchievement().get(position).getAchievementImageLeft());
        holder.ImgRight.setImageResource(getListAchievement().get(position).getAchievementImageRight());
        holder.AchievementTitle.setText(getListAchievement().get(position).getAchievementTitle());
        holder.AchievementDescription.setText(getListAchievement().get(position).getAchievementDesc());
    }

    @Override
    public int getItemCount() {
        return getListAchievement().size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView ImgLeft, ImgRight;
        TextView AchievementTitle, AchievementDescription;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            ImgLeft = itemView.findViewById(R.id.achievementImageLeft);
            ImgRight = itemView.findViewById(R.id.achievementImageRight);
            AchievementTitle = itemView.findViewById(R.id.achievement_title);
            AchievementDescription = itemView.findViewById(R.id.achievement_description);
        }
    }
}
