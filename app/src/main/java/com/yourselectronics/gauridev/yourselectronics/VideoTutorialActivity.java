package com.yourselectronics.gauridev.yourselectronics;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoTutorialActivity extends YouTubeBaseActivity {

    YouTubePlayerView myouTubePlayerView;
    private final static String API_KEY= "AIzaSyDJ3I-0bXL-TTS5l-EBGOFzM_RkS7VPCO0";
    private YouTubePlayer.OnInitializedListener monInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_tutorial);
        myouTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayer);

        if (getIntent()!=null){
            String video_id = getIntent().getStringExtra("video_id");
            displayVideo(video_id);
        }



    }

    private void displayVideo(final String video_id) {

        monInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(video_id);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        myouTubePlayerView.initialize(API_KEY,monInitializedListener);

    }
}
