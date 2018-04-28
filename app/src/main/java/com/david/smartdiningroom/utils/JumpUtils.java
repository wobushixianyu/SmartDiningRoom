package com.david.smartdiningroom.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class JumpUtils {

    public static void JumpActivity(Activity activity, Class<?> clz, Bundle bundle, boolean isFinish){
        Intent intent = new Intent(activity,clz);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        if (isFinish){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.finishAfterTransition();
            }else {
                activity.finish();
            }
        }
    }
}
