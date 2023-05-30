package com.example.nottingbot;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.nottingbot.databinding.WebChatBinding;
import com.example.nottingbot.util.ConnectionCheckUtil;

/**
 * Embed watson web chat to application
 *
 * @author Junhan LIU
 */
public class WebChatFragment extends Fragment {
    /**
     * web_chat.xml's binding variable
     */
    private WebChatBinding webChatBinding;

    /**
     * onCreate is a method that is called when a fragment is first created. It is a
     * part of the fragment lifecycle and is used to initialize the fragment
     *
     * @param savedInstanceState A bundle that contains the saved state of the fragment, which includes
     *                           information such as the fragment's UI state, variables, and values.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // check whether the
        super.onCreate(savedInstanceState);
        if (!(ConnectionCheckUtil.checkConnectNetwork(this.getContext()))){
            // if the application dose not connect to the Internet
            // go back to welcome page
            NavHostFragment.findNavController(WebChatFragment.this)
                    .navigate(R.id.WebChat_to_WelcomePage);
        }

    }

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        webChatBinding = WebChatBinding.inflate(inflater,container,false);
        // enable javascript
        WebSettings settings = webChatBinding.webChatView.getSettings();
        settings.setJavaScriptEnabled(true);


        webChatBinding.webChatView.loadUrl("file:///android_asset/index.html");
        return webChatBinding.getRoot();
    }

    /**
     * This is a method in the Fragment class of Android that is called after the fragment's view has been created.
     * It is used to initialize the views that belong to the fragment's layout.
     *
     * @param view               The root view of the fragment's layout hierarchy.
     * @param savedInstanceState A bundle that contains the saved state of the fragment, which includes
     *                           information such as the fragment's UI state, variables, and values.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webChatBinding.backToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(WebChatFragment.this)
                        .navigate(R.id.WebChat_to_WelcomePage);
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
        webChatBinding = null;
    }

}
