package com.example.nottingbot;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;


import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Implement AR 3D models using sceneform.
 *
 * @author Junhan LIU
 */
public class ARFragment extends Fragment {

    /**
     * ArFragment is a class in the ARCore library for Android that provides
     * an implementation of a fragment for AR applications.
     */
    private ArFragment arFragment;
    /**
     * ModelRenderable is a class in the Sceneform SDK for Android that represents
     * a 3D model that can be rendered in an AR scene.
     */
    private ModelRenderable controlViewRenderable;
    /**
     * 3D avatars' ids
     */
    private final int[] avatarIDs = {R.raw.avatar1, R.raw.avatar3};

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
        return inflater.inflate(R.layout.ar_page, container, false);
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
        int avatarID = avatarIDs[0];
        SharedPreferences sp = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        int savedAvatar = sp.getInt("SavedAvatar", 0);
        if (savedAvatar == 3) {
            avatarID = avatarIDs[1];
        }
        if (!checkIsSupportedDeviceOrFinish(getActivity())) {
            return;
        }

        arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
        /*
         * This builder class is used to construct a ModelRenderable, which represents a 3D model
         * that can be rendered in an AR scene
         */
        ModelRenderable.builder()
                .setSource(requireActivity(), avatarID)
                .build()
                .thenAccept(renderable -> controlViewRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(getActivity(), "Unable to load renderable",
                                            Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });

        /*
         * It sets a listener that is called when a tap gesture is detected on a plane in the AR
         * scene. The method takes an instance of BaseArFragment.OnTapArPlaneListener
         */
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (controlViewRenderable == null) {
                        return;
                    }
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    TransformableNode andy = new TransformableNode(
                            arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(controlViewRenderable);
                    andy.select();
                });
        ImageButton imageButton = requireActivity().findViewById(R.id.arToWelcome);
        ImageButton captureButton = requireActivity().findViewById(R.id.takePhoto);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ARFragment.this)
                        .navigate(R.id.AR_to_WelcomePage);
            }
        });
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date now = new Date();
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(now);
                saveArScreenshotToGallery(requireContext(), arFragment, timeStamp + "avatar");
            }
        });
    }

    /**
     * Checks if the device is supported by Sceneform or not. If the device is not supported, this method
     * displays a Toast message and finishes the activity.
     *
     * @param activity The activity where this method is called from.
     * @return true if the device is supported, false otherwise.
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {

            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {

            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    /**
     * Minimum openGL version
     */
    private static final double MIN_OPENGL_VERSION = 3.0;

    /**
     * Takes a screenshot of the current arFragment, saves it as a JPEG image in the device's external storage directory,
     * and then opens the saved screenshot. The filename is determined by the current date and time.
     * This method will only capture the contents of the Fragment that this method is called from.
     *
     * @param context    The context of the calling activity or fragment.
     * @param arFragment The ARFragment containing the AR scene to capture.
     * @param imageName  The desired name of the saved image.
     */
    public void saveArScreenshotToGallery(Context context, ArFragment arFragment, String imageName) {
        // Create a bitmap from the AR Scene
        ArSceneView view = arFragment.getArSceneView();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, imageName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
                }

                ContentResolver resolver = context.getContentResolver();
                Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                try {
                    OutputStream outputStream = resolver.openOutputStream(uri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();

                    Toast.makeText(context, "AR photo saved to gallery", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error saving AR photo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Failed to capture AR photo", Toast.LENGTH_SHORT).show();
            }
        }, new Handler(Looper.getMainLooper()));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}