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

public class NumbersActivity extends AppCompatActivity {

    private MediaPlayer music;
    private AudioManager audioManager;

    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
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
                        releaseMediaPlayer();
                    }
                }
            };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        audioManager=(AudioManager)getSystemService(AUDIO_SERVICE);

        ArrayList<word> words = new ArrayList<word>();

        words.add(new word("lutti", "one", R.drawable.number_one, R.raw.number_one));
        words.add(new word("ottiko", "two", R.drawable.number_two, R.raw.number_two));
        words.add(new word("tolookosu", "three", R.drawable.number_three, R.raw.number_three));
        words.add(new word("oyyisa", "four", R.drawable.number_four, R.raw.number_four));
        words.add(new word("massokka", "five", R.drawable.number_five, R.raw.number_five));
        words.add(new word("temmokka", "six", R.drawable.number_six, R.raw.number_six));
        words.add(new word("kenekaku", "seven", R.drawable.number_seven, R.raw.number_seven));
        words.add(new word("kawinta", "eight", R.drawable.number_eight, R.raw.number_eight));
        words.add(new word("wo'e", "nine", R.drawable.number_nine, R.raw.number_nine));
        words.add(new word("na'accha", "ten", R.drawable.number_ten, R.raw.number_ten));

        //Implemented wordadapter as @list_item.xml is complex view
        wordadapter itemsAdapter = new wordadapter(this, R.layout.list_item, words, R.color.category_numbers);

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(itemsAdapter);

        /**************************
         * Adding mp3 part
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                releaseMediaPlayer();
                word currentword = words.get(i);
                int musicid=currentword.getMusicid();

                int result=audioManager.requestAudioFocus(onAudioFocusChangeListener,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if(result == audioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    music=MediaPlayer.create(NumbersActivity.this, musicid);
                    music.start();
                    music.setOnCompletionListener(mCompletionListener);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer()
    {
        // If the media player is not null, then it may be currently playing a sound.
        if (music != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            music.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            music = null;

            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }
}
