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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

class EmptyActivity extends Activity {

    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int max = getIntent().getIntExtra(AlbumUtil.PICTURE_COUNT, 1);
        Intent intent = new Intent(EmptyActivity.this, AlbumActivity.class);
        intent.putExtra(AlbumUtil.PICTURE_COUNT, max);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    List<String> paths = data.getStringArrayListExtra(AlbumUtil.PICTURE_SELECTED);
                    AlbumUtil.getInstance().onResult(paths);
                }
                break;
        }
        finish();
    }
}
