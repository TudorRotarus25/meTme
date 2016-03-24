package com.tudor.rotarus.unibuc.metme.fragments.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.tudor.rotarus.unibuc.metme.R;

/**
 * Created by Tudor on 21.03.2016.
 */
public class AddMeetingNotificationDialog extends DialogFragment {

    public interface DialogListener {
        void onItemClick(int selectedIndex);
    }

    private DialogListener dialogListener;

    public DialogListener getDialogListener() {
        return dialogListener;
    }

    public void setDialogListener(DialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setTitle(R.string.add_meeting_notification_dialog_title)
                .setItems(R.array.notification_choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDialogListener().onItemClick(which);
                    }
                });

        return builder.create();
    }
}
