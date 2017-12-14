package com.naymushin.videofonmv;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.naymushin.videofonmv.client.ClientActivity;
import com.naymushin.videofonmv.databinding.ActivityMainBinding;
import com.naymushin.videofonmv.server.ServerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.launchServerButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,
                ServerActivity.class)));
        binding.launchClientButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,
                ClientActivity.class)));
    }
}