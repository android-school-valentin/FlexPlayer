package com.v.flexplayer.song;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.v.flexplayer.R;

import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {

    private ArrayList<Song> songList;

    private OnItemClickListener listener;

    private int selectedPos = RecyclerView.NO_POSITION;

    public SongsAdapter(ArrayList<Song> songList) {
        this.songList = songList;
    }


    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Picasso.with(holder.itemView.getContext())
                .load(songList.get(position).getAlbumImageUri()).placeholder(R.drawable.song_placeholder)
                .into(holder.albumImage);
        holder.itemView.setSelected(selectedPos == position);
        holder.songNameText.setText(songList.get(position).getName());
        holder.artistNameText.setText(songList.get(position).getArtistName());
        holder.durationText.setText(songList.get(position).getDuration());
    }


    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        private ImageView albumImage;
        private TextView songNameText;
        private TextView artistNameText;
        private TextView durationText;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.song_album_image);
            songNameText = itemView.findViewById(R.id.song_name_text);
            artistNameText = itemView.findViewById(R.id.song_artist_name_text);
            durationText = itemView.findViewById(R.id.song_duration_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
                    notifyItemChanged(selectedPos);
                    selectedPos = getLayoutPosition();
                    notifyItemChanged(selectedPos);
                    listener.OnItemClick(getAdapterPosition());
                }
            });
        }
    }
}
