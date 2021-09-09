package com.darren.goku;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.darren.goku.utils.StorageUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private File storage;
    private String[] storagePaths;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        //load data here
        storagePaths = StorageUtil.getStorageDirectories(this);

        for (String path : storagePaths) {
            storage = new File(path);
            LoadFile.load_Directory_Files(storage);
        }

        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
