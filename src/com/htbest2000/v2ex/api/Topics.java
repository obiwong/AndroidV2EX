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
		public Topic(long _id, String _title, String _url, String _content, String _cr, int _replies,
				long _member_id, String _member_username,
				long _node_id, String _node_name, String _node_title, String _node_title_alternative,
					String _node_url, int _node_topics) {
			id = _id;
			title = _title;
			url = _url;
			content = _content;
			content_rendered = _cr;
			replies = _replies;
			
			member_id = _member_id;
			member_username = _member_username;

			node_id = _node_id;
			node_name = _node_name;
			node_title = _node_title;
			node_title_alternative = _node_title_alternative;
			node_url = _node_url;
			node_topics = _node_topics;
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
		
		public long   member_id;
		public String member_username;
		
		public long   node_id;
		public String node_name;
		public String node_title;
		public String node_title_alternative;
		public String node_url;
		public int    node_topics;
		
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
			setNumberMap("member_id", Long.class);
			setNumberMap("node_id", Long.class);
			setNumberMap("node_topics", Integer.class);
		}

		@Override
		public void createObject(Topic obj) {
			if (mVisitor != null) {
				mVisitor.visit(obj);
			}
		}
	}
}
