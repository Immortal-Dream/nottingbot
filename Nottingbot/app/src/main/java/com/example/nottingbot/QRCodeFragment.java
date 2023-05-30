package com.example.nottingbot;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.nottingbot.databinding.QrCodeBinding;

import java.io.OutputStream;

/**
 * Share QR code page, user can show and download QR code for installation
 *
 * @author Zeyu WEI
 */
public class QRCodeFragment extends Fragment {
    private QrCodeBinding qrCodeBinding;

    /**
     * This method is used to save image into the local gallery
     *
     * @param context   context object
     * @param imageView ImageView object that displays the image to be saved
     * @param imageName a String representing the desired name for saved image.
     */
    public void saveImageToGallery(Context context, ImageView imageView, String imageName) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

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

            Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving image", Toast.LENGTH_SHORT).show();
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
        qrCodeBinding = QrCodeBinding.inflate(inflater,container,false);
        return qrCodeBinding.getRoot();
    }

    /**
     * The implementation of onViewCreated()
     *
     * @param view  the root View of fragment's layout hierarchy
     * @param savedInstanceState Bundle object
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        qrCodeBinding.saveQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView = getView().findViewById(R.id.imageView2);
                saveImageToGallery(requireContext(), imageView, "image_name.jpg");
            }
        });
        qrCodeBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(QRCodeFragment.this)
                        .navigate(R.id.SaveQRCode_to_SecondFragment);
            }
        });
    }
    
    /**
     * The implementation of onDestroyView
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        qrCodeBinding = null;
    }
}
