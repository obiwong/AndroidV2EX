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

package com.htbest2000.v2ex.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

import com.htbest2000.v2ex.api.Nodes;
import com.htbest2000.v2ex.api.Topics;
import com.htbest2000.v2ex.api.Nodes.Node;
import com.htbest2000.v2ex.api.Nodes.Visitor;
import com.htbest2000.v2ex.api.Topics.Topic;
import com.htbest2000.v2ex.io.Downloader;
import com.htbest2000.v2ex.provider.Database;
import com.htbest2000.v2ex.util.Misc;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import roboguice.service.RoboIntentService;

/**
 * Background Service that synchronizes data living in
 * Database.
 */
public class SyncService extends RoboIntentService {
	public static final boolean DEBUG = true;
	public static final String TAG = "=ht= Android V2EX sync service";
	
    public static final String EXTRA_STATUS_RECEIVER =
        "com.htbest2000.v2ex.service.STATUS_RECEIVER";

	public static final String PATH = "path";
	public static final String OPERATOR = "operator";
	
	public static final int OPERATOR_FETCH_TOPIC_LIST = 0;
	public static final int OPERATOR_FETCH_TOPIC_NODES = 1;
	
	private static final int COMMAND_FETCH_TOPIC_LIST = 0;
	private static final int COMMAND_FETCH_TOPIC_NODES = 1;
	
    public static final int STATUS_RUNNING = 1;
    public static final int STATUS_ERROR = 2;
    public static final int STATUS_FINISHED = 3;
	
	Downloader mDownloader;

	public SyncService() {
		super( TAG );
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mDownloader = new Downloader();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final String path     = intent.getStringExtra(PATH);
		final int    operator = intent.getIntExtra(   OPERATOR, -1);

		final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_STATUS_RECEIVER);
		if (receiver != null) receiver.send(STATUS_RUNNING, Bundle.EMPTY);

		try {
			switch (operator) {
			case OPERATOR_FETCH_TOPIC_LIST:
				fetchTopicList( path );
				break;

			case OPERATOR_FETCH_TOPIC_NODES:
				fetchNodes( path );
				break;
			}
		} catch (Exception e) {
            if (receiver != null) {
                final Bundle bundle = new Bundle();
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
		}
		
		if (receiver != null)
			receiver.send(STATUS_FINISHED, Bundle.EMPTY);
	}

	private void fetchTopicList(String path) throws IOException {
		mDownloader.setCommand( commandFactory(COMMAND_FETCH_TOPIC_LIST) );
		Log.i("=ht=", "start fetchTopicList");
		mDownloader.fetchHtml( path );
		Log.i("=ht=", "end fetchTopicList");
	}
	
	private void fetchNodes(String path) throws IOException {
		mDownloader.setCommand( commandFactory(COMMAND_FETCH_TOPIC_NODES) );
		Log.i("=ht=", "start fetchNodes");
		mDownloader.fetchHtml(path);
		Log.i("=ht=", "end fetchNodes");
	}

	private Downloader.Command commandFactory(int command) {
		Downloader.Command ret = null;
		
		switch (command) {
		case COMMAND_FETCH_TOPIC_LIST:
			ret = getTopicListCommand();
			break;
			
		case COMMAND_FETCH_TOPIC_NODES:
			ret = getTopicNodesCommand();
			break;
		}
		
		return ret;
	}
	
	private Downloader.Command getTopicListCommand() {
		return new Downloader.Command() {
			@Override
			public void onFeed(BufferedInputStream in) {
				Topics topics = new Topics();
				topics.setVisitor(new Topics.Visitor() {
					@Override
					public void visit(Topic topic) {
						Log.i("=ht=", "visit a topic :" + topic.toString());
						final Database db = Database.getInstance(SyncService.this);
						if (Misc.getTopicCount(SyncService.this, topic.id) < 1) {
							final ContentValues cv = new ContentValues();
							cv.put(Database.Columns.Topics.NAME_TOPTIC_ID, topic.id);
							cv.put(Database.Columns.Topics.NAME_TITLE, topic.title);
							cv.put(Database.Columns.Topics.NAME_URL, topic.url);
							cv.put(Database.Columns.Topics.NAME_CONTENT, topic.content);
							cv.put(Database.Columns.Topics.NAME_CONTENT_RENDERED, topic.content_rendered);
							cv.put(Database.Columns.Topics.NAME_REPLIES, topic.replies);
							cv.put(Database.Columns.Topics.NAME_TIME, System.currentTimeMillis());
							db.insert(Database.TABLE_TOPICS, cv);
						}
					}
				});
				try {
					topics.travel(in);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onPostOut(BufferedOutputStream out) {}

			@Override
			public void onPostIn(BufferedInputStream in) {}
		};
	}

	private Downloader.Command getTopicNodesCommand() {
		return new Downloader.Command() {
			@Override
			public void onFeed(BufferedInputStream in) {
				Nodes nodes = new Nodes();
				nodes.setVisitor(new Visitor() {
					@Override
					public void visit(Node node) {
						if (DEBUG) Log.i(TAG, "node: " + node);
						final ContentValues cv = new ContentValues();
						cv.put(Database.Columns.Nodes.NAME_NODE_ID, node.id);
						cv.put(Database.Columns.Nodes.NAME_NAME, node.name);
						cv.put(Database.Columns.Nodes.NAME_URL, node.url);
						cv.put(Database.Columns.Nodes.NAME_TITLE, node.title);
						cv.put(Database.Columns.Nodes.NAME_TITLE_ALTERNATIVE, node.title_alternative);
						cv.put(Database.Columns.Nodes.NAME_HEADER, node.header);
						cv.put(Database.Columns.Nodes.NAME_FOOTER, node.footer);
						cv.put(Database.Columns.Nodes.NAME_TOPICS, node.topics);
						cv.put(Database.Columns.Nodes.NAME_CREATED_TIME, node.created);

						final Database db = Database.getInstance(SyncService.this);
						Log.i("=ht=", "count: " + Misc.getNodeCount(SyncService.this, node.id));
						if (Misc.getNodeCount(SyncService.this, node.id) < 1) {
							db.insert(Database.TABLE_NODES, cv);
						} else {
							db.update(Database.TABLE_NODES, cv, Database.Columns.Nodes.NAME_NODE_ID + "=" + node.id, null);
						}
					}
				});

				try {
					nodes.travel(in);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onPostOut(BufferedOutputStream out) {
			}

			@Override
			public void onPostIn(BufferedInputStream in) {
			}
		};
	}
}