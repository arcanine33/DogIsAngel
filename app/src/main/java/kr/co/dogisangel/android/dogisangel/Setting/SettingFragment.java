package kr.co.dogisangel.android.dogisangel.Setting;



import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.dogisangel.android.dogisangel.R;


public class SettingFragment extends PreferenceFragment {
    Setting owner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_frame);


        setHasOptionsMenu(true);
        owner = (Setting) getActivity();


        bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        Preference setting_1_1 = (Preference)findPreference("setting_1_1");
        Preference setting_2_2 = (Preference)findPreference("setting_2_2");


        final DialogInterface.OnClickListener yes = new DialogInterface.OnClickListener() {//로그아웃에 필요한 리스너
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        };

        final DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };



        setting_1_1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            Fragment fragment = null;
            AlertDialog dialog = null;
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onPreferenceClick(Preference preference) {
//                logout();
                dialog = new AlertDialog.Builder(owner)
                        .setView(R.layout.dialog_logout).create();
                dialog.show();

                Button dialogButton = (Button) dialog.findViewById(R.id.btnReopenId2);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button dialogButton2 = (Button) dialog.findViewById(R.id.btnCancelId2);
                dialogButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                return false;

            }
            android.app.FragmentManager manager = getFragmentManager();
            android.app.FragmentTransaction transaction = manager.beginTransaction();

        });



        setting_2_2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            Fragment fragment = null;
            AlertDialog dialog1 = null;
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onPreferenceClick(Preference preference) {
//                logout();
                dialog1 = new AlertDialog.Builder(owner)
                        .setView(R.layout.custom_dialog).create();
                dialog1.show();

                Button dialogButton3 = (Button) dialog1.findViewById(R.id.btnReopenId1);
                // if button is clicked, close the custom dialog
                dialogButton3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                Button dialogButton4 = (Button) dialog1.findViewById(R.id.btnCancelId1);
                dialogButton4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                return false;

            }
            android.app.FragmentManager manager = getFragmentManager();
            android.app.FragmentTransaction transaction = manager.beginTransaction();


        });

    }




    private static void bindPreferenceSummaryToValue(Preference preference) {

        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }





    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {

                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);


                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {

                if (TextUtils.isEmpty(stringValue)) {

                    preference.setSummary("pref_ringtone_silent");

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {

                        preference.setSummary(null);
                    } else {

                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {

                preference.setSummary(stringValue);
            }
            return true;
        }
    };


}