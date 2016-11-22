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

abstract class BaseActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 101;

    private AlertDialog mAlertDialog;
    private boolean isPermission = true;


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
                    if (isPermission) {
                        initData();
                        isPermission = false;
                    }
                } else {
                    mAlertDialog = new AlertDialog.Builder(this)
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

    private void requestPermission() {
        int hasPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            isPermission = true;
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                mAlertDialog = new AlertDialog.Builder(this)
                        .setMessage("我们需要存储权限用于访问图片")
                        .setPositiveButton("了解", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(BaseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
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
            if (isPermission) {
                initData();
                isPermission = false;
            }
        }
    }

    abstract protected void initData();
}
