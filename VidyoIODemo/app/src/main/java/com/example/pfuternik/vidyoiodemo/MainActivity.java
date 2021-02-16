package com.example.pfuternik.vidyoiodemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vidyo.VidyoClient.Connector.Connector;
import com.vidyo.VidyoClient.Connector.ConnectorPkg;

public class MainActivity extends AppCompatActivity implements Connector.IConnect {

    private Connector vc;
    private FrameLayout videoFrame;
    private static final int CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectorPkg.setApplicationUIContext(this);
        ConnectorPkg.initialize();
        videoFrame = (FrameLayout)findViewById(R.id.videoFrame);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return;
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA},
                    MainActivity.CAMERA_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(MainActivity.this,
                        R.string.permissions,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void Start(View v) {
        checkPermission();
        vc = new Connector(videoFrame, Connector.ConnectorViewStyle.VIDYO_CONNECTORVIEWSTYLE_Default, 15, "warning info@VidyoClient info@VidyoConnector", "", 0);
        vc.showViewAt(videoFrame, 0, 0, videoFrame.getWidth(), videoFrame.getHeight());
    }

    public void Connect(View v) {
        String token = "InsertValidTokenHere";
        vc.connect("prod.vidyo.io", token, "DemoUser", "DemoRoom", this);
    }

    public void Disconnect(View v) {
        vc.disconnect();
    }

    public void onSuccess() {}

    public void onFailure(Connector.ConnectorFailReason reason) {}

    public void onDisconnected(Connector.ConnectorDisconnectReason reason) {}
}
