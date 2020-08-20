package com.cynoteck.HeadAct;

import android.app.Activity;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Permission {

    public static int checkPermission(Context context, String permission) {

        return ContextCompat.checkSelfPermission(context, permission);
    }

    public static boolean checkPermissions(Context context, String[] permissionsToGrant, int PERMISSION_REQUEST_CODE) {
        ArrayList<String> permissions = new ArrayList<>();
        for (String permission : permissionsToGrant)
        {
            if (checkPermission(context, permission) != 0)
            {
                permissions.add(permission);
            }
        }
        if (permissions.size() == 0)
        {
            return true;
        }
        ActivityCompat.requestPermissions((Activity) context, permissions.toArray(new String[permissions.size()]), PERMISSION_REQUEST_CODE);
        return false;
    }
}