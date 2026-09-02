package org.telegram.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.ColorUtils;
import okhttp3.internal.url._UrlKt;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

public abstract class CodeFieldContainer extends LinearLayout {
    Paint bitmapPaint;
    public CodeNumberField[] codeField;
    public boolean ignoreOnTextChange;
    public boolean isFocusSuppressed;
    Paint paint;
    float strokeWidth;

    protected abstract void processNextPressed();

    public CodeFieldContainer(Context context) {
        super(context);
        this.paint = new Paint(1);
        this.bitmapPaint = new Paint(1);
        this.paint.setStyle(Paint.Style.STROKE);
        setOrientation(0);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        Paint paint = this.paint;
        float fDp = AndroidUtilities.dp(1.5f);
        this.strokeWidth = fDp;
        paint.setStrokeWidth(fDp);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof CodeNumberField) {
                CodeNumberField codeNumberField = (CodeNumberField) childAt;
                if (!this.isFocusSuppressed) {
                    if (childAt.isFocused()) {
                        codeNumberField.animateFocusedProgress(1.0f);
                    } else if (!childAt.isFocused()) {
                        codeNumberField.animateFocusedProgress(0.0f);
                    }
                }
                float successProgress = codeNumberField.getSuccessProgress();
                this.paint.setColor(ColorUtils.blendARGB(ColorUtils.blendARGB(ColorUtils.blendARGB(Theme.getColor(Theme.key_windowBackgroundWhiteInputField), Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated), codeNumberField.getFocusedProgress()), Theme.getColor(Theme.key_text_RedBold), codeNumberField.getErrorProgress()), Theme.getColor(Theme.key_checkbox), successProgress));
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                float f = this.strokeWidth;
                rectF.inset(f, f);
                if (successProgress != 0.0f) {
                    float f2 = -Math.max(0.0f, this.strokeWidth * (codeNumberField.getSuccessScaleProgress() - 1.0f));
                    rectF.inset(f2, f2);
                }
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(4.0f), AndroidUtilities.dp(4.0f), this.paint);
            }
        }
        super.dispatchDraw(canvas);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        if (view instanceof CodeNumberField) {
            CodeNumberField codeNumberField = (CodeNumberField) view;
            canvas.save();
            float f = codeNumberField.enterAnimation;
            RectF rectF = AndroidUtilities.rectTmp;
            rectF.set(view.getX(), view.getY(), view.getX() + view.getMeasuredWidth(), view.getY() + view.getMeasuredHeight());
            float f2 = this.strokeWidth;
            rectF.inset(f2, f2);
            canvas.clipRect(rectF);
            if (codeNumberField.replaceAnimation) {
                float f3 = (f * 0.5f) + 0.5f;
                view.setAlpha(f);
                canvas.scale(f3, f3, codeNumberField.getX() + (codeNumberField.getMeasuredWidth() / 2.0f), codeNumberField.getY() + (codeNumberField.getMeasuredHeight() / 2.0f));
            } else {
                view.setAlpha(1.0f);
                canvas.translate(0.0f, view.getMeasuredHeight() * (1.0f - f));
            }
            super.drawChild(canvas, view, j);
            canvas.restore();
            float f4 = codeNumberField.exitAnimation;
            if (f4 >= 1.0f) {
                return true;
            }
            canvas.save();
            float f5 = 1.0f - f4;
            float f6 = (f5 * 0.5f) + 0.5f;
            canvas.scale(f6, f6, codeNumberField.getX() + (codeNumberField.getMeasuredWidth() / 2.0f), codeNumberField.getY() + (codeNumberField.getMeasuredHeight() / 2.0f));
            this.bitmapPaint.setAlpha((int) (f5 * 255.0f));
            canvas.drawBitmap(codeNumberField.exitBitmap, codeNumberField.getX(), codeNumberField.getY(), this.bitmapPaint);
            canvas.restore();
            return true;
        }
        return super.drawChild(canvas, view, j);
    }

    /* JADX WARN: Code duplicated, block: B:30:0x00b2  */
    /* JADX WARN: Code duplicated, block: B:31:0x00b4  */
    public void setNumbersCount(final int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        CodeNumberField[] codeNumberFieldArr = this.codeField;
        int i7 = 0;
        if (codeNumberFieldArr == null || codeNumberFieldArr.length != i) {
            if (codeNumberFieldArr != null) {
                for (CodeNumberField codeNumberField : codeNumberFieldArr) {
                    removeView(codeNumberField);
                }
            }
            this.codeField = new CodeNumberField[i];
            for (final int i8 = 0; i8 < i; i8++) {
                this.codeField[i8] = new CodeNumberField(getContext()) { // from class: org.telegram.ui.CodeFieldContainer.1
                    @Override // android.view.View
                    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                        int i9;
                        int i10 = 0;
                        if (keyEvent.getKeyCode() == 4) {
                            return false;
                        }
                        int keyCode = keyEvent.getKeyCode();
                        if (i8 >= CodeFieldContainer.this.codeField.length) {
                            return false;
                        }
                        if (keyEvent.getAction() != 1) {
                            return isFocused();
                        }
                        if (keyCode == 67 && CodeFieldContainer.this.codeField[i8].length() == 1) {
                            CodeFieldContainer.this.codeField[i8].startExitAnimation();
                            CodeFieldContainer.this.codeField[i8].setText(_UrlKt.FRAGMENT_ENCODE_SET);
                            return true;
                        }
                        if (keyCode != 67 || CodeFieldContainer.this.codeField[i8].length() != 0 || (i9 = i8) <= 0) {
                            if (keyCode >= 7 && keyCode <= 16) {
                                String string = Integer.toString(keyCode - 7);
                                if (CodeFieldContainer.this.codeField[i8].getText() != null && string.equals(CodeFieldContainer.this.codeField[i8].getText().toString())) {
                                    int i11 = i8;
                                    if (i11 >= i - 1) {
                                        CodeFieldContainer.this.processNextPressed();
                                    } else {
                                        CodeFieldContainer.this.codeField[i11 + 1].requestFocus();
                                    }
                                    return true;
                                }
                                if (CodeFieldContainer.this.codeField[i8].length() > 0) {
                                    CodeFieldContainer.this.codeField[i8].startExitAnimation();
                                }
                                CodeFieldContainer.this.codeField[i8].setText(string);
                            }
                            return true;
                        }
                        CodeNumberField[] codeNumberFieldArr2 = CodeFieldContainer.this.codeField;
                        codeNumberFieldArr2[i9 - 1].setSelection(codeNumberFieldArr2[i9 - 1].length());
                        while (true) {
                            int i12 = i8;
                            if (i10 >= i12) {
                                CodeFieldContainer.this.codeField[i12 - 1].startExitAnimation();
                                CodeFieldContainer.this.codeField[i8 - 1].setText(_UrlKt.FRAGMENT_ENCODE_SET);
                                return true;
                            }
                            if (i10 == i12 - 1) {
                                CodeFieldContainer.this.codeField[i12 - 1].requestFocus();
                            } else {
                                CodeFieldContainer.this.codeField[i10].clearFocus();
                            }
                            i10++;
                        }
                    }
                };
                this.codeField[i8].setImeOptions(268435461);
                this.codeField[i8].setTextSize(1, 20.0f);
                this.codeField[i8].setMaxLines(1);
                this.codeField[i8].setTypeface(AndroidUtilities.bold());
                this.codeField[i8].setPadding(0, 0, 0, 0);
                this.codeField[i8].setGravity(17);
                if (i2 == 3) {
                    this.codeField[i8].setEnabled(false);
                    this.codeField[i8].setInputType(0);
                    this.codeField[i8].setVisibility(8);
                } else {
                    this.codeField[i8].setInputType(3);
                }
                int i9 = 42;
                int i10 = 10;
                if (i2 == 10) {
                    i3 = 47;
                } else {
                    i3 = 34;
                    if (i2 == 11) {
                        i9 = 28;
                        i10 = 5;
                    } else {
                        i10 = 7;
                        i4 = 42;
                        i5 = 34;
                    }
                    CodeNumberField codeNumberField2 = this.codeField[i8];
                    if (i8 != i - 1) {
                        i6 = i10;
                    } else {
                        i6 = 0;
                    }
                    addView(codeNumberField2, LayoutHelper.createLinear(i5, i4, 1, 0, 0, i6, 0));
                    this.codeField[i8].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.CodeFieldContainer.2
                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
                        }

                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable editable) {
                            int length;
                            if (!CodeFieldContainer.this.ignoreOnTextChange && (length = editable.length()) >= 1) {
                                int i11 = i8;
                                if (length > 1) {
                                    String string = editable.toString();
                                    CodeFieldContainer.this.ignoreOnTextChange = true;
                                    for (int i12 = 0; i12 < Math.min(i - i8, length); i12++) {
                                        if (i12 == 0) {
                                            editable.replace(0, length, string.substring(i12, i12 + 1));
                                        } else {
                                            i11++;
                                            int i13 = i8;
                                            int i14 = i13 + i12;
                                            CodeNumberField[] codeNumberFieldArr2 = CodeFieldContainer.this.codeField;
                                            if (i14 < codeNumberFieldArr2.length) {
                                                codeNumberFieldArr2[i13 + i12].setText(string.substring(i12, i12 + 1));
                                            }
                                        }
                                    }
                                    CodeFieldContainer.this.ignoreOnTextChange = false;
                                }
                                int i15 = i11 + 1;
                                if (i15 >= 0) {
                                    CodeNumberField[] codeNumberFieldArr3 = CodeFieldContainer.this.codeField;
                                    if (i15 < codeNumberFieldArr3.length) {
                                        CodeNumberField codeNumberField3 = codeNumberFieldArr3[i15];
                                        codeNumberField3.setSelection(codeNumberField3.length());
                                        CodeFieldContainer.this.codeField[i15].requestFocus();
                                    }
                                }
                                int i16 = i;
                                if ((i11 == i16 - 1 || (i11 == i16 - 2 && length >= 2)) && CodeFieldContainer.this.getCode().length() == i) {
                                    CodeFieldContainer.this.processNextPressed();
                                }
                            }
                        }
                    });
                    this.codeField[i8].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.CodeFieldContainer$$ExternalSyntheticLambda0
                        @Override // android.widget.TextView.OnEditorActionListener
                        public final boolean onEditorAction(TextView textView, int i11, KeyEvent keyEvent) {
                            return this.f$0.lambda$setNumbersCount$0(textView, i11, keyEvent);
                        }
                    });
                }
                i5 = i9;
                i4 = i3;
                CodeNumberField codeNumberField3 = this.codeField[i8];
                if (i8 != i - 1) {
                    i6 = i10;
                } else {
                    i6 = 0;
                }
                addView(codeNumberField3, LayoutHelper.createLinear(i5, i4, 1, 0, 0, i6, 0));
                this.codeField[i8].addTextChangedListener(new TextWatcher() { // from class: org.telegram.ui.CodeFieldContainer.2
                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence charSequence, int i11, int i12, int i13) {
                    }

                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable editable) {
                        int length;
                        if (!CodeFieldContainer.this.ignoreOnTextChange && (length = editable.length()) >= 1) {
                            int i11 = i8;
                            if (length > 1) {
                                String string = editable.toString();
                                CodeFieldContainer.this.ignoreOnTextChange = true;
                                for (int i12 = 0; i12 < Math.min(i - i8, length); i12++) {
                                    if (i12 == 0) {
                                        editable.replace(0, length, string.substring(i12, i12 + 1));
                                    } else {
                                        i11++;
                                        int i13 = i8;
                                        int i14 = i13 + i12;
                                        CodeNumberField[] codeNumberFieldArr2 = CodeFieldContainer.this.codeField;
                                        if (i14 < codeNumberFieldArr2.length) {
                                            codeNumberFieldArr2[i13 + i12].setText(string.substring(i12, i12 + 1));
                                        }
                                    }
                                }
                                CodeFieldContainer.this.ignoreOnTextChange = false;
                            }
                            int i15 = i11 + 1;
                            if (i15 >= 0) {
                                CodeNumberField[] codeNumberFieldArr3 = CodeFieldContainer.this.codeField;
                                if (i15 < codeNumberFieldArr3.length) {
                                    CodeNumberField codeNumberField4 = codeNumberFieldArr3[i15];
                                    codeNumberField4.setSelection(codeNumberField4.length());
                                    CodeFieldContainer.this.codeField[i15].requestFocus();
                                }
                            }
                            int i16 = i;
                            if ((i11 == i16 - 1 || (i11 == i16 - 2 && length >= 2)) && CodeFieldContainer.this.getCode().length() == i) {
                                CodeFieldContainer.this.processNextPressed();
                            }
                        }
                    }
                });
                this.codeField[i8].setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: org.telegram.ui.CodeFieldContainer$$ExternalSyntheticLambda0
                    @Override // android.widget.TextView.OnEditorActionListener
                    public final boolean onEditorAction(TextView textView, int i11, KeyEvent keyEvent) {
                        return this.f$0.lambda$setNumbersCount$0(textView, i11, keyEvent);
                    }
                });
            }
            return;
        }
        while (true) {
            CodeNumberField[] codeNumberFieldArr2 = this.codeField;
            if (i7 >= codeNumberFieldArr2.length) {
                return;
            }
            codeNumberFieldArr2[i7].setText(_UrlKt.FRAGMENT_ENCODE_SET);
            i7++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setNumbersCount$0(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 5) {
            return false;
        }
        processNextPressed();
        return true;
    }

    public String getCode() {
        if (this.codeField == null) {
            return _UrlKt.FRAGMENT_ENCODE_SET;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (true) {
            CodeNumberField[] codeNumberFieldArr = this.codeField;
            if (i < codeNumberFieldArr.length) {
                sb.append(PhoneFormat.stripExceptNumbers(codeNumberFieldArr[i].getText().toString()));
                i++;
            } else {
                return sb.toString();
            }
        }
    }

    public void setCode(String str) {
        this.codeField[0].setText(str);
    }

    public void setText(String str) {
        setText(str, false);
    }

    public void setText(String str, boolean z) {
        if (this.codeField == null) {
            return;
        }
        int i = 0;
        if (z) {
            int i2 = 0;
            while (true) {
                CodeNumberField[] codeNumberFieldArr = this.codeField;
                if (i2 >= codeNumberFieldArr.length) {
                    break;
                }
                if (codeNumberFieldArr[i2].isFocused()) {
                    i = i2;
                    break;
                }
                i2++;
            }
        }
        for (int i3 = i; i3 < Math.min(this.codeField.length, str.length() + i); i3++) {
            this.codeField[i3].setText(Character.toString(str.charAt(i3 - i)));
        }
    }
}
