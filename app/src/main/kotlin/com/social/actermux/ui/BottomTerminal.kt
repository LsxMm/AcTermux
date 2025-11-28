package com.social.actermux.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cn.mucute.merminal.composable.Terminal
import cn.mucute.merminal.composable.rememberSessionController
import cn.mucute.merminal.view.TerminalView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomTerminalPanel(
    visible: Boolean,
    onDismissRequest: () -> Unit,
) {
    if (!visible) return

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    val context = LocalContext.current

    // 1. 记住一个 TerminalView 实例
    val terminalView = remember {
        TerminalView(context, null)
    }

    // 2. 记住一个 SessionController（这里 cwd 用你 Termux 的 HOME）
    val sessionController = rememberSessionController(
        context = context,
        command = null, // 需要的话可以传一个初始命令
        currentWorkingDirectory = "/data/data/com.social.actermux/files/home"
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        // 内容区半屏高度
        Terminal(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            terminalView = terminalView,
            sessionController = sessionController
            // 颜色不传就用默认 TerminalDefaults.terminalColors()
        )
    }

    // 初次显示时拉到半展开
    LaunchedEffect(Unit) {
        sheetState.partialExpand()
    }
}