package com.example.petcare.ui.main.home.chatbot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.petcare.data.remote.response.Message
import com.example.petcare.databinding.FragmentChatbotBinding
import com.example.petcare.helper.BotResponse
import com.example.petcare.helper.Constants.RECEIVE_ID
import com.example.petcare.helper.Constants.SEND_ID
import com.example.petcare.helper.Time
import kotlinx.coroutines.*

class ChatbotFragment : Fragment() {

    private var _binding: FragmentChatbotBinding? = null
    private val binding get() = _binding!!
    private var messagesList = mutableListOf<Message>()
    private lateinit var adapter: MessageAdapter
    private val botList = listOf("Peter", "Francesca", "Luigi", "Igor")
    private val random = (0..3).random()

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView()

        clickEvents()

        customStart()
    }

    override fun onStart() {
        super.onStart()

        uiScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
    private fun customStart(){
        customBotMessage("Hello and welcome fellow pet owner! Today you're speaking with ${botList[random]}, i'm one of Pet Care Application chat bot! You can consult with me about your pet.\n\n" +
                "Before we start, there are a few things to note : \n" +
                "1. Please answer me according to directions, if you enter input that is not appropriate, then the consultation session will not run smoothly\n" +
                "2. Please understand the limitations of animals that can be diagnosed because I am still in the development stage\n" +
                "3. If you wish to repeat everything from the beginning, type \"RESET\"\n"+
                "\n If you understand then let's start ^^. Can you tell me what kind of pet you want to consult?\n" +
                "q0 : Lovebird Bird\n\n NOTE = just answer by id (example : q0)")
    }

    private fun clickEvents() {

        //Send a message
        binding.btnSendMessage.setOnClickListener {
            sendMessage()
        }

        //Scroll back to correct position when user clicks on text view
        binding.etMessage.setOnClickListener {
            uiScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private fun recyclerView() {
        adapter = MessageAdapter()
        binding.rvMessages.adapter = adapter
        binding.rvMessages.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun sendMessage() {
        val message = binding.etMessage.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            //Adds it to our local list
            messagesList.add(Message(message, SEND_ID, timeStamp))
            binding.etMessage.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            binding.rvMessages.scrollToPosition(adapter.itemCount - 1)

            if(message == "RESET" || message == "reset"){
                adapter.resetAll()
                customStart()
            }else{
                botResponse(message)
            }
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        uiScope.launch {
            //Fake response delay
            delay(1000)

            withContext(Dispatchers.Main) {
                //Gets the response
                val response = BotResponse.basicResponses(message)

                //Adds it to our local list
                messagesList.add(Message(response, RECEIVE_ID, timeStamp))

                //Inserts our message into the adapter
                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))

                //Scrolls us to the position of the latest message
                binding.rvMessages.scrollToPosition(adapter.itemCount - 1)

            }
        }
    }

    private fun customBotMessage(message: String) {

        uiScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                messagesList.add(Message(message, RECEIVE_ID, timeStamp))
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))

                binding.rvMessages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}