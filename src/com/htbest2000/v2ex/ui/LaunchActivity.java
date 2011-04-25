/*
 * Copyright (C) 2011 htbest2000@gmail.com
 * Copyright (C) 2007 The Android Open Source Project
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

import com.htbest2000.v2ex.TheApplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import roboguice.activity.RoboActivity;

public class LaunchActivity extends RoboActivity {
    private Bundle mExtras;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        ////////////////////////
        // setup for pass intent
        ////////////////////////
        //
        // Get the data for from this intent, if any
        final Intent myIntent = getIntent();
        Uri myData = myIntent.getData();
        //
        // Set up the intent for the start activity
        final Intent intent = new Intent();
        if (myData != null) {
            intent.setData(myData);
        }
        intent.putExtras(myIntent);

        ///////////////////////////
        // determine first Activity
        ///////////////////////////
        //
        SharedPreferences prefs = PreferenceActivity.getSharedPreferences(this);
        final String startActivity = prefs.getString(
        		PreferenceActivity.KEY_START_ACTIVITY,
        		TheApplication.ACTIVITY_NAMES[TheApplication.ACTIVITY_HOME_ID]);
        intent.setClassName(this, startActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity( intent );
        finish();
    }
}
