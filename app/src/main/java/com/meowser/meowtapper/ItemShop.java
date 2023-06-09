package com.meowser.meowtapper;

import android.os.Parcel;
import android.os.Parcelable;


public class ItemShop implements Parcelable {
    private final String ItemName;
    private double ItemPrice;
    private final int ItemImageID;
    private int ItemNumberBought;
    private final double ItemMeowPerSec;
    private final double ItemMeowPerTap;

    public ItemShop(String itemName, double itemPrice, int itemImageID, int itemNumberBought, double itemMeowPerSec, double itemMeowPerTap) {
        ItemName = itemName;
        ItemPrice = itemPrice;
        ItemImageID = itemImageID;
        ItemNumberBought = itemNumberBought;
        ItemMeowPerSec = itemMeowPerSec;
        ItemMeowPerTap = itemMeowPerTap;
    }

    protected ItemShop(Parcel in) {
        ItemName = in.readString();
        ItemPrice = in.readDouble();
        ItemImageID = in.readInt();
        ItemNumberBought = in.readInt();
        ItemMeowPerSec = in.readDouble();
        ItemMeowPerTap = in.readDouble();
    }

    public static final Creator<ItemShop> CREATOR = new Creator<ItemShop>() {
        @Override
        public ItemShop createFromParcel(Parcel in) {
            return new ItemShop(in);
        }

        @Override
        public ItemShop[] newArray(int size) {
            return new ItemShop[size];
        }
    };

    public String getItemName() {
        return ItemName;
    }

    public double getItemPrice() {
        return ItemPrice;
    }

    public int getItemImageID() {
        return ItemImageID;
    }

    public int getItemNumberBought() {
        return ItemNumberBought;
    }

    public double getItemMeowPerSec() {
        return ItemMeowPerSec;
    }

    public double getItemMeowPerTap() {return ItemMeowPerTap;}

    public double getNewItemPrice(double itemPrice) {return Math.floor(itemPrice + 0.4 * itemPrice);}

    public void setItemNumberBought(int itemNumberBought) {
        ItemNumberBought = itemNumberBought;
    }

    public void setItemPrice(double itemPrice) {
        ItemPrice = itemPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ItemName);
        parcel.writeDouble(ItemPrice);
        parcel.writeInt(ItemImageID);
        parcel.writeInt(ItemNumberBought);
        parcel.writeDouble(ItemMeowPerSec);
        parcel.writeDouble(ItemMeowPerTap);
    }
}
