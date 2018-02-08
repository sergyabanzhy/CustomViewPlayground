package com.example.sergzhy.cristmastree;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TreeView) findViewById(R.id.tree)).clearCanvas();
            }
        });

        findViewById(R.id.control_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TreeView) findViewById(R.id.tree)).lightingControl();
            }
        });
    }
}
