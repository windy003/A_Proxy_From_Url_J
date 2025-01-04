package com.example.proxyclient.adapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import java.util.List;
import com.example.proxyclient.R;
import com.example.proxyclient.model.TrojanConfig;

public class TrojanConfigAdapter extends RecyclerView.Adapter<TrojanConfigAdapter.ViewHolder> {
    private List<TrojanConfig> configs;

    // ViewHolder 内部类定义
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvServer;

        public ViewHolder(View view) {
            super(view);
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
                .inflate(R.layout.item_trojan_config, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TrojanConfig config = configs.get(position);
        holder.tvName.setText(config.getRemark());
        holder.tvServer.setText(config.getServerAddress() + ":" + config.getServerPort());
    }

    @Override
    public int getItemCount() {
        return configs.size();
    }
} 