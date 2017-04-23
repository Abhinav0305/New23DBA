package nav.library.new23db;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import nav.library.new23db.database.DatabaseAdapter;
import nav.library.new23db.model.Booking;

/**
 * Created by abhin on 4/21/2017.
 */

public class DeleteBookingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseAdapter dbAdapter;
    private String employeeID;
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_deletebooking);
        toolbar=(Toolbar) findViewById(R.id.Toolbar_delete);
        setSupportActionBar(toolbar);
        TextView tv = new TextView(this);
        tv.setTextSize(30);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setText("Deletion");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);
        init();

    }
    public void init() {
        employeeID=getIntent().getExtras().getString("employee_id");
        dbAdapter=new DatabaseAdapter();
        final List<Booking> bookings=dbAdapter.getBookings(DeleteBookingActivity.this,employeeID);
        final TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setTextSize(20);
        tv0.setText(" Date ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setTextSize(20);
        tv1.setText(" Time1 ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setTextSize(20);
        tv2.setText(" Time2 ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setTextSize(20);
        tv3.setText(" Room ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);
        TextView tv4 = new TextView(this);
        tv4.setTextSize(20);
        tv4.setText(" Action ");
        tv4.setTextColor(Color.WHITE);
        tbrow0.addView(tv4);
        stk.addView(tbrow0);
        if(bookings!=null && bookings.size()>0){
            for(final Booking book: bookings){
                final TableRow tbrow = new TableRow(this);
                TextView t1v = new TextView(this);
                t1v.setText(book.getBookinDate());
                t1v.setTextColor(Color.WHITE);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);

                TextView t2v = new TextView(this);
                t2v.setText(book.getStartTime());
                t2v.setTextColor(Color.WHITE);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);

                TextView t3v = new TextView(this);
                t3v.setText(book.getEndTime());
                t3v.setTextColor(Color.WHITE);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);

                TextView t4v = new TextView(this);
                if(book.getRoomID().equals(getResources().getString(R.string.room1))){
                    t4v.setText("Amber");
                }else{
                    t4v.setText("Red");
                }
                t4v.setTextColor(Color.WHITE);
                t4v.setGravity(Gravity.CENTER);
                tbrow.addView(t4v);

                final Button btn = new Button(DeleteBookingActivity.this);
                btn.setText("delete");
                btn.setTextColor(Color.RED);
                btn.setGravity(Gravity.CENTER);
                btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        int value=dbAdapter.deleteBoking(DeleteBookingActivity.this,book);
                        if(value>0){
                            stk.removeView(tbrow);
                        }
                    }
                });
                tbrow.addView(btn);

                stk.addView(tbrow);
            }
        }
    }

}