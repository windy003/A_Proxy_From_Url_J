public class YourActivity extends AppCompatActivity {
    private TrojanConfigAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ... 其他初始化代码 ...
        
        adapter = new TrojanConfigAdapter(configs);
        adapter.setOnItemClickListener(config -> {
            // 处理连接逻辑
            connectToTrojan(config);
        });
        
        recyclerView.setAdapter(adapter);
    }
    
    private void connectToTrojan(TrojanConfig config) {
        // 这里实现连接逻辑
        try {
            // 1. 显示连接中的提示
            showLoading("正在连接...");
            
            // 2. 创建连接配置
            String serverAddress = config.getServerAddress();
            int port = config.getServerPort();
            String password = config.getPassword();
            
            // 3. 启动 Trojan 服务
            // 注意：这里需要根据你的具体 Trojan 实现来调用相应的方法
            TrojanService.getInstance().connect(serverAddress, port, password);
            
            // 4. 连接成功后的处理
            showSuccess("连接成功");
            
        } catch (Exception e) {
            // 5. 处理连接失败的情况
            showError("连接失败：" + e.getMessage());
        }
    }
} 