package com.example.geoapp2020.ui.webview;
/**
 * WebView model to display the Homepage of all University projects 2020 on the server
 *
 */

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.geoapp2020.R;

public class WebViewFragment extends Fragment {

    private WebViewModel webViewModel;
    private WebView webView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        webViewModel = ViewModelProviders.of(this).get(WebViewModel.class);
        View root = inflater.inflate(R.layout.fragment_webview, container, false);
        // Header for the Webview

    // Webview
        // Load the Webview for the webview
        webView = root.findViewById(R.id.webview);
        webView.loadUrl("http://fk08srv01-2.geo.private.hm.edu/~teske/");

        // Activate the Javascrip
        // source: https://stackoverflow.com/questions/29584597/android-studio-how-to-set-a-webview-loading-and-error-view
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());


        /**
         * Activate can go back with Back-button press on the device
         * source: https://stackoverflow.com/questions/10631425/how-to-add-go-back-function-in-webview-inside-fragment
         */
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return false;
            }
        });


        return root;
    }
}




