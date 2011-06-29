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

package com.htbest2000.v2ex.receiver;

import com.htbest2000.v2ex.util.StatFile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NormalReceiver extends BroadcastReceiver {
	public static final boolean DEBUG = true;
	public static final String TAG = "=AV2EX=NormalReceiver=";

	@Override
	public void onReceive(Context ctx, Intent intent) {
		if (DEBUG) Log.i(TAG, "on boot");
		if (StatFile.getSyncBush(ctx)) {
			StatFile.setSyncBusy(ctx, false);
		}
	}
}
