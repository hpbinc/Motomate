package com.hashim.motomate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;

public class data extends SQLiteOpenHelper {

    float oldx, oldy, oldz;
    public static long count=0;
    public static int j=0;
    public static long oldsec=0;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "drivedata.db";
    private static final String TABLE_DATAA = "acceleration";
    private static final String TABLE_DATAB = "speed";
    private static final String TABLE_DATAC = "speedacc";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ACCX = "accx";
    public static final String COLUMN_ACCY = "accy";
    public static final String COLUMN_ACCZ = "accz";
    public static final String COLUMN_GYRX = "gyrox";
    public static final String COLUMN_GYRY = "gyroy";
    public static final String COLUMN_GYRZ = "gyroz";


    public static final String COLUMN_SPEED = "speed";
    public static final String COLUMN_TRAVERSE = "traverse";
    public static final String COLUMN_AVGSPEED = "avgspeed";
    public static final String COLUMN_AVGACC = "avgacc";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TIME="time";

    String [] s1=new String[20000];
    public data(Context context, String name,
                SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_DATA_TABLEA = "CREATE TABLE " +
                TABLE_DATAA + "("
                + COLUMN_ID + " TEXT ,"
                + COLUMN_ACCX + " INTEGER, "
                + COLUMN_ACCY + " INTEGER, "
                + COLUMN_ACCZ + " INTEGER, "
                + COLUMN_TIME + " INTEGER" + ")";
        db.execSQL(CREATE_DATA_TABLEA);


        String CREATE_DATA_TABLEB = "CREATE TABLE " +
                TABLE_DATAB + "("

                + COLUMN_SPEED + " INTEGER, "
                + COLUMN_TIME + " INTEGER "+ ")";
        db.execSQL(CREATE_DATA_TABLEB);


        String CREATE_DATA_TABLEC = "CREATE TABLE " +
                TABLE_DATAC + "("
                + COLUMN_ID + " TEXT ,"
                + COLUMN_GYRX + " INTEGER, "
                + COLUMN_GYRY + " INTEGER, "
                + COLUMN_GYRZ + " INTEGER, "
                + COLUMN_TIME + " TEXT "+ ")";
        db.execSQL(CREATE_DATA_TABLEC);


      /*  String CREATE_DATA_TABLEC = "CREATE TABLE " +
                TABLE_DATAC + "("
                + COLUMN_ID + " INTEGER, "
                + COLUMN_AVGACC + " INTEGER, "
                + COLUMN_AVGSPEED + " INTEGER, "
                + COLUMN_STATUS + " TEXT " + ")";
        db.execSQL(CREATE_DATA_TABLEC);
        oldx = 0;
        oldy = 0;
        oldz = 0;
        for(int i=0;i<20000;i++)
        {
            s[i]="0 km/hr";
            s1[i]="0";
        }
*/
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATAA);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATAB);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATAC);
        onCreate(db);


    }
    /*   public static String getDateTime() {
           SimpleDateFormat dateFormat = new SimpleDateFormat(
                   "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
           Date date = new Date();
           return dateFormat.format(date);
       }
   */
    public  void addgyro(String _id1, double x, double y, double z,long time) {
        int avg = 0;
        long sum = 0;



            count++;
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, _id1);
            values.put(COLUMN_GYRX, x);
            values.put(COLUMN_GYRY, y);
            values.put(COLUMN_GYRZ, z);
            values.put(COLUMN_TIME, time);
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_DATAC, null, values);
            db.close();

        }
    public  void addacc(String _id, double x, double y, double z, long t) {
        int avg=0;
        long sum = 0;



        count++;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, _id);
        values.put(COLUMN_ACCX, x);
        values.put(COLUMN_ACCY, y);
        values.put(COLUMN_ACCZ, z);
        values.put(COLUMN_TIME, t);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_DATAA, null, values);
        db.close();

    }

    public  void addspe(double spe, long sec) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_SPEED, spe);
        values.put(COLUMN_TIME, sec);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_DATAB, null, values);
        db.close();


    }


    /*   public String displayacc() {



   String s="";
           String query = "SELECT * FROM " + TABLE_DATAA;
         //   String query1="UPDATE TABLEA SET "+COLUMN_TRAVERSE+" =1";
        //   String query2 = "DELETE FROM persons WHERE id=" + id + ";";
           SQLiteDatabase db = this.getWritableDatabase();

           Cursor cursor = db.rawQuery(query, null);
           if (cursor.moveToFirst()) {
               cursor.moveToFirst();

               while (!cursor.isAfterLast()) {


                   s = s + cursor.getString(0)+" "+ cursor.getString(1)+" "+ cursor.getString(2)+" "+ cursor.getString(3)+"  / ";


                   cursor.moveToNext();
               }
               cursor.close();
               db.close();
               return s;

           } else {
               db.close();
               return "nothing to show /";

           }

       }*/
    public float[][] findacc()
    {
        j=0;
        float [][] s=new float[20000][4];
        String []b={"nothing to show","error","404"};
        String query = "SELECT * FROM " + TABLE_DATAA;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

               /* s[j]="";

                s[j] =s[j]+""+cursor.getString(0)+" "+ cursor.getString(1)+" "+ cursor.getString(2)+" "+ cursor.getString(3)
                        +" "+ cursor.getString(4)+" ";
                j++;if(j==19999)
                    break;
              */
            String f=cursor.getString(0)   ;
            s[j][0]=cursor.getFloat(1);
            s[j][1]=cursor.getFloat(2);
            s[j][2]=cursor.getFloat(3);
            s[j][3]=cursor.getFloat(4);

                j++;

                if(j>19999)
                    break;

                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return s;

        } else {
            cursor.close();
            db.close();
            return s;

        }
    }

    public float[][] findgyro()
    {
        float [][] s=new float[20000][4];
        j=0;
        String []b={"nothing to show","error","404"};
        String query = "SELECT * FROM " + TABLE_DATAC;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                s[j][0]=cursor.getFloat(1);
                s[j][1]=cursor.getFloat(2);
                s[j][2]=cursor.getFloat(3);
                s[j][3]=cursor.getFloat(4);

                j++;

                if(j>19999)
                    break;

                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return s;

        } else {
            cursor.close();
            db.close();
            return s;

        }
    }




    public float[][] findspe()
    {
        float [][] s=new float[20000][2];

        j=0;
        String []b={"nothing to show","error","404"};
        String query = "SELECT * FROM " + TABLE_DATAB;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {

                s[j][0]=cursor.getFloat(0);
                s[j][1]=cursor.getFloat(1);


                j++;

                if(j>19999)
                    break;

                cursor.moveToNext();
            }
            cursor.close();
            db.close();
            return s;

        } else {
            cursor.close();
            db.close();
            return s;

        }
    }

    public void cleardata()
    {

       SQLiteDatabase db = this.getWritableDatabase();


        db.delete(TABLE_DATAA,null,null);
        db.delete(TABLE_DATAB,null,null);
        db.delete(TABLE_DATAC,null,null);
        db.close();

    }


}




