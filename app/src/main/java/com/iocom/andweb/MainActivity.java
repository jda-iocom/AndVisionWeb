package com.iocom.andweb;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private WebView wv;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a webview to embed VisiWebRTC client into
        wv = new WebView(this);

        // Comment out this next line if you don't want the HTML page to display, and just
        // control it via the webrtc object in the web page. You will, however,
        // need to display the video in an embedded web page somehow.
        setContentView(wv);

        // A bunch of useful settings for our webview
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setSupportMultipleWindows(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setDatabaseEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.getSettings().setGeolocationEnabled(true);
        wv.getSettings().setLoadsImagesAutomatically(true);
        wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        // JavaScript can get at methods decorated with @JavascriptInterface via an object called "plugin"
        wv.addJavascriptInterface(this, "plugin");

        // use "chrome://inspect/#devices" in Chrome and click on the WebView to open a console debugger
        wv.setWebContentsDebuggingEnabled(true);

        // Need to accept cookies for the PHPSESSID
        CookieManager.getInstance().setAcceptThirdPartyCookies(wv, true);

        wv.setWebChromeClient(new WebChromeClient() {
            // Grant permission to access the getUsermedia (camera & microphone) call
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());
                }
            }

        });

        // Load our embedded sample HTML file
        wv.loadUrl("file:///android_asset/visionable_webrtc.html");

        final Button button = (Button) findViewById(R.id.button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // You can call into the JavaScript from the native app too!
                    wv.evaluateJavascript("joinMeeting()", null);
                }
            });
        }

        Log.d("andweb", "done with OnCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        wv.loadUrl("[Insert URL Here]");
    }

    // These are the methods accessible to the JavaScript in the embedded WebView. Above, we
    // share the object name as "plugin". So, to ask the native app for the username, the
    // JavaScript calls "plugin.GetUsername()"
    //
    // The methods below are used by the embedded test HTML file to pre-populate the user fields, but of course
    // can be used however you want

    @JavascriptInterface
    public String GetUsername() {

        // This is the user (ie, email) to use for auth
        return "user@visionable.com";
    }

    @JavascriptInterface
    public String GetPassword() {

        // User's password
        return "visionable123";
    }

    @JavascriptInterface
    public String GetName() {

        // Name that will show up in the meeting
        return "User Q. Android";
    }

    @JavascriptInterface
    public String GetHostname() {

        // Server name where user and meeting are located. You must have API access to this
        // site as well
        return "cloud.visionable.com";
    }

    @JavascriptInterface
    public String GetMeetingId() {

        // Meeting id to join
        return "84efddd92e-dd7d-4d97-bffd-96a5ad228555";
    }
}
