package com.radolyn.ayugram.preferences.components;

import android.content.Context;
import com.radolyn.ayugram.AyuConstants;
import com.radolyn.ayugram.utils.AyuMessageUtils;
import j$.util.Objects;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.ui.Cells.UserCell;

public class PeerCell extends UserCell {
    private CharSequence subtext;
    private CharSequence text;

    public PeerCell(Context context) {
        super(context, 12, 0, false);
        this.disableFilter = true;
    }

    public void setPeer(long j, CharSequence charSequence, CharSequence charSequence2) {
        this.dialogId = j;
        this.text = charSequence;
        this.subtext = charSequence2;
        TLObject dialogInAnyWay = AyuMessageUtils.getDialogInAnyWay(j, Integer.valueOf(UserConfig.selectedAccount));
        if (dialogInAnyWay == null) {
            return;
        }
        setData(dialogInAnyWay, charSequence, charSequence2, 0);
    }

    @Override // org.telegram.ui.Cells.UserCell, android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        NotificationCenter.getGlobalInstance().addObserver(this, AyuConstants.PEER_RESOLVED_NOTIFICATION);
        super.onAttachedToWindow();
    }

    @Override // org.telegram.ui.Cells.UserCell, android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        NotificationCenter.getGlobalInstance().removeObserver(this, AyuConstants.PEER_RESOLVED_NOTIFICATION);
        super.onDetachedFromWindow();
    }

    @Override // org.telegram.ui.Cells.UserCell, org.telegram.messenger.NotificationCenter.NotificationCenterDelegate
    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == AyuConstants.PEER_RESOLVED_NOTIFICATION) {
            setPeer(this.dialogId, this.text, this.subtext);
            invalidate();
        } else {
            super.didReceivedNotification(i, i2, objArr);
        }
    }

    public boolean equals(Object obj) {
        if (obj != null && getClass() == obj.getClass()) {
            PeerCell peerCell = (PeerCell) obj;
            if (Objects.equals(this.text, peerCell.text) && Objects.equals(this.subtext, peerCell.subtext)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.text, this.subtext);
    }
}
