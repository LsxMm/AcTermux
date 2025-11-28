package com.social.actermux

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.social.actermux.ui.BottomTerminalPanel
import com.termux.app.TermuxActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    // 在 Composable 里用 LocalContext 拿 context
    val context = LocalContext.current
    // 控制底部终端是否显示
    var showEmbeddedTerminal by remember { mutableStateOf(false) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // ① 保留：跳 TermuxActivity 的按钮
                Button(onClick = {
                    context.startActivity(
                        Intent(context, TermuxActivity::class.java)
                    )
                }) {
                    Text(text = "打开整页终端 (TermuxActivity)")
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ② 新增：底部半屏终端
                Button(onClick = {
                    showEmbeddedTerminal = true
                }) {
                    Text(text = "打开底部终端（半屏）")
                }
            }

            // ③ 底部弹出的终端面板
            BottomTerminalPanel(
                visible = showEmbeddedTerminal,
                onDismissRequest = { showEmbeddedTerminal = false }
            )
        }
    }
}



