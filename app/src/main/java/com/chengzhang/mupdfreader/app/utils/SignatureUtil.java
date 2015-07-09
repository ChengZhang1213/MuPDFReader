package com.chengzhang.mupdfreader.app.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import com.chengzhang.mupdfreader.app.MuPDFCore;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * Created by zhangcheng on 15/7/9.
 */
public class SignatureUtil {
    private Context context;
    private MuPDFCore core;

    public SignatureUtil(Context context, MuPDFCore core) {
        this.context = context;
        this.core = core;
    }

    public void signPDFWithWritePad(float x, float y) {
        //the window default size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;
        int heightPixels = displayMetrics.heightPixels;
        //the pdf default size
        float pageW = core.getPageW();
        float pageH = core.getpageH();

        float showH = pageH * widthPixels / pageW;
        int topY = (int) ((heightPixels - showH) / 2);
        int bottomY = heightPixels - topY;
        if (y > topY && y < bottomY) {
            try {
                String srcPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/DngNoiseModel.pdf";
                String desPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/DngNoiseModel_1.pdf";
                String keyStorePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/test443_sign.pfx";
                String keyStorePassword = "";
                String keyPassword = "";
                PdfReader reader = new PdfReader(srcPath);
                Rectangle pageSize = reader.getPageSize(1);
                float top = pageSize.getTop();
                float right = pageSize.getRight();
                Image image = Image.getInstance(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/signTest1.png");
                float imageWidth = image.getWidth();
                float imageHeight = image.getHeight();
                int transX = (int) (x * right / widthPixels);
                int transY = (int) ((heightPixels - y) * top / heightPixels);
                Rectangle rectangle = new Rectangle(transX - imageWidth / 4, transY - imageHeight / 4, transX - imageWidth / 4, transY + imageHeight / 4);
                KeyStore keyStore = KeyStore.getInstance("pkcs12", "BC");
                keyStore.load(new FileInputStream(keyStorePath), keyStorePassword.toCharArray());
                String aliases = keyStore.aliases().nextElement();
                PrivateKey key = (PrivateKey) keyStore.getKey(aliases, keyPassword.toCharArray());
                Certificate[] certificateChain = keyStore.getCertificateChain(aliases);
                //stamper
                FileOutputStream fileOutputStream = new FileOutputStream(desPath);
                PdfStamper signature = PdfStamper.createSignature(reader, fileOutputStream, '\0', null, true);
                //appearance
                PdfSignatureAppearance signatureAppearance = signature.getSignatureAppearance();
                signatureAppearance.setSignatureGraphic(image);
                signatureAppearance.setReason("Do something!");
                signatureAppearance.setLocation("Foobar");
                signatureAppearance.setVisibleSignature(rectangle, 1, "First");
                signatureAppearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
                //digital signature
                ExternalSignature externalSignature = new PrivateKeySignature(key, "SHA-256", "BC");
                BouncyCastleDigest digest = new BouncyCastleDigest();
                MakeSignature.signDetached(signatureAppearance, digest, externalSignature, certificateChain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);
                System.out.println("success to signature--------");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //System.out.println(pageW+"----"+pageH+"----"+widthPixels+",----"+heightPixels);
        // System.out.println("leavey = "+leaveY+"bottomY = "+bottomY);

    }
}
