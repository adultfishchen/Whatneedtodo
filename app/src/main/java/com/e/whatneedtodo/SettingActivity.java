package com.e.whatneedtodo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.util.Log;


import com.e.whatneedtodo.data.TaskContract;


public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        // 替换 Fragment
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MyPreference myPreference = new MyPreference(); //宣告剛剛做好的PreferenceFragment
        transaction.replace(android.R.id.content, myPreference); //將content內容取代為myPreference
        transaction.commit(); //送出交易
    }

    // PreferenceFragment
    public static class MyPreference extends PreferenceFragment  implements SharedPreferences.OnSharedPreferenceChangeListener {

        // 監聽 Preference Click
        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

            //選擇靜音與否
            if (preference.getKey().equals("ref_key_mute")) {
                SwitchPreference child_switch_preference = (SwitchPreference) preference;
                Music.isPlay = child_switch_preference.isChecked();
            }

            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }
        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            // 添加 Preferences XML
            addPreferencesFromResource(R.xml.preference);
            getPreferenceManager().setSharedPreferencesName("mysetting");

            //選擇靜音與否
            Preference preference = findPreference("ref_key_mute");
            SwitchPreference child_switch_preference = (SwitchPreference) preference;
            child_switch_preference.setChecked(Music.isPlay);

            //選擇曲目
            preference = findPreference("ref_key_music");
            ListPreference list = (ListPreference) preference;
            switch (Music.resource)
            {
                case R.raw.song_one:
                    list.setValue("1");
                    break;
                case R.raw.song_two:
                    list.setValue("2");
                    break;
                case R.raw.song_three:
                    list.setValue("3");
                    break;
            }

            //選擇排序方式
            preference = findPreference("ref_key_sort");
            list = (ListPreference) preference;
            switch (MainActivity.sort_type)
            {
                case TaskContract.TaskEntry.COLUMN_DESCRIPTION:
                    list.setValue("1");
                    break;
                case TaskContract.TaskEntry.COLUMN_IMPORTANCE:
                    list.setValue("2");
                    break;
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public  void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(
                SharedPreferences sharedPreferences, String key) {
            if (key.equals("ref_key_music")) {
                Preference connectionPref = findPreference(key);
                ListPreference lp = (ListPreference) connectionPref;
                switch (Integer.parseInt(lp.getValue()))
                {
                    case 1:
                        Music.resource = R.raw.song_one;
                        break;
                    case 2:
                        Music.resource = R.raw.song_two;
                        break;
                    case 3:
                        Music.resource = R.raw.song_three;
                        break;
                }
            }
            if (key.equals("ref_key_sort")) {
                Preference connectionPref = findPreference(key);
                ListPreference lp = (ListPreference) connectionPref;
                switch (Integer.parseInt(lp.getValue()))
                {
                    case 1:
                        MainActivity.sort_type = TaskContract.TaskEntry.COLUMN_DESCRIPTION;
                        break;
                    case 2:
                        MainActivity.sort_type = TaskContract.TaskEntry.COLUMN_IMPORTANCE;
                        break;
                }
            }
        }// onSharedPreferenceChanged()方法結束
        public void onClick() {
            Log.d("test", "onClick: 成功");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Music.play(this);}

    @Override
    protected void onPause() {
        super.onPause();
        Music.stop(this);
    }

    @Override
    public void finish() {
        Music.stop(this);
        super.finish();
    }
}
