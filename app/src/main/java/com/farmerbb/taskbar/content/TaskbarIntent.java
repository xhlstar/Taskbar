/* Copyright 2020 Braden Farmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.farmerbb.taskbar.content;

public class TaskbarIntent {

    private TaskbarIntent() {}

    public static final String ACTION_HIDE_TASKBAR = "com.farmerbb.taskbar.HIDE_TASKBAR";
    public static final String ACTION_HIDE_START_MENU = "com.farmerbb.taskbar.HIDE_START_MENU";
    public static final String ACTION_TOGGLE_START_MENU = "com.farmerbb.taskbar.TOGGLE_START_MENU";
    public static final String ACTION_START_MENU_DISAPPEARING =
            "com.farmerbb.taskbar.START_MENU_DISAPPEARING";
    public static final String ACTION_START_MENU_APPEARING =
            "com.farmerbb.taskbar.START_MENU_APPEARING";
    public static final String ACTION_RESET_START_MENU = "com.farmerbb.taskbar.RESET_START_MENU";
    public static final String ACTION_HIDE_START_MENU_NO_RESET =
            "com.farmerbb.taskbar.HIDE_START_MENU_NO_RESET";
    public static final String ACTION_SHOW_START_MENU_SPACE =
            "com.farmerbb.taskbar.SHOW_START_MENU_SPACE";
    public static final String ACTION_HIDE_START_MENU_SPACE =
            "com.farmerbb.taskbar.HIDE_START_MENU_SPACE";
}