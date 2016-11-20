package me.example.album;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import me.reed.album.AlbumUtil;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumUtil.getInstance().openAlbum(MainActivity.this,6, new AlbumUtil.AlbumCallback() {
                    @Override
                    public void onResult(List<String> paths) {
                        for (String path: paths) {
                            Log.i("TAG", path);
                        }
                    }
                });
            }
        });


    }
}
