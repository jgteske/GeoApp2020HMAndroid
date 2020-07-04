package com.example.geoapp2020.ui.dialog;
/**
 * Based on https://developer.android.com/reference/android/app/DialogFragment
 *
 * Creates a DialogFragment that is displayed as an Tip
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.geoapp2020.R;

public class DialogTipFragment extends DialogFragment {

    public static DialogTipFragment newInstance(int title) {
        DialogTipFragment frag = new DialogTipFragment();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int title = getArguments().getInt("title");

        return new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_tip)
                .setTitle(title)
                .setPositiveButton(R.string.button_ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .setNegativeButton(R.string.button_cancle,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .create();
    }
}
