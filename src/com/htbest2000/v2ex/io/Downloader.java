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

package com.htbest2000.v2ex.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;

import android.net.Uri;
import android.util.Log;

public class Downloader {
	public final String TAG = "=AV2EX=Downloader";
	public final boolean DEBUG = true;
	
	public final static String SCHEME = "http";
	public final static String HOST = "www.v2ex.com";
	public final static String HOST_WITH_SCHEME = SCHEME + "://" + HOST;
	public final static String AGENT = "Mozilla/5.0 (Linux; U; Android 2.3.3; zh-cn; SK17i Build/4.0.A.1.38) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";

	// ---
	public interface Command {
		public abstract void onFeed(    BufferedInputStream  in  ) throws IOException ; 
		public abstract void onPostOut( BufferedOutputStream out ) throws IOException ;
		public abstract void onPostIn(  BufferedInputStream  in  ) throws IOException ;
	}
	
	Command mCommand;
	
	public void setCommand( Command command ) {
		mCommand = command;
	}

	public Command getCommand() {
		return mCommand;
	}
	// ---

	public static void basicAuth(final String name, final String pass) {
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(name, pass.toCharArray());
			}
		});
	}

	public void fetchHtml(String path) throws IOException {
		if (DEBUG) Log.i(TAG, "will fetch: " + HOST_WITH_SCHEME + path);

		URL url = null;
		try {
			url = new URL( HOST_WITH_SCHEME + path );
		} catch (MalformedURLException e) {
			Log.e(TAG, "ERROR", e);
			e.printStackTrace();
		}

		if (url != null) {
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			// urlConn.setRequestProperty("User-Agent", AGENT);
			BufferedInputStream in = new BufferedInputStream( urlConn.getInputStream() );

			mCommand.onFeed( in );

			in.close();
			urlConn.disconnect();

		} else {
			Log.e("=ht=", " url NULL");
		}
	}

	public void post(String path) throws IOException {
		URL url = new URL(HOST_WITH_SCHEME + path);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		try {
			urlConnection.setDoOutput(true);
			urlConnection.setChunkedStreamingMode(0);

			if (null != mCommand) {
				BufferedOutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
				mCommand.onPostOut(out);
			}

			if (null != mCommand) {
				BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
				mCommand.onPostIn(in);
			}
		}
		finally {
			urlConnection.disconnect();
		}
	}
}
