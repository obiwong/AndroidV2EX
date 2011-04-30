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

import com.htbest2000.reflect.Unmarshalling;

public class Topics {

	public static class Topic {
		public Topic(long _id, String _title, String _url, String _content, String _cr, int _replies) {
			id = _id;
			title = _title;
			url = _url;
			content = _content;
			content_rendered = _cr;
			replies = _replies;
		}
		
		public Topic(){
			id = -1;
		}
		
		public long id;
		public String title;
		public String url;
		public String content;
		public String content_rendered;
		public int replies;
		
		public String toString() {
			return "Topic with id: " + id + ", title: " + title;
		}
	}
	
	// ---
	public static interface Visitor {
		public void visit(Topic topic);
	}

	Visitor mVisitor;

	public Visitor getVisitor() {
		return mVisitor;
	}

	public void setVisitor(Visitor visitor) {
		this.mVisitor = visitor;
	}
	// ---

	public void travel(InputStream in) throws IOException {
		CreateTopic tc = new CreateTopic(in);
		tc.unmarshalled();
	}

	private class CreateTopic extends Unmarshalling.ObjectArray<Topic> {

		public CreateTopic(InputStream in) {
			super(in, null, Topic.class);
			setNumberMap("id", Long.class);
			setNumberMap("replies", Integer.class);
		}

		@Override
		public void createObject(Topic obj) {
			if (mVisitor != null) {
				mVisitor.visit(obj);
			}
		}
	}
}
