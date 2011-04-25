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
import java.io.InputStreamReader;
import java.util.HashMap;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.htbest2000.reflect.New;
import com.htbest2000.reflect.Unmarshalling;

public class Nodes {
	
	public static class Node {
		public Node(long _id, String _name, String _url, String _title, String _title_alternative,
					int _topics, String _header, String _footer, String _created) {
			id = _id;
			name = _name;
			url = _url;
			title = _title;
			title_alternative = _title_alternative;
			topics = _topics;
			header = _header;
			footer = _footer;
			created = _created;
		}
		
		public Node(){
			id = -1;
		}

		public long id;
		public String name;
		public String url;
		public String title;
		public String title_alternative;
		public int topics;
		public String header;
		public String footer;
		public String created;
		
		public String toString() {
			return "Node with id: " + id + ", name: " + name;
		}
	}

	// ---
	public static interface Visitor {
		public void visit(Node topic);
	}

	Visitor mVisitor;

	public Visitor getVisitor() {
		return mVisitor;
	}
	public void setVisitor(Visitor mVisitor) {
		this.mVisitor = mVisitor;
	}
	// ---
	
	public void travel(InputStream in) throws IOException {
		CreateNote tc = new CreateNote(null);

		HashMap<String, Object> maps = new HashMap<String, Object>();
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		reader.beginArray();
		
		while (true) {
			JsonToken token = reader.peek();
			if (JsonToken.END_ARRAY.equals(token))
				break;
			
			maps.clear();
			tc.proceed(reader, maps);
			Node node = New.inflate(maps, Node.class);
			mVisitor.visit(node);
		}

		reader.close();
		in.close();
	}
	
	public static class CreateNote extends Unmarshalling<Node> {

		public CreateNote(InputStream in) {
			super(in, null, null);
		}

		@Override
		protected void proceed(JsonReader reader, HashMap<String, Object> maps)
				throws IOException {
			reader.beginObject();
			
			reader.nextName();
			maps.put("id", reader.nextLong());
			reader.nextName();
			maps.put("name", reader.nextString());
			reader.nextName();
			maps.put("url", reader.nextString());
			reader.nextName();
			maps.put("title", reader.nextString());
			reader.nextName();
			maps.put("title_alternative", reader.nextString());
			reader.nextName();
			maps.put("topics", reader.nextInt());
			reader.nextName();
			if (JsonToken.NULL.equals(reader.peek())) {
				maps.put("header", null);
				reader.nextNull();
			} else {
				maps.put("header", reader.nextString());
			}
			reader.nextName();
			if (JsonToken.NULL.equals(reader.peek())) {
				maps.put("header", null);
				reader.nextNull();
			} else {
				maps.put("footer", reader.nextString());
			}
			reader.nextName();
			maps.put("created", reader.nextString());

			reader.endObject();
		}
		
	}
}
