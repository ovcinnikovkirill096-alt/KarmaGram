package com.exteragram.messenger.badges;

import com.exteragram.messenger.api.db.ExteraDatabase;
import com.exteragram.messenger.api.dto.BadgeDTO;
import com.exteragram.messenger.badges.source.ApiBadgeSource;
import com.exteragram.messenger.utils.chats.ChatUtils;
import com.exteragram.messenger.utils.network.RemoteUtils;
import java.util.function.Consumer;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.SetsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Dispatchers;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

public final class BadgesController {
    private static final BadgeDTO DEV_BADGE;
    public static final BadgesController INSTANCE = new BadgesController();
    private static final BadgeDTO SUPPORTER_BADGE;
    private static final ApiBadgeSource apiBadgeSource;
    private static final CoroutineScope scope;
    private static final CachedRemoteSet trustedPluginsCache;

    public final BadgeDTO getBadge() {
        return getBadge$default(this, null, 1, null);
    }

    public final boolean hasBadge() {
        return hasBadge$default(this, null, 1, null);
    }

    public final boolean isDeveloper() {
        return isDeveloper$default(this, null, 1, null);
    }

    private BadgesController() {
    }

    static {
        CoroutineScope CoroutineScope = CoroutineScopeKt.CoroutineScope(Dispatchers.getIO());
        scope = CoroutineScope;
        trustedPluginsCache = new CachedRemoteSet("trusted_plugins", SetsKt.setOf((Object) 2562664432L));
        DEV_BADGE = new BadgeDTO(5359407509327085568L, null);
        SUPPORTER_BADGE = new BadgeDTO(5391059537102927631L, null);
        apiBadgeSource = new ApiBadgeSource(ExteraDatabase.Companion.getInstance().profileDao());
        BuildersKt__Builders_commonKt.launch$default(CoroutineScope, null, null, new AnonymousClass1(null), 3, null);
    }

    /* JADX INFO: renamed from: com.exteragram.messenger.badges.BadgesController$1, reason: invalid class name */
    static final class AnonymousClass1 extends SuspendLambda implements Function2 {
        int label;

        AnonymousClass1(Continuation continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation create(Object obj, Continuation continuation) {
            return new AnonymousClass1(continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
            return ((AnonymousClass1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                ApiBadgeSource apiBadgeSource = BadgesController.apiBadgeSource;
                this.label = 1;
                if (apiBadgeSource.loadToCache(this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            return Unit.INSTANCE;
        }
    }

    public static /* synthetic */ BadgeDTO getBadge$default(BadgesController badgesController, TLObject tLObject, int i, Object obj) {
        if ((i & 1) != 0) {
            tLObject = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
        }
        return badgesController.getBadge(tLObject);
    }

    public final BadgeDTO getBadge(TLObject tLObject) {
        Pair pair;
        try {
            if (tLObject instanceof TLRPC.User) {
                pair = TuplesKt.to(Long.valueOf(((TLRPC.User) tLObject).id), Boolean.TRUE);
            } else {
                if (!(tLObject instanceof TLRPC.Chat)) {
                    return null;
                }
                pair = TuplesKt.to(Long.valueOf(((TLRPC.Chat) tLObject).id), Boolean.FALSE);
            }
            BadgeDTO badge = apiBadgeSource.getBadge(((Number) pair.component1()).longValue(), ((Boolean) pair.component2()).booleanValue());
            if (badge != null) {
                return badge;
            }
            return null;
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    public static /* synthetic */ boolean hasBadge$default(BadgesController badgesController, TLObject tLObject, int i, Object obj) {
        if ((i & 1) != 0) {
            tLObject = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
            Intrinsics.checkNotNullExpressionValue(tLObject, "getCurrentUser(...)");
        }
        return badgesController.hasBadge(tLObject);
    }

    public final boolean hasBadge(TLObject obj) {
        Intrinsics.checkNotNullParameter(obj, "obj");
        return getBadge(obj) != null;
    }

    public final boolean isTrusted(long j) {
        return trustedPluginsCache.contains(j);
    }

    public final boolean isExtera(long j) {
        return apiBadgeSource.isDeveloper(j);
    }

    public final boolean isExtera(TLRPC.Chat chat) {
        return chat != null && INSTANCE.isExtera(chat.id);
    }

    public final BadgeDTO getDefaultBadge() {
        if (apiBadgeSource.isDeveloper(UserConfig.getInstance(UserConfig.selectedAccount).clientUserId)) {
            return DEV_BADGE;
        }
        return SUPPORTER_BADGE;
    }

    public final boolean canChangeBadge() {
        return apiBadgeSource.canChangeBadge(UserConfig.getInstance(UserConfig.selectedAccount).clientUserId) || isDeveloper$default(this, null, 1, null);
    }

    public final void updateBadge(final BadgeDTO badgeDTO, final Consumer callback) {
        String str;
        Intrinsics.checkNotNullParameter(callback, "callback");
        if (badgeDTO != null) {
            String text = badgeDTO.getText();
            if (text == null || text.length() == 0) {
                str = "badge " + badgeDTO.getDocumentId();
            } else {
                str = "badge " + badgeDTO.getDocumentId() + ' ' + text;
            }
            ChatUtils.getInstance(UserConfig.selectedAccount).sendBotRequest(str, false, new Utilities.Callback() { // from class: com.exteragram.messenger.badges.BadgesController$$ExternalSyntheticLambda0
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    BadgesController.updateBadge$lambda$0(callback, badgeDTO, (String) obj);
                }
            });
            return;
        }
        callback.v(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void updateBadge$lambda$0(Consumer consumer, BadgeDTO badgeDTO, String str) {
        if (Intrinsics.areEqual("ok", str)) {
            BuildersKt__Builders_commonKt.launch$default(scope, null, null, new BadgesController$updateBadge$1$1(badgeDTO, null), 3, null);
        }
        consumer.v(str);
    }

    public static /* synthetic */ boolean isDeveloper$default(BadgesController badgesController, TLRPC.User user, int i, Object obj) {
        if ((i & 1) != 0) {
            user = UserConfig.getInstance(UserConfig.selectedAccount).getCurrentUser();
            Intrinsics.checkNotNullExpressionValue(user, "getCurrentUser(...)");
        }
        return badgesController.isDeveloper(user);
    }

    public final boolean isDeveloper(TLRPC.User user) {
        Intrinsics.checkNotNullParameter(user, "user");
        return apiBadgeSource.isDeveloper(user.id);
    }

    public final boolean isAyuModerator(TLRPC.User user) {
        return user != null && INSTANCE.isAyuModerator(user.id);
    }

    public final boolean isAyuModerator(long j) {
        return RemoteUtils.getStringSetConfigValue("moders", SetsKt.emptySet()).contains(String.valueOf(j));
    }
}
