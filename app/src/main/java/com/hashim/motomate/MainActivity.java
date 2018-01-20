package com.hashim.motomate;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.hardware.SensorEventListener;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.RED;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Context context;

    String speed[] = new String[20000];
    String acc[] = new String[20000];
    String gyro[]=new String[20000];
    float facc[][] = new float[20000][4];
    float fgyro[][]=new float[20000][4];
    float fspeed[][]=new float[20000][3];
    data ob=new data(MainActivity.this,null,null,1);

    String q="No location information";


    float [] rating = new float[10];
    int flag = 0,ACCIDENT=0;
    int j, i,g, h = 0,minflag=0,minflag1=0,h1=0,first=0;
    float fx, fy, fz;
    double latitude,longitude;

    public static int k, spe;
    private Sensor mySensor,mysensor2;
    private SensorManager SM,SM2;

    Button rotate;

    private boolean backbutton = false;
    int flag1 = 0;

    public void rotater() {
        rotate = (Button) findViewById(R.id.stopbutton);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.android_rotate_animation);
        rotate.startAnimation(animation);
    }

    public void back(View v) {
        if (flag1 == 0) {
            //engine starts
            flag1 = 1;
            registerUser("1");
            rotater();

        } else {
            flag1 = 0;
            calculate();
            registerUser("0");

            rotate.setAnimation(null);
        }
    }

    @Override
    public void onBackPressed() {

        if (!backbutton) {
            Toast.makeText(this, "Please press BACK again to exit", Toast.LENGTH_LONG).show();
            backbutton = true;
        } else {
            super.onBackPressed();


            SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_MULTI_PROCESS).edit();
            editor.putFloat("rating1", rating[0]);
            editor.putFloat("rating2", rating[1]);
            editor.putFloat("rating3", rating[2]);
            float d=(5 * rating[0] +3* rating[1] +2*rating[2] )/10;

            editor.putFloat("driverrating", d);

            editor.commit();



            System.exit(0);
        }
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                backbutton = false;
            }
        }.start();
    }


    public  void rating(View v)
    {
        calculate();


        Intent k = new Intent(this, rating.class);

       k.putExtra("pass",rating);

        startActivity(k);


    }




    public void calculate() {



       int a=0,g=0,s=0,acount=0,speedcount=0,turncount=0,valuei=0;



     float speedrating=0,speedavg=0;

      data ob=new data(MainActivity.this,null,null,1);

     facc=ob.findacc();


        data ob1=new data(MainActivity.this,null,null,1);
      fgyro=ob.findgyro();


       data ob2=new data(MainActivity.this,null,null,1);

       fspeed=ob.findspe();

  // String []finl=new String[2000];

/*
       for(i=0;i<2000;i++)
       {


          finl[i]=facc[i][0]+" "+ facc[i][1] + " " + facc[i][2] + " " +  facc[i][3] +" \n"+fgyro[i][0]+" "+ fgyro[i][1] + " " + fgyro[i][2] + " " +  fgyro[i][3]+"\n" +fspeed[i][0]+" "+fspeed[i][1];

       }

*/




        //compare time of first elements
       if(facc[0][3]!=fgyro[0][3])
       {
               if(facc[0][3]>fgyro[0][3])
               {

                   a=1;
                   g=0;

               }
               else
               {

                   g=1;
                   a=0;
               }

       }

       // if not equal neglect the first element and append string






       for(i=0 ; i< 20000 && fspeed[i][1] != 0.0 ;i++)
       {

           if(fspeed[i][0]>80)
           {
               speedcount++;
           }

///speed=0

              if(fspeed[i][0]>0)
                 speedavg += fspeed[i][0];


       }


        //algorithm speed

       i++;



        speedrating=100-(speedcount/i)*100;

       rating[6]=i;

        speedavg=speedavg/i;

       rating[4]=speedavg;



       rating[0]=12-speedavg/10;



       rating[0]=rating[0]/2;

       if(i>1000)
           rating[0]=rating[0]-speedcount/(i/100);
       else
           rating[0]=rating[0]-speedcount/10;


       // pass speed rating to rating activity

       // algorithm  acc & braking

       for(i=a,j=g,s=0;i<20000 && facc[j][3]!=0.0 ;i++,j++,s++) {

           if (facc[i][0] > 0.25 || facc[i][0] < -0.25 || facc[i][1] > 0.25 || facc[i][1] < -0.25
                   || facc[i][2] > 0.25 || facc[i][2] < -0.25)

           {

               acount++;

           }

           if (fgyro[s][1] > 0.2) {

               int r;

               for (r = 0; fspeed[r][1] != 0.0; r++)
                   if (fspeed[r][1] == fgyro[s][3] && fspeed[r][0] >50)

                       turncount++;

                       break;



           }

       }

       j++;

       float turnrate=100 - turncount*5;

       acount=acount-j/250;

       float accrate=100 - acount*5 ;

       rating[1]=(accrate/10)/2;

       rating[2]=(turnrate/10)/2;

       rating[3]= (5 * rating[0] +3* rating[1] +2*rating[2] )/10;


       rating[5]=acount;



        SharedPreferences.Editor editor = getSharedPreferences("userdata", MODE_MULTI_PROCESS).edit();
        editor.putFloat("rating1", rating[0]);
        editor.putFloat("rating2", rating[1]);
        editor.putFloat("rating3", rating[2]);
        float d=rating[3];

        editor.putFloat("driverrating", d);

        editor.commit();



      }

      public void spider(View v) {

          data ob=new data(MainActivity.this,null,null,1);

          facc=ob.findacc();



          fgyro=ob.findgyro();



          fspeed=ob.findspe();

          String []finl=new String[20000];


       for(i=0;i<2000;i++)
       {


          finl[i]="\n"+i+"\n\nAccelerometer\n" + facc[i][0]+"  "+ facc[i][1] + "  " + facc[i][2] + "\n" +  facc[i][3] +" \n\nGyroscope \n"+fgyro[i][0]+"  "+ fgyro[i][1] + "  " + fgyro[i][2] + "\n" +  fgyro[i][3]+"\n\nSpeed \n" +fspeed[i][0]+"      "+fspeed[i][1];

       }




          Intent intent = new Intent(this, spider.class);
          intent.putExtra("pass",finl);
          startActivity(intent);
      }

      public void about(View v) {
          Intent intent = new Intent(this, php.class);
          startActivity(intent);
      }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        TextView t=(TextView)findViewById(R.id.speed);


        Typeface zeronero= Typeface.createFromAsset(getAssets(),"fonts/zeronero.ttf");
        Typeface anson=Typeface.createFromAsset(getAssets(),"fonts/Anson-Regular.otf");
        Typeface gillsans=Typeface.createFromAsset(getAssets(),"fonts/gillsans.ttf");
        Typeface voni=Typeface.createFromAsset(getAssets(),"fonts/Vonique64.ttf");

        t.setTypeface(gillsans);









      //  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      //  Date date = new Date();
      //  System.out.println(dateFormat.format(date));





        for(j=0;j<20000;j++)
        {
            speed[j]=" ";
            acc[j]=" ";
            gyro[j]=" ";
        }

        SM = (SensorManager) getSystemService(SENSOR_SERVICE);
        SM2=(SensorManager) getSystemService(SENSOR_SERVICE);

        mySensor = SM.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mysensor2=SM2.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        SM.registerListener(this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
        SM2.registerListener(this, mysensor2, SensorManager.SENSOR_DELAY_NORMAL);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
// Define a listener that responds to location updates

        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {

                if (flag1 == 1) {

                    latitude=location.getLatitude();

                    longitude=location.getLongitude();


                    Geocoder geocoder;
                    List<Address> addresses=new List<Address>() {
                        @Override
                        public int size() {
                            return 0;
                        }

                        @Override
                        public boolean isEmpty() {
                            return false;
                        }

                        @Override
                        public boolean contains(Object o) {
                            return false;
                        }

                        @NonNull
                        @Override
                        public Iterator<Address> iterator() {
                            return null;
                        }

                        @NonNull
                        @Override
                        public Object[] toArray() {
                            return new Object[0];
                        }

                        @NonNull
                        @Override
                        public <T> T[] toArray(T[] a) {
                            return null;
                        }

                        @Override
                        public boolean add(Address address) {
                            return false;
                        }

                        @Override
                        public boolean remove(Object o) {
                            return false;
                        }

                        @Override
                        public boolean containsAll(Collection<?> c) {
                            return false;
                        }

                        @Override
                        public boolean addAll(Collection<? extends Address> c) {
                            return false;
                        }

                        @Override
                        public boolean addAll(int index, Collection<? extends Address> c) {
                            return false;
                        }

                        @Override
                        public boolean removeAll(Collection<?> c) {
                            return false;
                        }

                        @Override
                        public boolean retainAll(Collection<?> c) {
                            return false;
                        }

                        @Override
                        public void clear() {

                        }

                        @Override
                        public Address get(int index) {
                            return null;
                        }

                        @Override
                        public Address set(int index, Address element) {
                            return null;
                        }

                        @Override
                        public void add(int index, Address element) {

                        }

                        @Override
                        public Address remove(int index) {
                            return null;
                        }

                        @Override
                        public int indexOf(Object o) {
                            return 0;
                        }

                        @Override
                        public int lastIndexOf(Object o) {
                            return 0;
                        }

                        @Override
                        public ListIterator<Address> listIterator() {
                            return null;
                        }

                        @NonNull
                        @Override
                        public ListIterator<Address> listIterator(int index) {
                            return null;
                        }

                        @NonNull
                        @Override
                        public List<Address> subList(int fromIndex, int toIndex) {
                            return null;
                        }
                    };
                    geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                    q=address+" , " + city +" , " + state + " , " + country+" , "+postalCode+" "+knownName;











                    if (first == 0) {
                        k = i;
                        first = 1;
                    }
                 TextView t5 = (TextView) findViewById(R.id.speed);

                    if ((int) (location.getSpeed() * 3.6 * 100) / 100 > 60)
                        flag = 1;

                    //if flag==1 reckless driving

                    speed[k] = String.valueOf("SPEED : " + (int) (location.getSpeed() * 3.6 * 100) / 100 + " km/hr");

                //    TextView t4 = (TextView) findViewById(R.id.t4);
                    Calendar c = Calendar.getInstance();

                    int sec = c.get(Calendar.SECOND);

                 //   t5.setText("TIME : " + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));

                    t5.setText("Speed : " + (int) (location.getSpeed() * 3.6 * 100) / 100 + " km/hr " );
                    if (k < 20000) {


                     //   t5.setText("TIME : " + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));

                     //   t4.setText("Speed : " + (int) (location.getSpeed() * 3.6 * 100) / 100 + " km/hr " + " Time " + sec);

                        speed[k] = String.valueOf("SPEED : " + (int) (location.getSpeed() * 3.6 * 100) / 100 + " km/hr  " + "\nTIME " + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));

                        ob.addspe((location.getSpeed() * 3.6 * 100) / 100, Long.parseLong("" + c.get(Calendar.HOUR) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND)));

                        k = k + 1;


                    }

                }
                else {}
            }


            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        if (flag1 == 1) {

            Sensor sensor = event.sensor;

            if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {

                fx = event.values[0];
                fy = event.values[1];
                fz = event.values[2];


                if (fx / 9.81 > 20 || fx / 9.81 < -20 || fy / 9.81 > 20 || fy / 9.81 < -20 || fz / 9.81 > 20 || fz / 9.81 < -20) {

                    //exit app reporting accident

                    registerUser("2");


                }


              /*  TextView t1 = (TextView) findViewById(R.id.t1);
                TextView t2 = (TextView) findViewById(R.id.t2);
                TextView t3 = (TextView) findViewById(R.id.t3);

                t1.setText("acc X:" + fx);
                t2.setText("acc Y:" + fy);
                t3.setText("acc Z:" + fz);
*/

                Calendar c = Calendar.getInstance();

                if (c.get(Calendar.SECOND) > h) {
                    acc[i] = " " + " \n\nACCELERATION  \n" + fx / 9.81 + " \n " + fy / 9.81 + " \n " + fz / 9.81 + "\nTIME" + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
                    i++;

                    ob.addacc("1", fx / 9.81, fy / 9.81, fz / 9.81, Long.parseLong("" + c.get(Calendar.HOUR) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND)));


                    h = c.get(Calendar.SECOND);

                    minflag = 0;

                }

                if (c.get(Calendar.SECOND) > -1 && c.get(Calendar.SECOND) < 1 && minflag == 0) {

                    acc[i] = "      \n\nACCELERATION  \n" + fx / 9.81 + " \n " + fy / 9.81 + " \n " + fz / 9.81 + "\nTIME" + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
                    i++;
                    h = c.get(Calendar.SECOND);

                    ob.addacc("1", fx / 9.81, fy / 9.81, fz / 9.81, Long.parseLong("" + c.get(Calendar.HOUR) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND)));

                    minflag = 1;
                }
            } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {


                fx = event.values[0];
                fy = event.values[1];
                fz = event.values[2];


              //  TextView t5 = (TextView) findViewById(R.id.t6);
               // TextView t6 = (TextView) findViewById(R.id.t7);
               // TextView t7 = (TextView) findViewById(R.id.t8);

              //  t5.setText("gyro X:" + fx);
               // t6.setText("gyro Y:" + fy);
               // t7.setText("gyro Z:" + fz);


                Calendar c1 = Calendar.getInstance();

                if (c1.get(Calendar.SECOND) > h1) {
                    gyro[g] = "\n\nGyroscope  \n" + fx + " \n " + fy + " \n " + fz + "\nTIME" + c1.get(Calendar.HOUR) + ":" + c1.get(Calendar.MINUTE) + ":" + c1.get(Calendar.SECOND);
                    g++;
                    h1 = c1.get(Calendar.SECOND);
                    ob.addgyro("1", fx, fy, fz, Long.parseLong("" + c1.get(Calendar.HOUR) + c1.get(Calendar.MINUTE) + c1.get(Calendar.SECOND)));
                    minflag1 = 0;

                }

                if (c1.get(Calendar.SECOND) > -1 && c1.get(Calendar.SECOND) < 1 && minflag1 == 0) {

                    gyro[g] = "      \n\nGyroscope  \n" + fx / 9.8 + " \n " + fy / 9.8 + " \n " + fz / 9.8 + "\nTIME" + c1.get(Calendar.HOUR) + ":" + c1.get(Calendar.MINUTE) + ":" + c1.get(Calendar.SECOND);
                    g++;
                    h1 = c1.get(Calendar.SECOND);
                    ob.addgyro("1", fx, fy, fz, Long.parseLong("" + c1.get(Calendar.HOUR) + c1.get(Calendar.MINUTE) + c1.get(Calendar.SECOND)));

                    minflag1 = 1;

                }


            }


        }

    }










    public void registerUser(String flag) {





        SharedPreferences prefs = getSharedPreferences("userdata", MODE_PRIVATE);

        String name = "null";

        String loc=q;

        String rating = ""+((int)((prefs.getFloat("driverrating",3.5f)))*10)/10+"/5";

        if((prefs.getFloat("driverrating",3.5f)/10)*10 > 5 )
            rating="5/5";

        String email = prefs.getString("name", null);

        register(flag,rating,name,email,loc);
    }

    private void register(String name, String username, String password, String email,String loc) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            // ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //   loading = ProgressDialog.show(php.this, "Please Wait",null, true, true);
            }

            //nichthooo

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // loading.dismiss();
             //  Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();


            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("flag",params[0]);
                data.put("rating",params[1]);
                data.put("name",params[2]);
                data.put("email",params[3]);

                data.put("loc",params[4]);


                String result = ruc.sendPostRequest("http://motomate-hashimn33911457.codeanyapp.com/register.php",data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(name,username,password,email,loc);
    }



}







