package com.meowser.meowtapper;

import android.os.Parcel;
import android.os.Parcelable;

public class Stat implements Parcelable {
    private String statTitle;
    private double statNumber;


    public Stat(String statTitle, double statNumber) {
        this.statTitle = statTitle;
        this.statNumber = statNumber;
    }

    protected Stat(Parcel in) {
        statTitle = in.readString();
        statNumber = in.readDouble();
    }

    public static final Creator<Stat> CREATOR = new Creator<Stat>() {
        @Override
        public Stat createFromParcel(Parcel in) {
            return new Stat(in);
        }

        @Override
        public Stat[] newArray(int size) {
            return new Stat[size];
        }
    };

    public String getStatTitle() {
        return statTitle;
    }

    public double getStatNumber() {
        return statNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(statTitle);
        parcel.writeDouble(statNumber);
    }
}
