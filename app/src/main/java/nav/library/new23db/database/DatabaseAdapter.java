package nav.library.new23db.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import nav.library.new23db.constant.SQLConstant;
import nav.library.new23db.model.Address;
import nav.library.new23db.model.Booking;
import nav.library.new23db.model.Department;
import nav.library.new23db.model.Employee;
import nav.library.new23db.model.Leave;
import nav.library.new23db.model.LeaveDetails;
import nav.library.new23db.model.User;

/**
 * Created by abhin on 4/4/2017.
 */

public class DatabaseAdapter {
    private DatabaseHelper helper;
    private SQLiteDatabase sqLiteDatabase;
    public boolean getUser(String username, String password, User user, Context context){
        boolean status=true;
        sqLiteDatabase=DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(SQLConstant.getUser,new String []{username,password});
        cursor.moveToFirst();
        if(cursor.getCount()<1){
            status=false;
            return status;
        }
        if(user!=null){
            user.setEmailID(username);
            user.setLastLogin(cursor.getString(0));
            user.setEmployeeID(cursor.getString(1));
            user.setName(cursor.getString(2));
        }
        cursor.close();
        sqLiteDatabase.close();
        return status;
    }
    public boolean isManager(String empID,Context context){
        boolean status=true;
        sqLiteDatabase=DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(SQLConstant.isManager,new String []{empID});
        cursor.moveToFirst();
        if(cursor.getCount()<1){
            status=false;
            return status;
        }
        int manager=cursor.getShort(0);
        if(manager==0){
            status=false;
        }
        cursor.close();
        sqLiteDatabase.close();
        return status;
    }
    public Employee getSalarySlip(Context context,String empID){
        Employee employee=new Employee();
        Department department=new Department();
        sqLiteDatabase=DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(SQLConstant.salarySlip,new String[]{empID});
        cursor.moveToFirst();
        if(cursor.getCount()<1){
            employee=null;
            department=null;
        }
        //select e.emp_id,d.dname,d.dept_id,e.dob,e.doj,e.manager_id,e.fname||' '||e.lname,e.salary,e.gender
        employee.setEmployeeID(empID);
        employee.setDateOfBirth(cursor.getString(3));
        employee.setDateOfJoining(cursor.getString(4));
        employee.setEmployeeName(cursor.getString(6));
        employee.setManagerID(cursor.getString(5));
        employee.setGender(cursor.getString(8));
        employee.setSalary(cursor.getDouble(7));
        department.setDepartmentID(cursor.getString(2));
        department.setDepartmentName(cursor.getString(1));
        employee.setDepartment(department);
        return employee;
    }
    public Employee getEmployeeDetails(Context context,String empID){
        sqLiteDatabase=DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(SQLConstant.user_profile,new String[]{empID});
        cursor.moveToFirst();
        if(cursor.getCount()<1){
            return null;
        }
        Employee employee=new Employee();
        Address address=new Address();
        employee.setEmployeeID(empID);//select fname,lname,dob,zipcode,city,state,address,gender
        employee.setEmployeeName(cursor.getString(0)+" "+cursor.getString(1));
        employee.setGender(cursor.getString(7));
        address.setZipCode(cursor.getInt(3));
        address.setCity(cursor.getString(4));
        address.setState(cursor.getString(5));
        address.setAddress(cursor.getString(6));
        employee.setAddress(address);
        employee.setContactNo(cursor.getString(8));
        return employee;
    }
    public boolean updateEmployee(Context context,Employee employee){
        boolean status=false;
        sqLiteDatabase=DatabaseHelper.getInstance(context).getWritableDatabase();
        ContentValues content=new ContentValues();
        content.put("ZIPCODE",employee.getAddress().getZipCode());
        content.put("CITY",employee.getAddress().getCity());
        content.put("ADDRESS",employee.getAddress().getAddress());
        content.put("STATE",employee.getAddress().getState());
        content.put("CONTACT_NO",employee.getContactNo());
        int result=sqLiteDatabase.update(SQLConstant.employee,content,"EMP_ID"+"=?",new String []{employee.getEmployeeID()});
        if(result==1){
            status=true;
        }
        sqLiteDatabase.close();
        return status;
    }
    public boolean checkDuplicateLeave(Context context,String employeeID,String fromDate,String endDate){  //Workaround
        boolean status=false;
        sqLiteDatabase=DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor=null;
        try{
            cursor=sqLiteDatabase.rawQuery(SQLConstant.duplicate_leave,new String[]{employeeID,fromDate,endDate});
            cursor.moveToFirst();
            if(cursor.getCount()>0){
                status=cursor.getInt(0)>0?true:false;
            }
            cursor.close();
            sqLiteDatabase.close();
        }catch(SQLException e){
            status=true;
            cursor.close();
            sqLiteDatabase.close();
        }
        return status;
    }
    public long addLeaves(Leave leave, Context context){
        long value=0;
        sqLiteDatabase=DatabaseHelper.getInstance(context).getWritableDatabase();
        ContentValues values=null;
        if(leave!=null){
            values=new ContentValues();
            values.put("EMP_ID",leave.getEmployeeID());
            values.put("FROM_DATE",leave.getFromDATE());
            values.put("END_DATE",leave.getToDATE());
            values.put("DATE_APPLIED",leave.getDateApplied());
            values.put("LEAVE_TYPE",leave.getLeaveType());
            values.put("EMPLOYEE_REASONS",leave.getEmployeeReasons());
            values.put("NOOFDAYS",leave.getNumberOfDays());
            value=sqLiteDatabase.insert(SQLConstant.leave,null,values);
        }
        sqLiteDatabase.close();
        return value;
    }
    public List<Leave> getLeavesOfEmployeeUnderManager(Context context,String employeeID) {
        List<Leave> list = new ArrayList<Leave>();
        Cursor cursor = null;
        try {//e.manager_id,e.emp_id,l.leave_id,l.from_date,l.end_date,l.date_applied,leave_type,l.employee_reasons,l.noofdays
            sqLiteDatabase = DatabaseHelper.getInstance(context).getWritableDatabase();
            cursor = sqLiteDatabase.rawQuery(SQLConstant.manager_approval, new String[]{employeeID});
            cursor.moveToFirst();
            if(cursor.getCount()>0){
                while (cursor.isAfterLast()==false){
                    Leave tmp=new Leave();
                    Employee emp=new Employee();
                    emp.setEmployeeID(cursor.getString(1));
                    emp.setEmployeeName(cursor.getString(9));
                    tmp.setEmployeeID(cursor.getString(1));
                    tmp.setDateApplied(cursor.getString(5));
                    tmp.setFromDATE(cursor.getString(3));
                    tmp.setToDATE(cursor.getString(4));
                    tmp.setNumberOfDays(cursor.getInt(8));
                    tmp.setLeaveID(cursor.getLong(2));
                    tmp.setLeaveType(cursor.getString(6));
                    tmp.setEmployeeReasons(cursor.getString(7));
                    tmp.setEmployee(emp);
                    list.add(tmp);
                    cursor.moveToNext();
                }
            }
            return list;
        } catch (SQLException e) {
            list=null;
            return list;
        } finally {
            cursor.close();
            sqLiteDatabase.close();
        }
    }
    public LeaveDetails getLeaveDetails(Context context,String employeeID){
        LeaveDetails details=new LeaveDetails();
        sqLiteDatabase=DatabaseHelper.getInstance(context).getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(SQLConstant.leave_entitled,new String[]{employeeID});
        try{
            cursor.moveToFirst();
            if(cursor.getCount()<1){
                details=null;
            }
            details.setEmployeeID(employeeID);
            details.setSickLeaveTaken(cursor.getInt(0));
            details.setPaidLeaveTaken(cursor.getInt(1));
            return details;
        }catch (SQLException e){
            Log.e("Exception",e.getMessage());
            details=null;
            return details;
        }finally {
            cursor.close();
            sqLiteDatabase.close();
        }
    }
    public boolean updateLeave(String table1,String table2,Context context,ContentValues leave,ContentValues leaveDetails,long leaveID,String employeeID){
        sqLiteDatabase=DatabaseHelper.getInstance(context).getWritableDatabase();
        int status=0;
        //int result=sqLiteDatabase.update(SQLConstant.employee,content,"EMP_ID"+"=?",new String []{employee.getEmployeeID()});
        try{
            if(leave!=null && leaveDetails!=null){
                status=sqLiteDatabase.update(table1,leave,"LEAVE_ID="+leaveID,null);
                status=sqLiteDatabase.update(table2,leaveDetails,"EMP_ID="+employeeID,null);
            }
            return status>0?true:false;
        }catch (SQLException e){
            sqLiteDatabase.close();
            return false;
        }
    }
    public long addBooking(Context context,Booking booking){
        long value=0;
        ContentValues contentValues=new ContentValues();
        if(booking!=null && !checkBooking(context,booking)){
            try {
                sqLiteDatabase=DatabaseHelper.getInstance(context).getWritableDatabase();
                contentValues.put("BOOK_DATE",booking.getBookinDate());
                contentValues.put("EMP_ID",booking.getEmpID());
                contentValues.put("ROOM_ID",booking.getRoomID());
                contentValues.put("START_TIME",booking.getStartTime());
                contentValues.put("END_TIME",booking.getEndTime());
                contentValues.put("ISRESERVED",booking.getIsReserved());
                value=sqLiteDatabase.insert(SQLConstant.Booking,null,contentValues);
                sqLiteDatabase.close();
            }catch (SQLException e){
                Log.e("Error ",e.getMessage());
                sqLiteDatabase.close();
                value=0;
            }
        }
        return value;
    }
    public boolean checkBooking(Context context,Booking booking){
        boolean status=false;
        Cursor cursor=null;
        try{
            sqLiteDatabase=DatabaseHelper.getInstance(context).getReadableDatabase();
            cursor=sqLiteDatabase.rawQuery(SQLConstant.duplicate_booking,new String[]{booking.getBookinDate(),booking.getStartTime(),booking.getEndTime(),booking.getRoomID()});
            cursor.moveToFirst();
            if(cursor.getCount()>0){
                status=true;
            }
            cursor.close();
            sqLiteDatabase.close();
        }catch (SQLException e){
            Log.d("exception",e.getMessage());
            cursor.close();
            sqLiteDatabase.close();
        }
        return status;
    }
    public List<Booking> getBookings(Context context,String employeeID){
        List<Booking> bookings=new ArrayList<Booking>();
        Cursor cursor=null;
        try{
            sqLiteDatabase=DatabaseHelper.getInstance(context).getReadableDatabase();
            cursor=sqLiteDatabase.rawQuery(SQLConstant.get_booking,new String[]{employeeID});
            cursor.moveToFirst();
            if(cursor.getCount()>0){  //select book_id,room_id,book_date,start_time,end_time from booking where emp_id=?
                while (cursor.isAfterLast()==false){
                    Booking tmp=new Booking();
                    tmp.setBookingID(cursor.getLong(0));
                    tmp.setRoomID(Integer.toString(cursor.getInt(1)));
                    tmp.setBookinDate(cursor.getString(2));
                    tmp.setStartTime(cursor.getString(3));
                    tmp.setEndTime(cursor.getString(4));
                    tmp.setEmpID(employeeID);
                    bookings.add(tmp);
                    cursor.moveToNext();
                }
                cursor.close();
                sqLiteDatabase.close();
            }
        }catch (SQLException e){
            bookings=null;
            cursor.close();
            sqLiteDatabase.close();
        }
        return bookings;
    }
    public int deleteBoking(Context context,Booking booking){
        int value=0;
        sqLiteDatabase=DatabaseHelper.getInstance(context).getWritableDatabase();
        if(booking!=null){
            try{
                value=sqLiteDatabase.delete(SQLConstant.Booking,SQLConstant.whereBooking+"="+booking.getBookingID(),null);
            }catch (SQLException e){
                sqLiteDatabase.close();
                value=0;
            }
        }
        return value;
    }
    public List<Leave> getLeaveofEmployee(Context context,String employeeID){
        Cursor cursor=null;
        List<Leave> leaves=new ArrayList<Leave>();
        sqLiteDatabase=DatabaseHelper.getInstance(context).getWritableDatabase();
        try{
            cursor=sqLiteDatabase.rawQuery(SQLConstant.leaveOfEmployees,new String[]{employeeID});
            cursor.moveToFirst();//leave_id,emp_id,from_Date,end_date,date_applied,leave_type,approve
            if(cursor.getCount()>0){
                while (cursor.isAfterLast()==false){
                    Leave leave=new Leave();
                    leave.setLeaveID(cursor.getLong(0));
                    leave.setApproved(cursor.getString(6));
                    leave.setLeaveType(cursor.getString(5));
                    leave.setEmployeeID(cursor.getString(1));
                    leave.setFromDATE(cursor.getString(2));
                    leave.setToDATE(cursor.getString(3));
                    leave.setDateApplied(cursor.getString(4));
                    leaves.add(leave);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            sqLiteDatabase.close();
        }catch (SQLException e){
            cursor.close();
            sqLiteDatabase.close();
            leaves=null;
        }
        return leaves;
    }
}
