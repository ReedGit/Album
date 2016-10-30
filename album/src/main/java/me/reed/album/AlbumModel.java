package me.reed.album;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlbumModel {

    private Map<String, ImageFolder> albumMap;//图库信息

    private int total;//可选择的图片数

    private Map<String, List<String>> selectedMap;//已选择的图片

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Map<String, List<String>> getSelectedMap() {
        return selectedMap;
    }

    public void setSelectedMap(Map<String, List<String>> selectedMap) {
        this.selectedMap = selectedMap;
    }

    public Map<String, ImageFolder> getAlbumMap() {
        return albumMap;
    }

    public void setAlbumMap(Map<String, ImageFolder> albumMap) {
        this.albumMap = albumMap;
    }

    public List<ImageFolder> getImageFolders() {
        if (albumMap == null) {
            return null;
        }
        List<ImageFolder> imageFolders = new ArrayList<>();
        for (ImageFolder imageFolder : albumMap.values()) {
            imageFolders.add(imageFolder);
        }
        return imageFolders;
    }

    //获取已选择图片的数量
    public int getSelectedCount(){
        int count = 0;
        for (List<String> list : selectedMap.values()) {
            count += list.size();
        }
        return count;
    }
}
