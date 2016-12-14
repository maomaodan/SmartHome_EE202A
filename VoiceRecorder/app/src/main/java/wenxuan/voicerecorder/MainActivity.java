package wenxuan.voicerecorder;

import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


import java.lang.Object;

public class MainActivity extends AppCompatActivity
{
    public static final int HEIGHT = 127;
    /**
     * 2PI
     **/


    public boolean start = true;
    public Button play;
    public Button sendEmail;
    public View view;
    public TextView text;
    public TextView statusText;
    public AudioTrack audioTrack;




    public int sampleRate;
    public AudioRecord audioRecord;
    private Thread thread;

    Handler handler2 = new Handler();

    public byte wave[];
    public static List<Integer> datas = new ArrayList<Integer>();
    public ArrayList<Double> realData = new ArrayList<Double>();
    public int bufferSize = 512;
    public boolean isRecording;
    private Object tmp = new Object();
    public static int Count;
    public double intense1 = 0;
    public double intense2 = 0;
    public double dif = 0;
    public double sig = 0;
    public String sig_str;
    //public int band = 3344;//3344, 2731

    public MediaPlayer mediaPlayer;
    private static final int MSG_SLEEP = 1;

    public static boolean right;

    private Handler handler1 = new Handler()
    {
        public void handleMessage(Message msg)
        {//此方法在ui线程运行
            switch (msg.what)
            {
                case MSG_SLEEP:
                    statusText.setText("Energy Saver Mode");
                    text.setText("Click Start Button");
                    play.setText("Start");
                    start = true;
                    //handler1.removeCallbacks(updateThread);
                    //handler2.removeCallbacks(printDoge);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        sampleRate = 44100;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /*
        wave = new byte[44100];       //sound wave buffer

        //Hz=14698;//real frequency 18001.75
        Hz = 18002;
        waveLen = 44100 / Hz;

        sampleRate = 44100;

        play = (Button) findViewById(R.id.button);
        play.setOnClickListener(new playButtonListener());

        sendEmail = (Button) findViewById(R.id.sendEmail);
        sendEmail.setOnClickListener(new sendButtonListener());


        text = (TextView) findViewById(R.id.text);
        statusText = (TextView) findViewById(R.id.textView2);
        statusText.setText("PLEASE CLICK START");
        text.setText("APPLICATION STOPED");
        */
        bufferSize = 8192;
        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize);


        if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED)
            System.out.println("error");



        //        mediaPlayer= MediaPlayer.create(this, R.raw.sine);

        // right = true;
    }
    /*
    @Override
        protected void onPause() {
            super.onPause();
            isRecording=false;
            handler2.removeCallbacks(printDoge);
            play.setText("start");
            statusText.setText("PLEASE CLICK START");
            text.setText("APPLICATION PAUSED");
            start=true;
        }
        */
    /*
    class playButtonListener implements View.OnClickListener
    {
        public void onClick(View v)
        {
            if (start)
            {
                Count = 21;
                datas = new ArrayList<Integer>();

                isRecording = true;
                if (mediaPlayer != null)
                {

                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();

                }

                thread = new Thread(updateThread);
                thread.start();
                handler2.post(printDoge);
                play.setText("stop");
                start = false;
            } else
            {
                isRecording = false;


                handler2.removeCallbacks(printDoge);

                mediaPlayer.pause();

                play.setText("start");
                statusText.setText("PLEASE CLICK START");
                text.setText("APLICATION STOPED");

                start = true;
            }
        }

    }*/

