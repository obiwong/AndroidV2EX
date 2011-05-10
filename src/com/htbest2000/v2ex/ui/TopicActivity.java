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

package com.htbest2000.v2ex.ui;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.htbest2000.v2ex.R;
import com.htbest2000.v2ex.R.id;
import com.htbest2000.v2ex.R.layout;
import com.htbest2000.v2ex.api.Topics;
import com.htbest2000.v2ex.util.Misc;

public class TopicActivity extends RoboActivity {
	@InjectView(R.id.topic_author) private TextView mTopicAuthor;
	@InjectView(R.id.topic_node) private TextView mTopicNode;
	@InjectView(R.id.topic_replies) private TextView mTopicReplies;
	@InjectView(R.id.topic_title) private TextView mTopicTitle;
	@InjectView(R.id.topic_content) private TextView mTopicContent;

	// @Inject LayoutInflater mLayoutInflater;
	
	Topics.Topic mTopic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_activity);
		long tid = getIntent().getLongExtra(TopicListActivity.TOPIC_ID, -1);
		if (-1 == tid) {
			mTopic = new Topics.Topic();
		} else {
			mTopic = Misc.getTopic(this, tid);
			loadTopic();
		}
	}
	
	

	@Override
	public Object onRetainNonConfigurationInstance() {
		// TODO Auto-generated method stub
		return super.onRetainNonConfigurationInstance();
	}



	private void loadTopic() {
		final Topics.Topic topic = mTopic;
		mTopicTitle.setText(topic.title);
		mTopicAuthor.setText(topic.member_username);
		mTopicNode.setText(topic.node_title);
		mTopicReplies.setText(String.valueOf(topic.replies));
		if (0 < topic.content.length()) {
			mTopicContent.setVisibility(View.VISIBLE);
			mTopicContent.setText(topic.content);
		} else {
			mTopicContent.setVisibility(View.GONE);
		}
	}
	
	public void onBrowserClick(View v) {
		if (-1 != mTopic.id) {
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(mTopic.url));
			startActivity(i);
		}
	}

}
