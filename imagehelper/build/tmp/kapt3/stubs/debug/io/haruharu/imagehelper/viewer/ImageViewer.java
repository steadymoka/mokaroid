package io.haruharu.imagehelper.viewer;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 15}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\t2\u0006\u0010\u001c\u001a\u00020\u001dJ\u000e\u0010\u000e\u001a\u00020\t2\u0006\u0010\u001e\u001a\u00020\u001dJ\u0006\u0010\u0011\u001a\u00020\tJ0\u0010\u001f\u001a\u00020\t2\u0006\u0010 \u001a\u00020!2\u0016\u0010\"\u001a\u0012\u0012\u0004\u0012\u00020$0#j\b\u0012\u0004\u0012\u00020$`%2\b\b\u0002\u0010&\u001a\u00020\u0018R7\u0010\u0003\u001a\u001f\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0004\u0012\u00020\t\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR7\u0010\u000e\u001a\u001f\u0012\u0013\u0012\u00110\u0005\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\b\u0012\u0004\u0012\u00020\t\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u000b\"\u0004\b\u0010\u0010\rR\"\u0010\u0011\u001a\n\u0012\u0004\u0012\u00020\t\u0018\u00010\u0012X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R7\u0010\u0017\u001a\u001f\u0012\u0013\u0012\u00110\u0018\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u0019\u0012\u0004\u0012\u00020\t\u0018\u00010\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u000b\"\u0004\b\u001b\u0010\r\u00a8\u0006\'"}, d2 = {"Lio/haruharu/imagehelper/viewer/ImageViewer;", "", "()V", "addFooter", "Lkotlin/Function1;", "Landroid/widget/FrameLayout;", "Lkotlin/ParameterName;", "name", "parent", "", "getAddFooter", "()Lkotlin/jvm/functions/Function1;", "setAddFooter", "(Lkotlin/jvm/functions/Function1;)V", "addHeader", "getAddHeader", "setAddHeader", "finish", "Lkotlin/Function0;", "getFinish", "()Lkotlin/jvm/functions/Function0;", "setFinish", "(Lkotlin/jvm/functions/Function0;)V", "onPageSelected", "", "position", "getOnPageSelected", "setOnPageSelected", "footer", "Landroid/view/View;", "header", "show", "context", "Landroid/content/Context;", "paths", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "selectedPosition", "imagehelper_debug"})
public final class ImageViewer {
    @org.jetbrains.annotations.Nullable()
    private static kotlin.jvm.functions.Function0<kotlin.Unit> finish;
    @org.jetbrains.annotations.Nullable()
    private static kotlin.jvm.functions.Function1<? super android.widget.FrameLayout, kotlin.Unit> addHeader;
    @org.jetbrains.annotations.Nullable()
    private static kotlin.jvm.functions.Function1<? super android.widget.FrameLayout, kotlin.Unit> addFooter;
    @org.jetbrains.annotations.Nullable()
    private static kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onPageSelected;
    public static final io.haruharu.imagehelper.viewer.ImageViewer INSTANCE = null;
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function0<kotlin.Unit> getFinish() {
        return null;
    }
    
    public final void setFinish(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function0<kotlin.Unit> p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function1<android.widget.FrameLayout, kotlin.Unit> getAddHeader() {
        return null;
    }
    
    public final void setAddHeader(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super android.widget.FrameLayout, kotlin.Unit> p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function1<android.widget.FrameLayout, kotlin.Unit> getAddFooter() {
        return null;
    }
    
    public final void setAddFooter(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super android.widget.FrameLayout, kotlin.Unit> p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final kotlin.jvm.functions.Function1<java.lang.Integer, kotlin.Unit> getOnPageSelected() {
        return null;
    }
    
    public final void setOnPageSelected(@org.jetbrains.annotations.Nullable()
    kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> p0) {
    }
    
    public final void show(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.util.ArrayList<java.lang.String> paths, int selectedPosition) {
    }
    
    public final void addHeader(@org.jetbrains.annotations.NotNull()
    android.view.View header) {
    }
    
    public final void addFooter(@org.jetbrains.annotations.NotNull()
    android.view.View footer) {
    }
    
    public final void finish() {
    }
    
    private ImageViewer() {
        super();
    }
}