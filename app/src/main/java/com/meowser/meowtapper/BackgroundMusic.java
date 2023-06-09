package com.meowser.meowtapper;

import android.content.Context;
import android.media.MediaPlayer;

public class BackgroundMusic {
    public static MediaPlayer backgroundMusic;
        public static void SoundPlayer(Context context, int raw_id){
            backgroundMusic = MediaPlayer.create(context, raw_id);
            backgroundMusic.setLooping(true);

            backgroundMusic.start();
        }
}
