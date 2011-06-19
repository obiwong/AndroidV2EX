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

package com.htbest2000.v2ex.util;

import android.content.Context;
import android.content.SharedPreferences;

public class StatFile {
	private static final String FILENAME = "stat.conf";
	private static final String SYNCING = "syncing";
	
	public static void setSyncBusy(Context ctx, boolean busy) {
		SharedPreferences spref = ctx.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		spref.edit().putInt(SYNCING, busy?1:0).commit();
	}
	
	public static boolean getSyncBush(Context ctx) {
		SharedPreferences spref = ctx.getSharedPreferences(FILENAME, Context.MODE_PRIVATE);
		int stat = spref.getInt(SYNCING, -1);
		return stat == 1;
	}
}
