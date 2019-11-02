## libtaskbar

**libtaskbar allows you to quickly and easily add support for Android 10’s Desktop Mode to any third-party launcher, powered by the Taskbar app.**

It's a plug-and-play solution that is lightweight (less than 0.5 MB) and doesn't require Taskbar to already be installed.  libtaskbar gives your users on Android 10 a fully-featured desktop-style experience with a taskbar, start menu, desktop icons, and more, while being unobtrusive to your launcher's existing phone or tablet experience.

### Setup

Adding Desktop Mode support to your existing launcher is as easy as including these lines in your build.gradle file:

```
repositories {
    jcenter()
}

dependencies {
    implementation 'com.farmerbb:libtaskbar:1.0.0'
}
```

That's it!  As long as your launcher is set as the system default, and the user has enabled the "Force desktop mode" developer option, then Taskbar will appear whenever the user plugs in their HDMI-enabled phone into an external display.

### Additional configuration

You may wish to include a link to the Taskbar settings inside your launcher's settings UI.  To open Taskbar's settings, simply call this function:

    Taskbar.openSettings(context)

If desired, you can also supply a title to show in the top level of the Taskbar settings page, as well as a theme to apply to the activity:

    Taskbar.openSettings(context, "Desktop Mode Settings", R.style.AppTheme)

Finally, while Taskbar's desktop mode functionality is enabled out-of-the-box, it can be programmatically enabled and disabled by calling:

    Taskbar.setEnabled(true)

### Things to consider

* libtaskbar doesn't include any UI for informing the user to enable the "Force desktop mode" developer option.  You may wish to include a setup flow inside your launcher to guide the user with enabling the option.  Note that a reboot is required for the option to take effect.  Enabling the "Enable freeform windows" option is also strongly recommended when enabling desktop mode.

* libtaskbar will add the `SYSTEM_ALERT_WINDOW` and `PACKAGE_USAGE_STATS` permissions to your app's manifest, as well as a small number of non-runtime permissions for additional functionality such as displaying a status area on the taskbar.  As a result, your app will appear inside the "Display over other apps" and "Usage access" sections of the "Special app access" page in Android's settings.

* libtaskbar's only transitive dependencies are support-v4, appcompat-v7, and the design support library.  If you're using AndroidX, you'll need to make sure the Jetifier is enabled inside your gradle.properties file. (Taskbar doesn't use AndroidX because it must remain buildable from AOSP source on 8.1 and later; AndroidX wasn't included in AOSP until 9.0)

* If aapt complains about any resource conflicts, you may need to exclude the `com.android.support:design` transitive dependency from libtaskbar inside your build.gradle file.

### Example implementation

An example implementation of libtaskbar using Lawnchair is available at https://github.com/farmerbb/libtaskbar-Lawnchair-Example.  

You can also download a prebuilt APK here: https://github.com/farmerbb/libtaskbar-Lawnchair-Example/releases