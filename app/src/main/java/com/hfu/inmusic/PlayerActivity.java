package com.hfu.inmusic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.hfu.inmusic.MainActivity.musicFiles;
import static com.hfu.inmusic.MainActivity.repeatboolean;
import static com.hfu.inmusic.MainActivity.suffleboolean;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{

    TextView song_name,artist_name,duration_played,duration_total;
    ImageView cover_art,nextbtn,prevbtn,backbtn,suffelebtn,repeatbtn;
    FloatingActionButton playbtn;
    SeekBar seekBar;
    int position = -1;
    static  Uri uri;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playThread ,prevThread,nextThread;


    static  ArrayList<MusicFiles>  listSongs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initView();
        getIntentMethod();
        song_name.setText(listSongs.get(position).getTitle());
        artist_name.setText(listSongs.get(position).getArtist());
        mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null &&  fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentposition = mediaPlayer.getCurrentPosition() /1000;
                    seekBar.setProgress(mCurrentposition);
                    duration_played.setText(formattedTime(mCurrentposition));
                }
                handler.postDelayed(this,1000);
            }
        });


    }

    @Override
    protected void onResume() {
        playThread();
        nextThread();
        prevThread();
        super.onResume();
    }

    private void prevThread() {
        prevThread = new Thread(){
            @Override
            public void run() {
                super.run();
                prevbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        prevbtnclick();
                    }
                });
            }
        };
        prevThread.start();
    }

    private void prevbtnclick() {

        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (listSongs.size() -1 ) : (position -1) );
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentposition = mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentposition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playbtn.setBackgroundResource(R.drawable.ic_baseline_pause);
            mediaPlayer.start();
        }else{
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) < 0 ? (listSongs.size() -1 ) : (position -1) );
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentposition = mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentposition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playbtn.setBackgroundResource(R.drawable.ic_baseline_play_arrow);


        }
    
    }

    private void nextThread() {
        nextThread = new Thread(){
            @Override
            public void run() {
                super.run();
                nextbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextbtnclick();
                    }
                });
            }
        };
        nextThread.start();
    }

    private void nextbtnclick() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();


            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentposition = mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentposition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
             playbtn.setBackgroundResource(R.drawable.ic_baseline_pause);
             mediaPlayer.start();
        }else{
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % listSongs.size());
            uri = Uri.parse(listSongs.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);
            song_name.setText(listSongs.get(position).getTitle());
            artist_name.setText(listSongs.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentposition = mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentposition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playbtn.setBackgroundResource(R.drawable.ic_baseline_play_arrow);


        }
    }

    private void playThread() {
        playThread = new Thread(){
            @Override
            public void run() {
                super.run();
                playbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playbtnclick();
                    }
                });
            }
        };
        playThread.start();
    }

    private void playbtnclick() {
        if(mediaPlayer.isPlaying()){
            playbtn.setImageResource(R.drawable.ic_baseline_play_arrow);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration() /1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentposition = mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentposition);

                    }
                    handler.postDelayed(this,1000);
                }
            });
        }else{
            playbtn.setImageResource(R.drawable.ic_baseline_pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer != null){
                        int mCurrentposition = mediaPlayer.getCurrentPosition() /1000;
                        seekBar.setProgress(mCurrentposition);

                    }
                    handler.postDelayed(this,1000);
                }
            });


        }
    }

    private String formattedTime(int mCurrentposition) {
        String totalout = "";
        String totalNew = "";
        String seconds = String.valueOf(mCurrentposition % 60);
        String minits = String.valueOf(mCurrentposition / 60);
        totalout = minits + ":" + seconds;
        totalNew = minits + ":" + "0" + seconds;
        if(seconds.length() == 1){
            return  totalNew;
        }
        else{
            return totalout;
        }



    }

    private void getIntentMethod(){
        position = getIntent().getIntExtra("position",-1);
        listSongs = musicFiles;
        if(listSongs != null){
            playbtn.setImageResource(R.drawable.ic_baseline_pause);
            uri = Uri.parse(listSongs.get(position).getPath());
        }
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        metadata(uri);

    }
   private void initView(){
        song_name = findViewById(R.id.song_name);
        artist_name = findViewById(R.id.song_artist);
        duration_played = findViewById(R.id.durationplayed);
        duration_total = findViewById(R.id.durationpTotal);

        cover_art = findViewById(R.id.coveer_artist);
        nextbtn = findViewById(R.id.id_next);
        prevbtn = findViewById(R.id.id_prev);
        backbtn = findViewById(R.id.back_btn);
        suffelebtn = findViewById(R.id.id_suffele);
        repeatbtn = findViewById(R.id.id_repeat);
        playbtn = findViewById(R.id.play_pause);
        seekBar = findViewById(R.id.sekk_bar);
   }
   private void metadata(Uri uri){
       MediaMetadataRetriever retriever = new MediaMetadataRetriever();
       retriever.setDataSource(uri.toString());
       int durationTotal = Integer.parseInt(listSongs.get(position).getDuration()) / 1000;
       duration_total.setText(formattedTime(durationTotal));
       byte[] art = retriever.getEmbeddedPicture();
       Bitmap bitmap ;
       if(art != null){

         bitmap  = BitmapFactory.decodeByteArray(art,0,art.length);
           ImageAnimation(this,cover_art,bitmap);
         Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
           @Override
           public void onGenerated(@Nullable Palette palette) {
               Palette.Swatch swatch = palette.getDominantSwatch();
                if(swatch != null){
                    ImageView gredial = findViewById(R.id.image_gr);
                    RelativeLayout mContainer =findViewById(R.id.mContain);
                    gredial.setBackgroundResource(R.drawable.gr_bg);
                    mContainer.setBackgroundResource(R.drawable.main_bg);
                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{swatch.getRgb(),0x00000000});
                    gredial.setBackground(gradientDrawable);

                    GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{swatch.getRgb(),swatch.getRgb()});
                    mContainer.setBackground(gradientDrawableBg);

                    song_name.setTextColor(swatch.getTitleTextColor());
                    artist_name.setTextColor(swatch.getBodyTextColor());
                }else{
                    ImageView gredial = findViewById(R.id.image_gr);
                    RelativeLayout mContainer =findViewById(R.id.mContain);
                    gredial.setBackgroundResource(R.drawable.gr_bg);
                    mContainer.setBackgroundResource(R.drawable.main_bg);
                    GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{0xff000000,0x00000000});
                    gredial.setBackground(gradientDrawable);

                    GradientDrawable gradientDrawableBg = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                            new int[]{0xff000000,0xff000000});
                    mContainer.setBackground(gradientDrawableBg);
                    song_name.setTextColor(Color.WHITE);
                    artist_name.setTextColor(Color.DKGRAY);
                }
           }

       });
       }else{
           Glide.with(this)
                   .asBitmap()
                   .load(R.drawable.logo)
                   .into(cover_art);
           ImageView gredial = findViewById(R.id.image_gr);
           RelativeLayout mContainer =findViewById(R.id.mContain);
           gredial.setBackgroundResource(R.drawable.gr_bg);
           mContainer.setBackgroundResource(R.drawable.main_bg);
           song_name.setTextColor(Color.WHITE);
           artist_name.setTextColor(Color.DKGRAY);
       }

   }

   public void ImageAnimation(final Context context, final ImageView imageView, final Bitmap bitmap){
       Animation animationOut = AnimationUtils.loadAnimation(context,android.R.anim.fade_out);
       final Animation animationIn = AnimationUtils.loadAnimation(context,android.R.anim.fade_in);
       animationOut.setAnimationListener(new Animation.AnimationListener() {
           @Override
           public void onAnimationStart(Animation animation) {

           }

           @Override
           public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animationIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animationIn);
           }

           @Override
           public void onAnimationRepeat(Animation animation) {

           }
       });
       imageView.startAnimation(animationOut);

   }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextbtnclick();
        if(mediaPlayer != null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);

        }

    }
}