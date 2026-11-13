/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.explorefaith.pixelkey.latin.suggestions;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.core.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.explorefaith.pixelkey.accessibility.AccessibilityUtils;
import com.explorefaith.pixelkey.keyboard.Keyboard;
import com.explorefaith.pixelkey.keyboard.MainKeyboardView;
import com.explorefaith.pixelkey.keyboard.MoreKeysPanel;
import com.explorefaith.pixelkey.latin.AudioAndHapticFeedbackManager;
import com.explorefaith.pixelkey.latin.R;
import com.explorefaith.pixelkey.latin.SuggestedWords;
import com.explorefaith.pixelkey.latin.SuggestedWords.SuggestedWordInfo;
import com.explorefaith.pixelkey.latin.common.Constants;
import com.explorefaith.pixelkey.latin.define.DebugFlags;
import com.explorefaith.pixelkey.latin.settings.Settings;
import com.explorefaith.pixelkey.latin.settings.SettingsValues;
import com.explorefaith.pixelkey.latin.suggestions.MoreSuggestionsView.MoreSuggestionsListener;
import com.explorefaith.pixelkey.latin.utils.ImportantNoticeUtils;

import java.util.ArrayList;

public final class SuggestionStripView extends RelativeLayout implements OnClickListener,
        OnLongClickListener {
    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    public interface Listener {
        public void showImportantNoticeContents();
        public void pickSuggestionManually(SuggestedWordInfo word);
        public void requestForgetWord(SuggestedWordInfo word);
        public void onCodeInput(int primaryCode, int x, int y, boolean isKeyRepeat);
    }

    static final boolean DBG = DebugFlags.DEBUG_ENABLED;

    /**
     * Construct a {@link SuggestionStripView} for showing suggestions to be picked by the user.
     * @param context
     * @param attrs
     */
    public SuggestionStripView(final Context context, final AttributeSet attrs) {
        this(context, attrs, R.attr.suggestionStripViewStyle);
    }

    public SuggestionStripView(final Context context, final AttributeSet attrs,
            final int defStyle) {
        super(context, attrs, defStyle);

    }
}
