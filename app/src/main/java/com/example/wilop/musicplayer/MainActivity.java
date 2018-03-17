package com.example.wilop.musicplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.content.pm.ActivityInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import static java.util.Arrays.asList;



public class MainActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    SeekBar soundBar;
    SeekBar volumenBar;
    Boolean playFlag = true;
    Boolean SongLoaded = false;
    Button play;
    TextView SongName;
    ListView SongsList;
    ArrayList<String> elementos;
    ArrayList<Integer>  SongListRaw =new  ArrayList<Integer>();
    //ArrayList<Integer>  TexListRaw =new  ArrayList<Integer>();
    TextView  textView2;
    TextView  textView3;
    TextView SongLetter;
    CountDownTimer timer;
    Button playpause;
    AudioManager audioManager;
    int actualSong;
    int maxVolumen;
    int CurrentVolumen;
    int tmpDuration;
    //private static final String texto = "Episode IV A NEW HOPE\n\nIt is a period of civil war. Rebel spaceships, striking from a hidden base, have won their first victory against the evil Galactic Empire.\n\nDuring the battle, Rebel spies managed to steal secret plans to the Empire's ultimate weapon, the DEATH STAR, an armored space station with enough power to destroy an entire planet.\n\nPursued by the Empire's sinister agents, Princess Leia races home aboard her starship, custodian of the stolen plans that can save her people and restore freedom to the galaxy.....";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        playpause = findViewById(R.id.PlayButton);
        setContentView(R.layout.activity_main);
        soundBar = findViewById(R.id.ProgresBar);
        SongsList = findViewById(R.id.Songslist);
        elementos = new ArrayList<String>(asList("Let You Down-NF","Casate Conmigo-Silvestre Ft. Nicky Jam","Besos En Guerra-Morat Ft. Juanes","El Farsante-Ozuna Ft. Romeo Santos","Cinto","Seis","Siete"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,elementos);
        //Add new song
        SongListRaw.add(R.raw.let_you_down);
        SongListRaw.add(R.raw.casate_conmigo);
        SongListRaw.add(R.raw.besos_en_guerra);
        SongListRaw.add(R.raw.el_farsante);
        //Add letters
        //TexListRaw.add(R.raw.let_you_downt);
        //TexListRaw.add(R.raw.casate_conmigot);
        //TexListRaw.add(R.raw.besos_en_guerrat);
        //TexListRaw.add(R.raw.el_farsantet);
        //
        SongsList.setAdapter(arrayAdapter);
        SongsList = findViewById(R.id.Songslist);
        play = findViewById(R.id.PlayButton);

        //SongLetter.findViewById(R.id.SongLetter);

        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        maxVolumen = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        CurrentVolumen = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumenBar = findViewById(R.id.Volumenbar);
        volumenBar.setMax(maxVolumen);






        soundBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
                // TODO Auto-generated method stub


            }
        });

        SongsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                actualSong=position;
                if(SongLoaded){
                    mediaPlayer.stop();
                    timer.cancel();
                    Play(view,SongListRaw.get(position), elementos.get(position));
                }else{
                    Play(view,SongListRaw.get(position), elementos.get(position));
                }


            }
        });
    }
    public void Play(View view,int song, String name) {

        SongLoaded = true;
        mediaPlayer = MediaPlayer.create(this,song);

        //SetNameSong
        SongName = findViewById(R.id.SongName);
        SongName.setText(name);
        SongName.setVisibility(View.VISIBLE);

        //
        int n = mediaPlayer.getDuration()/1000;
        int m = n/60;
        int s = n-m*60;
        textView3.setText(Integer.toString(m)+":"+Integer.toString(s));
        textView3.setVisibility(View.VISIBLE);
        textView2.setText(Integer.toString(m)+":"+Integer.toString(s));
        textView2.setVisibility(View.VISIBLE);


        //SongLetter
        //SongLetter.setText(texto);


        //Set Seekbar max
        soundBar.setMax(mediaPlayer.getDuration()/1000);
        //Set Seekbar position
        soundBar.setProgress(0);

        //play song
       mediaPlayer.start();


        play.setBackgroundResource(R.drawable.pausa);
        playFlag = false;
        SongLoaded = true;

        MoveBar(mediaPlayer.getDuration(),view);



    }


    public void MoveBar(final int time, final View view){
        timer = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                soundBar.setProgress((int)millisUntilFinished/1000);
                int n = (int)millisUntilFinished/1000;
                int m = n/60;
                int s = n-m*60;
                textView3.setText(Integer.toString(m)+":"+Integer.toString(s));
                tmpDuration = (int)millisUntilFinished;


            }

            public void onFinish() {
                Next(view);

            }
        }.start();
    }

    public void play_pause(View view){
        if (SongLoaded){
            if(playFlag){
                mediaPlayer.start();
                play.setBackgroundResource(R.drawable.pausa);
                playFlag = false;
                MoveBar(tmpDuration, view);



            }else{
                mediaPlayer.pause();
                mediaPlayer.getDuration();
                play.setBackgroundResource(R.drawable.play);
                playFlag = true;
                //handler.removeCallbacks(r);
                timer.cancel();


            }
        }


    }

    public  void Next(View view){
        if (actualSong<SongListRaw.size() && SongLoaded){
            mediaPlayer.stop();
            timer.cancel();
            Play(view,SongListRaw.get(actualSong+1),elementos.get(actualSong+1));
        }

    }
    public  void Back(View view){
        if(actualSong>0 && SongLoaded) {
            mediaPlayer.stop();
            timer.cancel();
            Play(view, SongListRaw.get(actualSong + 1), elementos.get(actualSong + 1));
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            Log.d("VOLUMEN", "UP");
            CurrentVolumen = CurrentVolumen+5;
            volumenBar.setProgress(CurrentVolumen);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,CurrentVolumen,0);
            volumenBar.setVisibility(View.VISIBLE);
            new CountDownTimer(8000, 1000) {
                public void onTick(long millisUntilFinished) {
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    volumenBar.setVisibility(View.INVISIBLE);
                }
            }.start();

            return true;
        }else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            Log.d("VOLUMEN", "DOWM");
            CurrentVolumen = CurrentVolumen-5;
            volumenBar.setProgress(CurrentVolumen);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,CurrentVolumen,0);
            volumenBar.setVisibility(View.VISIBLE);
            new CountDownTimer(8000, 1000) {
                public void onTick(long millisUntilFinished) {
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {
                    volumenBar.setVisibility(View.INVISIBLE);
                }
            }.start();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }





}
