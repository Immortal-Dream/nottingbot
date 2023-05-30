package com.example.nottingbot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.nottingbot.databinding.SettingsMenuBinding;

public class SettingFragment extends Fragment {

    /**
     * binding variable of settings_menu.xml
     */
    private SettingsMenuBinding binding;
    /**
     * Avatar Images' ids in the res/drawable package
     */
    private int[] AvatarImages = {R.drawable.chatbot_avatar0, R.drawable.chatbot_avatar1, R.drawable.chatbot_avatar2, R.drawable.chatbot_avatar3};


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
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = SettingsMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    /**
     * This is a method in the Fragment class of Android that is called after the fragment's view has been created.
     * It is used to initialize the views that belong to the fragment's layout.
     *
     * @param view               The root view of the fragment's layout hierarchy.
     * @param savedInstanceState A bundle that contains the saved state of the fragment, which includes
     *                           information such as the fragment's UI state, variables, and values.
     */
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Load previous saved avatar pic
        SharedPreferences sp = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int SavedAvatar = sp.getInt("SavedAvatar", 0);
        ImageView img = (ImageView) getView().findViewById(R.id.imageView);
        img.setImageResource(AvatarImages[SavedAvatar]);


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SettingFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
        binding.changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SettingFragment.this)
                        .navigate(R.id.action_SecondFragment_to_ChangeAvatarFragment);
            }
        });

        binding.feedbackCentre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SettingFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FeedbackFragment);
            }
        });
        binding.shareCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SettingFragment.this)
                        .navigate(R.id.action_SecondFragment_to_QRCodeFragment);
            }
        });
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