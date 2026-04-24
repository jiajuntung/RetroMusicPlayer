/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import code.name.monkey.retromusic.AUTO_DOWNLOAD_IMAGES_POLICY
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.RESET_CUSTOM_ARTIST_IMAGES
import code.name.monkey.retromusic.extensions.showToast
import code.name.monkey.retromusic.util.CustomArtistImageUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

/**
 * @author Hemanth S (h4h13).
 */

class ImageSettingFragment : AbsSettingsFragment() {
    override fun invalidateSettings() {
        val autoDownloadImagesPolicy: Preference = findPreference(AUTO_DOWNLOAD_IMAGES_POLICY)!!
        setSummary(autoDownloadImagesPolicy)
        autoDownloadImagesPolicy.setOnPreferenceChangeListener { _, o ->
            setSummary(autoDownloadImagesPolicy, o)
            true
        }

        val resetCustomArtistImages: Preference? = findPreference(RESET_CUSTOM_ARTIST_IMAGES)
        resetCustomArtistImages?.setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.pref_title_reset_custom_artist_images)
                .setMessage(R.string.pref_message_reset_custom_artist_images)
                .setPositiveButton(R.string.reset_action) { _, _ ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        val deletedCount =
                            CustomArtistImageUtil.getInstance(requireContext())
                                .resetAllCustomArtistImages()
                        showToast(
                            getString(
                                R.string.message_reset_custom_artist_images,
                                deletedCount
                            )
                        )
                    }
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
            true
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_images)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preference: Preference? = findPreference(AUTO_DOWNLOAD_IMAGES_POLICY)
        preference?.let { setSummary(it) }
    }
}
