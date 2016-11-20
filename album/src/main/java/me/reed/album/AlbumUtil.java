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

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * the util of how to use the library
 * Created by reed on 2016/11/6.
 */
public class AlbumUtil {

    static final String PICTURE_COUNT = "picture_count";
    static final String PICTURE_SELECTED = "picture_selected";

    private volatile static AlbumUtil instance;


    public interface AlbumCallback{
        void onResult(List<String> paths);
    }

    private AlbumCallback callback;

    public static AlbumUtil getInstance() {
        if (instance == null) {
            synchronized (AlbumUtil.class) {
                if (instance == null) {
                    instance = new AlbumUtil();
                }
            }
        }
        return instance;
    }


    public void openAlbum(Context context, AlbumCallback callback) {
        openAlbum(context, 1, callback);
    }

    public void openAlbum(Context context, int max, AlbumCallback callback) {
        if (context == null || callback == null) {
            throw new NullPointerException("Context or AlbumCallback cannot be null");
        }
        if (max < 1) {
            throw new IllegalArgumentException("The maximum number of pictures to choose is at least 1");
        }
        this.callback = callback;
        Intent intent = new Intent(context, EmptyActivity.class);
        intent.putExtra(PICTURE_COUNT, max);
        context.startActivity(intent);
    }

    void onResult(List<String> paths) {
        if (callback == null){
            throw new NullPointerException("AlbumCallback cannot be null");
        }
        if (paths == null){
            callback.onResult(new ArrayList<String>());
        } else {
            callback.onResult(paths);
        }

    }

}
