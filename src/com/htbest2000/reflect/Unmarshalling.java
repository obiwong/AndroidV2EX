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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public abstract class Unmarshalling<T> {
	protected final InputStream  mIn;
	protected final String   mCoding;
	protected final Class<T>  mClazz;

	//------------------------------------------------

	protected final HashMap<String, Class<?>> mNumberMap = new HashMap<String, Class<?>>();
	
	public void setNumberMap(String name, Class<?> value) {
		mNumberMap.put(name, value);
	}

	public void clearNumberMap() {
		mNumberMap.clear();
	}

	//------------------------------------------------
	
	/*
	 * Constructor
	 */
	public Unmarshalling(InputStream in, String coding, Class<T> clazz) {
		mIn = in;
		mClazz = clazz;
		mCoding = (null == coding) ? "UTF-8" : coding;
	}
	
	//------------------------------------------------

	public void collectData(JsonReader reader, HashMap<String, Object> dataMap)
																		throws IOException {
		ArrayList<String> parents = new ArrayList<String>();

		reader.beginObject();
		while (true) {
			JsonToken token = reader.peek();
			if (JsonToken.END_ARRAY.equals(token)) {
				break;
			}
			if (JsonToken.END_OBJECT.equals(token)) {
				reader.endObject();
				if (parents.isEmpty()) {
					break;
				} else {
					parents.remove(parents.size() - 1);
					continue;
				}
			}
			if (JsonToken.END_DOCUMENT.equals(token)) {
				break;
			}

			String name = "";
			for (String n : parents) {
				name += n + "_";
			}
			name = name + reader.nextName();
			JsonToken tok = reader.peek();

			if (JsonToken.STRING.equals(tok)) {
				dataMap.put(name, reader.nextString());
			} else if (JsonToken.NULL.equals(tok)) {
				dataMap.put(name, null);
				reader.nextNull();
			} else if (JsonToken.BOOLEAN.equals(tok)) {
				dataMap.put(name, reader.nextBoolean());
			} else if (JsonToken.NUMBER.equals(tok)) {
				Class<?> clazz = mNumberMap.get(name);
				if (null != clazz) {
					if (Integer.class.equals(clazz)) {
						dataMap.put(name, reader.nextInt());
					} else if (Long.class.equals(clazz)) {
						dataMap.put(name, reader.nextLong());
					} else if (Double.class.equals(clazz)) {
						dataMap.put(name, reader.nextDouble());
					}
				}
			} else if (JsonToken.BEGIN_OBJECT.equals(tok)) {
				parents.add(name);
				reader.beginObject();
			}
		}
	}

	public abstract void unmarshalled() throws IOException;
	public abstract void createObject(T obj);

	//===================================================================
	
	public static abstract class SingleObject<T> extends Unmarshalling<T> {
		public SingleObject(InputStream in, String coding, Class<T> clazz) {
			super(in, coding, clazz);
		}

		@Override
		public final void unmarshalled() throws IOException {
			JsonReader reader = new JsonReader(new InputStreamReader(mIn, mCoding));
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			collectData( reader, dataMap );
			createObject( New.inflate(dataMap, mClazz) );
			reader.close();
			mIn.close();
		}
	}
	
	//--------------------------------------------------------------------
	
	public static abstract class ObjectArray<T> extends Unmarshalling<T> {
		public ObjectArray(InputStream in, String coding, Class<T> clazz) {
			super(in, coding, clazz);
		}

		@Override
		public final void unmarshalled() throws IOException {
			JsonReader reader = new JsonReader(new InputStreamReader(mIn, mCoding));
			HashMap<String, Object> dataMap = new HashMap<String, Object>();
			
			reader.beginArray();
			while (true) {
				JsonToken token = reader.peek();
				if (JsonToken.END_ARRAY.equals(token))
					break;

				dataMap.clear();
				collectData(reader, dataMap);
				
				createObject( New.inflate(dataMap, mClazz) );
			}
			reader.close();
			mIn.close();
		}
	}

}
