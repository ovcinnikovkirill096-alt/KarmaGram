package androidx.work.impl.constraints;

import androidx.work.impl.model.WorkSpec;

public interface OnConstraintsStateChangedListener {
    void onConstraintsStateChanged(WorkSpec workSpec, ConstraintsState constraintsState);
}
