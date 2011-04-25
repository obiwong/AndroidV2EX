/*
 * Copyright (C) 2011 htbest2000@gmail.com
 * Copyright 2010 Google Inc.
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

package com.htbest2000.v2ex.util;

import android.app.Activity;
import android.os.Handler;

import com.htbest2000.v2ex.ui.ActivityHome;

/**
 * State specific to {@link ActivityHome} that is held between configuration
 * changes. Any strong {@link Activity} references <strong>must</strong> be
 * cleared before {@link #onRetainNonConfigurationInstance()}, and this
 * class should remain {@code static class}.
 */
public class State {
    public DetachableResultReceiver mReceiver;
    public boolean mSyncing = false;
    public boolean mGotError = false;

    public State() {
        mReceiver = new DetachableResultReceiver(new Handler());
    }
}
