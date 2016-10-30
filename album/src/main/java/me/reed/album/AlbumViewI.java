package me.reed.album;


import android.app.Activity;

import java.util.List;
import java.util.Map;

public interface AlbumViewI {

    Map<String, List<String>> getAlbum();
    Activity getActivity();


}
