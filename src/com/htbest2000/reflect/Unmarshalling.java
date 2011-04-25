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
import java.util.HashMap;

import com.google.gson.stream.JsonReader;

public abstract class Unmarshalling<T> {
	private InputStream mIn;
	private String mCoding;
	private Class<T> mClazz;

	public Unmarshalling(InputStream in, String coding, Class<T> clazz) {
		mIn = in;
		mClazz = clazz;
		if (null == coding)
			mCoding = "UTF-8";
		else 
			mCoding = coding;
	}

	public T unmarshalling() throws IOException {
		HashMap<String, Object> maps = new HashMap<String, Object>();
		JsonReader reader = new JsonReader(new InputStreamReader(mIn, mCoding));
		proceed( reader, maps );
		reader.close();
		mIn.close();
		return New.inflate(maps, mClazz);
	}
	
	public void unmarshallingArray() throws IOException {
		
	}
	
	protected abstract void proceed(JsonReader reader, HashMap<String, Object> maps)
	 									throws IOException ;
}
