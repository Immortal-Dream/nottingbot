package com.example.nottingbot;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.nottingbot.databinding.WelcomePageBinding;

/**
 * @author Junhan LIU, James Thomas, Wyson, Mengtong XIE
 */
public class WelcomeFragment extends Fragment {
    /**
     * binding variable of welcome_page.xml
     */
    private WelcomePageBinding binding;
    /**
     * ImageView objects of two background images
     */
    private ImageView bgOne, bgTwo;
    /**
     *ValueAnimator is a class in Android that provides a way to perform smooth animations by
     * interpolating the values of an object property over a specified duration of time.
     */
    private ValueAnimator valueAnimator;
    /**
     * Avatar images' ids
     */
    private int[] AvatarImages = {R.drawable.chatbot_avatar0,R.drawable.chatbot_avatar1, R.drawable.chatbot_avatar2, R.drawable.chatbot_avatar3};

    /**
     * This is a method that is called when a fragment's UI is being created. In this method,
     * we can create and configure the UI components for the fragment, and return the root view
     * of the fragment's layout.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle object that contains the saved state of the fragment, or null if there is no saved state.
     * @return root view of the fragment's layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = WelcomePageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Define the control flow of "go to settings" button
     *
     * @param view View object
     * @param savedInstanceState Bundle object
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load previous saved avatar pic
        SharedPreferences sp = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int SavedAvatar = sp.getInt("SavedAvatar", 0);
        ImageView img= (ImageView) getView().findViewById(R.id.imageView3);
        img.setImageResource(AvatarImages[SavedAvatar]);

        binding.settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(WelcomeFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.goToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(WelcomeFragment.this)
                        .navigate(R.id.action_Welcome_to_ChatFragment);
            }
        });
        binding.arButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(WelcomeFragment.this)
                        .navigate(R.id.action_Welcome_to_AR);
            }
        });
        startAnimation();

    }

    /**
     * Control the background animation for the welcome page
     * Uses two identical images with source "bubbleAnim" and scrolls them infinitely
     */
    public void startAnimation() {

        // Retrieve the two image views
        bgOne = binding.imageView5;
        bgTwo = binding.imageView6;

        // ValueAnimator used to scroll the images smoothly
        valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);

        // Loop animation infinitely
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());

        // Speed of scrolling, lower value -> faster scroll
        valueAnimator.setDuration(5000);

        // Update listener for ValueAnimator
        valueAnimator.addUpdateListener(animation -> {

            // Local variables for position calculation
            float scroll = (float) animation.getAnimatedValue();
            float width = bgOne.getWidth();

            // Calculate next x value
            float x = width * scroll;

            // Translate bgOne on X axis based on scroll value and image width
            bgOne.setTranslationX(x);

            // Translate bgTwo based on X axis, scroll width and an offset to create infinite scrolling with bgOne
            bgTwo.setTranslationX(x - width);

        });

        // Start the value animator
        valueAnimator.start();
    }

    /**
     * onDestroyView() is a lifecycle method in Android that is called when the view hierarchy
     * associated with a fragment is being removed or destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}