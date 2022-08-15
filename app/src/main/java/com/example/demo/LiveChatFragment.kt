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
import com.integration.bold.boldchat.visitor.api.*
import com.integration.core.*
import com.nanorep.nanoengine.model.conversation.statement.*
import com.nanorep.sdkcore.model.StatementScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


//
class LiveChatFragment : Fragment(), LiveChatListener {

    private var _binding: FragmentLiveChatBinding? = null
    val binding get() = _binding!!

    val key = BuildConfig.LiveAgentKey
    val account: Account = Account(key)


    lateinit var listener: CreateChatListener
    lateinit var agentResponse : OnStatementResponse


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
        binding.agentResponse.text = "An agent will be with you shortly..."


    }


    private fun createChat() {

        listener = object : CreateChatListener {
            override fun onChatCreated(p0: PreChat?, p1: Chat?) {

                Handler(Looper.getMainLooper()).post(Runnable {
                    binding.chatBox.setEndIconOnClickListener {
                        val userMessage = binding.editText.text
                        p1?.sendMessage(userMessage.toString())
                        Log.d("user_message", userMessage.toString())
                        binding.userMessage.text = userMessage.toString()
                        it.hideKeyboard()
                        userMessage?.clear()

                    }
                })


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


    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }


}