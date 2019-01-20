package com.example.poy.chatrks;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute2){
        //Do something with the user chosen time
        //Get reference of host activity (XML Layout File) TextView widget
        TextView tv = (TextView) getActivity().findViewById(R.id.in_time);

        String formattedTime = "";
        int hour = hourOfDay;
        String sHour = "00";
        if(hour < 10){
            sHour = "0"+hour;
        } else {
            sHour = String.valueOf(hour);
        }

        int minute = minute2;
        String sMinute = "00";
        if(minute < 10){
            sMinute = "0"+minute;
        } else {
            sMinute = String.valueOf(minute);
        }

        formattedTime = sHour+":"+sMinute+":"+"00"; // Sorry you can't get seconds from a TimePicker


        tv.setText(formattedTime);
    }
}