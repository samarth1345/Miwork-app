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

public class FamilyActivity extends AppCompatActivity {

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

        ArrayList<word> words=new ArrayList<word>();

        audioManager=(AudioManager)getSystemService(AUDIO_SERVICE);

        words.add(new word("әpә","father",R.drawable.family_father,R.raw.family_father));
        words.add(new word("әṭa","mother",R.drawable.family_mother,R.raw.family_mother));
        words.add(new word("angsi","son",R.drawable.family_son,R.raw.family_son));
        words.add(new word("tune","daughter",R.drawable.family_daughter,R.raw.family_daughter));
        words.add(new word("taachi","older brother",R.drawable.family_older_brother,R.raw.family_older_brother));
        words.add(new word("chalitti","younger brother",R.drawable.family_younger_brother,R.raw.family_younger_brother));
        words.add(new word("teṭe","older sister",R.drawable.family_older_sister,R.raw.family_older_sister));
        words.add(new word("kolliti","younger sister",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        words.add(new word("ama","grandmother ",R.drawable.family_grandmother,R.raw.family_grandmother));
        words.add(new word("paapa","grandfather",R.drawable.family_grandfather,R.raw.family_grandfather));

        wordadapter itemsAdapter = new wordadapter(this, R.layout.list_item, words,R.color.category_family);

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

                    music=MediaPlayer.create(FamilyActivity.this, musicid);
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
