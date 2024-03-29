package com.example.geoapp2020.ui.media;

import android.os.Bundle;
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


public class MediaFragment extends Fragment implements SurfaceHolder.Callback {

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


        // BackVideo Clickable to play it
        videoView = (VideoView) root.findViewById(R.id.video_view);
        prepareVideo(VIDEO_DATEI);

        videoView1 = (VideoView) root.findViewById(R.id.video_view1);
        prepareVideo(VIDEO_DATEI_1);



        return root;
    }

    /**
     * Prepare video-files - get videoPaths, get First Frame to show in VideoView and ads the MediaController
     *
     * @param videoName
     */
    private void prepareVideo(String videoName){
        if (videoName == "video_1.mp4"){
            //videoView.setVisibility(View.VISIBLE);
            videoView.setVideoPath("android.resource://" + getContext().getPackageName() + "/" + R.raw.video_1);

            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.seekTo(1);

        }else if(videoName == "video_2.mp4"){
            //videoView1.setVisibility(View.VISIBLE);
            videoView1.setVideoPath("android.resource://" + getContext().getPackageName() + "/" + R.raw.video_2);

            MediaController mediaController = new MediaController(getContext());
            mediaController.setAnchorView(videoView1);
            videoView1.setMediaController(mediaController);
            videoView1.seekTo(1);
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

}
