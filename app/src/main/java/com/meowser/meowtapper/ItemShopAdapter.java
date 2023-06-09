package com.meowser.meowtapper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ItemShopAdapter extends RecyclerView.Adapter<ItemShopAdapter.CardViewHolder>{
    Context context;
    ArrayList<ItemShop> itemList;
    onBuyListener mOnBuyListener;

    public ItemShopAdapter(Context context, ArrayList<ItemShop> itemList, onBuyListener mOnBuyListener) {
        this.context = context;
        this.itemList = itemList;
        this.mOnBuyListener = mOnBuyListener;
    }

    public ArrayList<ItemShop> getItemList() {
        return itemList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shop, parent, false);
        return new CardViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {


        holder.ItemImage.setImageResource(getItemList().get(position).getItemImageID());
        holder.ItemName.setText(getItemList().get(position).getItemName());
        holder.ItemPrice.setText(String.format("Meows: %s",
                NumberFormat.getInstance().format(getItemList().get(position).getItemPrice())));
        holder.ItemNumBought.setText(String.format("%d", getItemList().get(position).getItemNumberBought()));
        holder.ItemMPS.setText(String.format("+%s/s",
                NumberFormat.getInstance().format(getItemList().get(position).getItemMeowPerSec())));
        holder.ItemMPT.setText(String.format("+%s/tap",
                NumberFormat.getInstance().format(getItemList().get(position).getItemMeowPerTap())));

        holder.ItemBuy.setOnClickListener(view -> mOnBuyListener.onBuyClick(holder, position));

    }

    @Override
    public int getItemCount() {
        return getItemList().size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView ItemImage;
        TextView ItemName, ItemPrice, ItemNumBought, ItemMPS, ItemMPT;
        ImageButton ItemBuy;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            this.ItemImage = itemView.findViewById(R.id.shop_image);
            this.ItemName = itemView.findViewById(R.id.shop_name);
            this.ItemPrice = itemView.findViewById(R.id.shop_price);
            this.ItemNumBought = itemView.findViewById(R.id.shop_numberBought);
            this.ItemMPS = itemView.findViewById(R.id.shop_meowpersec);
            this.ItemMPT = itemView.findViewById(R.id.shop_meowpertap);
            this.ItemBuy = itemView.findViewById(R.id.shop_buy);
        }
    }

    public interface onBuyListener{
        void onBuyClick(CardViewHolder holder, int position);
    }
}
