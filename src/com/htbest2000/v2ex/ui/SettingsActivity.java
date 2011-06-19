/*
 * Copyright (C) 2011 htbest2000@gmail.com
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

package com.htbest2000.v2ex.ui;

import com.htbest2000.v2ex.R;

import roboguice.activity.RoboPreferenceActivity;
import roboguice.inject.InjectPreference;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

public class SettingsActivity extends RoboPreferenceActivity implements OnPreferenceChangeListener {
	@InjectPreference("auto_update_interval") protected ListPreference mAutoUpdateInterval;
	@InjectPreference("days_to_sync")         protected ListPreference mDaysToSync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        
        mAutoUpdateInterval.setOnPreferenceChangeListener(this);
        mDaysToSync.setOnPreferenceChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	@Override
	public boolean onPreferenceChange(Preference pref, Object newValue) {
        final ListPreference listPref = (ListPreference) pref;
        final int idx = listPref.findIndexOfValue((String) newValue);
        listPref.setSummary(listPref.getEntries()[idx]);
        return true;
    }

	private void refresh() {
		mAutoUpdateInterval.setSummary(mAutoUpdateInterval.getEntry());
		mDaysToSync.setSummary(mDaysToSync.getEntry());
	}
	
}
