package com.meowser.meowtapper;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static boolean backgroundSound;
    public static boolean soundEffect;

    private ArrayList<ItemShop> itemList;
    private ArrayList<Achievement> achievementList;

    private double totalTaps, achievementComplete, upgradeBought, meowAllTime; //Variables for Stat
    private double meowWallet, meowPerSec, meowPerTap = 1;

    Dialog exitDialog; //Dialog popup when user clicks back button on phone

    private TextView meowCurrent, meowGenerate;

    ActivityResultLauncher<Intent> activityLauncher = registerForActivityResult( //Method to catch Meows generated from other activities
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 420){
                        Intent intent = result.getData();

                        if (intent != null){
                            meowWallet = intent.getDoubleExtra("key_wallet", 0);
                            meowPerSec = intent.getDoubleExtra("key_mps", 0);
                            meowPerTap = intent.getDoubleExtra("key_mpt", 1);
                            totalTaps = intent.getDoubleExtra("key_total_tap", totalTaps);
                            achievementComplete = intent.getDoubleExtra("key_achievementComplete", achievementComplete);
                            upgradeBought = intent.getDoubleExtra("key_upgradeBought", upgradeBought);
                            meowAllTime = intent.getDoubleExtra("key_meowAllTime", meowAllTime);

                            meowCurrent.setText(getString(R.string.update_meows, meowWallet));
                            meowGenerate.setText(getString(R.string.update_mps, meowPerSec));

                            itemList = intent.getExtras().getParcelableArrayList("Shop_Array");
                            achievementList = intent.getExtras().getParcelableArrayList(("Achievement_Array"));
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        load();

        BackgroundMusic.SoundPlayer(this, R.raw.background);

        meowCurrent = findViewById(R.id.displayMeow); //Display current currency gained from meowWallet
        meowGenerate = findViewById(R.id.displayMPS); //Display Meows being generated per second

        exitDialog = new Dialog(this);

        ImageButton catTapper = findViewById(R.id.catTapper); //The main button to click and gain Meows
        ImageButton shopScreen = findViewById(R.id.shopButton); //Redirect to Shop Screen
        ImageButton achievementScreen = findViewById(R.id.achievementButton); //Redirect to Achievement Screen
        ImageButton statScreen = findViewById(R.id.statButton); //Redirect to Stat Screen
        ImageButton settingScreen = findViewById(R.id.settingButton); //Redirect to Setting Screen

        if (!backgroundSound) { //Check status upon opening Application
            BackgroundMusic.backgroundMusic.pause();
        }

        checkArrays(); //Initialize Arrays for Shop and Achievement
        checkAchievements(); //Check if user completed an achievement on all Activities

        onCatTapperTapped(catTapper);
        meowCounter();

        onShopButtonTapped(shopScreen);
        onAchievementButtonTapped(achievementScreen);
        onStatButtonTapped(statScreen);
        onSettingButtonTapped(settingScreen);

    }

    public void load() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", 0);
        if (sharedPreferences != null) {
            Gson gson = new Gson();
            String itemshop = sharedPreferences.getString("itemShopList", null);
            String achievement = sharedPreferences.getString("achievementList", null);
            Type type1 = new TypeToken<ArrayList<ItemShop>>() {}.getType();
            Type type2 = new TypeToken<ArrayList<Achievement>>() {}.getType();
            itemList = gson.fromJson(itemshop, type1);
            achievementList = gson.fromJson(achievement, type2);
            if (itemList == null){
                itemList = new ArrayList<>();
            }

            if (achievementList == null){
                achievementList = new ArrayList<>();
            }
            meowWallet = getDouble(sharedPreferences, "meowWallet", 0.0);
            meowPerSec = getDouble(sharedPreferences, "meowPerSec", 0);
            meowPerTap = getDouble(sharedPreferences, "meowPerTap", 1);
            meowAllTime = getDouble(sharedPreferences, "meowAllTime", 0.0);
            upgradeBought = getDouble(sharedPreferences, "upgradeBought", 0.0);
            totalTaps = getDouble(sharedPreferences, "totalTaps", 0.0);
            achievementComplete = getDouble(sharedPreferences, "achievementComplete", 0.0);
            backgroundSound = sharedPreferences.getBoolean("backgroundMusic", true);
            soundEffect = sharedPreferences.getBoolean("soundEffect", true);
        }
    }

    double getDouble(final SharedPreferences prefs, final String key, final double v) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(v)));
    }

    public void onBackPressed() {
        if (soundEffect){
            final MediaPlayer exitSound = MediaPlayer.create(this, R.raw.exit_dialogue);
            exitSound.start();
        }
        exitDialog.setContentView(R.layout.custom_exit_dialog);
        exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ImageButton confirmExit = exitDialog.findViewById(R.id.confirmExit);
        ImageButton cancelExit = exitDialog.findViewById(R.id.cancelExit);
        exitDialog.show();

        confirmExit.setOnClickListener(view ->{
            if (soundEffect){
                final MediaPlayer exitSound = MediaPlayer.create(this, R.raw.exit_dialogue);
                exitSound.start();
            }
            BackgroundMusic.backgroundMusic.stop();
            finish();
            System.exit(0);
        });

        cancelExit.setOnClickListener(view ->{
            if (soundEffect){
                final MediaPlayer exitSound = MediaPlayer.create(this, R.raw.exit_dialogue);
                exitSound.start();
            }
            exitDialog.dismiss();
        });
    }

    public void checkArrays(){
        if (itemList.isEmpty()){
            setItemList();
        }

        if (achievementList.isEmpty()){
            setAchievementList();
        }
    }

    public void checkAchievements(){
        Timer timer = new Timer();
        final MediaPlayer achievementSound = MediaPlayer.create(this, R.raw.achievement_unlock);
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                runOnUiThread(() -> {
                    for( int index = 0; index < 10; index++ ){
                        if ( index == 0 && totalTaps >= 50 && !achievementList.get(index).isAchievementComplete()) {
                            if (soundEffect){
                                achievementSound.start();
                            }
                            achievementList.get(index).setAchievementComplete(true);
                            achievementList.get(index).setAchievementImageLeft(R.drawable.achievement01_complete);
                            achievementList.get(index).setAchievementImageRight(R.drawable.achievement01_complete);
                            Toast.makeText(getBaseContext(), "Achieved Casual Taps!", Toast.LENGTH_SHORT).show();
                            achievementComplete++;
                        }

                        if ( index == 1 && totalTaps >= 500 && !achievementList.get(index).isAchievementComplete()) {
                            if (soundEffect){
                                achievementSound.start();
                            }
                            achievementList.get(index).setAchievementComplete(true);
                            achievementList.get(index).setAchievementImageLeft(R.drawable.achievement01_complete);
                            achievementList.get(index).setAchievementImageRight(R.drawable.achievement01_complete);
                            Toast.makeText(getBaseContext(), "Achieved Tappy Tap Tap!", Toast.LENGTH_SHORT).show();
                            achievementComplete++;
                        }

                        if ( index == 2 && totalTaps >= 5000 && !achievementList.get(index).isAchievementComplete()) {
                            if (soundEffect){
                                achievementSound.start();
                            }
                            achievementList.get(index).setAchievementComplete(true);
                            achievementList.get(index).setAchievementImageLeft(R.drawable.achievement01_complete);
                            achievementList.get(index).setAchievementImageRight(R.drawable.achievement01_complete);
                            Toast.makeText(getBaseContext(), "Achieved Tap Tap Meowser!", Toast.LENGTH_SHORT).show();
                            achievementComplete++;
                        }

                        if ( index == 3 && meowAllTime >= 1000 && !achievementList.get(index).isAchievementComplete()) {
                            if (soundEffect){
                                achievementSound.start();
                            }
                            achievementList.get(index).setAchievementComplete(true);
                            achievementList.get(index).setAchievementImageLeft(R.drawable.achievement02_complete);
                            achievementList.get(index).setAchievementImageRight(R.drawable.achievement02_complete);
                            Toast.makeText(getBaseContext(), "Achieved Make some noise!", Toast.LENGTH_SHORT).show();
                            achievementComplete++;
                        }

                        if ( index == 4 && meowAllTime >= 10000 && !achievementList.get(index).isAchievementComplete()) {
                            if (soundEffect){
                                achievementSound.start();
                            }
                            achievementList.get(index).setAchievementComplete(true);
                            achievementList.get(index).setAchievementImageLeft(R.drawable.achievement02_complete);
                            achievementList.get(index).setAchievementImageRight(R.drawable.achievement02_complete);
                            Toast.makeText(getBaseContext(), "Achieved Purr noises intensifies!", Toast.LENGTH_SHORT).show();
                            achievementComplete++;
                        }

                        if ( index == 5 && meowAllTime >= 30000 && !achievementList.get(index).isAchievementComplete()) {
                            if (soundEffect){
                                achievementSound.start();
                            }
                            achievementList.get(index).setAchievementComplete(true);
                            achievementList.get(index).setAchievementImageLeft(R.drawable.achievement02_complete);
                            achievementList.get(index).setAchievementImageRight(R.drawable.achievement02_complete);
                            Toast.makeText(getBaseContext(), "Achieved It's a Meow Concert!", Toast.LENGTH_SHORT).show();
                            achievementComplete++;
                        }

                        if ( index == 6 && upgradeBought >= 10 && !achievementList.get(index).isAchievementComplete()) {
                            if (soundEffect){
                                achievementSound.start();
                            }
                            achievementList.get(index).setAchievementComplete(true);
                            achievementList.get(index).setAchievementImageLeft(R.drawable.achievement03_complete);
                            achievementList.get(index).setAchievementImageRight(R.drawable.achievement03_complete);
                            Toast.makeText(getBaseContext(), "Achieved Meow The Builder!", Toast.LENGTH_SHORT).show();
                            achievementComplete++;
                        }

                        if ( index == 7 && upgradeBought >= 25 && !achievementList.get(index).isAchievementComplete()) {
                            if (soundEffect){
                                achievementSound.start();
                            }
                            achievementList.get(index).setAchievementComplete(true);
                            achievementList.get(index).setAchievementImageLeft(R.drawable.achievement03_complete);
                            achievementList.get(index).setAchievementImageRight(R.drawable.achievement03_complete);
                            Toast.makeText(getBaseContext(), "Achieved Construction Meower!", Toast.LENGTH_SHORT).show();
                            achievementComplete++;
                        }

                        if ( index == 8 && upgradeBought >= 50 && !achievementList.get(index).isAchievementComplete()) {
                            if (soundEffect){
                                achievementSound.start();
                            }
                            achievementList.get(index).setAchievementComplete(true);
                            achievementList.get(index).setAchievementImageLeft(R.drawable.achievement03_complete);
                            achievementList.get(index).setAchievementImageRight(R.drawable.achievement03_complete);
                            Toast.makeText(getBaseContext(), "Achieved Meowsers, Assemble!", Toast.LENGTH_SHORT).show();
                            achievementComplete++;
                        }

                        if ( index == 9 && achievementComplete == 9 && !achievementList.get(index).isAchievementComplete()) {
                            if (soundEffect){
                                achievementSound.start();
                            }
                            achievementList.get(index).setAchievementComplete(true);
                            achievementList.get(index).setAchievementImageLeft(R.drawable.achievement04_complete);
                            achievementList.get(index).setAchievementImageRight(R.drawable.achievement04_complete);
                            Toast.makeText(getBaseContext(), "Achieved Meowtastic, Meowser!", Toast.LENGTH_SHORT).show();
                            achievementComplete++;
                        }
                    }
                });
            }
        }, 0, 1000);
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

    private void onCatTapperTapped(ImageButton catTapper) {
        catTapper.setOnClickListener(view -> {
            catTapper.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.cat_tapper_clicked));
            meowWallet+=meowPerTap;
            meowAllTime++;
            totalTaps++;
            if (soundEffect){
                final MediaPlayer catTapperSound = MediaPlayer.create(this, R.raw.cat_tapper_tap);
                catTapperSound.start();
            }
            meowCurrent.setText(getString(R.string.update_meows, meowWallet));
        });
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
        meowCurrent.setText(getString(R.string.update_meows, meowWallet));
        meowGenerate.setText(getString(R.string.update_mps, meowPerSec));
    }

    private void onShopButtonTapped(ImageButton shopScreen){
        shopScreen.setOnClickListener(view -> {
            Intent intent = new Intent(this, ShopActivity.class);
            intent.putExtra("key_wallet", meowWallet);
            intent.putExtra("key_mps", meowPerSec);
            intent.putExtra("key_mpt", meowPerTap);
            intent.putExtra("key_total_tap", totalTaps);
            intent.putExtra("key_meowAllTime", meowAllTime);
            intent.putExtra("key_achievementComplete", achievementComplete);
            intent.putExtra("key_upgradeBought", upgradeBought);

            if (soundEffect){
                final MediaPlayer activityMoveSound = MediaPlayer.create(this, R.raw.activity_move);
                activityMoveSound.start();
            }

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Shop_Array", itemList);
            bundle.putParcelableArrayList("Achievement_Array", achievementList);
            intent.putExtras(bundle);
            activityLauncher.launch(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void onAchievementButtonTapped(ImageButton achievementScreen){
        achievementScreen.setOnClickListener(view -> {
            Intent intent = new Intent(this,AchievementActivity.class);
            intent.putExtra("key_wallet", meowWallet);
            intent.putExtra("key_mps", meowPerSec);
            intent.putExtra("key_mpt", meowPerTap);
            intent.putExtra("key_total_tap", totalTaps);
            intent.putExtra("key_meowAllTime", meowAllTime);
            intent.putExtra("key_achievementComplete", achievementComplete);
            intent.putExtra("key_upgradeBought", upgradeBought);

            if (soundEffect){
                final MediaPlayer activityMoveSound = MediaPlayer.create(this, R.raw.activity_move);
                activityMoveSound.start();
            }

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Shop_Array", itemList);
            bundle.putParcelableArrayList("Achievement_Array", achievementList);
            intent.putExtras(bundle);
            activityLauncher.launch(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void onStatButtonTapped(ImageButton statScreen){
        statScreen.setOnClickListener(view -> {
            Intent intent = new Intent(this, StatActivity.class);
            intent.putExtra("key_wallet", meowWallet);
            intent.putExtra("key_mps", meowPerSec);
            intent.putExtra("key_mpt", meowPerTap);
            intent.putExtra("key_total_tap", totalTaps);
            intent.putExtra("key_meowAllTime", meowAllTime);
            intent.putExtra("key_achievementComplete", achievementComplete);
            intent.putExtra("key_upgradeBought", upgradeBought);

            if (soundEffect){
                final MediaPlayer activityMoveSound = MediaPlayer.create(this, R.raw.activity_move);
                activityMoveSound.start();
            }

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Shop_Array", itemList);
            bundle.putParcelableArrayList("Achievement_Array", achievementList);
            intent.putExtras(bundle);
            activityLauncher.launch(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void onSettingButtonTapped(ImageButton settingScreen){
        settingScreen.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingActivity.class);
            intent.putExtra("key_wallet", meowWallet);
            intent.putExtra("key_mps", meowPerSec);
            intent.putExtra("key_mpt", meowPerTap);
            intent.putExtra("key_total_tap", totalTaps);
            intent.putExtra("key_meowAllTime", meowAllTime);
            intent.putExtra("key_achievementComplete", achievementComplete);
            intent.putExtra("key_upgradeBought", upgradeBought);

            if (soundEffect){
                final MediaPlayer activityMoveSound = MediaPlayer.create(this, R.raw.activity_move);
                activityMoveSound.start();
            }

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("Shop_Array", itemList);
            bundle.putParcelableArrayList("Achievement_Array", achievementList);
            intent.putExtras(bundle);
            activityLauncher.launch(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }
}

