package com.meowser.meowtapper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SettingActivity extends AppCompatActivity {

    private ArrayList<ItemShop> itemList;
    private ArrayList<Achievement> achievementList;

    private double totalTaps, achievementComplete, upgradeBought, meowAllTime; //Variables for Stat
    private double meowWallet, meowPerSec, meowPerTap;

    Dialog resetDialog; //Dialog popup when user clicks reset button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        itemList = getIntent().getExtras().getParcelableArrayList("Shop_Array");
        achievementList = getIntent().getExtras().getParcelableArrayList("Achievement_Array");

        meowWallet = getIntent().getDoubleExtra("key_wallet", 0);
        meowPerSec = getIntent().getDoubleExtra("key_mps", 0);
        meowPerTap = getIntent().getDoubleExtra("key_mpt", 1);
        totalTaps = getIntent().getDoubleExtra("key_total_tap", totalTaps);
        meowAllTime = getIntent().getDoubleExtra("key_meowAllTime", meowAllTime);
        achievementComplete = getIntent().getDoubleExtra("key_achievementComplete", achievementComplete);
        upgradeBought = getIntent().getDoubleExtra("key_upgradeBought", upgradeBought);

        meowCounter();

        resetDialog = new Dialog(this);

        ImageButton shopScreen = findViewById(R.id.shopButton); //Redirect to Shop Screen
        ImageButton achievementScreen = findViewById(R.id.achievementButton); //Redirect to Achievement Screen
        ImageButton statScreen = findViewById(R.id.statButton); //Redirect to Stat Screen
        ImageButton backToClickerScreen = findViewById(R.id.exitButton); //Redirect user back to Clicker Screen
        ImageButton saveData = findViewById(R.id.saveProgress);
        ImageButton resetData = findViewById(R.id.resetProgress);
        CheckBox background_checkbox = findViewById(R.id.background_checkBox);
        CheckBox soundEffect_checkbox = findViewById(R.id.soundEffect_checkBox);

        background_checkbox.setChecked(MainActivity.backgroundSound);
        soundEffect_checkbox.setChecked(MainActivity.soundEffect);

        shopScreen.setEnabled(false);
        achievementScreen.setEnabled(false);
        statScreen.setEnabled(false);

        onBackgroundCheckTapped(background_checkbox);
        onSoundEffectCheckTapped(soundEffect_checkbox);
        onSaveButtonTapped(saveData);
        onResetButtonTapped(resetData);
        onExitButtonTapped(backToClickerScreen);
    }

    private void onSaveButtonTapped(ImageButton saveData) {
        saveData.setOnClickListener(view -> {
            SharedPreferences.Editor editor = getSharedPreferences("MyData", 0).edit();
            Gson gson = new Gson();
            String itemshop = gson.toJson(itemList);
            String achievement = gson.toJson(achievementList);

            editor.putString("itemShopList", itemshop);
            editor.putString("achievementList", achievement);
            editor.putBoolean("backgroundMusic", MainActivity.backgroundSound);
            editor.putBoolean("soundEffect", MainActivity.soundEffect);

            putDouble(editor, "meowWallet", meowWallet);
            putDouble(editor, "meowPerSec", meowPerSec);
            putDouble(editor, "meowPerTap", meowPerTap);
            putDouble(editor, "meowAllTime", meowAllTime);
            putDouble(editor, "upgradeBought", upgradeBought);
            putDouble(editor, "totalTaps", totalTaps);
            putDouble(editor, "achievementComplete", achievementComplete);

            editor.apply();

            if (MainActivity.soundEffect){
                final MediaPlayer saveSound = MediaPlayer.create(this, R.raw.save_sound);
                saveSound.start();
            }

            Toast.makeText(SettingActivity.this, "Successfully Saved!", Toast.LENGTH_SHORT).show();
        });
    }

    public static void putDouble(final SharedPreferences.Editor edit, final String key, final double value){
        edit.putLong(key, Double.doubleToLongBits(value));
    }

    private void onResetButtonTapped(ImageButton resetData) {
        resetData.setOnClickListener(view -> {
            if (MainActivity.soundEffect){
                final MediaPlayer resetSound = MediaPlayer.create(this, R.raw.reset_sound);
                resetSound.start();
            }
            openResetDialog();
        });
    }

    private void openResetDialog() {
        resetDialog.setContentView(R.layout.custom_reset_dialog);
        resetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageButton confirmReset = resetDialog.findViewById(R.id.confirmReset);
        ImageButton cancelReset = resetDialog.findViewById(R.id.cancelReset);
        resetDialog.show();

        confirmReset.setOnClickListener(view -> {
            if (MainActivity.soundEffect){
                final MediaPlayer resetSound = MediaPlayer.create(this, R.raw.reset_sound);
                resetSound.start();
            }
            SharedPreferences.Editor editor = getSharedPreferences("MyData", 0).edit();
            editor.clear();
            editor.apply();

            itemList.clear(); //Clear item and achievement Array
            achievementList.clear();
            setItemList(); //Setting default item and achievement Array
            setAchievementList();

            meowWallet = 0; //Setting variables to their default state
            meowPerTap = 1;
            meowPerSec = 0;
            meowAllTime = 0;
            upgradeBought = 0;
            totalTaps = 0;
            achievementComplete = 0;

            Toast.makeText(SettingActivity.this, "Successfully Reset!", Toast.LENGTH_SHORT).show();
            resetDialog.dismiss();
        });

        cancelReset.setOnClickListener(view -> {
            if (MainActivity.soundEffect){
                final MediaPlayer resetSound = MediaPlayer.create(this, R.raw.reset_sound);
                resetSound.start();
            }
            resetDialog.dismiss();
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("key_wallet", meowWallet);
        intent.putExtra("key_mps", meowPerSec);
        intent.putExtra("key_mpt", meowPerTap);
        intent.putExtra("key_total_tap", totalTaps);
        intent.putExtra("key_meowAllTime", meowAllTime);
        intent.putExtra("key_achievementComplete", achievementComplete);
        intent.putExtra("key_upgradeBought", upgradeBought);
        intent.putExtra("key_total_tap", totalTaps);
        intent.putExtra("key_achievementComplete", achievementComplete);
        intent.putExtra("key_total_tap", totalTaps);
        intent.putExtra("key_achievementComplete", achievementComplete);

        if (MainActivity.soundEffect){
            final MediaPlayer exitSound = MediaPlayer.create(this, R.raw.exit_button);
            exitSound.start();
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Shop_Array", itemList);
        bundle.putParcelableArrayList("Achievement_Array", achievementList);
        intent.putExtras(bundle);
        setResult(420, intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void onSoundEffectCheckTapped(CheckBox checkbox) {
        checkbox.setOnClickListener(view -> {
            if (checkbox.isChecked()) {
                final MediaPlayer toggleSound = MediaPlayer.create(this, R.raw.sound_toggle);
                toggleSound.start();
                MainActivity.soundEffect = true;
            } else {
                MainActivity.soundEffect = false;
            }
        });
    }

    private void onBackgroundCheckTapped(CheckBox checkbox) {
        checkbox.setOnClickListener(view -> {
            if (checkbox.isChecked()) {
                if (MainActivity.soundEffect){
                    final MediaPlayer toggleSound = MediaPlayer.create(this, R.raw.sound_toggle);
                    toggleSound.start();
                }
                MainActivity.backgroundSound = true;
                BackgroundMusic.backgroundMusic.start();
            } else {
                if (MainActivity.soundEffect){
                    final MediaPlayer toggleSound = MediaPlayer.create(this, R.raw.sound_toggle);
                    toggleSound.start();
                }
                MainActivity.backgroundSound = false;
                BackgroundMusic.backgroundMusic.pause();
            }
        });
    }

    public void setItemList(){
        itemList.add(new ItemShop("Kibble", 10.0, R.drawable.kibble, 0, 1.0, 0));
        itemList.add(new ItemShop("C. Tuna", 100.0, R.drawable.canned_tuna, 0, 10.0, 1.0));
        itemList.add(new ItemShop("Sushi", 1000.0, R.drawable.sushi, 0, 50.0, 3.0));
        itemList.add(new ItemShop("Fish", 9998.0, R.drawable.fish, 0, 100.0, 5.0));
        itemList.add(new ItemShop("Tower", 21499.0, R.drawable.tower, 0, 200.0, 10.0));
    }

    public void setAchievementList(){
        achievementList.add(new Achievement("Casual Taps", "Tap 50 times.", R.drawable.achievement01, R.drawable.achievement01, false));
        achievementList.add(new Achievement("Tappy Tap Tap", "Tap 500 times.", R.drawable.achievement01, R.drawable.achievement01, false));
        achievementList.add(new Achievement("Tap Tap Meowser", "Tap 5,000 times.", R.drawable.achievement01, R.drawable.achievement01, false));
        achievementList.add(new Achievement("Make some noise", "Generate 1,000 Meows", R.drawable.achievement02, R.drawable.achievement02, false));
        achievementList.add(new Achievement("Purr noises intensifies", "Generate 10,000 Meows", R.drawable.achievement02, R.drawable.achievement02, false));
        achievementList.add(new Achievement("It's a Meow Concert!", "Generate 30,000 Meows", R.drawable.achievement02, R.drawable.achievement02, false));
        achievementList.add(new Achievement("Meow the Builder", "Upgrade 10 times.", R.drawable.achievement03, R.drawable.achievement03, false));
        achievementList.add(new Achievement("Construction Meower", "Upgrade 25 times.", R.drawable.achievement03, R.drawable.achievement03, false));
        achievementList.add(new Achievement("Meowsers, Assemble!", "Upgrade 50 times.", R.drawable.achievement03, R.drawable.achievement03, false));
        achievementList.add(new Achievement("Meowtastic, Meowser!", "Complete all Achievements.", R.drawable.achievement04, R.drawable.achievement04, false));
    }

    private void meowCounter() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                runOnUiThread(() -> autoMeowGenerator());
            }
        }, 0, 1000);
    }

    private void autoMeowGenerator(){
        meowWallet += meowPerSec;
        meowAllTime += meowPerSec;
    }

    private void onExitButtonTapped(ImageButton backToClickerScreen) {
        backToClickerScreen.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("key_wallet", meowWallet);
            intent.putExtra("key_mps", meowPerSec);
            intent.putExtra("key_mpt", meowPerTap);
            intent.putExtra("key_total_tap", totalTaps);
            intent.putExtra("key_meowAllTime", meowAllTime);
            intent.putExtra("key_achievementComplete", achievementComplete);
            intent.putExtra("key_upgradeBought", upgradeBought);
            intent.putExtra("key_total_tap", totalTaps);
            intent.putExtra("key_achievementComplete", achievementComplete);

            if (MainActivity.soundEffect){
                final MediaPlayer exitSound = MediaPlayer.create(this, R.raw.exit_button);
                exitSound.start();
            }

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Shop_Array", itemList);
            bundle.putParcelableArrayList("Achievement_Array", achievementList);
            intent.putExtras(bundle);
            setResult(420, intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }
}