package com.chengzhang.mupdfreader.app.ui;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by zhangcheng on 15/7/8.
 */
public class ProgressDialogX extends ProgressDialog {
    public ProgressDialogX(Context context) {
        super(context);
    }

    private boolean mCancelled = false;

    public boolean isCancelled() {
        return mCancelled;
    }

    @Override
    public void cancel() {
        mCancelled = true;
        super.cancel();
    }
}
