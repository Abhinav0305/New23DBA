package nav.library.new23db;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ListMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import nav.library.new23db.database.DatabaseAdapter;
import nav.library.new23db.model.Leave;

/**
 * Created by abhin on 4/22/2017.
 */

public class SickLeaveActivity extends AppCompatActivity {

    private TableLayout layout;
    private Toolbar toolbar;
    private String employeeID;
    private DatabaseAdapter dbAdapter;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_sickleave);
        layout=(TableLayout) findViewById(R.id.tablelayout);
        toolbar=(Toolbar) findViewById(R.id.toolbar_sick);
        setSupportActionBar(toolbar);
        TextView tv=new TextView(this);
        tv.setText("Leave Detail");
        tv.setTextSize(30);
        tv.setTextColor(Color.WHITE);
        tv.setTypeface(null, Typeface.BOLD);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(tv);
        init();
    }
    public void init(){
        employeeID=getIntent().getExtras().getString("employee_id");
        dbAdapter=new DatabaseAdapter();
        List<Leave> leave=dbAdapter.getLeaveofEmployee(SickLeaveActivity.this,employeeID);
        TableRow rowHeader = new TableRow(this);
        rowHeader.setBackgroundColor(Color.parseColor("#c0c0c0"));
        rowHeader.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        String[] headerText={"FROMDATE","TODATE","TYPE","APPLIED","Y/N"};
        for(String c:headerText) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setPadding(5, 5, 5, 5);
            tv.setText(c);
            rowHeader.addView(tv);
        }
        layout.addView(rowHeader);
        if(leave!=null && leave.size()>0){
            for(Leave leaves : leave){
                String dateApplied=leaves.getDateApplied();
                String fromDate=leaves.getFromDATE();
                String toDate=leaves.getToDATE();
                String approved=null;
                if(leaves.getApproved()==null){
                    approved="P";
                }else{
                    approved=leaves.getApproved();
                }
                String leaveType=leaves.getLeaveType();

                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT));
                String [] colText={fromDate,toDate,leaveType,dateApplied,approved};

                for(String text : colText){
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(16);
                    tv.setPadding(5, 5, 5, 5);
                    tv.setText(text);
                    tv.setTextColor(Color.BLACK);
                    tv.setTypeface(null, Typeface.BOLD);
                    row.addView(tv);
                }
                layout.addView(row);
            }
        }
    }
}
