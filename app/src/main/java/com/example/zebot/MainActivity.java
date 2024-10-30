package com.example.zebot;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ai.client.generativeai.java.ChatFutures;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText queryEditText;
    private ImageView sendQuery,logo,appIcon;
    FloatingActionButton btnShowDialog;
    private ProgressBar progressBar;
    private LinearLayout chatResponse;
    private ChatFutures chatModel;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dialog= new Dialog(this);
        dialog.setContentView(R.layout.mesage_dialog);

        if(dialog.getWindow()!=null){

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
        }
        sendQuery= dialog.findViewById(R.id.sendMessage);
        queryEditText=dialog.findViewById(R.id.queryEditText);

        btnShowDialog= findViewById(R.id.showMessageDialog);
        progressBar=findViewById(R.id.progressBar);
        chatResponse=findViewById(R.id.chatResponse);
        appIcon=findViewById(R.id.appIcon);

        chatModel=getChatModel();

        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        sendQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                progressBar.setVisibility(View.VISIBLE);
                appIcon.setVisibility(View.GONE);
                String query= queryEditText.getText().toString();

                queryEditText.setText("");

                chatBody("You",query,getDrawable(R.drawable.user));

                GeminiResponse.getResponse(chatModel, query, new ResponseCallback() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);
                        chatBody("ZeBot",response,getDrawable(R.drawable.ai));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                       // throwable.printStackTrace();
                        chatBody("ZeBot","Please try again.",getDrawable(R.drawable.ai));
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

private  ChatFutures getChatModel(){

        GeminiResponse model =new GeminiResponse();
        GenerativeModelFutures modelFutures=model.getModel();


        return modelFutures.startChat();

}
    private void chatBody(String userName, String query, Drawable image) {

        LayoutInflater inflater=(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.chat_message,null);

        TextView name=view.findViewById(R.id.name);
        TextView message=view.findViewById(R.id.agentMessage);
        ImageView logo=view.findViewById(R.id.logo);

        name.setText(userName);
        message.setText(query);
        logo.setImageDrawable(image);

        chatResponse.addView(view);

        ScrollView scrollView=findViewById(R.id.scrollView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }


}