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
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.htbest2000.v2ex.R;
import com.htbest2000.v2ex.provider.Database;
import com.htbest2000.v2ex.service.SyncService;
import com.htbest2000.v2ex.util.DetachableResultReceiver;
import com.htbest2000.v2ex.util.Misc;
import com.htbest2000.v2ex.util.State;
import com.htbest2000.v2ex.util.ToastMaster;

public class AllNodesActivity extends RoboActivity implements DetachableResultReceiver.Receiver {
	public static final String ALL_NODES = "/api/nodes/all.json";
	private State mState;
	private SimpleCursorAdapter mNodesAdapter;
	
	static final String NODE_ID = "node_id";
	static final String LATEST_TOPICS="/api/topics/latest.json";
	
	Handler mHandler = new Handler();

	@InjectView(R.id.nodes_list) private ListView mNodeList;
	@InjectView(R.id.btn_title_refresh) private ImageView mRefreshView;
	@InjectResource(R.anim.spin_self) private Animation mAniSpinSelf;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_nodes_activity);
		
        // restore/create activity state
		mState = (State) getLastNonConfigurationInstance();
        final boolean previousState = (mState != null);
        if (previousState) {
            // Start listening for SyncService updates again
            mState.mReceiver.setReceiver(this);
        } else {
            mState = new State();
            mState.mReceiver.setReceiver(this);
        }
	}
	
    @Override
    public Object onRetainNonConfigurationInstance() {
        mState.mReceiver.clearReceiver();
        return mState;
    }

	@Override
	protected void onResume() {
		super.onResume();
		setupNodesListAdapter();
	}

	public void onRefreshClick(View v) {
		Database.getInstance(this).delete(Database.TABLE_NODES, null, null);
    	
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, this, SyncService.class);
        intent.putExtra(SyncService.EXTRA_STATUS_RECEIVER, mState.mReceiver);
        intent.putExtra(SyncService.OPERATOR, SyncService.OPERATOR_FETCH_TOPIC_NODES);
        intent.putExtra(SyncService.PATH, ALL_NODES);
        startService(intent);
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
    		case SyncService.STATUS_RUNNING: {
    			mState.mSyncing = true;
    			mRefreshView.setFocusable(false);
    			mRefreshView.setClickable(false);
    			mRefreshView.startAnimation(mAniSpinSelf);
    			break;
    		}
    		case SyncService.STATUS_FINISHED: {
    			mState.mSyncing = false;
    			mState.mGotError = false;
    			mRefreshView.setFocusable(true);
    			mRefreshView.setClickable(true);
    			mRefreshView.clearAnimation();
    			setupNodesListAdapter();
    			break;
    		}
    		case SyncService.STATUS_ERROR: {
    			// Error happened down in SyncService, show as toast.
    			mState.mSyncing = false;
    			mState.mGotError = true;
    			mRefreshView.setFocusable(true);
    			mRefreshView.setClickable(true);
    			mRefreshView.clearAnimation();
    			final Toast toast = Toast.makeText(AllNodesActivity.this.getApplicationContext(),
    					R.string.fetch_topic_list_error, Toast.LENGTH_SHORT);
    			ToastMaster.setToast(toast);
    			toast.show();
    			break;
    		}
        }
	}

	private void setupNodesListAdapter() {
		Cursor cur = Misc.getAllNodesCursor( this );
		if (null == cur)
			return;
		
		Log.i("=ht=", "node cur: " + cur.getCount());
		
		if (0 == cur.getCount()) {
			cur.close();
			return;
		}
		
		Log.i("=ht=", "node cur: " + cur);
		
		cur.moveToFirst();
		this.startManagingCursor(cur);
		mNodesAdapter = new SimpleCursorAdapter( this, R.layout.nodes_list_row, cur,
			new String[] {Database.Columns.Nodes.NAME_TITLE, Database.Columns.Nodes.NAME_TOPICS},
			new int[] { R.id.node_title, R.id.node_topics });
		
		mNodeList.setAdapter( mNodesAdapter );
	}
}
