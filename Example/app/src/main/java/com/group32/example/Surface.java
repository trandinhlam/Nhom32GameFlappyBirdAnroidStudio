package com.group32.example;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDLAM123 on 5/27/2017.
 */

public class Surface extends SurfaceView implements SurfaceHolder.Callback{
    private static final int DISTANCE2IMPEDIMENTS=500*4/3; //(pixel)
    private int Point=0; //(pixel)
    private int highScore;
    protected Chibicharacter chibi1;
    protected List<BottomImpediment> Bottoms;// chướng ngại vật bên dưới
    protected List<TopImpediment> Tops;// chướng ngại vật trên
    protected List<Ground> Grounds; // mặt đất
    protected List<Clouds> Clouds;// các đám mây
    protected GameThread gameThread;
    protected boolean GameOver=false;

    protected Bitmap tempBottoms=BitmapFactory.decodeResource(this.getResources(),
            R.drawable.image1);
    Bitmap ground=BitmapFactory.decodeResource(this.getResources(),
            R.drawable.ground);
    Bitmap clouds=BitmapFactory.decodeResource(this.getResources(),
            R.drawable.clouds);

    Bitmap imageCup=BitmapFactory.decodeResource(this.getResources(),
            R.drawable.cup);
    // CÁc biến phục vụ việc phát âm thanh
    private int soundIdJump;
    private int soundIdPass;
    private int soundIdCry;
    private int soundIdFail;
    private static final int MAX_STREAMS=100;
    private int soundIdBackground;
    private boolean soundPoolLoaded;
    private SoundPool soundPool;

    public Surface(Context context) {
        super(context);

        // Dảm bảo GameSurface này có thể focus để điều khiển các sự kiện
        this.setFocusable(true);
        // đặt các sự kiện liên quan tới game
        this.getHolder().addCallback(this);
        this.initSoundPool();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap chibiBit1= BitmapFactory.decodeResource(this.getResources(),
                R.drawable.chibi1);
        // Gan nhan vat ban dau la chibit1
        this.chibi1=new Chibicharacter(this,chibiBit1,GameObject.screenwidth/3,300);
        // tạo chướng ngại vật bottom và top
        this.Bottoms=new ArrayList<BottomImpediment>() ;
        this.Bottoms.add(new BottomImpediment(this,tempBottoms));
        this.Tops = new ArrayList<TopImpediment>();
        this.Tops.add(new TopImpediment(this,tempBottoms));
        this.Tops.get(0).Velocity=0;
        this.Bottoms.get(0).Velocity=0;
        //tạo mặt đất
        this.Grounds = new ArrayList<Ground>();
        this.Grounds.add(new Ground(ground,0));
        this.Grounds.add(new Ground(ground,ground.getWidth()/2));// tạo mặt đất ở cuối hình ground1
        this.Grounds.add(new Ground(ground,ground.getWidth()));// tạo mặt đất ở cuối hình ground1
        // tạo bầu trời
        this.Clouds = new ArrayList<Clouds>();
        this.Clouds.add(new Clouds(clouds,0));
        this.Clouds.add(new Clouds(clouds,clouds.getWidth()*9/10));//
        this.Clouds.add(new Clouds(clouds,clouds.getWidth()*2*9/10));//
        //Lấy điểm cao nhất
        SharedPreferences setting= this.getContext().getSharedPreferences("GAME_DATA",Context.MODE_PRIVATE);
        highScore=setting.getInt("HIGH_SCORE",0);

        // tao thread cho game va start'
        this.gameThread=new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // bầu trời được vẽ đầu tiên
        for(int i=this.Clouds.size()-1; i>=0 ; i--){
            this.Clouds.get(i).draw(canvas);
        }
        if(!GameOver) {// nếu chưa gameover thì vẽ
            this.chibi1.draw(canvas);
            for (int i = 0; i < this.Bottoms.size(); i++) {
                this.Tops.get(i).draw(canvas);
                this.Bottoms.get(i).draw(canvas);
            }
        }
        else { // nếu game over
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setTextSize(100);
            canvas.drawText("Game Over!! ",
                    GameObject.screenwidth / 5, GameObject.screenheight / 2, paint);
            canvas.drawText(String.valueOf(Point),
                    GameObject.screenwidth / 5 + 220, GameObject.screenheight / 2 + 150, paint);
            canvas.drawText("Tap to retry!!",
                    GameObject.screenwidth / 5, GameObject.screenheight / 2 + 300, paint);
        }
        // Mặt đất sẽ được vẽ sau cùng
        for(Ground item:this.Grounds) {
            item.draw(canvas);
        }
        if(!GameOver){
            // hiển thị điểm hiện tại
            Paint paint = new Paint();
            paint.setTextSize(200);
            paint.setColor(Color.parseColor("#5C9047"));
            int Pointsposition=Impediment.screenheight-Impediment.GROUND+400;
            int Highscoreposition=Impediment.screenheight-Impediment.GROUND+300;
            canvas.drawText(String.valueOf(Point), GameObject.screenwidth/4,Pointsposition, paint);
            // Hiển thị điểm cao nhất phía góc phải trên cùng màn hình
            paint.setTextSize(100);
            paint.setColor(Color.RED);
            canvas.drawText(String.valueOf(highScore), GameObject.screenwidth*3/4+50,  Highscoreposition, paint);
            canvas.drawBitmap(Bitmap.createScaledBitmap(imageCup,200,200,false),
                    GameObject.screenwidth*3/4,// hoanh do x cua anh
                    Highscoreposition+100,
                    null);
        }

    }

