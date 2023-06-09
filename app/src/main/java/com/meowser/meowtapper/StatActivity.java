package com.meowser.meowtapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class StatActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StatAdapter statAdapter;

    private ArrayList<ItemShop> itemList;
    private ArrayList<Achievement> achievementList;
    private ArrayList<Stat> statList;

    private double totalTaps, achievementComplete, upgradeBought, meowAllTime; //Variables for Stat
    private double meowWallet, meowPerSec, meowPerTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        itemList = getIntent().getExtras().getParcelableArrayList("Shop_Array");
        achievementList = getIntent().getExtras().getParcelableArrayList("Achievement_Array");
        statList = new ArrayList<>();

        meowWallet = getIntent().getDoubleExtra("key_wallet", 0);
        meowPerSec = getIntent().getDoubleExtra("key_mps", 0);
        meowPerTap = getIntent().getDoubleExtra("key_mpt", 1);
        totalTaps = getIntent().getDoubleExtra("key_total_tap", totalTaps);
        meowAllTime = getIntent().getDoubleExtra("key_meowAllTime", meowAllTime);
        achievementComplete = getIntent().getDoubleExtra("key_achievementComplete", achievementComplete);
        upgradeBought = getIntent().getDoubleExtra("key_upgradeBought", upgradeBought);

        setStatList();
        meowCounter();

        recyclerView = findViewById(R.id.statList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        statAdapter = new StatAdapter(this, statList);
        recyclerView.setAdapter(statAdapter);

        ImageButton shopScreen = findViewById(R.id.shopButton); //Redirect to Shop Screen
        ImageButton achievementScreen = findViewById(R.id.achievementButton); //Redirect to Achievement Screen
        ImageButton settingScreen = findViewById(R.id.settingButton); //Redirect to Setting Screen
        ImageButton backToClickerScreen = findViewById(R.id.exitButton); //Redirect user back to Clicker Screen

        shopScreen.setEnabled(false);
        achievementScreen.setEnabled(false);
        settingScreen.setEnabled(false);

        onExitButtonTapped(backToClickerScreen);
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

    public void setStatList(){
        statList.add(new Stat("Total Taps", totalTaps));
        statList.add(new Stat("Total Meows (All Time)", meowAllTime));
        statList.add(new Stat("Total Meows (Current)", meowWallet));
        statList.add(new Stat("Upgrades Bought", upgradeBought));
        statList.add(new Stat("Meows Per Tap", meowPerTap));
        statList.add(new Stat("Total Achievements", achievementComplete));
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