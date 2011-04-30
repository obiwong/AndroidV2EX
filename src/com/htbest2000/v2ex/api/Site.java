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

package com.htbest2000.v2ex.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.google.gson.stream.JsonReader;
import com.htbest2000.reflect.Unmarshalling;

public class Site {
	public String title;
	public String slogan;
	public String description;
	public String domain;

	public static Site create(InputStream in) throws IOException {
		Creater cs = new Creater(in, null, Site.class);
		cs.unmarshalled();
		return cs.mSite;
	}

	private static class Creater extends Unmarshalling.SingleObject<Site> {
		Site mSite;
		
		public Creater(InputStream in, String coding, Class<Site> clazz) {
			super(in, coding, clazz);
		}

		@Override
		public void collectData(JsonReader reader,
				HashMap<String, Object> dataMap) throws IOException {
			String name;
			String val;

			reader.beginObject();
			for (int i=0; i<4; i++) {
				name = reader.nextName();
				val  = reader.nextString();
				dataMap.put(name, val);
			}
		}

		@Override
		public void createObject(Site obj) {
			mSite = obj;
		}
	}
}
