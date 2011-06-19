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

package com.htbest2000.v2ex.provider;

import java.util.Iterator;

import com.htbest2000.v2ex.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


public class Database {
	public static final String TAG = "Database";
	
	public static final String TABLE_TOPICS = "topics";
	public static final String TABLE_NODES = "nodes";
	
	private SQLiteOpenHelper mOpenHelper;
	
	public int delete(String table, String where, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int ret = db.delete(table, where, whereArgs);
		db.close();
		return ret;
	}
	
	public long insert(String table, ContentValues values) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long ret = db.insert(table, null, values);
		db.close();
		return ret;
	}
	
	public long update(String table, ContentValues values,
					  String whereClause, String[] whereArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long ret = db.update(table, values, whereClause, whereArgs);
		db.close();
		return ret;
	}
	
	public Cursor query(String table, String[] columns,
						String selection, String[] selectionArgs,
						String groupBy, String having, String orderBy){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Cursor ret = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
		return ret;
	}
	

	private Database() {}
	
	private static Database mInstance; 
	public  static Database getInstance( Context ctx ) {
		if (null == mInstance) {
			synchronized (Database.class) {
				if (null == mInstance) {
					mInstance = new Database();
					mInstance.mOpenHelper = new DatabaseHelper(ctx);
				}
			}
		}

		return mInstance;
	}
	
	public static class Columns {
		public static class Topics implements BaseColumns {
			public static final String NAME_TOPTIC_ID = "topic_id";
			public static final String NAME_TITLE = "title";
			public static final String NAME_URL = "url";
			public static final String NAME_CONTENT = "content";
			public static final String NAME_CONTENT_RENDERED = "content_rendered";
			public static final String NAME_REPLIES = "replies";
			public static final String NAME_MEMBER_ID = "member_id";
			public static final String NAME_MEMBER_USERNAME = "member_username";
			public static final String NAME_NODE_ID = "node_id";
			public static final String NAME_NODE_NAME = "node_name";
			public static final String NAME_NODE_TITLE = "node_title";
			public static final String NAME_NODE_TITLE_ALTERNATIVE = "node_title_alternative";
			public static final String NAME_NODE_URL = "node_url";
			public static final String NAME_NODE_TOPICS = "node_topics";
			public static final String NAME_CREATED = "created";
			public static final String NAME_LAST_MODIFIED = "last_modified";
			public static final String NAME_LAST_TOUCHED = "last_touched";

			public static final int ID_TOPTIC_ID = 1;
			public static final int ID_TITLE = 2;
			public static final int ID_URL = 3;
			public static final int ID_CONTENT = 4;
			public static final int ID_CONTENT_RENDERED = 5;
			public static final int ID_REPLIES = 6;
			public static final int ID_MEMBER_ID = 7;
			public static final int ID_MEMBER_NAME = 8;
			public static final int ID_NODE_ID = 9;
			public static final int ID_NODE_NAME = 10;
			public static final int ID_NODE_TITLE = 11;
			public static final int ID_NODE_TITLE_ALTERNATIVE = 12;
			public static final int ID_NODE_URL = 13;
			public static final int ID_NODE_TOPICS = 14;
			public static final int ID_CREATED = 15;
			public static final int ID_LAST_MODIFIED = 16;
			public static final int ID_LAST_TOUCHED = 17;
		}

		public static class Nodes implements BaseColumns {
			public static final String NAME_NODE_ID = "node_id";
			public static final String NAME_NAME = "name";
			public static final String NAME_URL = "url";
			public static final String NAME_TITLE = "title";
			public static final String NAME_TITLE_ALTERNATIVE = "title_alternative";
			public static final String NAME_TOPICS = "topics";
			public static final String NAME_HEADER = "header";
			public static final String NAME_FOOTER = "footer";
			public static final String NAME_CREATED_TIME = "created";

			public static final int ID_NODE_ID = 1;
			public static final int ID_NAME = 2;
			public static final int ID_URL = 3;
			public static final int ID_TITLE = 4;
			public static final int ID_TITLE_ALTERNATIVE = 5;
			public static final int ID_TOPICS = 6;
			public static final int ID_HEADER = 7;
			public static final int ID_FOOTER = 7;
			public static final int ID_CREATED_TIME = 7;
		}
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final int    DATABASE_VERSION = 5;
        private static final String DATABASE_NAME = "data.db";

        private static String[] CREATE_TABLES; 

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            CREATE_TABLES = context.getResources().getStringArray(R.array.sql_create_table_set);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        	for (String sql : CREATE_TABLES) {
                db.execSQL( sql );
        	}
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
        	Log.v(TAG, 
                    "Upgrading clips database from version " +
                    oldVersion + " to " + currentVersion +
                    ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS + ";");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NODES + ";");
            onCreate(db);
        }
	}
}
