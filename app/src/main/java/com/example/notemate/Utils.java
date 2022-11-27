package com.example.notemate;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;
import com.example.notemate.Models.UserData;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.io.*;
import java.security.*;

class MD5Util {
    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    public static String md5Hex (String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex (md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
}

public class Utils {

    private final static String TAG = "Utils.java";

    public static void openHome(Context context)
    {
        context.startActivity(new Intent(context, HomeActivity.class));
    }


    public static void userSignedIn(Context context)
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if ( firebaseAuth.getCurrentUser() != null )
        {
            openHome(context);
        }
        else{
            Toast.makeText(context, "User not authorised.", Toast.LENGTH_SHORT).show();
//            context.startActivity(new Intent(context, LoginActivity.class));
        }
    }

    public static String profileImage(String uid)
    {
        String email = "someone@somewhere.com";
        String hash = MD5Util.md5Hex(email);
        return "https://www.gravatar.com/avatar/"+hash;
    }


    public static void openPdf(Context context, Uri pdfUri)
    {
        String mime = "application/pdf";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, mime);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try{
            context.startActivity(intent);
        }
        catch ( ActivityNotFoundException e)
        {
            Toast.makeText(context, "No pdf viewer found.", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onCreate: " + e.getMessage());
        }
    }


    public static Bitmap getBitmapFromPdf(Context context, Uri pdfUri)
    {
        Bitmap sol=null;
        try {
            File file = FileUtil.from(context, pdfUri);
            Log.d("file", "File...:::: uti - "+file .getPath()+" file -" + file + " : " + file .exists());
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
            PdfRenderer.Page page = renderer.openPage(0);
            int width = context.getResources().getDisplayMetrics().densityDpi / 72 * page.getWidth();
            int height = context.getResources().getDisplayMetrics().densityDpi / 72 * page.getHeight();
            sol = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            page.render(sol, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            page.close();
        } catch (IOException e) {
            Toast.makeText(context, "e"+e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return sol;
    }


//    public static void checkIfUserData(Context context)
//    {
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//    }

}
