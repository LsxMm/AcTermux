package cn.mucute.merminal.composable

import android.content.Context
import android.view.KeyEvent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import cn.mucute.merminal.view.TermSessionCallback
import cn.mucute.merminal.view.TermViewClient
import cn.mucute.merminal.view.TerminalView

@Composable
fun Terminal(
    modifier: Modifier = Modifier,
    terminalView: TerminalView,
    sessionController: SessionController,
    colorScheme: TerminalColorScheme = TerminalDefaults.terminalColors(),
) {
    val context = LocalContext.current
    val focusRequester = FocusRequester()
    val terminalView = remember {
       terminalView
    }
    val shortcutKeyController = remember { ShortcutKeyController() }
    val sessionCallback = remember { TermSessionCallback(terminalView) }
    val session = remember { sessionController.create(sessionCallback) }
    LaunchedEffect(Unit) {
        //focusRequester.requestFocus()
    }
    Scaffold(bottomBar = {
        ShortcutKey(shortcutKeyController = shortcutKeyController, onPressKey = {
            terminalView.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN,it))
            terminalView.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_UP,it))
        }, onWriteSymbol = {
            session.write(it)
        })
    }) {
        AndroidView(factory = {
            terminalView
        }, modifier = modifier
            .focusRequester(focusRequester)
            .padding(it), update = { terminalView ->
            terminalView.setBackgroundColor(colorScheme.background.toArgb())


            val viewClient = TermViewClient(context, terminalView, session, shortcutKeyController)
            session.colorScheme = (
                    cn.mucute.merminal.core.TerminalColorScheme().apply {
                        updateWith(
                            colorScheme.text.toArgb(),
                            colorScheme.background.toArgb(),
                            colorScheme.cursor.toArgb(),
                            mutableMapOf()
                        )
                    })
            terminalView.setEnableWordBasedIme(false)
            terminalView.setTerminalViewClient(viewClient)
            terminalView.attachSession(session)
            //terminalView.requestFocus()
        })

    }
}

@Composable
fun rememberSessionController(
    context: Context,
    command: String? = null,
    currentWorkingDirectory: String,
    environment: MutableMap<String, String> = systemEnvironment(),
) = remember(command, currentWorkingDirectory, environment) {

    SessionController(
        context,
        command,
        currentWorkingDirectory,
        environment
    )
}

fun systemEnvironment() = mutableMapOf(
    "TERM" to "xterm-256color",
    "ANDROID_ROOT" to System.getenv("ANDROID_ROOT")!!,
    "ANDROID_DATA" to System.getenv("ANDROID_DATA")!!,
    "COLORTERM" to "truecolor",
)