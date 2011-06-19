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

	protected final New<T>      mNew = new New<T>();

	protected final HashMap<String, Class<?>> mNumberMap = new HashMap<String, Class<?>>();
	protected final HashMap<String, Customize<?,?>> mCustomizeMap = new HashMap<String, Customize<?,?>>();

	//------------------------------------------------

	public void setNumberMap(String name, Class<?> value) {
		mNumberMap.put(name, value);
	}

	public void clearNumberMap() {
		mNumberMap.clear();
	}
	
	//------------------------------------------------
	
	public static interface Customize<D,S> {
		public D inflate( Object object );
	}

	public void addCustomize(String name, Customize<?,?> cust) {
		mCustomizeMap.put(name, cust);
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

			final StringBuilder nameBuilder = new StringBuilder();
			for (String n : parents) {
				nameBuilder.append(n).append("_");
			}
			nameBuilder.append(reader.nextName());
			final String name = nameBuilder.toString();
			JsonToken tok = reader.peek();

			if (JsonToken.STRING.equals(tok)) {
				String str = reader.nextString();
				if (mCustomizeMap.containsKey(name)) {
					dataMap.put(name, mCustomizeMap.get(name).inflate(str));
				} else {
					dataMap.put(name, str);
				}
			} else if (JsonToken.NULL.equals(tok)) {
				dataMap.put(name, null);
				reader.nextNull();
			} else if (JsonToken.BOOLEAN.equals(tok)) {
				boolean boo = reader.nextBoolean();
				if (mCustomizeMap.containsKey(name)) {
					dataMap.put(name, mCustomizeMap.get(name).inflate(boo));
				} else {
					dataMap.put(name, boo);
				}
			} else if (JsonToken.NUMBER.equals(tok)) {
				Class<?> clazz = mNumberMap.get(name);
				if (null != clazz) {
					if (Integer.class.equals(clazz)) {
						Integer val = reader.nextInt();
						if (mCustomizeMap.containsKey(name)) {
							dataMap.put(name, mCustomizeMap.get(name).inflate(val));
						} else {
							dataMap.put(name, val);
						}
					} else if (Long.class.equals(clazz)) {
						Long val = reader.nextLong();
						if (mCustomizeMap.containsKey(name)) {
							dataMap.put(name, mCustomizeMap.get(name).inflate(val));
						} else {
							dataMap.put(name, val);
						}
					} else if (Double.class.equals(clazz)) {
						Double val = reader.nextDouble();
						if (mCustomizeMap.containsKey(name)) {
							dataMap.put(name, mCustomizeMap.get(name).inflate(val));
						} else {
							dataMap.put(name, val);
						}
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
			createObject( mNew.inflate(dataMap, mClazz) );
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
				
				createObject( mNew.inflate(dataMap, mClazz) );
			}
			reader.close();
			mIn.close();
		}
	}

}
