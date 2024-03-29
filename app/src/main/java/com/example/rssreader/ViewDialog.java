package com.example.rssreader;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

class ViewDialog {
    private Activity activity;
    private Dialog dialog;

    ViewDialog(Activity activity) {
        this.activity = activity;
    }

    void showDialog() {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_layout);

        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

//        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        dialog.getWindow().setBackgroundDrawableResource(R.color.colorAccent);

        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

        Glide.with(activity)
                .load(R.drawable.loading)
                .placeholder(R.drawable.loading)
                .centerCrop()
                .crossFade()
                .into(imageViewTarget);

        dialog.show();
    }

    void hideDialog(){
        dialog.dismiss();
    }
}
