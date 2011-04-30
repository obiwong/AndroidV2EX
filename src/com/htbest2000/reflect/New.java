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

package com.htbest2000.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;

import android.util.Log;

public class New {
	public static final String TAG = "=AV2EX=New=";

	/**
	 * Inflate object from map
	 * @param src the values
	 * @param type the class class
	 * @return object with fulfilled values
	 */
	public static <T> T inflate( HashMap<String, ?> src, Class<T> type ) {
        T ret;
        try {
            ret = type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            Log.e(TAG, "InstantiationException: " + e.toString());
            return null;
        } catch (IllegalAccessException e) {
        	Log.e(TAG, "IllegalAccessException: " + e.toString());
            e.printStackTrace();
            return null;
        }

        final Field[] fields = type.getFields();
        for (Field field : fields) {
            String name = field.getName();
            Class<?> field_type = field.getType();
            try {
            	if (field_type.equals(String.class)) {
            		field.set(ret, src.get(name));
            	} else if (field_type.equals(Integer.class) || field_type.equals(int.class)) {
            		field.setInt(ret, (Integer)src.get(name));
            	} else if (field_type.equals(Long.class) || field_type.equals(long.class)) {
            		field.setLong(ret, (Long)src.get(name));
            	}
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "set field failed: " + e.toString());
                return null;
            }
        }

        return ret;
    }
}
