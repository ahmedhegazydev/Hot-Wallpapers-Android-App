package com.ahmed.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DBController extends SQLiteOpenHelper {

	public static final String tablename1 = "MyUsers"; // tablename
	private static final String tablename2 = "courses"; // tablename
	private static final String username = "username"; // column name
	private static final String id = "id"; // auto generated ID column
	private static final String password = "password"; // column name
	private static final String firstName = "firstname";
	private static final String lastName = "lastname";
	private static final String email = "email";
	private static final String gender = "gender";
	private static final String country = "country";
	private static final String city = "city";
	private static final String phone = "phone";
	private static final String bloodgroup = "bloodgroup";
	private static final String donorOrNot = "donorOrNot";
	private static final String iamge = "image";
	private static final String databasename = "BloodBankDb"; // Dtabasename
	
	private static final int versioncode = 7; // versioncode of the database

	// constructor
	public DBController(Context context) {
		super(context, databasename, null, versioncode);

	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		String query;
		query = "CREATE TABLE IF NOT EXISTS " + tablename1 + "(" + id + " integer primary key, " + this.firstName
				+ " text, " + this.lastName + " text , " + this.email + " text , " + this.gender + " text , "
				+ this.country + " text , " + this.phone + " text , " + this.city + " text , " + this.username
				+ " text , " + this.password + " text, " + this.bloodgroup + " text, " + this.donorOrNot + " text, " + 
				this.iamge + " blob" + ")";

		database.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query;
		query = "DROP TABLE IF EXISTS " + tablename1;
		database.execSQL(query);
		onCreate(database);
	}

	public void deleteAllRecords() {
		// TODO Auto-generated method stub

		SQLiteDatabase db = this.getWritableDatabase();
		//db.execSQL("delete from "+ this.tablename1);
		db.delete(this.tablename1, null, null);
		db.close();
		
		/*
		 * or, if you want the function to return the count of deleted rows,
		 * 
		 * db.delete(TABLE_NAME, "1", null); From the documentation of
		 * SQLiteDatabase delete method:
		 * 
		 * To remove all rows and get a count pass "1" as the whereClause.
		 */

	}

	
	
	public boolean saveBytes(byte[] bytes, int id){

        boolean ret = false;
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try{

        String sql   =   "INSERT INTO IMAGES " 
                + " ( IMAGE_ID" 
                + ", IMAGE_BLOB" 
                + " ) VALUES(?,?)";

        SQLiteStatement insertStmt      =   db.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindLong(1, id);
        insertStmt.bindBlob(2, bytes);
        insertStmt.executeInsert();

        db.setTransactionSuccessful();
        db.endTransaction();

        ret = true;
        }catch(Exception e){
            e.printStackTrace();
            ret = false;
        }

        return ret;
    }

    public byte[] getBytes( int id) throws Exception {

        byte[] ret = null;

        try {

            String selectQuery = "SELECT  I.IMAGE_BLOB " 
                    + "         FROM IMAGES I WHERE I.IMAGE_ID = ?";

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery,new String[]{String.valueOf(id)});


            if (!c.isClosed() && c.moveToFirst() && c.getCount() > 0) {

                if (c.getBlob(c.getColumnIndex("IMAGE_BLOB")) != null)
                {                   
                    ret = c.getBlob(c.getColumnIndex("IMAGE_BLOB"));

                }
                c.close();
                if (db != null && db.isOpen())
                    db.close();
            }
            System.gc();
        } catch (Exception e) {
            System.gc();
            throw e;

        }
		return ret;
    }
        
        
	
	
	public boolean insertIntoDb(String fn, String ln, String email, String gender, String country, String city,
			String username, String password, String phone, String donorOrNot, byte[] image, String bloodGroup) {
		// TODO Auto-generated method stub

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(this.firstName, fn);
		cv.put(this.lastName, ln);
		cv.put(this.email, email);
		cv.put(this.gender, gender);
		cv.put(this.country, country);
		cv.put(this.city, city);
		cv.put(this.username, username);
		cv.put(this.password, password);
		cv.put(this.phone, phone);
		cv.put(this.bloodgroup, bloodGroup);
		cv.put(this.donorOrNot, donorOrNot);
		cv.put(this.iamge, image);
		db.insert(this.tablename1, null, cv);
		// int i = (int) db.insert(this.tablename1, null, cv);
		// if (i == -1) {
		// return false;
		// } else {
		//
		// }
		db.close();
		return true;
	}

	// For returning all users from database
	public ArrayList<HashMap<String, String>> getAllUsers(String username, String password) {
		ArrayList<HashMap<String, String>> arrayList;
		arrayList = new ArrayList<HashMap<String, String>>();
		// String selectQuery = "SELECT * FROM " + this.tablename1;
		String selectQuery = "select * from " + this.tablename1 + " where username  = '" + username
				+ "' and password =  '" + password + "'     ";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {

				HashMap<String, String> map = new HashMap<String, String>();
				// Respectively as inserted at first time
				map.put(this.id, cursor.getString(0));
				map.put(this.firstName, cursor.getString(1));
				map.put(this.lastName, cursor.getString(2));
				map.put(this.email, cursor.getString(3));
				map.put(this.gender, cursor.getString(4));
				map.put(this.country, cursor.getString(5));
				map.put(this.phone, cursor.getString(6));
				map.put(this.city, cursor.getString(7));
				map.put(this.username, cursor.getString(8));
				map.put(this.password, cursor.getString(9));
				map.put(this.bloodgroup, cursor.getString(10));
				map.put(this.donorOrNot, cursor.getString(11));
				//map.put(this.iamge, cursor.getBlob(12));
				arrayList.add(map);

			} while (cursor.moveToNext());

		}

		// return contact list
		return arrayList;
	}

	// For returning all users from database
	public ArrayList<HashMap<String, String>> getAllUsers(String selectQuery) {
		ArrayList<HashMap<String, String>> arrayList;
		arrayList = new ArrayList<HashMap<String, String>>();
		// //String selectQuery = "SELECT * FROM " + this.tablename1;
		// String selectQuery = "select * from " + this.tablename1 +
		// " where username = '"+username+"' and password = '"+password+"' ";

		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> map = new HashMap<String, String>();
				// Respectively as inserted at first time
				map.put(this.id, cursor.getString(0));
				map.put(this.firstName, cursor.getString(1));
				map.put(this.lastName, cursor.getString(2));
				map.put(this.email, cursor.getString(3));
				map.put(this.gender, cursor.getString(4));
				map.put(this.country, cursor.getString(5));
				map.put(this.phone, cursor.getString(6));
				map.put(this.city, cursor.getString(7));
				map.put(this.username, cursor.getString(8));
				map.put(this.password, cursor.getString(9));
				map.put(this.bloodgroup, cursor.getString(10));
				map.put(this.donorOrNot, cursor.getString(11));
				//map.put(this.iamge, cursor.getBlob(12));
				arrayList.add(map);

			} while (cursor.moveToNext());

		}

		// return contact list
		return arrayList;
	}
	
	
	// For returning all users from database
		public ArrayList<HashMap<String, byte[]>> getAllImagesBySql(String selectQuery) {
			ArrayList<HashMap<String, byte[]>> arrayList;
			arrayList = new ArrayList<HashMap<String, byte[]>>();
			// //String selectQuery = "SELECT * FROM " + this.tablename1;
			// String selectQuery = "select * from " + this.tablename1 +
			// " where username = '"+username+"' and password = '"+password+"' ";

			SQLiteDatabase database = this.getWritableDatabase();
			Cursor cursor = database.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					HashMap<String, byte[]> map = new HashMap<String, byte[]>();
					map.put(this.iamge, cursor.getBlob(12));
					arrayList.add(map);
					
				} while (cursor.moveToNext());

			}

			// return contact list
			return arrayList;
		}
		

	public int numberOfRows() {
		SQLiteDatabase db = this.getReadableDatabase();
		int numRows = (int) DatabaseUtils.queryNumEntries(db, this.tablename1);
		return numRows;
	}

	public Cursor getData(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor res = db.rawQuery("select * from '" + this.tablename1 + "' where id=" + id + "", null);
		return res;
	}

	public boolean updateData(String password, String firstName, String lastName) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		// contentValues.put("username", username);
		contentValues.put("password", password);
		// contentValues.put("firstname", firstName);
		// contentValues.put("lastname", lastName);
		db.update(this.tablename1, contentValues, "firstname = ? and lastname = ?",
				new String[] { firstName, lastName });
		return true;
	}

	public boolean updatePassword(String username, String password) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		// contentValues.put("username", username);
		contentValues.put("password", password);
		// contentValues.put("firstname", firstName);
		// contentValues.put("lastname", lastName);
		db.update(this.tablename1, contentValues, "username = ?",
				new String[] { username });
		return true;
	}
	
	public boolean updateData(String username, String password, String firstName, String lastName) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("username", username);
		contentValues.put("password", password);
		contentValues.put("firstname", firstName);
		contentValues.put("lastname", lastName);
		db.update(this.tablename1, contentValues, "firstname = ? and lastname = ?",
				new String[] { firstName, lastName });
		return true;
	}
	
	
	public boolean updateData(String username, String password, String firstName, String lastName, String phone, String gender, String 
			bloodGroup, String email, String city, String country, String donorOrNot, byte[] bytes) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("username", username);
		contentValues.put("password", password);
		contentValues.put("firstname", firstName);
		contentValues.put("lastname", lastName);
		contentValues.put("city", city);
		contentValues.put("country", country);
		contentValues.put("email", email);
		contentValues.put("phone", phone);
		contentValues.put("donorornot", donorOrNot);
		contentValues.put("bloodgroup", bloodGroup);
		contentValues.put("image", bytes);
		contentValues.put("gender", gender);
		
		db.update(this.tablename1, contentValues, "username = ? and password = ?",
				new String[] { username, password });
		return true;
	}
	
	
	
	
	

}