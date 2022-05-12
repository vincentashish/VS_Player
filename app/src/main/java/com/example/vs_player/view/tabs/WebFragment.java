package com.example.vs_player.view.tabs;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vs_player.R;
import com.example.vs_player.view.WebPlayerActivity;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.material.textfield.TextInputEditText;

public class WebFragment extends Fragment {

    TextInputEditText inputURL;
    AppCompatButton playButton;
    ExoPlayer webPlayer;
    //URI uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputURL = requireView().findViewById(R.id.inputText);

        playButton = requireView().findViewById(R.id.webPlay);
        playButton.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), WebPlayerActivity.class);
            Log.d("Checkkk",inputURL.getText().toString());
            intent.putExtra("web_url",inputURL.getText().toString());
            requireContext().startActivity(intent);
        });
    }


}