    public void update() {
        this.chibi1.update();
        // Cập nhật Bottoms
        for (BottomImpediment im : this.Bottoms) {
            im.update();
        }
        // Cập nhật Tops
        for (TopImpediment im : this.Tops) {
            im.update();
        }
        for(Ground item:this.Grounds){
            item.update();
        }
        for(int i=this.Clouds.size()-1; i>=0 ; i--){
            this.Clouds.get(i).update();
        }
        if (this.Bottoms.get(Bottoms.size()-1).getX()<GameObject.screenwidth-DISTANCE2IMPEDIMENTS ){// mỗi chướng ngại cách nhau một khoảng cách nhất định
            this.Bottoms.add(new BottomImpediment(this, tempBottoms));
            this.Tops.add(new TopImpediment(this,tempBottoms));
        }
        if (this.Bottoms.get(0).getX()<0-this.Bottoms.get(0).getWidth()) {// nếu đi hết màn hình thì xóa đi
            this.Bottoms.remove(0);
            this.Tops.remove(0);
        }
        // cập nhật cho ground
        if(this.Grounds.get(0).getX()<0-this.Grounds.get(0).getWidth()/2){
                this.Grounds.remove(0);
                this.Grounds.add(new Ground(ground,ground.getWidth()/2));// tạo mặt đất ở cuối hình ground1

        }
        // cập nhật cho bầu trời
        if(this.Clouds.get(0).getX()<0-this.Clouds.get(0).getWidth()*3/2){
            this.Clouds.remove(0);
            this.Clouds.add(new Clouds(clouds,clouds.getWidth()*9/10));// tạo mặt đất ở cuối hình ground1
        }
        if(GameOver){
            for(int i=0;i<Grounds.size();i++){
                this.Grounds.get(i).Velocity=0;
                this.Clouds.get(i).Velocity =0;
            }
        }
        if(checkPass() && !GameOver ){// tính điểm
            this.playSound(this.soundIdPass);
            Point++;
        }
        TurnOnFlag();// kiểm tra và bật cờ

        //Xử lí sự kiện khi Chibi chạm vào Chướng ngại vật
        if(CheckCollision()) {// Khi có đụng độ xảy ra thì game over
            if (!GameOver) {
                this.playSound(this.soundIdFail);
                this.playSound(this.soundIdCry);
                GameOver = true;
               try {
                    gameThread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // cập nhật điểm cao nhất
        SharedPreferences setting= this.getContext().getSharedPreferences("GAME_DATA",Context.MODE_PRIVATE);
        highScore=setting.getInt("HIGH_SCORE",0);
        if(Point>highScore){
                // lưu lại
            highScore=Point;
            SharedPreferences.Editor editor=setting.edit();
            editor.putInt("HIGH_SCORE",Point);
            editor.commit();
        }

    }

    private void TurnOnFlag() {// nếu chướng ngại đầu tiên bị xóa khỏi màn hình, tức [1] thay [0] thì bật cờ
                // điều này đảm bảo mỗi lần đi qua được 1 chướng ngại thì Point chỉ cộng 1 lần
        if (chibi1.getX() < Bottoms.get(0).getX() + Bottoms.get(0).getWidth()-200) {
                 flagpoint=true;
        }
    }

    boolean flagpoint=true; // tắt cờ
    private boolean checkPass() {
        if (chibi1.getX() > Bottoms.get(0).getX() + Bottoms.get(0).getWidth()) {// nếu bay tới được ngưỡng này thì coi như pass qua
            if(flagpoint){// nếu cờ bật
                flagpoint = false;// tắt cờ
                return true;
            }
            return false;
        }
        else {
            return false;
        }
    }

    private boolean CheckCollision() {
        //kiểm tra xem có rớt xuống đất chưa
        if(this.chibi1.getY()>=GameObject.screenheight-Impediment.GROUND-this.chibi1.getHeight()/10){
            this.chibi1.setY(Impediment.screenheight-Impediment.GROUND);
            return true;
        }
        int saiso=100;
        int x=chibi1.getX();
        int y=chibi1.getY();
        int x2=x+chibi1.getWidth()*7/10;
        int y2=y+chibi1.getHeight()*7/10;
        // kiểm tra xem 1 trong 4 điểm của chibi có nằm trong 2 "vùng" của chướng ngại đầu tiên hay không
        if(x>=Tops.get(0).getX() && x <=Tops.get(0).getX()+Tops.get(0).getWidth()
                && y>=Tops.get(0).getY()  && y<= Tops.get(0).getY()+Tops.get(0).getHeight()){
            return true;
        }
        if(x>= Bottoms.get(0).getX() && x<=Bottoms.get(0).getX()+Bottoms.get(0).getWidth()
                && y2>= Bottoms.get(0).getY()  && y2 <= Bottoms.get(0).getY()+Bottoms.get(0).getHeight()){
            return true;
        }
        if(x2>=Tops.get(0).getX() && x2 <=Tops.get(0).getX()+Tops.get(0).getWidth()
                && y>=Tops.get(0).getY()  && y<= Tops.get(0).getY()+Tops.get(0).getHeight()){
            return true;
        }
        if(x2>= Bottoms.get(0).getX() && x2 <=Bottoms.get(0).getX()+Bottoms.get(0).getWidth()
                && y2 >= Bottoms.get(0).getY()  && y2 <= Bottoms.get(0).getY()+Bottoms.get(0).getHeight()){
            return true;
        }


        return false;
    }
    private void initSoundPool()  {
        // Với phiên bản Android API >= 21
        if (Build.VERSION.SDK_INT >= 21 ) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);
            this.soundPool = builder.build();
        }
        // Với phiên bản Android API < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }


        // Sự kiện SoundPool đã tải lên bộ nhớ thành công.
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPoolLoaded = true;
            }
        });


        // Tải file nhạc jump.wav, pass, explosion, cry
        this.soundIdJump = this.soundPool.load(this.getContext(), R.raw.jump,1);
        this.soundIdPass = this.soundPool.load(this.getContext(),R.raw.pass,1);
        this.soundIdFail = this.soundPool.load(this.getContext(),R.raw.explosion,1);
        this.soundIdCry = this.soundPool.load(this.getContext(),R.raw.cry,1);

    }
    private void playSound(int idsound) {
        if(this.soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn =  0.8f;
            int streamId = this.soundPool.play(idsound,leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry= true;
        while(retry) {
            try {
                this.gameThread.setRunning(false);
                // Luồng cha, cần phải tạm dừng chờ GameThread kết thúc.
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }
    }
    // bắt sự kiện chạm vào màn hình
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(GameOver){
                Point = 0;
                // tao lai chuong ngai vat
                this.Bottoms.clear();
                this.Tops.clear();
                // tạo chướng ngại vật bottom và top
                this.Bottoms.add(new BottomImpediment(this,tempBottoms));
                this.Tops.add(new TopImpediment(this,tempBottoms));
                // tao thread cho game và start
                GameOver = false; // chơi lại từ đầu
                chibi1.resetPosition(300,400);
                for (int i = 0; i < this.Bottoms.size(); i++) {
                    this.Tops.get(i).Velocity=0;
                    this.Bottoms.get(i).Velocity=0;
                }

            }
            else{

                this.chibi1.MoveUp();
                this.playSound(this.soundIdJump);
                if(0f == this.Tops.get(0).Velocity)// kiểm tra trước khi
                    for (int i = 0; i < this.Bottoms.size(); i++) {
                        this.Tops.get(i).Velocity=Impediment.VELOCITY;
                        this.Bottoms.get(i).Velocity=Impediment.VELOCITY;
                    }
                    for(int i=0;i<Grounds.size();i++){
                        this.Grounds.get(i).Velocity=Impediment.VELOCITY;
                        this.Clouds.get(i).Velocity =Impediment.VELOCITY;
                    }
            }
            return true;
        }
        return false;
    }
}
