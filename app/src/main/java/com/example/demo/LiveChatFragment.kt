package com.example.demo

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.demo.databinding.FragmentLiveChatBinding
import com.integration.async.core.LiveChatListener
import com.integration.bold.BoldChat
import com.integration.bold.BoldChatListener
import com.integration.bold.boldchat.core.PostChatData
import com.integration.bold.boldchat.visitor.api.*
import com.integration.bold.boldchat.visitor.api.internal.RPCSender
import com.integration.core.*
import com.nanorep.nanoengine.model.conversation.SessionInfo
import com.nanorep.sdkcore.utils.stringFields
import java.lang.ref.WeakReference


//
class LiveChatFragment : Fragment() {

    private var _binding: FragmentLiveChatBinding? = null
    val binding get() = _binding!!

    val key = BuildConfig.LiveAgentKey

    private val boldChat: BoldChat = BoldChat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createChat()
    }


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
            boldChat.postMessage(userMessage.toString())
            binding.userMessage.text = userMessage.toString()
            it.hideKeyboard()
            userMessage?.clear()
        }

    }


    private fun createChat() {

        val info = SessionInfo("").apply {
            addConfigurations("skipPreChat" to true)
        }

        val boldChatListener = object : BoldChatListener {
            override fun error(code: Int, message: String?, data: Any?) {
                // implement to handle errors
            }

            override fun chatAccepted(timestamp: Long, operator: Chatter) {
                // at this point the agent accepted user's chat, the chat won't be canceled due to
                // acceptance timeout, and the chat can start.

            }

            override fun messageArrived(
                messageId: String,
                message: String,
                timestamp: Long,
                sender: String
            ) {
                // agent messages will be received on this implementation
                // sender should indicate one of PersonType enum. agent messages are followed with sender-> Operator
            }

            override fun chatEnded(formData: PostChatData?) {
                // Chat ended.

            }

            override fun visitorInfoUpdated() {
                // visitorInfo property was updated on the Boldchat with ChatId and visitorId
                // in case needed for the app. (visitorId is used for returning chats of the same user)
            }
        }

        boldChat.wListener = WeakReference(boldChatListener)

        boldChat.visitorInfo = info

        boldChat.prepare(requireContext(), key, HashMap())

        boldChat.start()

    }


    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

}