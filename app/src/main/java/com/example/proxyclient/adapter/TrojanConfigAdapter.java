package com.example.proxyclient.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proxyclient.R;
import com.example.proxyclient.model.TrojanConfig;
import java.util.List;

public class TrojanConfigAdapter extends RecyclerView.Adapter<TrojanConfigAdapter.ViewHolder> {
    private List<TrojanConfig> configs;
    private int selectedPosition = -1;  // 记录选中的位置
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TrojanConfig config);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvRegion;
        public TextView tvName;
        public TextView tvServer;

        public ViewHolder(View view) {
            super(view);
            tvRegion = view.findViewById(R.id.tvRegion);
            tvName = view.findViewById(R.id.tvName);
            tvServer = view.findViewById(R.id.tvServer);
        }
    }

    public TrojanConfigAdapter(List<TrojanConfig> configs) {
        this.configs = configs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trojan_node, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrojanConfig config = configs.get(position);
        holder.itemView.setSelected(position == selectedPosition);
        holder.tvRegion.setText(String.format("[%s]", config.getRegion()));
        holder.tvName.setText(config.getRemark());
        holder.tvServer.setText(String.format("%s:%d", 
            config.getServerAddress(), config.getServerPort()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(config);
            }
        });
    }

    @Override
    public int getItemCount() {
        return configs.size();
    }

    public void updateData(List<TrojanConfig> newConfigs) {
        this.configs = newConfigs;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }
} 