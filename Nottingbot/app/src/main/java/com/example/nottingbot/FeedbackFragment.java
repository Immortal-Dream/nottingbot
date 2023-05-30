package com.example.nottingbot;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.nottingbot.databinding.FeedbackBinding;

import com.example.nottingbot.util.*;
import com.ibm.cloud.cloudant.v1.Cloudant;

import android.content.Intent;
import android.speech.RecognizerIntent;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.cloudant.v1.model.Document;
import com.ibm.cloud.cloudant.v1.model.DocumentResult;
import com.ibm.cloud.cloudant.v1.model.PostDocumentOptions;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneHelper;
import com.ibm.watson.developer_cloud.android.library.audio.MicrophoneInputStream;
import com.ibm.watson.speech_to_text.v1.SpeechToText;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/**
 * Feedback centre page, user can submit their feedback in this page
 *
 * @author Zeyu WEI, Junhan LIU
 */
public class FeedbackFragment extends Fragment {

    /**
     * current Context
     */
    private Context context;
    /**
     * feedback.xml 's binding variable
     */
    private FeedbackBinding binding;
    /**
     * the result of the speech
     */
    protected static final int RESULT_SPEECH = 1;
    /**
     * Cloudant service object
     */
    private Cloudant cloudant;
    /**
     * Watson speech to text service
     */
    private SpeechToText speechToText;
    /**
     * Watson text to speech service
     */
    private TextToSpeech textToSpeech;
    /**
     * Whether the application is listen to user's speech
     */
    private boolean isListen = false;
    /**
     * It is used to capture audio input from the device's microphone.
     */
    private MicrophoneInputStream microphoneInputStream;
    /**
     * Helper class that is used to manage the microphone capture.
     */
    private MicrophoneHelper microphoneHelper;

    /**
     * This method creates an instance of Cloudant service and authenticates it using IBM Cloud IAM authentication.
     *
     * @return Cloudant - The Cloudant service object authenticated with IAM.
     */
    public Cloudant iamToken() {
        IamAuthenticator authenticator = new IamAuthenticator.Builder()
                .apikey(context.getString(R.string.Cloudant_api))
                .build();

        Cloudant service = new Cloudant(Cloudant.DEFAULT_SERVICE_NAME, authenticator);

        service.setServiceUrl(context.getString(R.string.Cloudant_url));
        return service;
    }

    /**
     * fragment. It inflates the layout for the fragment using the provided LayoutInflater,
     * creates an instance of Cloudant using the iamToken method, and returns the root View of the inflated layout.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to, if not null.
     * @param savedInstanceState A Bundle containing the saved state of the fragment, or null if there is no saved state.
     * @return The root View of the inflated layout.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FeedbackBinding.inflate(inflater, container, false);
        cloudant = iamToken();
        return binding.getRoot();
    }

    /**
     * onCreate is a method that is called when a fragment is first created. It is a
     * part of the fragment lifecycle and is used to initialize the fragment
     *
     * @param savedInstanceState A bundle that contains the saved state of the fragment, which includes
     *                           information such as the fragment's UI state, variables, and values.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    /**
     * Define the logic of "back" and "submit" button
     * use the async function to send request
     *
     * @param view               View object
     * @param savedInstanceState Bundle object
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // hide send successfully
        binding.success.setVisibility(View.INVISIBLE);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FeedbackFragment.this)
                        .navigate(R.id.FeedbackCentre_to_SecondFragment);
            }
        });
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionCheckUtil.checkConnectNetwork(context)) {
                    new WriteAsyncTask().execute();
                    // count down timer, user cannot send more message
                    new CountDownTimer(60000, 1000) {
                        @SuppressLint("SetTextI18n")
                        public void onTick(long millisUntilFinished) {
                            binding.advice.setVisibility(View.INVISIBLE);
                            binding.success.setVisibility(View.VISIBLE);

                            String countDown = context.getString(R.string.another_feedback);
                            String after = context.getString(R.string.after);
                            binding.editTextTextMultiLine.setText(after + " " + millisUntilFinished / 1000 + "s, " + countDown);
                            binding.editTextTextMultiLine.setEnabled(false);
                            binding.submit.setEnabled(false);
                            binding.microphone.setEnabled(false);
                            if (isListen) {
                                isListen = false;
                                microphoneHelper.closeInputStream();
                            }
                        }

                        public void onFinish() {
                            binding.advice.setVisibility(View.VISIBLE);
                            binding.success.setVisibility(View.INVISIBLE);

                            binding.editTextTextMultiLine.setText("");
                            binding.editTextTextMultiLine.setEnabled(true);
                            binding.submit.setEnabled(true);
                            binding.microphone.setEnabled(true);
                        }
                    }.start();

                }
            }
        });
        // define the button of speech to text
        binding.microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionCheckUtil.checkConnectNetwork(context)) {
                    Intent intent
                            = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                            Locale.getDefault());
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                    try {
                        startActivityForResult(intent, RESULT_SPEECH);
                    } catch (Exception e) {
                        Toast.makeText(context, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Async function to send request to IBM Cloudant server
     */
    class WriteAsyncTask extends AsyncTask<Void, Void, Feedback> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Feedback doInBackground(Void... arg0) {
            String feedbackText = String.valueOf(binding.editTextTextMultiLine.getText());
            Feedback feedback = new Feedback(feedbackText);
            try {

                Document document = new Document();
                document.put("feedback", feedback.getContent());
                document.put("time", feedback.getDate());

                PostDocumentOptions postDocumentOptions = new PostDocumentOptions.Builder()
                        .db("feedback_centre")
                        .document(document)
                        .build();
                DocumentResult documentResult = cloudant
                        .postDocument(postDocumentOptions)
                        .execute()
                        .getResult();
                document.setRev(documentResult.getRev());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return feedback;
        }
    }

    /**
     * Destroy the feedback fragment view
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    /**
     * Update the text to text editor immediately. It is a method in an Android
     * activity that gets called after an activity launched with
     *
     * @param requestCode the code that was used to start the activity
     * @param resultCode  the result code returned by the activity
     * @param data        an Intent object that can be used to retrieve the result data returned by the activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SPEECH) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                binding.editTextTextMultiLine.setText(
                        Objects.requireNonNull(result).get(0));
            }
        }
    }
}
