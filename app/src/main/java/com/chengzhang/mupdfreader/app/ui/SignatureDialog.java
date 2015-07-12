package com.chengzhang.mupdfreader.app.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import com.chengzhang.mupdfreader.app.MuPDFCore;
import com.chengzhang.mupdfreader.app.R;
import com.chengzhang.mupdfreader.app.utils.SignatureUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Steven on 2015/5/14.
 * describe:
 */
public class SignatureDialog extends Dialog {
    private Context context;
    private float x;
    private float y;
    private MuPDFCore core;
    public SignatureDialog(Context context,float x, float y,MuPDFCore core) {
        super(context);
        this.context = context;
        this.x = x;
        this.y = y;
        this.core = core;
    }

    public SignatureDialog(Context context, int theme) {
        super(context, theme);
    }

    protected SignatureDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_signaturepad);
        final SignatureView signatureView = (SignatureView) findViewById(R.id.signatureView);
        findViewById(R.id.btn_sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap signatureBitmap = signatureView.getSignatureBitmap();
                if(addSignatureToGallery(signatureBitmap)) {
                    Log.i("SignatureDialog", "success");
                    SignatureUtil signatureUtil  = new SignatureUtil(context,core);
                    signatureUtil.signPDFWithWritePad(x,y);
                    dismiss();
                } else {
                    Log.e("SignatureDialog","failed");
                }

            }
        });
        findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clear();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("SignatureDialog","dismiss");
    }
    public File getAlbumStorageDir(String albumName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
//        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        newBitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        stream.close();
    }

    public boolean addSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
           // File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            File photo = new File(getAlbumStorageDir("SignaturePad"), "signTest1.png");
            saveBitmapToJPG(signature, photo);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(photo);
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
