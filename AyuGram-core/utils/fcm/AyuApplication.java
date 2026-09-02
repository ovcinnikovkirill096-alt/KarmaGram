package com.radolyn.ayugram.utils.fcm;

import android.app.Application;
import android.content.AttributionSource;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextParams;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.IntConsumer;

public class AyuApplication extends Application {
    private final Application app;

    public AyuApplication(Application application) {
        this.app = application;
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        this.app.onConfigurationChanged(configuration);
    }

    @Override // android.app.Application
    public void onCreate() {
        this.app.onCreate();
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onLowMemory() {
        this.app.onLowMemory();
    }

    @Override // android.app.Application
    public void onTerminate() {
        this.app.onTerminate();
    }

    @Override // android.app.Application, android.content.ComponentCallbacks2
    public void onTrimMemory(int i) {
        this.app.onTrimMemory(i);
    }

    @Override // android.app.Application
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        this.app.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    @Override // android.app.Application, android.content.ContextWrapper, android.content.Context
    public void registerComponentCallbacks(ComponentCallbacks componentCallbacks) {
        this.app.registerComponentCallbacks(componentCallbacks);
    }

    @Override // android.app.Application
    public void registerOnProvideAssistDataListener(Application.OnProvideAssistDataListener onProvideAssistDataListener) {
        this.app.registerOnProvideAssistDataListener(onProvideAssistDataListener);
    }

    @Override // android.app.Application
    public void unregisterActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        this.app.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    @Override // android.app.Application, android.content.ContextWrapper, android.content.Context
    public void unregisterComponentCallbacks(ComponentCallbacks componentCallbacks) {
        this.app.unregisterComponentCallbacks(componentCallbacks);
    }

    @Override // android.app.Application
    public void unregisterOnProvideAssistDataListener(Application.OnProvideAssistDataListener onProvideAssistDataListener) {
        this.app.unregisterOnProvideAssistDataListener(onProvideAssistDataListener);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean bindIsolatedService(Intent intent, int i, String str, Executor executor, ServiceConnection serviceConnection) {
        return this.app.bindIsolatedService(intent, i, str, executor, serviceConnection);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean bindService(Intent intent, Context.BindServiceFlags bindServiceFlags, Executor executor, ServiceConnection serviceConnection) {
        return this.app.bindService(intent, bindServiceFlags, executor, serviceConnection);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, Context.BindServiceFlags bindServiceFlags) {
        return this.app.bindService(intent, serviceConnection, bindServiceFlags);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean bindService(Intent intent, int i, Executor executor, ServiceConnection serviceConnection) {
        return this.app.bindService(intent, i, executor, serviceConnection);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean bindServiceAsUser(Intent intent, ServiceConnection serviceConnection, Context.BindServiceFlags bindServiceFlags, UserHandle userHandle) {
        return this.app.bindServiceAsUser(intent, serviceConnection, bindServiceFlags, userHandle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean bindServiceAsUser(Intent intent, ServiceConnection serviceConnection, int i, UserHandle userHandle) {
        return this.app.bindServiceAsUser(intent, serviceConnection, i, userHandle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int checkCallingOrSelfPermission(String str) {
        return this.app.checkCallingOrSelfPermission(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int checkCallingOrSelfUriPermission(Uri uri, int i) {
        return this.app.checkCallingOrSelfUriPermission(uri, i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int[] checkCallingOrSelfUriPermissions(List list, int i) {
        return this.app.checkCallingOrSelfUriPermissions(list, i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int checkCallingPermission(String str) {
        return this.app.checkCallingPermission(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int checkCallingUriPermission(Uri uri, int i) {
        return this.app.checkCallingUriPermission(uri, i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int[] checkCallingUriPermissions(List list, int i) {
        return this.app.checkCallingUriPermissions(list, i);
    }

    public int checkContentUriPermissionFull(Uri uri, int i, int i2, int i3) {
        return this.app.checkContentUriPermissionFull(uri, i, i2, i3);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int checkPermission(String str, int i, int i2) {
        return this.app.checkPermission(str, i, i2);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int checkSelfPermission(String str) {
        return this.app.checkSelfPermission(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int checkUriPermission(Uri uri, int i, int i2, int i3) {
        return this.app.checkUriPermission(uri, i, i2, i3);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int checkUriPermission(Uri uri, String str, String str2, int i, int i2, int i3) {
        return this.app.checkUriPermission(uri, str, str2, i, i2, i3);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int[] checkUriPermissions(List list, int i, int i2, int i3) {
        return this.app.checkUriPermissions(list, i, i2, i3);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void clearWallpaper() throws IOException {
        this.app.clearWallpaper();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Context createAttributionContext(String str) {
        return this.app.createAttributionContext(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Context createConfigurationContext(Configuration configuration) {
        return this.app.createConfigurationContext(configuration);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Context createContext(ContextParams contextParams) {
        return this.app.createContext(contextParams);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Context createContextForSplit(String str) {
        return this.app.createContextForSplit(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Context createDeviceContext(int i) {
        return this.app.createDeviceContext(i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Context createDeviceProtectedStorageContext() {
        return this.app.createDeviceProtectedStorageContext();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Context createDisplayContext(Display display) {
        return this.app.createDisplayContext(display);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Context createPackageContext(String str, int i) {
        return this.app.createPackageContext(str, i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Context createWindowContext(Display display, int i, Bundle bundle) {
        return this.app.createWindowContext(display, i, bundle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Context createWindowContext(int i, Bundle bundle) {
        return this.app.createWindowContext(i, bundle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public String[] databaseList() {
        return this.app.databaseList();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean deleteDatabase(String str) {
        return this.app.deleteDatabase(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean deleteFile(String str) {
        return this.app.deleteFile(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean deleteSharedPreferences(String str) {
        return this.app.deleteSharedPreferences(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void enforceCallingOrSelfPermission(String str, String str2) {
        this.app.enforceCallingOrSelfPermission(str, str2);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void enforceCallingOrSelfUriPermission(Uri uri, int i, String str) {
        this.app.enforceCallingOrSelfUriPermission(uri, i, str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void enforceCallingPermission(String str, String str2) {
        this.app.enforceCallingPermission(str, str2);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void enforceCallingUriPermission(Uri uri, int i, String str) {
        this.app.enforceCallingUriPermission(uri, i, str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void enforcePermission(String str, int i, int i2, String str2) {
        this.app.enforcePermission(str, i, i2, str2);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void enforceUriPermission(Uri uri, int i, int i2, int i3, String str) {
        this.app.enforceUriPermission(uri, i, i2, i3, str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void enforceUriPermission(Uri uri, String str, String str2, int i, int i2, int i3, String str3) {
        this.app.enforceUriPermission(uri, str, str2, i, i2, i3, str3);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public String[] fileList() {
        return this.app.fileList();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Context getApplicationContext() {
        return this.app.getApplicationContext();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public ApplicationInfo getApplicationInfo() {
        return this.app.getApplicationInfo();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public AssetManager getAssets() {
        return this.app.getAssets();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public AttributionSource getAttributionSource() {
        return this.app.getAttributionSource();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public String getAttributionTag() {
        return this.app.getAttributionTag();
    }

    @Override // android.content.ContextWrapper
    public Context getBaseContext() {
        return this.app.getBaseContext();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File getCacheDir() {
        return this.app.getCacheDir();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public ClassLoader getClassLoader() {
        return this.app.getClassLoader();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File getCodeCacheDir() {
        return this.app.getCodeCacheDir();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public ContentResolver getContentResolver() {
        return this.app.getContentResolver();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File getDataDir() {
        return this.app.getDataDir();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File getDatabasePath(String str) {
        return this.app.getDatabasePath(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int getDeviceId() {
        return this.app.getDeviceId();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File getDir(String str, int i) {
        return this.app.getDir(str, i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Display getDisplay() {
        return this.app.getDisplay();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File getExternalCacheDir() {
        return this.app.getExternalCacheDir();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File[] getExternalCacheDirs() {
        return this.app.getExternalCacheDirs();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File getExternalFilesDir(String str) {
        return this.app.getExternalFilesDir(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File[] getExternalFilesDirs(String str) {
        return this.app.getExternalFilesDirs(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File[] getExternalMediaDirs() {
        return this.app.getExternalMediaDirs();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File getFileStreamPath(String str) {
        return this.app.getFileStreamPath(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File getFilesDir() {
        return this.app.getFilesDir();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Executor getMainExecutor() {
        return this.app.getMainExecutor();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Looper getMainLooper() {
        return this.app.getMainLooper();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File getNoBackupFilesDir() {
        return this.app.getNoBackupFilesDir();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File getObbDir() {
        return this.app.getObbDir();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public File[] getObbDirs() {
        return this.app.getObbDirs();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public String getOpPackageName() {
        return this.app.getOpPackageName();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public String getPackageCodePath() {
        return this.app.getPackageCodePath();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public PackageManager getPackageManager() {
        return this.app.getPackageManager();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public String getPackageName() {
        return "org.telegram.messenger.web";
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public String getPackageResourcePath() {
        return this.app.getPackageResourcePath();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public ContextParams getParams() {
        return this.app.getParams();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources getResources() {
        return this.app.getResources();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public SharedPreferences getSharedPreferences(String str, int i) {
        return this.app.getSharedPreferences(str, i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Object getSystemService(String str) {
        return this.app.getSystemService(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public String getSystemServiceName(Class cls) {
        return this.app.getSystemServiceName(cls);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Resources.Theme getTheme() {
        return this.app.getTheme();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Drawable getWallpaper() {
        return this.app.getWallpaper();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int getWallpaperDesiredMinimumHeight() {
        return this.app.getWallpaperDesiredMinimumHeight();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public int getWallpaperDesiredMinimumWidth() {
        return this.app.getWallpaperDesiredMinimumWidth();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void grantUriPermission(String str, Uri uri, int i) {
        this.app.grantUriPermission(str, uri, i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean isDeviceProtectedStorage() {
        if (Build.VERSION.SDK_INT >= 24) {
            return this.app.isDeviceProtectedStorage();
        }
        return false;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean isRestricted() {
        return this.app.isRestricted();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean isUiContext() {
        return this.app.isUiContext();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean moveDatabaseFrom(Context context, String str) {
        return this.app.moveDatabaseFrom(context, str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean moveSharedPreferencesFrom(Context context, String str) {
        return this.app.moveSharedPreferencesFrom(context, str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public FileInputStream openFileInput(String str) {
        return this.app.openFileInput(str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public FileOutputStream openFileOutput(String str, int i) {
        return this.app.openFileOutput(str, i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public SQLiteDatabase openOrCreateDatabase(String str, int i, SQLiteDatabase.CursorFactory cursorFactory) {
        return this.app.openOrCreateDatabase(str, i, cursorFactory);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public SQLiteDatabase openOrCreateDatabase(String str, int i, SQLiteDatabase.CursorFactory cursorFactory, DatabaseErrorHandler databaseErrorHandler) {
        return this.app.openOrCreateDatabase(str, i, cursorFactory, databaseErrorHandler);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Drawable peekWallpaper() {
        return this.app.peekWallpaper();
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void registerDeviceIdChangeListener(Executor executor, IntConsumer intConsumer) {
        this.app.registerDeviceIdChangeListener(executor, intConsumer);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        return this.app.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, int i) {
        return this.app.registerReceiver(broadcastReceiver, intentFilter, i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, String str, Handler handler, int i) {
        return this.app.registerReceiver(broadcastReceiver, intentFilter, str, handler, i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void removeStickyBroadcast(Intent intent) {
        this.app.removeStickyBroadcast(intent);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {
        this.app.removeStickyBroadcastAsUser(intent, userHandle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void revokeSelfPermissionsOnKill(Collection collection) {
        this.app.revokeSelfPermissionsOnKill(collection);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void revokeUriPermission(String str, Uri uri, int i) {
        this.app.revokeUriPermission(str, uri, i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void revokeUriPermission(Uri uri, int i) {
        this.app.revokeUriPermission(uri, i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void sendBroadcast(Intent intent) {
        this.app.sendBroadcast(intent);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void sendBroadcast(Intent intent, String str) {
        this.app.sendBroadcast(intent, str);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void sendBroadcast(Intent intent, String str, Bundle bundle) {
        this.app.sendBroadcast(intent, str, bundle);
    }

    @Override // android.content.ContextWrapper
    public void sendOrderedBroadcast(Intent intent, int i, String str, String str2, BroadcastReceiver broadcastReceiver, Handler handler, String str3, Bundle bundle, Bundle bundle2) {
        this.app.sendOrderedBroadcast(intent, i, str, str2, broadcastReceiver, handler, str3, bundle, bundle2);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void sendOrderedBroadcast(Intent intent, String str, Bundle bundle) {
        this.app.sendOrderedBroadcast(intent, str, bundle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void sendOrderedBroadcast(Intent intent, String str, Bundle bundle, BroadcastReceiver broadcastReceiver, Handler handler, int i, String str2, Bundle bundle2) {
        this.app.sendOrderedBroadcast(intent, str, bundle, broadcastReceiver, handler, i, str2, bundle2);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void sendOrderedBroadcast(Intent intent, String str, String str2, BroadcastReceiver broadcastReceiver, Handler handler, int i, String str3, Bundle bundle) {
        this.app.sendOrderedBroadcast(intent, str, str2, broadcastReceiver, handler, i, str3, bundle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void sendStickyBroadcast(Intent intent) {
        this.app.sendStickyBroadcast(intent);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void sendStickyBroadcast(Intent intent, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 31) {
            this.app.sendStickyBroadcast(intent, bundle);
        }
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle userHandle) {
        this.app.sendStickyBroadcastAsUser(intent, userHandle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver broadcastReceiver, Handler handler, int i, String str, Bundle bundle) {
        this.app.sendStickyOrderedBroadcast(intent, broadcastReceiver, handler, i, str, bundle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle userHandle, BroadcastReceiver broadcastReceiver, Handler handler, int i, String str, Bundle bundle) {
        this.app.sendStickyOrderedBroadcastAsUser(intent, userHandle, broadcastReceiver, handler, i, str, bundle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void setTheme(int i) {
        this.app.setTheme(i);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void setWallpaper(Bitmap bitmap) throws IOException {
        this.app.setWallpaper(bitmap);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void setWallpaper(InputStream inputStream) throws IOException {
        this.app.setWallpaper(inputStream);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void startActivities(Intent[] intentArr) {
        this.app.startActivities(intentArr);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void startActivities(Intent[] intentArr, Bundle bundle) {
        this.app.startActivities(intentArr, bundle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void startActivity(Intent intent) {
        this.app.startActivity(intent);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void startActivity(Intent intent, Bundle bundle) {
        this.app.startActivity(intent, bundle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public ComponentName startForegroundService(Intent intent) {
        if (Build.VERSION.SDK_INT >= 26) {
            return this.app.startForegroundService(intent);
        }
        return null;
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean startInstrumentation(ComponentName componentName, String str, Bundle bundle) {
        return this.app.startInstrumentation(componentName, str, bundle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void startIntentSender(IntentSender intentSender, Intent intent, int i, int i2, int i3) throws IntentSender.SendIntentException {
        this.app.startIntentSender(intentSender, intent, i, i2, i3);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void startIntentSender(IntentSender intentSender, Intent intent, int i, int i2, int i3, Bundle bundle) throws IntentSender.SendIntentException {
        this.app.startIntentSender(intentSender, intent, i, i2, i3, bundle);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public ComponentName startService(Intent intent) {
        return this.app.startService(intent);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public boolean stopService(Intent intent) {
        return this.app.stopService(intent);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void unbindService(ServiceConnection serviceConnection) {
        this.app.unbindService(serviceConnection);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void unregisterDeviceIdChangeListener(IntConsumer intConsumer) {
        this.app.unregisterDeviceIdChangeListener(intConsumer);
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void updateServiceGroup(ServiceConnection serviceConnection, int i, int i2) {
        this.app.updateServiceGroup(serviceConnection, i, i2);
    }

    @Override // android.content.Context
    public boolean bindIsolatedService(Intent intent, Context.BindServiceFlags bindServiceFlags, String str, Executor executor, ServiceConnection serviceConnection) {
        return this.app.bindIsolatedService(intent, bindServiceFlags, str, executor, serviceConnection);
    }

    @Override // android.content.Context
    public void revokeSelfPermissionOnKill(String str) {
        this.app.revokeSelfPermissionOnKill(str);
    }

    @Override // android.content.Context
    public void sendBroadcastWithMultiplePermissions(Intent intent, String[] strArr) {
        this.app.sendBroadcastWithMultiplePermissions(intent, strArr);
    }
}
