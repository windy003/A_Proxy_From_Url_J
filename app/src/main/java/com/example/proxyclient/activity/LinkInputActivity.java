package com.example.proxyclient.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proxyclient.R;
import com.example.proxyclient.service.TrojanService;

public class LinkInputActivity extends AppCompatActivity {
    
    private EditText linkInputEditText;
    private Button submitButton;
    private ProgressBar progressBar;
    private RadioGroup linkTypeRadioGroup;
    private RadioButton trojanRadioButton;
    private RadioButton subscriptionRadioButton;
    
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
        
        // 设置点击事件
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = linkInputEditText.getText().toString().trim();
                
                if (link.isEmpty()) {
                    Toast.makeText(LinkInputActivity.this, "请输入链接", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                Toast.makeText(LinkInputActivity.this, "接收到链接: " + link, Toast.LENGTH_SHORT).show();
                // 暂时不执行实际操作，只显示接收到的链接
                finish();
            }
        });
    }
} 