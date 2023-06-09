package com.meowser.meowtapper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.widget.ImageButton;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ShopActivity extends AppCompatActivity implements ItemShopAdapter.onBuyListener {

    RecyclerView recyclerView;
    ItemShopAdapter shopAdapter;

    private ArrayList<ItemShop> itemList;
    private ArrayList<Achievement> achievementList;

    private double totalTaps, achievementComplete, upgradeBought, meowAllTime; //Variables for Stat
    private double meowWallet, meowPerSec, meowPerTap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

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

        recyclerView = findViewById(R.id.itemShopList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shopAdapter = new ItemShopAdapter(this, itemList, this);
        recyclerView.setAdapter(shopAdapter);

        ImageButton backToClickerScreen = findViewById(R.id.exitButton); //Redirect user back to Clicker Screen
        ImageButton achievementScreen = findViewById(R.id.achievementButton); //Redirect to Achievement Screen
        ImageButton statScreen = findViewById(R.id.statButton); //Redirect to Stat Screen
        ImageButton settingScreen = findViewById(R.id.settingButton); //Redirect to Setting Screen

        achievementScreen.setEnabled(false);
        statScreen.setEnabled(false);
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

    @SuppressLint("DefaultLocale")
    @Override
    public void onBuyClick(ItemShopAdapter.CardViewHolder holder, int position) {
        final MediaPlayer buySuccessSound = MediaPlayer.create(this, R.raw.buy_upgrade);
        final MediaPlayer buyFailSound = MediaPlayer.create(this, R.raw.buy_upgrade_fail);

        if (meowWallet >= getItemList().get(position).getItemPrice()) {
            if (MainActivity.soundEffect){
                buySuccessSound.start();
            }
            meowWallet -= getItemList().get(position).getItemPrice();
            meowPerSec += getItemList().get(position).getItemMeowPerSec();
            meowPerTap += getItemList().get(position).getItemMeowPerTap();

            int i = getItemList().get(position).getItemNumberBought() + 1;
            getItemList().get(position).setItemNumberBought(i);
            holder.ItemNumBought.setText(String.format("%d", getItemList().get(position).getItemNumberBought()));

            double d1 = getItemList().get(position).getItemPrice();
            double d2 = getItemList().get(position).getNewItemPrice(d1);
            getItemList().get(position).setItemPrice(d2);
            holder.ItemPrice.setText(String.format("Meows: %s",
                    NumberFormat.getInstance().format(getItemList().get(position).getItemPrice())));
            upgradeBought++;
        } else {
            if (MainActivity.soundEffect){
                buyFailSound.start();
            }
            Toast.makeText(ShopActivity.this, "Currently have insufficient Meows.", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<ItemShop> getItemList() {
        return itemList;
    }
}