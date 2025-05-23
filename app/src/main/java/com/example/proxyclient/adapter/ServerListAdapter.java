package com.example.proxyclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proxyclient.R;
import com.example.proxyclient.model.TrojanConfig;
import java.util.List;
import java.util.ArrayList;

public class ServerListAdapter extends RecyclerView.Adapter<ServerListAdapter.ViewHolder> {
    private List<TrojanConfig> serverList = new ArrayList<>();
    private OnServerClickListener listener;
    private int selectedPosition = -1;

    public interface OnServerClickListener {
        void onServerClick(TrojanConfig config, int position);
        void onServerLongClick(TrojanConfig config, int position);
    }

    public void setOnServerClickListener(OnServerClickListener listener) {
        this.listener = listener;
    }

    public void updateServerList(List<TrojanConfig> newList) {
        this.serverList.clear();
        this.serverList.addAll(newList);
        notifyDataSetChanged();
    }

    public void setSelectedPosition(int position) {
        int oldPosition = selectedPosition;
        selectedPosition = position;
        if (oldPosition != -1) {
            notifyItemChanged(oldPosition);
        }
        if (selectedPosition != -1) {
            notifyItemChanged(selectedPosition);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_server, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrojanConfig config = serverList.get(position);
        holder.bind(config, position == selectedPosition);
    }

    @Override
    public int getItemCount() {
        return serverList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView serverName, serverAddress, serverInfo;
        ImageView statusIcon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            serverName = itemView.findViewById(R.id.server_name);
            serverAddress = itemView.findViewById(R.id.server_address);
            serverInfo = itemView.findViewById(R.id.server_info);
            statusIcon = itemView.findViewById(R.id.status_icon);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onServerClick(serverList.get(getAdapterPosition()), getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onServerLongClick(serverList.get(getAdapterPosition()), getAdapterPosition());
                }
                return true;
            });
        }

        void bind(TrojanConfig config, boolean isSelected) {
            serverName.setText(config.getName());
            serverAddress.setText(config.getServerAddress() + ":" + config.getServerPort());
            
            String info = "SNI: " + (config.getSni().isEmpty() ? "无" : config.getSni()) + 
                         " | UDP: " + (config.isEnableUdp() ? "启用" : "禁用");
            serverInfo.setText(info);

            // 设置选中状态
            itemView.setBackgroundResource(isSelected ? 
                R.drawable.item_selected_background : 
                R.drawable.item_normal_background);
            
            statusIcon.setImageResource(isSelected ? 
                R.drawable.ic_radio_selected : 
                R.drawable.ic_radio_unselected);
        }
    }
} 