package com.explorefaith.pixelkey.latin

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import com.explorefaith.pixelkey.latin.SuggestedWords.SuggestedWordInfo
import com.explorefaith.pixelkey.latin.settings.Settings
import com.explorefaith.pixelkey.latin.uix.SUGGESTION_BLACKLIST
import com.explorefaith.pixelkey.latin.uix.getSetting
import com.explorefaith.pixelkey.latin.uix.getSettingFlow
import com.explorefaith.pixelkey.latin.uix.settings.badWords
import com.explorefaith.pixelkey.latin.uix.settings.isFiltered

class SuggestionBlacklist(val settings: Settings, val context: Context, val lifecycleScope: LifecycleCoroutineScope) {
    var offensiveWordsAdded = false
    var currentBlacklist: Set<String> = setOf()

    fun init() {
        lifecycleScope.launch {
            context.getSettingFlow(SUGGESTION_BLACKLIST).collect { value ->
                currentBlacklist = value + if(offensiveWordsAdded) { badWords } else { setOf() }
            }
        }
    }

    fun isSuggestedWordOk(word: SuggestedWordInfo): Boolean {
        return (word.mWord !in currentBlacklist) && (!offensiveWordsAdded || !isFiltered(word.mWord))
    }

    fun filterBlacklistedSuggestions(suggestions: SuggestedWords): SuggestedWords {
        if(settings.current.mBlockPotentiallyOffensive && !offensiveWordsAdded) {
            currentBlacklist = currentBlacklist + badWords
            offensiveWordsAdded = true
        } else if(!settings.current.mBlockPotentiallyOffensive && offensiveWordsAdded) {
            currentBlacklist = runBlocking {
                context.getSetting(SUGGESTION_BLACKLIST)
            }
            offensiveWordsAdded = false
        }

        val filter: (SuggestedWordInfo) -> Boolean = { it -> isSuggestedWordOk(it) || (it == suggestions.mTypedWordInfo) }

        val shouldStillAutocorrect = suggestions.mWillAutoCorrect
                && suggestions.size() > SuggestedWords.INDEX_OF_AUTO_CORRECTION
                && (suggestions.getInfo(SuggestedWords.INDEX_OF_AUTO_CORRECTION)?.let { filter(it) } ?: false)

        val filtered = suggestions.mSuggestedWordInfoList.filter(filter)

        return SuggestedWords(
            ArrayList(filtered),
            suggestions.mRawSuggestions?.filter {
                (it.mWord !in currentBlacklist) || (it == suggestions.mTypedWordInfo)
            }?.let { ArrayList(it) },
            suggestions.mTypedWordInfo,
            suggestions.mTypedWordValid,
            shouldStillAutocorrect,
            suggestions.mIsObsoleteSuggestions,
            suggestions.mInputStyle,
            suggestions.mSequenceNumber
        )
    }
}