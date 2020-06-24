package com.example.geoapp2020.ui.webview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.geoapp2020.R;

public class WebViewFragment extends Fragment {

    private WebViewModel webViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        webViewModel = ViewModelProviders.of(this).get(WebViewModel.class);
        View root = inflater.inflate(R.layout.fragment_webview, container, false);
        // Header for the Webview
        final TextView textView = root.findViewById(R.id.text_webview);
        webViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
        public void onChanged(@Nullable String s) {
            textView.setText(s);
        }
    });
    // Load the Webview for the webview
    final WebView webView = root.findViewById(R.id.webview);
        webView.loadUrl("http://fk08srv01-2.geo.private.hm.edu/~teske/");
        return root;
    }
}



