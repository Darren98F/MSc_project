package com.darren.goku;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.UriPermission;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.darren.goku.Adapter.RecyclerViewAdapter;
import com.darren.goku.Interface.DialogListener;
import com.darren.goku.utils.StorageUtil;

import java.io.File;
import java.util.List;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class HomeActivity extends AppCompatActivity implements RecyclerViewAdapter.clickListener, com.darren.goku.BottomSheetDialog.bottomSheetListener, DialogListener {
    private final String TAG = this.getClass().getName();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Uri fileUri;
    private int videoPosition;
    private boolean permission;
    private File storage;
    private String[] storagePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Launch initialize in HomeActivity......");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        //In marshmallow and above we need to ask for permission first
        checkStorageAccessPermission();
        Log.d(TAG, "check permission......");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //if you face lack in scrolling then add following lines
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setNestedScrollingEnabled(false);

        recyclerViewAdapter = new RecyclerViewAdapter(this, this);

        recyclerView.setAdapter(recyclerViewAdapter);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //load data here
                //for first time data will be loaded here
                //then it will be loaded in splash screen
                //because if we could not have permission then we could not load data in splash screen window
                storagePaths = StorageUtil.getStorageDirectories(this);

                for (String path : storagePaths) {
                    storage = new File(path);
                    LoadFile.load_Directory_Files(storage);
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    private void checkStorageAccessPermission() {
        //ContextCompat use to retrieve resources. It provide uniform interface to access resources.
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Permission Needed")
                        .setMessage("This permission is needed to access media file in your phone")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(HomeActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        1);
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else {
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }


    //move to meActivity
    public void mePage(View view) {
        Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideUp(this);
        finish();
    }

    //move to searchActivity
    public void searchPage(View view) {
        Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideUp(this);
        finish();
    }


    //move to AddActivity
    public void addBtn(View view) {
        Intent intent = new Intent(HomeActivity.this, AddActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Animatoo.animateSlideUp(this);
        finish();
    }

    @Override
    public void deleteVideoFromList() {
        //        Toast.makeText(this, "Delete Video", Toast.LENGTH_SHORT).show();
        //show delete alert
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.show(getSupportFragmentManager(), deleteDialog.getTag());
    }

    @Override
    public void deleteFile() {
        //delete video logic
        Boolean result;
        File file = Constant.allMediaList.get(videoPosition);
        System.out.println("deketeFile clicked");
        System.out.println(file);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            System.out.println("Build.VERSION.SDK_INT >= Build.VERSION_CODES.M");

            List<UriPermission> permissions = getContentResolver().getPersistedUriPermissions();
            if(permissions != null && permissions.size() > 0){
                System.out.println("permissions != null");
                fileUri = permissions.get(0).getUri();
                deleteFileWithStorageAccessFramework(file);
            }else {
                System.out.println("permission == null");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Please select external storage directory (e.g. SDCard")
                        .setMessage("Due to change in Android security policy it is not possible to delete video from sdcard without permission")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                                startActivityForResult(intent, 1);
                            }
                        }).show();
            }
        }else {
            result = file.delete();
            if(result){
                Constant.allMediaList.remove(videoPosition);
            }else {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 1:
                if (resultCode == Activity.RESULT_OK){
                    fileUri  = data.getData();
                    getContentResolver().takePersistableUriPermission(fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
        }
    }
    private void deleteFileWithStorageAccessFramework(File selectedFile) {
        System.out.println("deleteFileWithStorageAccessFramework");
        DocumentFile documentFile = DocumentFile.fromTreeUri(this, fileUri);
        String[] parts = (selectedFile.getPath()).split("\\/");

        System.out.println(selectedFile.getPath());
        for( int i=3; i<parts.length; i++){
            if(documentFile != null){
                System.out.println("here is hot null");
                documentFile = documentFile.findFile(parts[i]);
                System.out.println(documentFile);
            }
        }
        if(documentFile == null){
            //just delete from list
            Constant.allMediaList.remove(videoPosition);
            recyclerViewAdapter.notifyItemRemoved(videoPosition);
            Toast.makeText(this, "Delete Succeed", Toast.LENGTH_SHORT).show();
        }else {
            //file found
            if(documentFile.delete()){
                //delete successfully
                Constant.allMediaList.remove(videoPosition);
                recyclerViewAdapter.notifyItemRemoved(videoPosition);
                Toast.makeText(this, "Delete Succeed", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Delete Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onIconMoreClick(int position) {
        //from arraylist this position file is selected
        videoPosition = position;
        com.darren.goku.BottomSheetDialog bottomSheetDialog = new com.darren.goku.BottomSheetDialog();
        bottomSheetDialog.setVideoPosition(position);
        bottomSheetDialog.show(getSupportFragmentManager(),bottomSheetDialog.getTag());
    }
}