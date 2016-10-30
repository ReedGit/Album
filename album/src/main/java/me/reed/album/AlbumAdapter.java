package me.reed.album;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter {

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
