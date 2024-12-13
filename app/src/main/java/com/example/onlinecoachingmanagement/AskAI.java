package com.example.onlinecoachingmanagement;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.List;

public class AskAI extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private EditText inputMessage;
    private ImageButton sendButton;
    private static final String API_KEY = "AIzaSyAjjGKFGunlk3tGdxEoRKlrDZCS6XolYBI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_ai);

        recyclerView = findViewById(R.id.recycler_view);
        inputMessage = findViewById(R.id.inpuetMessage);
        sendButton = findViewById(R.id.send_btn);
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, chatMessages);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputMessage.getText().toString();
                if (!message.isEmpty()) {
                    // Add user message to list
                    chatMessages.add(new ChatMessage("You:", message));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    recyclerView.scrollToPosition(chatMessages.size() - 1);

                    // Call the API for AI response
                    fetchAIResponse(message);
                    inputMessage.setText("");
                }
            }
        });
    }

    private void fetchAIResponse(String userMessage) {
        // Initialize the AI model with the API key
        GenerativeModel gm =
                new GenerativeModel(
                        /* modelName */ "gemini-1.5-flash",
                        /* apiKey */ API_KEY);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);

        Content content = new Content.Builder().addText(userMessage).build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
        Futures.addCallback(
                response,
                new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        String aiResponse = result.getText();
                        // Add AI response to the list
                        chatMessages.add(new ChatMessage("AI:", aiResponse));
                        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                        recyclerView.scrollToPosition(chatMessages.size() - 1);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                        // Handle API call failure
                        chatMessages.add(new ChatMessage("AI:", "Error! Please try again."));
                        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                        recyclerView.scrollToPosition(chatMessages.size() - 1);
                    }
                },
                this.getMainExecutor());
    }
}

class ChatMessage {
    private String sender;
    private String message;

    public ChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<ChatMessage> chatMessages;
    private Context context;

    public ChatAdapter(Context context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        holder.senderTextView.setText(chatMessage.getSender());
        holder.messageTextView.setText(chatMessage.getMessage());
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView senderTextView;
        TextView messageTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.left_chat_text_view);
            messageTextView = itemView.findViewById(R.id.right_chat_text_view);
        }
    }
}
