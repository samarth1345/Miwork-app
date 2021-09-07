/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    private MediaPlayer music;
    private AudioManager audioManager;
    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Releasemediaplayer();
        }
    };
    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener= new
            AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int i) {
                    if(i == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT || i==AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
                    {
                        music.pause();
                        music.seekTo(0);
                    }
                    else if(i == AudioManager.AUDIOFOCUS_GAIN)
                    {
                        music.start();
                    }
                    else if(i == AudioManager.AUDIOFOCUS_LOSS)
                    {
                        Releasemediaplayer();
                    }
                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        audioManager=(AudioManager)getSystemService(AUDIO_SERVICE);
        ArrayList<word> words=new ArrayList<word>();

        words.add(new word("weṭeṭṭi","Red",R.drawable.color_red,R.raw.color_red));
        words.add(new word("chokokki","green",R.drawable.color_green,R.raw.color_green));
        words.add(new word("ṭakaakki","brown",R.drawable.color_brown,R.raw.color_brown));
        words.add(new word("kululli","black",R.drawable.color_black,R.raw.color_black));
        words.add(new word("ṭopoppi","gray",R.drawable.color_gray,R.raw.color_gray));
        words.add(new word("kelelli","white",R.drawable.color_white,R.raw.color_white));
        words.add(new word("ṭopiisә","dusty yellow",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        words.add(new word("chiwiiṭә","mustard yellow",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));

        wordadapter itemsAdapter = new wordadapter(this, R.layout.list_item, words,R.color.category_colors);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);
        /**************************
         * Adding mp3 part
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                word currentword = words.get(i);
                int musicid=currentword.getMusicid();
                int result=audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == audioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    music=MediaPlayer.create(ColorsActivity.this, musicid);
                    music.start();
                    music.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        Releasemediaplayer();
    }

    private void Releasemediaplayer()
    {
        if(music != null)
        {
            music.release();

            music=null;

            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }
}
