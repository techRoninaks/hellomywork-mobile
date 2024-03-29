package com.roninaks.hellomywork.helpers;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.roninaks.hellomywork.R;

public class TransparentProgressDialog extends Dialog {

    private ImageView iv;
    //private TextView tv;

    public TransparentProgressDialog(Context context) {
        super(context);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.alpha = 0.8f;
        getWindow().getDecorView().setBackgroundColor(context.getResources().getColor(android.R.color.transparent));

        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);

        View layout = getLayoutInflater().inflate(R.layout.transparent_progress_dialog, null);
        iv = layout.findViewById(R.id.imageView);
                Glide.with(context)
                .load(R.raw.logo_spinner)
                .into(iv);
        addContentView(layout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void show() {
        super.show();
    }
}
