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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.htbest2000.v2ex.R;
import com.htbest2000.v2ex.R.id;
import com.htbest2000.v2ex.R.layout;
import com.htbest2000.v2ex.io.Downloader;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends RoboActivity {
	@InjectView(R.id.login_name) EditText mName;
	@InjectView(R.id.login_pass) EditText mPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		// for debug
		mName.setText("?");
		mPass.setText("?");
	}
	
	/**
	 * LoginActivity and save pass/name into pref file.
	 * 
	 */
	public void onLoginClick(View v) {
		Downloader.basicAuth(mName.getText().toString(), mPass.getText().toString());

		Downloader.Command mCommand = new Downloader.Command() {
			@Override
			public void onFeed(BufferedInputStream in) {
//				try {
//					InputStreamReader buffer = new InputStreamReader(in);
//					BufferedReader buffer2 = new BufferedReader(buffer);
//					 
//					BufferedReader reader = new BufferedReader(buffer2);
//					while (true) {
//						String line = reader.readLine();
//						if (null == line)
//							break;
//						Log.i("=ht=", "auth: " + line);
//					}
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
			}


			@Override
			public void onPostOut(BufferedOutputStream out) {				
//				BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(out) );
//				writer.append()
				
			}


			@Override
			public void onPostIn(BufferedInputStream in) throws IOException {
				BufferedReader reader = new BufferedReader( new InputStreamReader(in) );
				while (true) {
					String line = reader.readLine();
					if (null == line)
						break;
					Log.i("=ht=", "auth result: " + line);
				}
				
			}
		};
		
		Downloader ldr = new Downloader();
		ldr.setCommand(mCommand);
		try {
			ldr.post("/api/account/authenticate.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// save name/pass into shared pref
	}
	


}
