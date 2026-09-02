package com.exteragram.messenger.icons;

import java.io.File;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.MainCoroutineDispatcher;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.BulletinFactory;

final class IconManager$handleIconPack$1$1$bottomSheet$1$1 extends SuspendLambda implements Function2 {
    final /* synthetic */ BaseFragment $baseFragment;
    final /* synthetic */ boolean $enable;
    final /* synthetic */ File $file;
    final /* synthetic */ IconPack $pack;
    final /* synthetic */ boolean $update;
    boolean Z$0;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    IconManager$handleIconPack$1$1$bottomSheet$1$1(File file, BaseFragment baseFragment, boolean z, IconPack iconPack, boolean z2, Continuation continuation) {
        super(2, continuation);
        this.$file = file;
        this.$baseFragment = baseFragment;
        this.$update = z;
        this.$pack = iconPack;
        this.$enable = z2;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new IconManager$handleIconPack$1$1$bottomSheet$1$1(this.$file, this.$baseFragment, this.$update, this.$pack, this.$enable, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
        return ((IconManager$handleIconPack$1$1$bottomSheet$1$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x004e, code lost:
    
        if (kotlinx.coroutines.BuildersKt.withContext(r11, r3, r10) == r0) goto L15;
     */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object obj) {
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = this.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            IconPackStorage iconPackStorage = IconPackStorage.INSTANCE;
            File file = this.$file;
            this.label = 1;
            obj = iconPackStorage.installPack(file, this);
            if (obj != coroutine_suspended) {
            }
            return coroutine_suspended;
        }
        if (i == 1) {
            ResultKt.throwOnFailure(obj);
        } else {
            if (i != 2) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
        }
        return Unit.INSTANCE;
        boolean zBooleanValue = ((Boolean) obj).booleanValue();
        MainCoroutineDispatcher main = Dispatchers.getMain();
        AnonymousClass1 anonymousClass1 = new AnonymousClass1(zBooleanValue, this.$baseFragment, this.$update, this.$pack, this.$enable, null);
        this.Z$0 = zBooleanValue;
        this.label = 2;
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.icons.IconManager$handleIconPack$1$1$bottomSheet$1$1$1, reason: invalid class name */
    static final class AnonymousClass1 extends SuspendLambda implements Function2 {
        final /* synthetic */ BaseFragment $baseFragment;
        final /* synthetic */ boolean $enable;
        final /* synthetic */ IconPack $pack;
        final /* synthetic */ boolean $success;
        final /* synthetic */ boolean $update;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(boolean z, BaseFragment baseFragment, boolean z2, IconPack iconPack, boolean z3, Continuation continuation) {
            super(2, continuation);
            this.$success = z;
            this.$baseFragment = baseFragment;
            this.$update = z2;
            this.$pack = iconPack;
            this.$enable = z3;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            return new AnonymousClass1(this.$success, this.$baseFragment, this.$update, this.$pack, this.$enable, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            if (this.$success) {
                BulletinFactory.of(this.$baseFragment).createSimpleBulletin(R.raw.contact_check, LocaleController.formatString(this.$update ? R.string.PluginUpdated : R.string.PluginInstalled, this.$pack.getName())).show();
                if (this.$enable) {
                    IconManager.INSTANCE.setActiveCustomPack(this.$pack.getId());
                } else {
                    IconManager.INSTANCE.initialize(true);
                }
            } else {
                BulletinFactory.of(this.$baseFragment).createSimpleBulletin(R.raw.error, LocaleController.formatString(R.string.PluginInstallError, this.$pack.getName())).show();
            }
            return Unit.INSTANCE;
        }
    }
}
