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

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

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

        LinearLayout textcontainer = (LinearLayout) findViewById(R.id.text_container);


        ArrayList<word> words = new ArrayList<word>();

        words.add(new word("Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going));
        words.add(new word("What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        words.add(new word("My name is...", "oyaaset...", R.raw.phrase_my_name_is));
        words.add(new word("How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling));
        words.add(new word("I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good));
        words.add(new word("Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming));
        words.add(new word("Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        words.add(new word("I’m coming.", "әәnәm", R.raw.phrase_im_coming));
        words.add(new word("Let’s go.", "yoowutis", R.raw.phrase_lets_go));
        words.add(new word("Come here.", "әnni'nem", R.raw.phrase_come_here));

        wordadapter itemsAdapter = new wordadapter(this, R.layout.list_item, words, R.color.category_phrases);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);

        /**************************
         * Adding mp3 part
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Releasemediaplayer();
                word currentword = words.get(i);
                int musicid=currentword.getMusicid();
                int result=audioManager.requestAudioFocus(onAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == audioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    music=MediaPlayer.create(PhrasesActivity.this, musicid);
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
