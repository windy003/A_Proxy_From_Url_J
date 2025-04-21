package com.example.proxyclient.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proxyclient.R;
import com.example.proxyclient.service.TrojanService;
import com.example.proxyclient.model.TrojanConfig;

public class LinkInputActivity extends AppCompatActivity {
    
    private EditText linkInputEditText;
    private Button submitButton;
    private ProgressBar progressBar;
    private RadioGroup linkTypeRadioGroup;
    private RadioButton trojanRadioButton;
    private RadioButton subscriptionRadioButton;
    private TextView nodeInfoTextView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_input);
        
        // 初始化视图
        linkInputEditText = findViewById(R.id.link_input_edit_text);
        submitButton = findViewById(R.id.submit_button);
        progressBar = findViewById(R.id.progress_bar);
        linkTypeRadioGroup = findViewById(R.id.link_type_radio_group);
        trojanRadioButton = findViewById(R.id.trojan_radio_button);
        subscriptionRadioButton = findViewById(R.id.subscription_radio_button);
        nodeInfoTextView = findViewById(R.id.node_info_text_view);
        
        // 设置点击事件
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = linkInputEditText.getText().toString().trim();
                
                if (link.isEmpty()) {
                    Toast.makeText(LinkInputActivity.this, "请输入链接", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // 根据选择类型处理链接
                if (trojanRadioButton.isChecked()) {
                    // 处理Trojan链接
                    try {
                        // 解析Trojan链接
                        TrojanConfig config = TrojanConfig.fromUrl(link);
                        // 显示节点信息
                        displayNodeInfo(config);
                        
                        // 更新按钮文本为"连接"
                        submitButton.setText("连接");
                        // 修改按钮点击事件为连接服务器
                        submitButton.setOnClickListener(v1 -> connectToTrojan(config));
                    } catch (Exception e) {
                        Toast.makeText(LinkInputActivity.this, 
                            "无效的Trojan链接: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 处理订阅链接逻辑保持不变
                    Toast.makeText(LinkInputActivity.this, "接收到订阅链接: " + link, Toast.LENGTH_SHORT).show();
                    // 暂时不执行实际操作，只显示接收到的链接
                    finish();
                }
            }
        });
    }

    // 显示节点信息
    private void displayNodeInfo(TrojanConfig config) {
        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append("节点名称: ").append(config.getName()).append("\n");
        infoBuilder.append("服务器地址: ").append(config.getServerAddress()).append("\n");
        infoBuilder.append("端口: ").append(config.getServerPort()).append("\n");
        infoBuilder.append("SNI: ").append(config.getSni().isEmpty() ? "未设置" : config.getSni()).append("\n");
        infoBuilder.append("验证证书: ").append(config.isVerifyCert() ? "是" : "否").append("\n");
        infoBuilder.append("启用UDP: ").append(config.isEnableUdp() ? "是" : "否").append("\n");
        
        nodeInfoTextView.setText(infoBuilder.toString());
        nodeInfoTextView.setVisibility(View.VISIBLE);
    }

    // 连接到Trojan服务器
    private void connectToTrojan(TrojanConfig config) {
        // 显示进度
        progressBar.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);
        
        // 连接到服务器
        TrojanService.getInstance().connect(config, new TrojanService.ConnectCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    submitButton.setEnabled(true);
                    Toast.makeText(LinkInputActivity.this, 
                        "已连接到 " + config.getName(), 
                        Toast.LENGTH_SHORT).show();
                    finish(); // 连接成功后关闭此Activity
                });
            }
            
            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    submitButton.setEnabled(true);
                    Toast.makeText(LinkInputActivity.this, 
                        "连接失败: " + message, 
                        Toast.LENGTH_LONG).show();
                });
            }
        });
    }
} 