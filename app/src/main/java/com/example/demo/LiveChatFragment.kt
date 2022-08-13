package com.example.demo

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.demo.databinding.FragmentLiveChatBinding
import com.integration.bold.BoldChat
import com.integration.bold.BoldChatListener
import com.integration.bold.boldchat.core.BoldChatSessionListener
import com.integration.bold.boldchat.visitor.api.*
import com.integration.core.Chatter
import com.integration.core.FormResults
import com.integration.core.UnavailableReason
import com.nanorep.convesationui.bold.model.BoldAccount
import com.nanorep.nanoengine.bot.BotChat
import com.nanorep.nanoengine.model.AgentType
import com.nanorep.nanoengine.model.ChatChannel
import com.nanorep.nanoengine.model.configuration.ChatConfigurationProvider
import com.nanorep.nanoengine.model.conversation.CreateConversationResponse
import com.nanorep.nanoengine.model.conversation.statement.*
import com.nanorep.sdkcore.model.StatementScope
import java.sql.Statement
import java.util.*

//
class LiveChatFragment : Fragment() {

    private var _binding: FragmentLiveChatBinding? = null
    val binding get() = _binding!!
    val key = BuildConfig.LiveAgentKey
    val account: Account = Account(key)
    private lateinit var boldChat: StatementRequest
    lateinit var listener: CreateChatListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLiveChatBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.chatBox.setEndIconOnClickListener {
            val userMessage = binding.editText.text
            sendStatementToAgent(userMessage.toString())
            createChat()
            it.hideKeyboard()
            userMessage?.clear()

        }
    }

    private fun createChat() {
        boldChat = StatementRequest()
        listener = object : CreateChatListener {
            override fun onChatCreated(p0: PreChat?, p1: Chat?) {


            }

            override fun onChatUnavailable(
                p0: UnavailableReason?,
                p1: UnavailableForm?,
                p2: Chat?,
                p3: MutableMap<String, String>?
            ) {
                TODO("Not yet implemented")
            }

            override fun onChatCreateFailed(p0: Int, p1: String?) {
                TODO("Not yet implemented")
            }


        }
        account.createChat(listener)


    }

    private fun sendStatementToAgent(statement: String) {
        boldChat = StatementRequest()
        boldChat.statement(statement)

    }

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}