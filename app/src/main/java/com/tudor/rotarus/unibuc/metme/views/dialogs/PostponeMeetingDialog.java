package com.tudor.rotarus.unibuc.metme.views.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.tudor.rotarus.unibuc.metme.R;
import com.tudor.rotarus.unibuc.metme.pojos.interfaces.network.PostponeMeetingListener;

/**
 * Created by Tudor on 26.06.2016.
 */
public class PostponeMeetingDialog extends DialogFragment {

    private String TAG = getClass().getSimpleName();

    private PostponeMeetingOnClick onClickListener;

    private NumberPicker numberPicker;
    private Button cancelButton;
    private Button okButton;

    public PostponeMeetingDialog() {
    }

    public void setOnClickListener(PostponeMeetingOnClick onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_postpone_meeting_dialog, container, false);

        numberPicker = (NumberPicker) view.findViewById(R.id.content_postpone_meeting_dialog_picker);
        cancelButton = (Button) view.findViewById(R.id.content_postpone_meeting_dialog_cancel_button);
        okButton = (Button) view.findViewById(R.id.content_postpone_meeting_dialog_ok_button);

        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener != null) {
                    int number = numberPicker.getValue();
                    onClickListener.onClick(number);
                    dismiss();
                }
            }
        });

        getDialog().setTitle("Postpone meeting");

        return view;
    }

    public static PostponeMeetingDialog newInstance() {
        PostponeMeetingDialog frag = new PostponeMeetingDialog();
        return frag;
    }

    public interface PostponeMeetingOnClick {
        void onClick(int number);
    }
}
