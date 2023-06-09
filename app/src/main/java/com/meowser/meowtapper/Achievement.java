package com.meowser.meowtapper;

import android.os.Parcel;
import android.os.Parcelable;

public class Achievement implements Parcelable {
    private final String AchievementTitle;
    private final String AchievementDesc;
    private int AchievementImageLeft;
    private int AchievementImageRight;
    private boolean AchievementComplete;

    public Achievement(String achievementTitle, String achievementDesc, int achievementImageLeft, int achievementImageRight, boolean achievementComplete) {
        AchievementTitle = achievementTitle;
        AchievementDesc = achievementDesc;
        AchievementImageLeft = achievementImageLeft;
        AchievementImageRight = achievementImageRight;
        AchievementComplete = achievementComplete;
    }

    protected Achievement(Parcel in) {
        AchievementTitle = in.readString();
        AchievementDesc = in.readString();
        AchievementImageLeft = in.readInt();
        AchievementImageRight = in.readInt();
        AchievementComplete = in.readByte() != 0;
    }

    public static final Creator<Achievement> CREATOR = new Creator<Achievement>() {
        @Override
        public Achievement createFromParcel(Parcel in) {
            return new Achievement(in);
        }

        @Override
        public Achievement[] newArray(int size) {
            return new Achievement[size];
        }
    };

    public String getAchievementTitle() {
        return AchievementTitle;
    }

    public String getAchievementDesc() {
        return AchievementDesc;
    }

    public int getAchievementImageLeft() {
        return AchievementImageLeft;
    }

    public int getAchievementImageRight() {
        return AchievementImageRight;
    }

    public boolean isAchievementComplete() {
        return AchievementComplete;
    }

    public void setAchievementComplete(boolean achievementComplete) {
        AchievementComplete = achievementComplete;
    }

    public void setAchievementImageLeft(int achievementImageLeft) {
        AchievementImageLeft = achievementImageLeft;
    }

    public void setAchievementImageRight(int achievementImageRight) {
        AchievementImageRight = achievementImageRight;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(AchievementTitle);
        parcel.writeString(AchievementDesc);
        parcel.writeInt(AchievementImageLeft);
        parcel.writeInt(AchievementImageRight);
        parcel.writeByte((byte) (AchievementComplete ? 1 : 0));
    }
}
