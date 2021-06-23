package com.miljanpeles.lib.units;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class KeyboardHelper {

    private static final int TAG_LISTENER_ID = -10010;

    private KeyboardHelper() {}

    /**
     * Sklanja tastaturu i focus sa trenutno fokusiranog viewa
     */
    public static void hideSoftKeyboard(Activity activity) {
        hideSoftKeyboard(activity, activity.getCurrentFocus());
    }

    /**
     * Sklanja tastaturu i cisti focus sa datog viewa
     * @param context Context
     * @param focusedView trenutno fokusiran view
     */
    public static void hideSoftKeyboard(@NonNull Context context, @Nullable View focusedView) {
        if (focusedView == null) {
            return;
        }

        final InputMethodManager manager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);

        manager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        focusedView.clearFocus();
    }

    /**
     * Prikazi tastaturu i zatrazi fokus na dati view
     */
    public static void showSoftKeyboard(Context context, View view) {
        if (view == null) {
            return;
        }

        final InputMethodManager manager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        view.requestFocus();
        manager.showSoftInput(view, 0);
    }

    /**
     * Registruje listenere za prikazivanje i sklanjanje tastature
     * Status tastature je izracunat u odnosu na promenu visine rootViewa
     *
     * @param rootView treba biti view prosledjen Activity.setContentView(...) ili view vracen od Fragment.onCreateView(...)
     * @param listener listener
     */
    public static void addKeyboardListener(@NonNull final View rootView, @NonNull final OnKeyboardShowListener listener) {

        final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            private boolean isKeyboardShown;
            private int initialHeightsDiff = -1;

            @Override
            public void onGlobalLayout() {
                final Rect frame = new Rect();
                rootView.getWindowVisibleDisplayFrame(frame);

                int heightDiff = rootView.getRootView().getHeight() - (frame.bottom - frame.top);
                if (initialHeightsDiff == -1) {
                    initialHeightsDiff = heightDiff;
                }
                heightDiff -= initialHeightsDiff;

                if (heightDiff > 100) { // ako je vise od 100 piksela, vrv je tastatura
                    if (!isKeyboardShown) {
                        isKeyboardShown = true;
                        listener.onKeyboardShow(true);
                    }
                } else if (heightDiff < 50) {
                    if (isKeyboardShown) {
                        isKeyboardShown = false;
                        listener.onKeyboardShow(false);
                    }
                }
            }
        };

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        rootView.setTag(TAG_LISTENER_ID, layoutListener);
    }

    public static void removeKeyboardListener(@NonNull View rootView) {
        final ViewTreeObserver.OnGlobalLayoutListener layoutListener =
                (ViewTreeObserver.OnGlobalLayoutListener) rootView.getTag(TAG_LISTENER_ID);
        removeOnGlobalLayoutListener(rootView, layoutListener);
    }


    public interface OnKeyboardShowListener {
        void onKeyboardShow(boolean show);
    }

    private static void removeOnGlobalLayoutListener(@NonNull View view, @NonNull ViewTreeObserver.OnGlobalLayoutListener listener) {
        view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }

}