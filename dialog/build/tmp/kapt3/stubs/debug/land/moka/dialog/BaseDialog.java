package land.moka.dialog;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0012\u001a\u00020\u0005H\u0002J\b\u0010\u0013\u001a\u00020\u0005H\u0002J\b\u0010\u0014\u001a\u00020\u0015H&J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\u0017H\u0016J\b\u0010\u0019\u001a\u00020\u0017H\u0016J\u000f\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016\u00a2\u0006\u0002\u0010\u001cJ\b\u0010\u001d\u001a\u00020\u0005H&J\b\u0010\u001e\u001a\u00020\u001fH\u0016J\b\u0010 \u001a\u00020\u001fH\u0016J\b\u0010!\u001a\u00020\u001fH\u0016J\u0012\u0010\"\u001a\u00020\u00052\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J&\u0010%\u001a\u0004\u0018\u00010\u00152\u0006\u0010&\u001a\u00020\'2\b\u0010(\u001a\u0004\u0018\u00010)2\b\u0010#\u001a\u0004\u0018\u00010$H\u0016J\b\u0010*\u001a\u00020\u0005H\u0016R\"\u0010\u0003\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\"\u0010\n\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\u0007\"\u0004\b\f\u0010\tR\"\u0010\r\u001a\n\u0012\u0004\u0012\u00020\u0005\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u0007\"\u0004\b\u000f\u0010\tR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lland/moka/dialog/BaseDialog;", "Landroidx/appcompat/app/AppCompatDialogFragment;", "()V", "onClickNegative", "Lkotlin/Function0;", "", "getOnClickNegative", "()Lkotlin/jvm/functions/Function0;", "setOnClickNegative", "(Lkotlin/jvm/functions/Function0;)V", "onClickNeutral", "getOnClickNeutral", "setOnClickNeutral", "onClickPositive", "getOnClickPositive", "setOnClickPositive", "rootBinding", "Lland/moka/dialog/databinding/LayoutBaseDialogBinding;", "_bindView", "_init", "getContentView", "Landroid/view/View;", "getNegativeText", "", "getNeutralText", "getPositiveText", "getWidthRatio", "", "()Ljava/lang/Float;", "init", "isNegativeText", "", "isNeutralText", "isPositive", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onResume", "dialog_debug"})
public abstract class BaseDialog extends androidx.appcompat.app.AppCompatDialogFragment {
    private land.moka.dialog.databinding.LayoutBaseDialogBinding rootBinding;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function0<kotlin.Unit> onClickPositive;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function0<kotlin.Unit> onClickNegative;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function0<kotlin.Unit> onClickNeutral;
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function0<kotlin.Unit> getOnClickPositive() {
        return null;
    }
    
    public final void setOnClickPositive(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function0<kotlin.Unit> getOnClickNegative() {
        return null;
    }
    
    public final void setOnClickNegative(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function0<kotlin.Unit> getOnClickNeutral() {
        return null;
    }
    
    public final void setOnClickNeutral(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> p0) {
    }
    
    @java.lang.Override()
    public void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    private final void _init() {
    }
    
    private final void _bindView() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public java.lang.CharSequence getPositiveText() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public java.lang.CharSequence getNegativeText() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public java.lang.CharSequence getNeutralText() {
        return null;
    }
    
    public boolean isPositive() {
        return false;
    }
    
    public boolean isNegativeText() {
        return false;
    }
    
    public boolean isNeutralText() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public java.lang.Float getWidthRatio() {
        return null;
    }
    
    public abstract void init();
    
    @org.jetbrains.annotations.NotNull()
    public abstract android.view.View getContentView();
    
    public BaseDialog() {
        super();
    }
}