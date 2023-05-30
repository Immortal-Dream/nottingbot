package com.example.nottingbot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.nottingbot.databinding.ChangeAvatarBinding;

/**
 * This class is the controller class of change_avatar.xml page and
 * it defines the logic of the buttons in this page
 *
 * @author Junhan LIU, Wyson
 * @version 1.0
 */
public class ChangeAvatarFragment extends Fragment {

    /**
     * binding variable of change_avatar.xml which is
     * designed in MVVM model
     */
    private Button NextBtn, SaveBtn;
    /**
     * Image buttons
     */
    private ImageButton rightButton, leftButton;
    /**
     * ImageSwitcher objects
     */
    private ImageSwitcher ImgSw;
    /**
     * avatars' resource ids
     */
    private int[] AvatarImages = {R.drawable.chatbot_avatar0, R.drawable.chatbot_avatar1, R.drawable.chatbot_avatar2, R.drawable.chatbot_avatar3};
    /**
     * Image current position
     */
    private int ImgPosition = 0;
    /**
     * avatar's name
     */
    private static String avatar = "avatar1";
    /**
     * change_avatar.xml 's binding variable
     */
    private ChangeAvatarBinding binding;
    /**
     * is a class in Android that helps developers to detect and handle touch events
     * or gestures on the screen.
     */
    private GestureDetectorCompat gestureDetector;


    /**
     * A gesture listener that implements the {@link GestureDetector.SimpleOnGestureListener} interface
     * to handle fling events on a view.
     *
     * @see GestureDetector.SimpleOnGestureListener
     */
    private final GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            float diffX = event2.getX() - event1.getX();
            if (Math.abs(diffX) > Math.abs(velocityX)) {
                if (diffX > 0) {
                    System.out.println("Swiped!!!");
                    showPreviousImage(ImgPosition);
                } else {
                    showNextImage(ImgPosition);
                    System.out.println("Swiped!!!");
                }
                return true;
            }
            return false;
        }
    };

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return View object
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = ChangeAvatarBinding.inflate(inflater, container, false);
        gestureDetector = new GestureDetectorCompat(getActivity(), gestureListener);

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * This is a method in the Fragment class of Android that is called after the fragment's view has been created.
     * It is used to initialize the views that belong to the fragment's layout.
     *
     * @param view               The root view of the fragment's layout hierarchy.
     * @param savedInstanceState A bundle that contains the saved state of the fragment, which includes
     *                           information such as the fragment's UI state, variables, and values.
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.saveAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ChangeAvatarFragment.this)
                        .navigate(R.id.ChangeAvatarFragment_to_SecondFragment);
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ChangeAvatarFragment.this)
                        .navigate(R.id.ChangeAvatarFragment_to_SecondFragment);
            }
        });

        ImgSw = (ImageSwitcher) getView().findViewById(R.id.imgSw);
        rightButton = (ImageButton) getView().findViewById(R.id.rightButton);
        leftButton = (ImageButton) getView().findViewById(R.id.leftButton);
        SaveBtn = (Button) getView().findViewById(R.id.saveAvatar);
        ImgSw.setFactory(new ViewSwitcher.ViewFactory() {


            @Override
            public View makeView() {
                ImageView imgVw = new ImageView(getActivity());
                imgVw.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imgVw.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                imgVw.setImageResource(AvatarImages[ImgPosition]);
                return imgVw;
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImgPosition < AvatarImages.length)
                    ImgPosition++;
                if (ImgPosition >= AvatarImages.length)
                    ImgPosition = 0;
                ImgSw.setImageResource(AvatarImages[ImgPosition]);
            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImgPosition > 0)
                    ImgPosition--;
                if (ImgPosition <= 0)
                    ImgPosition = AvatarImages.length - 1;
                ImgSw.setImageResource(AvatarImages[ImgPosition]);
            }
        });

        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAvatar("avatar" + ImgPosition);
                System.out.println(avatar);

                //Save Avatar pic in shared preferences for SecondFragment to access
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("SavedAvatar", ImgPosition);
                editor.apply();

                //Exit to previous setting page after save
                NavHostFragment.findNavController(ChangeAvatarFragment.this)
                        .navigate(R.id.ChangeAvatarFragment_to_SecondFragment);
            }
        });

        // Attach the gesture detector to the image switcher
        ImgSw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
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

    /**
     * Set avatar's name
     *
     * @param avatarName avatar's name
     */
    public void setAvatar(String avatarName) {
        avatar = avatarName;
    }


    /**
     * Shows the next image in the ImageSwitcher and sets the appropriate in and out animations.
     *
     * @param imgPosition The current position of the image in the array of Avatar images.
     */
    private void showNextImage(int imgPosition) {
        // set the in and out animations for the ImageSwitcher
        Animation inAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.from_right);
        Animation outAnimation = AnimationUtils.loadAnimation(getActivity(), androidx.navigation.ui.R.anim.nav_default_exit_anim);
        ImgSw.setInAnimation(inAnimation);
        ImgSw.setOutAnimation(outAnimation);
        System.out.println(ImgPosition);
        if (imgPosition < AvatarImages.length - 1) {
            ImgPosition++;
            ImgSw.setImageResource(AvatarImages[ImgPosition]);

        } else if (imgPosition == AvatarImages.length - 1) {
            ImgPosition = 0;
            ImgSw.setImageResource(AvatarImages[ImgPosition]);
        }
    }

    /**
     * Show the previous image in the ImageSwitcher with a slide-in animation from left to right.
     * If the current image is the first one in the array, then the last image in the array is shown.
     *
     * @param imgPosition the index of the current image in the array of Avatar images
     */
    private void showPreviousImage(int imgPosition) {
        // set the in and out animations for the ImageSwitcher
        Animation inAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.from_left);
        Animation outAnimation = AnimationUtils.loadAnimation(getActivity(), androidx.navigation.ui.R.anim.nav_default_exit_anim);
        ImgSw.setInAnimation(inAnimation);
        ImgSw.setOutAnimation(outAnimation);

        if (imgPosition > 0) {
            ImgPosition--;
            ImgSw.setImageResource(AvatarImages[ImgPosition]);
        } else if (imgPosition == 0) {
            ImgPosition = 3;
            ImgSw.setImageResource((AvatarImages[ImgPosition]));
        }
    }

}
