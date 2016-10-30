package me.reed.album;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class FolderAdapter extends RecyclerView.Adapter {

    private List<ImageFolder> imageFolders;

    public void setImageFolders(List<ImageFolder> imageFolders) {
        this.imageFolders = imageFolders;
    }

    public interface OnItemClickListener{
        void onClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder_layout, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((FolderViewHolder) holder).bindData(imageFolders.get(position));
        ((FolderViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onClick(view, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageFolders == null ? 0 : imageFolders.size();
    }


    private class FolderViewHolder extends RecyclerView.ViewHolder {

        ImageView coverImageView;
        TextView folderTextView;

        private FolderViewHolder(View itemView) {
            super(itemView);
            coverImageView = (ImageView) itemView.findViewById(R.id.img_item_cover);
            folderTextView = (TextView) itemView.findViewById(R.id.tv_item_folder);
        }

        private void bindData(ImageFolder imageFolder){
            Glide.with(itemView.getContext()).load(imageFolder.getCover()).into(coverImageView);
            String folder = imageFolder.getName() + " (" + imageFolder.getPictures().size() + ")";
            folderTextView.setText(folder);
        }
    }
}