    Runnable updateThread = new Runnable()
    {
        @Override
        public void run()
        {

            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/reverseme.txt");
            // Delete any previous recording.
            if (file.exists())
                file.delete();
            // Create the new file.
            try
            {
                file.createNewFile();
            } catch (IOException e)
            {
                text.setText("file failed created");
                throw new IllegalStateException("Failed to create " + file.toString());
            }

            try
            {
                // Create a DataOuputStream to write the audio data into the saved file.
                OutputStream os = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);
                short[] buffer = new short[bufferSize];
                audioRecord.startRecording();

                Complex[] x = new Complex[bufferSize];
                boolean detected = false;//NEWLY ADDED
                long lastTime = System.currentTimeMillis();
                long expired = 0;

                while (isRecording)
                {
                    int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                    for (int i = 0; i < bufferReadResult; i++)
                    {
                        try{
                            dos.writeShort(buffer[i]);
                        }catch (IOException e){}
                        x[i] = new Complex(buffer[i], 0);
                    }


                }

/*
                    if (Count < 2)
                    {
                        Count++;
                        System.out.print(Count);

                    } else
                    {
                        realData = new ArrayList<Double>();
                        for (int i = 0; i < buffer.length; i++)
                        {
                            x[i] = new Complex(buffer[i], 0);
                            dos.writeDouble(val);
                        }
                        //Complex[] y = fft(x);
                        for (int i = 0; i < y.length; i++)
                        {
                            double value = y[i].abs();
                            //dos.writeDouble(value);
                            realData.add(value);
                        }

                        intense1 = 0;
                        intense2 = 0;
                        for (int i = band - 30; i < band; i++)
                        {
                            intense1 += realData.get(i);

                            if (intense1 < realData.get(i))
                                intense1 = realData.get(i);

                        }
                        //TODO modified band calculation
                        for (int i = band + 1; i < 31 + band; i++)
                        {
                            intense2 += realData.get(i);

                            if (intense2 < realData.get(i))
                                intense2 = realData.get(i);

                        }
                        intense1 = intense1 / 30;
                        intense2 = intense2 / 30;
                        dif = intense1 / intense2;


                        if (intense1 + intense2 >= 10000 && (!detected))
                        {
                            double dif_temp = dif;
                            String dif_val = dif + "";
                            dif_val = dif_val.substring(0, 3);
                            //TODO Change Threshold of gesture
                            if (dif_temp < 0.9) // 0.9
                            {
                                lastTime = System.currentTimeMillis();

                                //sig = dif;
                                sig_str = "CLOSER [Ratio: " + dif_val + "]";
                                detected = true;

                                lastTime = System.currentTimeMillis();

                            } else if (dif_temp > 1.1) //1.1
                            {

                                sig_str = "FURTHER [Ratio: " + dif_val + "]";
                                detected = true;

                                lastTime = System.currentTimeMillis();
                            } else
                            {
                                sig_str = "No Gesture Detected";
                                expired = System.currentTimeMillis() - lastTime;
                                // deactive sensors if no gesture for 10 seconds
                                // Para: expired = 10000ms -> 10sec
                                if (expired > 8000)
                                {
                                    System.out.println("sleep_start ...");
                                    isRecording = false;


                                    handler1.obtainMessage(MSG_SLEEP).sendToTarget();

                                    System.out.println("sleep_done ...");

                                }

                            }

                        } else if (intense1 + intense2 < 10000)
                        {
                            sig_str = "No Ultrasound Detected";
                            expired = System.currentTimeMillis() - lastTime;
                            // deactive sensors if no gesture for 10 seconds
                            // Para: expired = 10000ms -> 10sec
                            if (expired > 8000)
                            {
                                System.out.println("sleep_start ...");
                                isRecording = false;

                                //handler1.removeCallbacks(updateThread);
                                handler1.obtainMessage(MSG_SLEEP).sendToTarget();

                                System.out.println("sleep_done ...");

                            }
                            // TODO: add button to play ultrasound seperate.
                        }
                        if (System.currentTimeMillis() - lastTime > 800 && detected)
                        {
                            System.out.println("here set false");
                            detected = false;
                        }

                        if (intense1 >= 1200)
                            right = !right;

                        Count = 0;
                    }
                }*/
                audioRecord.stop();
                dos.close();

                System.out.println("recording stoped");
            } catch (Throwable t)
            {
                text.setText("Recording Failed");
            }
        }
    };
    /*

        Runnable printDoge = new Runnable()
        {
            public void run()
            {

                statusText.setText(sig_str);
                String ratio = dif + "";
                ratio = ratio.substring(0, 3);
                if (right)
                {

                    text.setText("Ratio (L/R): " + ratio);
                    //setContentView(R.layout.dogeright);
                } else
                {
                    text.setText("Ratio (L/R): " + ratio);
                    //setContentView(R.layout.dogeleft);
                }
                handler2.postDelayed(printDoge, 1);
            }
        };

    */
    class sendButtonListener implements View.OnClickListener
    {
        public void onClick(View v)
        {
            String[] reciver = new String[]{"wmao7@wisc.edu"};
            String[] mySbuject = new String[]{"test audio"};
            String myCc = "cc";
            String mybody = realData.toString();
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("plain/text");
            intent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
            intent.putExtra(android.content.Intent.EXTRA_CC, myCc);
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySbuject);

            startActivity(Intent.createChooser(intent, "mail test"));

        }
    }


    /**
     * Author: Kefei Fu
     * Function: do FFT on waveform generated by mic recorder.
     */
    /*
    public static Complex[] fft(Complex[] x)
    {


        int N = x.length;

        // base case
        if (N == 1) return new Complex[]{x[0]};

        // radix 2 Cooley-Tukey FFT
        if (N % 2 != 0)
        {
            throw new RuntimeException("N is not a power of 2");
        }

        // fft of even terms
        Complex[] even = new Complex[N / 2];
        for (int k = 0; k < N / 2; k++)
        {
            even[k] = x[2 * k];
        }
        Complex[] q = fft(even);

        // fft of odd terms
        Complex[] odd = even;  // reuse the array
        for (int k = 0; k < N / 2; k++)
        {
            odd[k] = x[2 * k + 1];
        }
        Complex[] r = fft(odd);

        // combine
        Complex[] y = new Complex[N];
        for (int k = 0; k < N / 2; k++)
        {
            double kth = -2 * k * Pi / N;
            Complex wk = new Complex(Math.cos(kth), Math.sin(kth));
            y[k] = q[k].plus(wk.times(r[k]));
            y[k + N / 2] = q[k].minus(wk.times(r[k]));
        }
        return y;
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}