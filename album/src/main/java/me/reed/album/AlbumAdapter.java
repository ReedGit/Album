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

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * @author reed
 */
class AlbumAdapter extends RecyclerView.Adapter {

    private List<String> paths;

    private List<String> selectedPaths;

    public void setPaths(List<String> paths, List<String> selectedPaths) {
        this.paths = paths;
        this.selectedPaths = selectedPaths;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);

        void onCheckedChanged(AppCompatCheckBox checkBox, boolean b, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_layout, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((AlbumViewHolder) holder).bindData(paths.get(position), selectedPaths != null && selectedPaths.contains(paths.get(position)));
        ((AlbumViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(view, holder.getAdapterPosition());
                }
            }
        });
        ((AlbumViewHolder) holder).albumCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onCheckedChanged((AppCompatCheckBox) view, ((AppCompatCheckBox) view).isChecked(), holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return paths == null ? 0 : paths.size();
    }

    private class AlbumViewHolder extends RecyclerView.ViewHolder {

        private ImageView albumImageView;
        private AppCompatCheckBox albumCheckbox;

        private AlbumViewHolder(View itemView) {
            super(itemView);
            albumImageView = (ImageView) itemView.findViewById(R.id.img_item_album);
            albumCheckbox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox_item_album);
        }

        private void bindData(String url, boolean isSelected) {
            Glide.with(itemView.getContext()).load(url).into(albumImageView);
            albumCheckbox.setChecked(isSelected);
        }
    }
}
