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

package com.htbest2000.v2ex;

import java.util.List;
import com.google.inject.Module;
import com.htbest2000.v2ex.ui.ActivityHome;
import com.htbest2000.v2ex.ui.TopicListActivity;

import roboguice.application.RoboApplication;


public class TheApplication extends RoboApplication {
	
    public static final int ACTIVITY_HOME_ID = 0;
    public static final int LATEST_TOPICS_ID = 1;

    public static final String[] ACTIVITY_NAMES = new String[] {
        ActivityHome.class.getName(),
        TopicListActivity.class.getName(),
    };

	@Override
	protected void addApplicationModules(List<Module> modules) {
		modules.add( new TheDiModule() );
	}

}
