package cn.mucute.merminal.composable

import android.content.Context
import cn.mucute.merminal.core.TerminalSession.SessionChangedCallback
import cn.mucute.merminal.view.ShellTermSession

class SessionController(
    private var context: Context,
    private val command: String? = null,
    private val currentWorkingDirectory: String,
    private val environment: MutableMap<String, String> = systemEnvironment(),
) {
    fun create(callback: SessionChangedCallback): ShellTermSession {
        val environmentList = mutableListOf<String>()
        environment.forEach { (t, u) ->
            environmentList.add("$t=$u")
        }
        return ShellTermSession(
             context,
            "/data/data/com.social.actermux/files/usr/bin/bash",
            currentWorkingDirectory,
            arrayOf(),
            environmentList.toTypedArray(),
            callback,
            command
        )
    }
}
