package nav.library.new23db;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import nav.library.new23db.database.DatabaseAdapter;
import nav.library.new23db.model.Booking;

/**
 * Created by abhin on 4/19/2017.
 */

public class BookingRoomActivity extends AppCompatActivity {
    Button btnDatePicker, btnFrmTimePicker,btnToTimePicker,submitBtn;
    EditText txtDate, txtTime,txtTime1;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private SimpleDateFormat dateFormatter;
    private Toolbar toolbar;
    private Spinner spinner;
    private DatabaseAdapter dbAdapter;
    private String employeeID;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_bookroom);
        TextView toolView=new TextView(this);
        toolView.setText("Book Room");
        toolView.setTextSize(30);
        toolView.setTypeface(null, Typeface.BOLD);
        toolView.setTextColor(Color.WHITE);
        toolbar=(Toolbar) findViewById(R.id.Toolbar_Room);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(toolView);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnFrmTimePicker=(Button)findViewById(R.id.btnfrom_time);
        btnToTimePicker=(Button) findViewById(R.id.btnto_time);
        submitBtn=(Button) findViewById(R.id.submit_Booking);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        txtTime1=(EditText) findViewById(R.id.in_time1);
        spinner=(Spinner) findViewById(R.id.spinner_room);
        ArrayAdapter<String> staticAdapter=new ArrayAdapter<String>(BookingRoomActivity.this,android.R.layout.simple_dropdown_item_1line,getResources().getStringArray(R.array.rooms));
        spinner.setAdapter(staticAdapter);
        dateFormatter=new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Select a Room"))
                {
                    //not needed since Select a Room not a room.
                }
                else
                {
                    //room name
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dbAdapter=new DatabaseAdapter();
        employeeID=getIntent().getExtras().getString("employee_id");
        btnDatePicker.setOnClickListener(new DateListener());
        btnFrmTimePicker.setOnClickListener(new TimeListener());
        btnToTimePicker.setOnClickListener(new TimeListener());
        submitBtn.setOnClickListener(new ButtonListener());

    }
    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            Booking booking=new Booking();
            if(txtTime.getText().toString().compareTo(txtTime1.getText().toString())>0){
                Toast.makeText(BookingRoomActivity.this,"From time cannot be greater than to time",Toast.LENGTH_LONG).show();
            }else{
                booking.setBookinDate(txtDate.getText().toString());
                booking.setEmpID(employeeID);
                booking.setStartTime(txtTime.getText().toString());
                booking.setEndTime(txtTime1.getText().toString());
                if(spinner.getSelectedItem().toString().equals("Amber")){
                    booking.setRoomID(getResources().getString(R.string.room1));
                }else{
                    booking.setRoomID(getResources().getString(R.string.room2));
                }
                booking.setIsReserved("Y");
                long value=dbAdapter.addBooking(BookingRoomActivity.this,booking);
                if(value==0){
                    Toast.makeText(BookingRoomActivity.this,"Already booked for this time and date ",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(BookingRoomActivity.this,"Success  ",Toast.LENGTH_LONG).show();
                    txtTime.setText("");
                    txtTime1.setText("");
                    txtDate.setText("");
                }
            }
        }
    }
    private class DateListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(BookingRoomActivity.this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            //fromDatePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
            c.add(Calendar.DAY_OF_WEEK,7);
            long afterWeek=c.getTimeInMillis();
            datePickerDialog.getDatePicker().setMaxDate(afterWeek);
            datePickerDialog.show();
        }
    }
    private class TimeListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            if(view==btnToTimePicker){
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(BookingRoomActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime1.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);

                timePickerDialog.show();
            }else{
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(BookingRoomActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        }
    }
}

