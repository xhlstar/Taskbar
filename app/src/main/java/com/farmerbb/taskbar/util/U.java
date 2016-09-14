/* Copyright 2016 Braden Farmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.farmerbb.taskbar.util;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Display;
import android.widget.Toast;

import com.farmerbb.taskbar.BuildConfig;
import com.farmerbb.taskbar.R;
import com.farmerbb.taskbar.activity.DummyActivity;
import com.farmerbb.taskbar.receiver.LockDeviceReceiver;
import com.farmerbb.taskbar.service.NotificationService;
import com.farmerbb.taskbar.service.StartMenuService;
import com.farmerbb.taskbar.service.TaskbarService;

public class U {

    private U() {}

    private static SharedPreferences pref;
    private static Toast toast;

    public static SharedPreferences getSharedPreferences(Context context) {
        if(pref == null) pref = context.getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", Context.MODE_PRIVATE);
        return pref;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void showPermissionDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.permission_dialog_title)
                .setMessage(R.string.permission_dialog_message)
                .setPositiveButton(R.string.action_grant_permission, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            context.startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
                        } catch (ActivityNotFoundException e) {
                            showErrorDialog(context, "SYSTEM_ALERT_WINDOW");
                        }
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
    }

    public static void showErrorDialog(final Context context, String appopCmd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.error_dialog_title)
                .setMessage(context.getString(R.string.error_dialog_message, BuildConfig.APPLICATION_ID, appopCmd))
                .setPositiveButton(R.string.action_ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void lockDevice(Context context) {
        ComponentName component = new ComponentName(BuildConfig.APPLICATION_ID, LockDeviceReceiver.class.getName());
        context.getPackageManager().setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        DevicePolicyManager mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        if(mDevicePolicyManager.isAdminActive(component))
            mDevicePolicyManager.lockNow();
        else {
            Intent intent = new Intent(context, DummyActivity.class);
            intent.putExtra("device_admin", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public static void showToast(Context context, int message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void showToastLong(Context context, int message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    private static void showToast(Context context, int message, int length) {
        if(toast != null) toast.cancel();

        toast = Toast.makeText(context, context.getString(message), length);
        toast.show();
    }

    public static void launchStandard(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) { /* Gracefully fail */ }
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.N)
    public static void launchFullscreen(Context context, Intent intent, boolean padStatusBar) {
        DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display display = dm.getDisplay(Display.DEFAULT_DISPLAY);

        int statusBarHeight = 0;
        if(padStatusBar) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if(resourceId > 0)
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }

        int left = 0;
        int top = statusBarHeight;
        int right = display.getWidth();
        int bottom = display.getHeight();
        int iconSize = context.getResources().getDimensionPixelSize(R.dimen.icon_size);

        SharedPreferences pref = getSharedPreferences(context);
        String position = pref.getString("position", "bottom_left");

        if(position.contains("vertical_left"))
            left = left + iconSize;
        else if(position.contains("vertical_right"))
            right = right - iconSize;
        else
            bottom = bottom - iconSize;

        try {
            context.startActivity(intent, ActivityOptions.makeBasic().setLaunchBounds(new Rect(
                    left,
                    top,
                    right,
                    bottom
            )).toBundle());
        } catch (ActivityNotFoundException e) { /* Gracefully fail */ }
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.N)
    public static void launchPhoneSize(Context context, Intent intent) {
        DisplayManager dm = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display display = dm.getDisplay(Display.DEFAULT_DISPLAY);

        int width1 = display.getWidth() / 2;
        int width2 = context.getResources().getDimensionPixelSize(R.dimen.phone_size_width) / 2;
        int height1 = display.getHeight() / 2;
        int height2 = context.getResources().getDimensionPixelSize(R.dimen.phone_size_height) / 2;

        try {
            context.startActivity(intent, ActivityOptions.makeBasic().setLaunchBounds(new Rect(
                    width1 - width2,
                    height1 - height2,
                    width1 + width2,
                    height1 + height2
            )).toBundle());
        } catch (ActivityNotFoundException e) { /* Gracefully fail */ }
    }

    public static void checkForUpdates(Context context) {
        String url;
        try {
            context.getPackageManager().getPackageInfo("com.android.vending", 0);
            url = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
        } catch (PackageManager.NameNotFoundException e) {
            url = "https://f-droid.org/repository/browse/?fdid=" + BuildConfig.APPLICATION_ID;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) { /* Gracefully fail */ }
    }

    public static void startTaskbar(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        if(!pref.getBoolean("taskbar_active", false)) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("is_hidden", false);

            if(pref.getBoolean("first_run", true)) {
                editor.putBoolean("first_run", false);
                editor.putBoolean("collapsed", true);
            }

            editor.putBoolean("taskbar_active", true);
            editor.putLong("time_of_service_start", System.currentTimeMillis());
            editor.apply();

            context.startService(new Intent(context, TaskbarService.class));
            context.startService(new Intent(context, StartMenuService.class));
            context.startService(new Intent(context, NotificationService.class));
        }
    }
}
