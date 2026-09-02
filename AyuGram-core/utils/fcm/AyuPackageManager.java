package com.radolyn.ayugram.utils.fcm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ArchivedPackageInfo;
import android.content.pm.ChangedPackages;
import android.content.pm.FeatureInfo;
import android.content.pm.InstallSourceInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.ModuleInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.SharedLibraryInfo;
import android.content.pm.Signature;
import android.content.pm.SigningInfo;
import android.content.pm.VersionedPackage;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.UserHandle;
import androidx.annotation.Keep;
import java.io.File;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;
import org.lsposed.lsparanoid.Deobfuscator$AyuGram4A$TMessagesProj;

@Keep
public class AyuPackageManager extends PackageManager {
    private final Signature originalSignature;
    private final PackageManager packageManager;

    public AyuPackageManager(PackageManager packageManager, Context context) {
        Signature signature;
        this.packageManager = packageManager;
        try {
            signature = packageManager.getPackageInfo(context.getPackageName(), 64).signatures[0];
        } catch (Throwable unused) {
            signature = null;
        }
        this.originalSignature = signature;
    }

    public Signature getOriginalSignature() {
        return this.originalSignature;
    }

    @Override // android.content.pm.PackageManager
    public PackageInfo getPackageInfo(String str, int i) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = this.packageManager.getPackageInfo(str, i);
        if (packageInfo.packageName.startsWith("org.telegram.messenger")) {
            packageInfo.signatures = new Signature[]{new Signature("3082021730820180a0030201020204521f9d49300d06092a864886f70d0101050500305031193017060355040713105361696e742d50657465727362757267310b3009060355040a1302564b310b3009060355040b1302564b31193017060355040313104e696b6f6c6179204b75646173686f76301e170d3133303832393139313331335a170d3338303832333139313331335a305031193017060355040713105361696e742d50657465727362757267310b3009060355040a1302564b310b3009060355040b1302564b31193017060355040313104e696b6f6c6179204b75646173686f7630819f300d06092a864886f70d010101050003818d0030818902818100df5e993a0dec0ab5b557dfff77e0b2227186cbf13d1fd1ed8e9deb5650c5fd4467bb51bfa585228d084bd27045f7415b7c4e38f08be362639a2eeb9b0c749da460f2705f6a7e14aca76abe3360af00b719cc5f3ff4d4da05958327e948b3679e6417ad7baa8779b9d689799ba345839a049fd44362499054a0803a0178c773790203010001300d06092a864886f70d010105050003818100dda58cdd90159c431ecc4a15902eafb07a50e01ba9d4f8e655ec14b06bd8e8771239710a28991039e02e352762eb524af07602bbdfb479d3718658a534d411dfab30122c8d0a5efd165a620669d80a221a04ac7d68b3811150c769cf97d3274be9b9f27c4c5877eabbcf8990409e5943df8deb509fa83d68eabc74f7c5976743")};
        }
        return packageInfo;
    }

    @Override // android.content.pm.PackageManager
    public PackageInfo getPackageInfo(String str, PackageManager.PackageInfoFlags packageInfoFlags) {
        return this.packageManager.getPackageInfo(str, packageInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public PackageInfo getPackageInfo(VersionedPackage versionedPackage, int i) {
        return this.packageManager.getPackageInfo(versionedPackage, i);
    }

    @Override // android.content.pm.PackageManager
    public PackageInfo getPackageInfo(VersionedPackage versionedPackage, PackageManager.PackageInfoFlags packageInfoFlags) {
        return this.packageManager.getPackageInfo(versionedPackage, packageInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public String[] currentToCanonicalPackageNames(String[] strArr) {
        return this.packageManager.currentToCanonicalPackageNames(strArr);
    }

    @Override // android.content.pm.PackageManager
    public String[] canonicalToCurrentPackageNames(String[] strArr) {
        return this.packageManager.canonicalToCurrentPackageNames(strArr);
    }

    @Override // android.content.pm.PackageManager
    public Intent getLaunchIntentForPackage(String str) {
        return this.packageManager.getLaunchIntentForPackage(str);
    }

    @Override // android.content.pm.PackageManager
    public Intent getLeanbackLaunchIntentForPackage(String str) {
        return this.packageManager.getLeanbackLaunchIntentForPackage(str);
    }

    @Override // android.content.pm.PackageManager
    public IntentSender getLaunchIntentSenderForPackage(String str) {
        return this.packageManager.getLaunchIntentSenderForPackage(str);
    }

    @Override // android.content.pm.PackageManager
    public int[] getPackageGids(String str) {
        return this.packageManager.getPackageGids(str);
    }

    @Override // android.content.pm.PackageManager
    public int[] getPackageGids(String str, int i) {
        return this.packageManager.getPackageGids(str, i);
    }

    @Override // android.content.pm.PackageManager
    public int[] getPackageGids(String str, PackageManager.PackageInfoFlags packageInfoFlags) {
        return this.packageManager.getPackageGids(str, packageInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public int getPackageUid(String str, int i) {
        return this.packageManager.getPackageUid(str, i);
    }

    @Override // android.content.pm.PackageManager
    public int getPackageUid(String str, PackageManager.PackageInfoFlags packageInfoFlags) {
        return this.packageManager.getPackageUid(str, packageInfoFlags);
    }

    public int getPackageUidAsUser(String str, int i) {
        return getPackageUidAsUser(str, 0, i);
    }

    public int getPackageUidAsUser(String str, int i, int i2) {
        return getPackageUidAsUser(str, PackageManager.PackageInfoFlags.of(i), i2);
    }

    public int getPackageUidAsUser(String str, PackageManager.PackageInfoFlags packageInfoFlags, int i) throws PackageManager.NameNotFoundException {
        try {
            try {
                return ((Integer) PackageManager.class.getMethod("getPackageUidAsUser", String.class, AyuPackageManager$$ExternalSyntheticApiModelOutline0.m(), Integer.TYPE).invoke(this.packageManager, str, packageInfoFlags, Integer.valueOf(i))).intValue();
            } catch (Exception unused) {
                int packageUid = this.packageManager.getPackageUid(str, packageInfoFlags);
                if (packageUid >= 0) {
                    return packageUid;
                }
                throw new PackageManager.NameNotFoundException(str);
            }
        } catch (Throwable th) {
            throw new Error("Your ROM is not supported. Please report crash log to devs.", th);
        }
    }

    @Keep
    public ApplicationInfo getApplicationInfoAsUser(String str, int i, int i2) {
        return getApplicationInfoAsUser(str, PackageManager.ApplicationInfoFlags.of(i), i2);
    }

    public ApplicationInfo getApplicationInfoAsUser(String str, PackageManager.ApplicationInfoFlags applicationInfoFlags, int i) {
        try {
            return (ApplicationInfo) PackageManager.class.getMethod("getApplicationInfoAsUser", String.class, AyuPackageManager$$ExternalSyntheticApiModelOutline1.m(), Integer.TYPE).invoke(this.packageManager, str, applicationInfoFlags, Integer.valueOf(i));
        } catch (Exception unused) {
            return null;
        }
    }

    @Override // android.content.pm.PackageManager
    public PermissionInfo getPermissionInfo(String str, int i) {
        return this.packageManager.getPermissionInfo(str, i);
    }

    @Override // android.content.pm.PackageManager
    public List<PermissionInfo> queryPermissionsByGroup(String str, int i) {
        return this.packageManager.queryPermissionsByGroup(str, i);
    }

    @Override // android.content.pm.PackageManager
    public PermissionGroupInfo getPermissionGroupInfo(String str, int i) {
        return this.packageManager.getPermissionGroupInfo(str, i);
    }

    @Override // android.content.pm.PackageManager
    public List<PermissionGroupInfo> getAllPermissionGroups(int i) {
        return this.packageManager.getAllPermissionGroups(i);
    }

    @Override // android.content.pm.PackageManager
    public void getPlatformPermissionsForGroup(String str, Executor executor, Consumer<List<String>> consumer) {
        this.packageManager.getPlatformPermissionsForGroup(str, executor, consumer);
    }

    @Override // android.content.pm.PackageManager
    public void getGroupOfPlatformPermission(String str, Executor executor, Consumer<String> consumer) {
        this.packageManager.getGroupOfPlatformPermission(str, executor, consumer);
    }

    @Override // android.content.pm.PackageManager
    public ApplicationInfo getApplicationInfo(String str, int i) {
        return this.packageManager.getApplicationInfo(str, i);
    }

    @Override // android.content.pm.PackageManager
    public ApplicationInfo getApplicationInfo(String str, PackageManager.ApplicationInfoFlags applicationInfoFlags) {
        return this.packageManager.getApplicationInfo(str, applicationInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public int getTargetSdkVersion(String str) {
        return this.packageManager.getTargetSdkVersion(str);
    }

    @Override // android.content.pm.PackageManager
    public ActivityInfo getActivityInfo(ComponentName componentName, int i) {
        return this.packageManager.getActivityInfo(componentName, i);
    }

    @Override // android.content.pm.PackageManager
    public ActivityInfo getActivityInfo(ComponentName componentName, PackageManager.ComponentInfoFlags componentInfoFlags) {
        return this.packageManager.getActivityInfo(componentName, componentInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public ActivityInfo getReceiverInfo(ComponentName componentName, int i) {
        return this.packageManager.getReceiverInfo(componentName, i);
    }

    @Override // android.content.pm.PackageManager
    public ActivityInfo getReceiverInfo(ComponentName componentName, PackageManager.ComponentInfoFlags componentInfoFlags) {
        return this.packageManager.getReceiverInfo(componentName, componentInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public ServiceInfo getServiceInfo(ComponentName componentName, int i) {
        return this.packageManager.getServiceInfo(componentName, i);
    }

    @Override // android.content.pm.PackageManager
    public ServiceInfo getServiceInfo(ComponentName componentName, PackageManager.ComponentInfoFlags componentInfoFlags) {
        return this.packageManager.getServiceInfo(componentName, componentInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public ProviderInfo getProviderInfo(ComponentName componentName, int i) {
        return this.packageManager.getProviderInfo(componentName, i);
    }

    @Override // android.content.pm.PackageManager
    public ProviderInfo getProviderInfo(ComponentName componentName, PackageManager.ComponentInfoFlags componentInfoFlags) {
        return this.packageManager.getProviderInfo(componentName, componentInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public ModuleInfo getModuleInfo(String str, int i) {
        return this.packageManager.getModuleInfo(str, i);
    }

    @Override // android.content.pm.PackageManager
    public List<ModuleInfo> getInstalledModules(int i) {
        return this.packageManager.getInstalledModules(i);
    }

    @Override // android.content.pm.PackageManager
    public List<PackageInfo> getInstalledPackages(int i) {
        return this.packageManager.getInstalledPackages(i);
    }

    @Override // android.content.pm.PackageManager
    public List<PackageInfo> getInstalledPackages(PackageManager.PackageInfoFlags packageInfoFlags) {
        return this.packageManager.getInstalledPackages(packageInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public List<PackageInfo> getPackagesHoldingPermissions(String[] strArr, int i) {
        return this.packageManager.getPackagesHoldingPermissions(strArr, i);
    }

    @Override // android.content.pm.PackageManager
    public List<PackageInfo> getPackagesHoldingPermissions(String[] strArr, PackageManager.PackageInfoFlags packageInfoFlags) {
        return this.packageManager.getPackagesHoldingPermissions(strArr, packageInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public int checkPermission(String str, String str2) {
        return this.packageManager.checkPermission(str, str2);
    }

    @Override // android.content.pm.PackageManager
    public boolean isPermissionRevokedByPolicy(String str, String str2) {
        return this.packageManager.isPermissionRevokedByPolicy(str, str2);
    }

    @Override // android.content.pm.PackageManager
    public boolean addPermission(PermissionInfo permissionInfo) {
        return this.packageManager.addPermission(permissionInfo);
    }

    @Override // android.content.pm.PackageManager
    public boolean addPermissionAsync(PermissionInfo permissionInfo) {
        return this.packageManager.addPermissionAsync(permissionInfo);
    }

    @Override // android.content.pm.PackageManager
    public void removePermission(String str) {
        this.packageManager.removePermission(str);
    }

    @Override // android.content.pm.PackageManager
    public Set<String> getWhitelistedRestrictedPermissions(String str, int i) {
        return this.packageManager.getWhitelistedRestrictedPermissions(str, i);
    }

    @Override // android.content.pm.PackageManager
    public boolean addWhitelistedRestrictedPermission(String str, String str2, int i) {
        return this.packageManager.addWhitelistedRestrictedPermission(str, str2, i);
    }

    @Override // android.content.pm.PackageManager
    public boolean removeWhitelistedRestrictedPermission(String str, String str2, int i) {
        return this.packageManager.removeWhitelistedRestrictedPermission(str, str2, i);
    }

    @Override // android.content.pm.PackageManager
    public boolean setAutoRevokeWhitelisted(String str, boolean z) {
        return this.packageManager.setAutoRevokeWhitelisted(str, z);
    }

    @Override // android.content.pm.PackageManager
    public boolean isAutoRevokeWhitelisted(String str) {
        return this.packageManager.isAutoRevokeWhitelisted(str);
    }

    @Override // android.content.pm.PackageManager
    public CharSequence getBackgroundPermissionOptionLabel() {
        return this.packageManager.getBackgroundPermissionOptionLabel();
    }

    @Override // android.content.pm.PackageManager
    public int checkSignatures(String str, String str2) {
        return this.packageManager.checkSignatures(str, str2);
    }

    @Override // android.content.pm.PackageManager
    public int checkSignatures(int i, int i2) {
        return this.packageManager.checkSignatures(i, i2);
    }

    @Override // android.content.pm.PackageManager
    public String[] getPackagesForUid(int i) {
        return this.packageManager.getPackagesForUid(i);
    }

    @Override // android.content.pm.PackageManager
    public String getNameForUid(int i) {
        return this.packageManager.getNameForUid(i);
    }

    @Override // android.content.pm.PackageManager
    public List<ApplicationInfo> getInstalledApplications(int i) {
        return this.packageManager.getInstalledApplications(i);
    }

    @Override // android.content.pm.PackageManager
    public List<ApplicationInfo> getInstalledApplications(PackageManager.ApplicationInfoFlags applicationInfoFlags) {
        return this.packageManager.getInstalledApplications(applicationInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public boolean isInstantApp() {
        return this.packageManager.isInstantApp();
    }

    @Override // android.content.pm.PackageManager
    public boolean isInstantApp(String str) {
        return this.packageManager.isInstantApp(str);
    }

    @Override // android.content.pm.PackageManager
    public int getInstantAppCookieMaxBytes() {
        return this.packageManager.getInstantAppCookieMaxBytes();
    }

    @Override // android.content.pm.PackageManager
    public byte[] getInstantAppCookie() {
        return this.packageManager.getInstantAppCookie();
    }

    @Override // android.content.pm.PackageManager
    public void clearInstantAppCookie() {
        this.packageManager.clearInstantAppCookie();
    }

    @Override // android.content.pm.PackageManager
    public void updateInstantAppCookie(byte[] bArr) {
        this.packageManager.updateInstantAppCookie(bArr);
    }

    @Override // android.content.pm.PackageManager
    public String[] getSystemSharedLibraryNames() {
        return this.packageManager.getSystemSharedLibraryNames();
    }

    @Override // android.content.pm.PackageManager
    public List<SharedLibraryInfo> getSharedLibraries(int i) {
        return this.packageManager.getSharedLibraries(i);
    }

    @Override // android.content.pm.PackageManager
    public List<SharedLibraryInfo> getSharedLibraries(PackageManager.PackageInfoFlags packageInfoFlags) {
        return this.packageManager.getSharedLibraries(packageInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public ChangedPackages getChangedPackages(int i) {
        return this.packageManager.getChangedPackages(i);
    }

    @Override // android.content.pm.PackageManager
    public FeatureInfo[] getSystemAvailableFeatures() {
        return this.packageManager.getSystemAvailableFeatures();
    }

    @Override // android.content.pm.PackageManager
    public boolean hasSystemFeature(String str) {
        return this.packageManager.hasSystemFeature(str);
    }

    @Override // android.content.pm.PackageManager
    public boolean hasSystemFeature(String str, int i) {
        return this.packageManager.hasSystemFeature(str, i);
    }

    @Override // android.content.pm.PackageManager
    public ResolveInfo resolveActivity(Intent intent, int i) {
        return this.packageManager.resolveActivity(intent, i);
    }

    @Override // android.content.pm.PackageManager
    public ResolveInfo resolveActivity(Intent intent, PackageManager.ResolveInfoFlags resolveInfoFlags) {
        return this.packageManager.resolveActivity(intent, resolveInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public List<ResolveInfo> queryIntentActivities(Intent intent, int i) {
        return this.packageManager.queryIntentActivities(intent, i);
    }

    @Override // android.content.pm.PackageManager
    public List<ResolveInfo> queryIntentActivities(Intent intent, PackageManager.ResolveInfoFlags resolveInfoFlags) {
        return this.packageManager.queryIntentActivities(intent, resolveInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public List<ResolveInfo> queryIntentActivityOptions(ComponentName componentName, Intent[] intentArr, Intent intent, int i) {
        return this.packageManager.queryIntentActivityOptions(componentName, intentArr, intent, i);
    }

    @Override // android.content.pm.PackageManager
    public List<ResolveInfo> queryIntentActivityOptions(ComponentName componentName, List<Intent> list, Intent intent, PackageManager.ResolveInfoFlags resolveInfoFlags) {
        return this.packageManager.queryIntentActivityOptions(componentName, list, intent, resolveInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public List<ResolveInfo> queryBroadcastReceivers(Intent intent, int i) {
        return this.packageManager.queryBroadcastReceivers(intent, i);
    }

    @Override // android.content.pm.PackageManager
    public List<ResolveInfo> queryBroadcastReceivers(Intent intent, PackageManager.ResolveInfoFlags resolveInfoFlags) {
        return this.packageManager.queryBroadcastReceivers(intent, resolveInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public ResolveInfo resolveService(Intent intent, int i) {
        return this.packageManager.resolveService(intent, i);
    }

    @Override // android.content.pm.PackageManager
    public ResolveInfo resolveService(Intent intent, PackageManager.ResolveInfoFlags resolveInfoFlags) {
        return this.packageManager.resolveService(intent, resolveInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public List<ResolveInfo> queryIntentServices(Intent intent, int i) {
        return this.packageManager.queryIntentServices(intent, i);
    }

    @Override // android.content.pm.PackageManager
    public List<ResolveInfo> queryIntentServices(Intent intent, PackageManager.ResolveInfoFlags resolveInfoFlags) {
        return this.packageManager.queryIntentServices(intent, resolveInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public List<ResolveInfo> queryIntentContentProviders(Intent intent, int i) {
        return this.packageManager.queryIntentContentProviders(intent, i);
    }

    @Override // android.content.pm.PackageManager
    public List<ResolveInfo> queryIntentContentProviders(Intent intent, PackageManager.ResolveInfoFlags resolveInfoFlags) {
        return this.packageManager.queryIntentContentProviders(intent, resolveInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public ProviderInfo resolveContentProvider(String str, int i) {
        return this.packageManager.resolveContentProvider(str, i);
    }

    @Override // android.content.pm.PackageManager
    public ProviderInfo resolveContentProvider(String str, PackageManager.ComponentInfoFlags componentInfoFlags) {
        return this.packageManager.resolveContentProvider(str, componentInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public List<ProviderInfo> queryContentProviders(String str, int i, int i2) {
        return this.packageManager.queryContentProviders(str, i, i2);
    }

    @Override // android.content.pm.PackageManager
    public List<ProviderInfo> queryContentProviders(String str, int i, PackageManager.ComponentInfoFlags componentInfoFlags) {
        return this.packageManager.queryContentProviders(str, i, componentInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public InstrumentationInfo getInstrumentationInfo(ComponentName componentName, int i) {
        return this.packageManager.getInstrumentationInfo(componentName, i);
    }

    @Override // android.content.pm.PackageManager
    public List<InstrumentationInfo> queryInstrumentation(String str, int i) {
        return this.packageManager.queryInstrumentation(str, i);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getDrawable(String str, int i, ApplicationInfo applicationInfo) {
        return this.packageManager.getDrawable(str, i, applicationInfo);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getActivityIcon(ComponentName componentName) {
        return this.packageManager.getActivityIcon(componentName);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getActivityIcon(Intent intent) {
        return this.packageManager.getActivityIcon(intent);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getActivityBanner(ComponentName componentName) {
        return this.packageManager.getActivityBanner(componentName);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getActivityBanner(Intent intent) {
        return this.packageManager.getActivityBanner(intent);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getDefaultActivityIcon() {
        return this.packageManager.getDefaultActivityIcon();
    }

    @Override // android.content.pm.PackageManager
    public Drawable getApplicationIcon(ApplicationInfo applicationInfo) {
        return this.packageManager.getApplicationIcon(applicationInfo);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getApplicationIcon(String str) {
        return this.packageManager.getApplicationIcon(str);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getApplicationBanner(ApplicationInfo applicationInfo) {
        return this.packageManager.getApplicationBanner(applicationInfo);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getApplicationBanner(String str) {
        return this.packageManager.getApplicationBanner(str);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getActivityLogo(ComponentName componentName) {
        return this.packageManager.getActivityLogo(componentName);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getActivityLogo(Intent intent) {
        return this.packageManager.getActivityLogo(intent);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getApplicationLogo(ApplicationInfo applicationInfo) {
        return this.packageManager.getApplicationLogo(applicationInfo);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getApplicationLogo(String str) {
        return this.packageManager.getApplicationLogo(str);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getUserBadgedIcon(Drawable drawable, UserHandle userHandle) {
        return this.packageManager.getUserBadgedIcon(drawable, userHandle);
    }

    @Override // android.content.pm.PackageManager
    public Drawable getUserBadgedDrawableForDensity(Drawable drawable, UserHandle userHandle, Rect rect, int i) {
        return this.packageManager.getUserBadgedDrawableForDensity(drawable, userHandle, rect, i);
    }

    @Override // android.content.pm.PackageManager
    public CharSequence getUserBadgedLabel(CharSequence charSequence, UserHandle userHandle) {
        return this.packageManager.getUserBadgedLabel(charSequence, userHandle);
    }

    @Override // android.content.pm.PackageManager
    public CharSequence getText(String str, int i, ApplicationInfo applicationInfo) {
        return this.packageManager.getText(str, i, applicationInfo);
    }

    @Override // android.content.pm.PackageManager
    public XmlResourceParser getXml(String str, int i, ApplicationInfo applicationInfo) {
        return this.packageManager.getXml(str, i, applicationInfo);
    }

    @Override // android.content.pm.PackageManager
    public CharSequence getApplicationLabel(ApplicationInfo applicationInfo) {
        return this.packageManager.getApplicationLabel(applicationInfo);
    }

    @Override // android.content.pm.PackageManager
    public Resources getResourcesForActivity(ComponentName componentName) {
        return this.packageManager.getResourcesForActivity(componentName);
    }

    @Override // android.content.pm.PackageManager
    public Resources getResourcesForApplication(ApplicationInfo applicationInfo) {
        return this.packageManager.getResourcesForApplication(applicationInfo);
    }

    @Override // android.content.pm.PackageManager
    public Resources getResourcesForApplication(ApplicationInfo applicationInfo, Configuration configuration) {
        return this.packageManager.getResourcesForApplication(applicationInfo, configuration);
    }

    @Override // android.content.pm.PackageManager
    public Resources getResourcesForApplication(String str) {
        return this.packageManager.getResourcesForApplication(str);
    }

    @Override // android.content.pm.PackageManager
    public PackageInfo getPackageArchiveInfo(String str, int i) {
        return this.packageManager.getPackageArchiveInfo(str, i);
    }

    @Override // android.content.pm.PackageManager
    public PackageInfo getPackageArchiveInfo(String str, PackageManager.PackageInfoFlags packageInfoFlags) {
        return this.packageManager.getPackageArchiveInfo(str, packageInfoFlags);
    }

    @Override // android.content.pm.PackageManager
    public void verifyPendingInstall(int i, int i2) {
        this.packageManager.verifyPendingInstall(i, i2);
    }

    @Override // android.content.pm.PackageManager
    public void extendVerificationTimeout(int i, int i2, long j) {
        this.packageManager.extendVerificationTimeout(i, i2, j);
    }

    @Override // android.content.pm.PackageManager
    public void setInstallerPackageName(String str, String str2) {
        this.packageManager.setInstallerPackageName(str, str2);
    }

    @Override // android.content.pm.PackageManager
    @Deprecated
    public String getInstallerPackageName(String str) {
        return this.packageManager.getInstallerPackageName(str);
    }

    @Override // android.content.pm.PackageManager
    public InstallSourceInfo getInstallSourceInfo(String str) {
        return this.packageManager.getInstallSourceInfo(str);
    }

    @Override // android.content.pm.PackageManager
    @Deprecated
    public void addPackageToPreferred(String str) {
        this.packageManager.addPackageToPreferred(str);
    }

    @Override // android.content.pm.PackageManager
    @Deprecated
    public void removePackageFromPreferred(String str) {
        this.packageManager.removePackageFromPreferred(str);
    }

    @Override // android.content.pm.PackageManager
    @Deprecated
    public List<PackageInfo> getPreferredPackages(int i) {
        return this.packageManager.getPreferredPackages(i);
    }

    @Override // android.content.pm.PackageManager
    @Deprecated
    public void addPreferredActivity(IntentFilter intentFilter, int i, ComponentName[] componentNameArr, ComponentName componentName) {
        this.packageManager.addPreferredActivity(intentFilter, i, componentNameArr, componentName);
    }

    @Override // android.content.pm.PackageManager
    @Deprecated
    public void clearPackagePreferredActivities(String str) {
        this.packageManager.clearPackagePreferredActivities(str);
    }

    @Override // android.content.pm.PackageManager
    @Deprecated
    public int getPreferredActivities(List<IntentFilter> list, List<ComponentName> list2, String str) {
        return this.packageManager.getPreferredActivities(list, list2, str);
    }

    @Override // android.content.pm.PackageManager
    public void setComponentEnabledSetting(ComponentName componentName, int i, int i2) {
        this.packageManager.setComponentEnabledSetting(componentName, i, i2);
    }

    @Override // android.content.pm.PackageManager
    public void setComponentEnabledSettings(List<PackageManager.ComponentEnabledSetting> list) {
        this.packageManager.setComponentEnabledSettings(list);
    }

    @Override // android.content.pm.PackageManager
    public int getComponentEnabledSetting(ComponentName componentName) {
        return this.packageManager.getComponentEnabledSetting(componentName);
    }

    @Override // android.content.pm.PackageManager
    public boolean getSyntheticAppDetailsActivityEnabled(String str) {
        return this.packageManager.getSyntheticAppDetailsActivityEnabled(str);
    }

    @Override // android.content.pm.PackageManager
    public void setApplicationEnabledSetting(String str, int i, int i2) {
        this.packageManager.setApplicationEnabledSetting(str, i, i2);
    }

    @Override // android.content.pm.PackageManager
    public int getApplicationEnabledSetting(String str) {
        return this.packageManager.getApplicationEnabledSetting(str);
    }

    @Override // android.content.pm.PackageManager
    public boolean isSafeMode() {
        return this.packageManager.isSafeMode();
    }

    @Override // android.content.pm.PackageManager
    public boolean isPackageSuspended(String str) {
        return this.packageManager.isPackageSuspended(str);
    }

    @Override // android.content.pm.PackageManager
    public boolean isPackageSuspended() {
        return this.packageManager.isPackageSuspended();
    }

    @Override // android.content.pm.PackageManager
    public Bundle getSuspendedPackageAppExtras() {
        return this.packageManager.getSuspendedPackageAppExtras();
    }

    @Override // android.content.pm.PackageManager
    public void setApplicationCategoryHint(String str, int i) {
        this.packageManager.setApplicationCategoryHint(str, i);
    }

    @Override // android.content.pm.PackageManager
    public boolean isDeviceUpgrading() {
        return this.packageManager.isDeviceUpgrading();
    }

    @Override // android.content.pm.PackageManager
    public PackageInstaller getPackageInstaller() {
        return this.packageManager.getPackageInstaller();
    }

    @Override // android.content.pm.PackageManager
    public boolean canRequestPackageInstalls() {
        return this.packageManager.canRequestPackageInstalls();
    }

    @Override // android.content.pm.PackageManager
    public boolean hasSigningCertificate(String str, byte[] bArr, int i) {
        return this.packageManager.hasSigningCertificate(str, bArr, i);
    }

    @Override // android.content.pm.PackageManager
    public boolean hasSigningCertificate(int i, byte[] bArr, int i2) {
        return this.packageManager.hasSigningCertificate(i, bArr, i2);
    }

    @Override // android.content.pm.PackageManager
    public void requestChecksums(String str, boolean z, int i, List<Certificate> list, PackageManager.OnChecksumsReadyListener onChecksumsReadyListener) throws PackageManager.NameNotFoundException, CertificateEncodingException {
        this.packageManager.requestChecksums(str, z, i, list, onChecksumsReadyListener);
    }

    @Override // android.content.pm.PackageManager
    public boolean isAutoRevokeWhitelisted() {
        return this.packageManager.isAutoRevokeWhitelisted();
    }

    @Override // android.content.pm.PackageManager
    public boolean isDefaultApplicationIcon(Drawable drawable) {
        return this.packageManager.isDefaultApplicationIcon(drawable);
    }

    @Override // android.content.pm.PackageManager
    public void setMimeGroup(String str, Set<String> set) {
        this.packageManager.setMimeGroup(str, set);
    }

    @Override // android.content.pm.PackageManager
    public Set<String> getMimeGroup(String str) {
        return this.packageManager.getMimeGroup(str);
    }

    @Override // android.content.pm.PackageManager
    public PackageManager.Property getProperty(String str, String str2) {
        return this.packageManager.getProperty(str, str2);
    }

    @Override // android.content.pm.PackageManager
    public PackageManager.Property getProperty(String str, ComponentName componentName) {
        return this.packageManager.getProperty(str, componentName);
    }

    @Override // android.content.pm.PackageManager
    public List<PackageManager.Property> queryApplicationProperty(String str) {
        return this.packageManager.queryApplicationProperty(str);
    }

    @Override // android.content.pm.PackageManager
    public List<PackageManager.Property> queryActivityProperty(String str) {
        return this.packageManager.queryActivityProperty(str);
    }

    @Override // android.content.pm.PackageManager
    public List<PackageManager.Property> queryProviderProperty(String str) {
        return this.packageManager.queryProviderProperty(str);
    }

    @Override // android.content.pm.PackageManager
    public List<PackageManager.Property> queryReceiverProperty(String str) {
        return this.packageManager.queryReceiverProperty(str);
    }

    @Override // android.content.pm.PackageManager
    public List<PackageManager.Property> queryServiceProperty(String str) {
        return this.packageManager.queryServiceProperty(str);
    }

    @Override // android.content.pm.PackageManager
    public boolean canPackageQuery(String str, String str2) {
        return this.packageManager.canPackageQuery(str, str2);
    }

    @Override // android.content.pm.PackageManager
    public boolean[] canPackageQuery(String str, String[] strArr) {
        return this.packageManager.canPackageQuery(str, strArr);
    }

    @Override // android.content.pm.PackageManager
    public void relinquishUpdateOwnership(String str) {
        this.packageManager.relinquishUpdateOwnership(str);
    }

    public String getPermissionControllerPackageName() {
        try {
            return (String) PackageManager.class.getMethod("getPermissionControllerPackageName", null).invoke(this.packageManager, null);
        } catch (Exception unused) {
            return null;
        }
    }

    public <T> T parseAndroidManifest(File file, Function<XmlResourceParser, T> function) {
        return (T) this.packageManager.parseAndroidManifest(file, function);
    }

    public <T> T parseAndroidManifest(ParcelFileDescriptor parcelFileDescriptor, Function<XmlResourceParser, T> function) {
        return (T) this.packageManager.parseAndroidManifest(parcelFileDescriptor, function);
    }

    public boolean isPackageStopped(String str) {
        return this.packageManager.isPackageStopped(str);
    }

    public boolean isAppArchivable(String str) {
        return this.packageManager.isAppArchivable(str);
    }

    public static SigningInfo getVerifiedSigningInfo(String str, int i) {
        return PackageManager.getVerifiedSigningInfo(str, i);
    }

    public ArchivedPackageInfo getArchivedPackage(String str) {
        return this.packageManager.getArchivedPackage(str);
    }
}
