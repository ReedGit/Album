package me.reed.album;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumPresenter {

    private AlbumViewI mView;
    private AlbumModel mModel;

    public AlbumPresenter(AlbumViewI view) {
        mView = view;
        mModel = new AlbumModel();
    }

    public void setAlbum() {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = mView.getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor == null || cursor.getCount() <= 0) {
            return;
        }
        Map<String, ImageFolder> albumMap = new HashMap<>();
        while (cursor.moveToNext()) {
            int index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursor.getString(index);
            File file = new File(path);
            if (file.exists()) {
                String name = file.getParentFile().getName();
                if (albumMap.containsKey(name)) {
                    albumMap.get(name).getPictures().add(path);
                } else {
                    ImageFolder imageFolder = new ImageFolder();
                    imageFolder.setName(name);
                    imageFolder.setCover(path);
                    List<String> list = new ArrayList<>();
                    list.add(path);
                    imageFolder.setPictures(list);
                    albumMap.put(name, imageFolder);
                }
            }
        }
        cursor.close();
        mModel.setAlbumMap(albumMap);
    }

    public List<ImageFolder> getImageFolders() {
        return mModel.getImageFolders();
    }

    public void setTotal(int total) {
        mModel.setTotal(total);
    }

    public Map<String, ImageFolder> getAlbumMap() {
        return mModel.getAlbumMap();
    }

    public int getTotal() {
        return mModel.getTotal();
    }

    public boolean setSelected(String folder, String path) {
        if (mModel.getSelectedMap() == null) {
            Map<String, List<String>> selectedMap = new HashMap<>();
            List<String> selectedPictures = new ArrayList<>();
            selectedPictures.add(path);
            selectedMap.put(folder, selectedPictures);
            mModel.setSelectedMap(selectedMap);
        } else {
            if (mModel.getSelectedCount() < mModel.getTotal()) {
                if (mModel.getSelectedMap().containsKey(folder)) {
                    mModel.getSelectedMap().get(folder).add(path);
                } else {
                    List<String> selectedPictures = new ArrayList<>();
                    selectedPictures.add(path);
                    mModel.getSelectedMap().put(folder, selectedPictures);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public void removeSelected(String folder, String path) {
        if (mModel.getSelectedMap() != null) {
            mModel.getSelectedMap().get(folder).remove(path);
        }
    }

    public Map<String, List<String>> getSelectedMap() {
        return mModel.getSelectedMap();
    }

    public String getCurrentState() {
        return "(" + mModel.getSelectedCount() + "/" + mModel.getTotal() + ")";
    }
}
