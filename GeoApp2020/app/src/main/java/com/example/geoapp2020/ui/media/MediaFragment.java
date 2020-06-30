package com.example.geoapp2020.ui.media;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.geoapp2020.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MediaFragment extends Fragment implements View.OnClickListener, SurfaceHolder.Callback {

    private MediaViewModel mediaViewModel;

    private VideoView videoView;
    private VideoView videoView1;
    private final String VIDEO_DATEI = "video_1.mp4";
    private final String VIDEO_DATEI_1 = "video_2.mp4";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mediaViewModel =
                ViewModelProviders.of(this).get(MediaViewModel.class);

        View root = inflater.inflate(R.layout.fragment_media, container, false);

        final TextView textView = root.findViewById(R.id.text_media);
        mediaViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // BackVideo Clickable to play it
        videoView = (VideoView) root.findViewById(R.id.video_view);
        videoView.setOnClickListener(this);
        videoView1 = (VideoView) root.findViewById(R.id.video_view1);
        videoView1.setOnClickListener(this);



        return root;
    }


    // Function to play the video
    private void playVideo(String videoName) {
        if (videoName == "video_1.mp4"){
            videoView = (VideoView) getActivity().findViewById(R.id.video_view);
            //videoView.setVisibility(View.VISIBLE);
            videoView.setVideoPath("android.resource://" + getContext().getPackageName() + "/" + R.raw.video_1);

            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            if(videoView.isPlaying()) {
                videoView1.stopPlayback();
            }

            videoView.start();
            videoView.requestFocus();

        }else if(videoName == "video_2.mp4"){
            videoView1 = (VideoView) getActivity().findViewById(R.id.video_view1);
            //videoView.setVisibility(View.VISIBLE);
            videoView1.setVideoPath("android.resource://" + getContext().getPackageName() + "/" + R.raw.video_2);

            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(videoView1);
            videoView1.setMediaController(mediaController);

            if(videoView1.isPlaying()) {
                videoView.stopPlayback();
            }

            videoView.start();
            videoView.requestFocus();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("carpelibrum", " camera surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("carpelibrum", " camera surface changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("carpelibrum", " camera surface destroyed");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.video_view: playVideo(VIDEO_DATEI);
                break;
            case R.id.video_view1: playVideo(VIDEO_DATEI_1);
        }
    }
}
