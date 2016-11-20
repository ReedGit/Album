/*
 * Copyright 2016 Reed.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.reed.album;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author reed
 */
class AlbumActivity extends AppCompatActivity implements AlbumViewI {

    private static final int PERMISSION_CODE = 101;

    private RecyclerView folderRecycler;
    private AlbumAdapter albumAdapter;
    private FolderAdapter folderAdapter;
    private AlertDialog mAlertDialog;
    private AlbumPresenter albumPresenter;
    private Toolbar albumToolbar;
    private TextView folderTextView;
    private TextView completeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        initView();
        initClickListener();
        albumPresenter = new AlbumPresenter(this);
        int total = getIntent().getIntExtra(AlbumUtil.PICTURE_COUNT, 1);//最多可选的图片数，如果没有，默认是一张
        albumPresenter.setTotal(total);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAlertDialog == null || !mAlertDialog.isShowing()) {
            requestPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initData();
                } else {
                    mAlertDialog = new AlertDialog.Builder(AlbumActivity.this)
                            .setMessage("您拒绝了存储访问权限，需前往“设置”赋予权限")
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Uri selfPackageUri = Uri.parse("package:" + getPackageName());
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            selfPackageUri);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .create();
                    mAlertDialog.show();
                }
                break;
        }
    }

    private void initView() {
        albumToolbar = (Toolbar) findViewById(R.id.toolbar_album);
        setSupportActionBar(albumToolbar);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.fl_album_operation);
        if (AlbumUtil.toolbarColor != AlbumUtil.COLOR_DEFAULT){
            albumToolbar.setBackgroundColor(AlbumUtil.toolbarColor);
            linearLayout.setBackgroundColor(AlbumUtil.toolbarColor);
        }

        RecyclerView albumRecycler = (RecyclerView) findViewById(R.id.recycler_album);
        albumAdapter = new AlbumAdapter();
        albumRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        albumRecycler.setAdapter(albumAdapter);
        albumRecycler.addItemDecoration(new DividerGridItemDecoration(this));

        folderTextView = (TextView) findViewById(R.id.tv_album_folder);
        completeTextView = (TextView) findViewById(R.id.tv_album_complete);

        folderRecycler = (RecyclerView) findViewById(R.id.recycler_folder);
        folderAdapter = new FolderAdapter();
        folderRecycler.setLayoutManager(new LinearLayoutManager(this));
        folderRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        folderRecycler.setAdapter(folderAdapter);

        if (AlbumUtil.textColor != AlbumUtil.COLOR_DEFAULT){
            albumToolbar.setTitleTextColor(AlbumUtil.textColor);
            folderTextView.setTextColor(AlbumUtil.textColor);
            completeTextView.setTextColor(AlbumUtil.textColor);
        }
    }

    private void initClickListener() {
        albumToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        folderTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (folderRecycler.getVisibility() == View.VISIBLE) {
                    folderRecycler.setVisibility(View.GONE);
                } else {
                    folderRecycler.setVisibility(View.VISIBLE);
                }
            }
        });
        folderAdapter.setOnItemClickListener(new FolderAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                folderTextView.setText(albumPresenter.getImageFolders().get(position).getName());
                albumAdapter.setPaths(albumPresenter.getImageFolders().get(position).getPictures(), albumPresenter.getSelectedMap() == null ? null : albumPresenter.getSelectedMap().get(folderTextView.getText().toString()));
                albumAdapter.notifyDataSetChanged();
                folderRecycler.setVisibility(View.GONE);
            }
        });
        albumAdapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onCheckedChanged(AppCompatCheckBox checkBox, boolean b, int position) {
                String path = albumPresenter.getAlbumMap().get(folderTextView.getText().toString()).getPictures().get(position);
                if (b) {
                    boolean result = albumPresenter.setSelected(folderTextView.getText().toString(), path);
                    if (!result) {
                        Toast.makeText(AlbumActivity.this, "最多允许选择" + albumPresenter.getTotal() + "张图片", Toast.LENGTH_SHORT).show();
                        checkBox.setChecked(false);
                    } else {
                        String text = "完成" + albumPresenter.getCurrentState();
                        completeTextView.setText(text);
                    }
                } else {
                    albumPresenter.removeSelected(folderTextView.getText().toString(), path);
                    String text = "完成" + albumPresenter.getCurrentState();
                    completeTextView.setText(text);
                }
            }
        });
        completeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> resultList = new ArrayList<>();
                if (albumPresenter.getSelectedMap() != null) {
                    for (List<String> list : albumPresenter.getSelectedMap().values()) {
                        resultList.addAll(list);
                    }
                }
                Intent intent = new Intent();
                intent.putStringArrayListExtra(AlbumUtil.PICTURE_SELECTED, resultList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void requestPermission() {
        int hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                mAlertDialog = new AlertDialog.Builder(this)
                        .setMessage("我们需要存储权限用于访问图片")
                        .setPositiveButton("了解", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(AlbumActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        })
                        .create();
                mAlertDialog.show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
            }
        } else {
            initData();
        }
    }

    private void initData() {
        albumPresenter.setAlbum();
        List<ImageFolder> imageFolders = albumPresenter.getImageFolders();
        if (imageFolders == null || imageFolders.size() == 0) {
            folderTextView.setText("无图片");
        } else {
            folderTextView.setText(imageFolders.get(0).getName());
            folderAdapter.setImageFolders(imageFolders);
            folderAdapter.notifyDataSetChanged();
            albumAdapter.setPaths(imageFolders.get(0).getPictures(), albumPresenter.getSelectedMap() == null ? null : albumPresenter.getSelectedMap().get(folderTextView.getText().toString()));
            albumAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public Map<String, List<String>> getAlbum() {
        return null;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (folderRecycler.getVisibility() == View.VISIBLE) {
                folderRecycler.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
