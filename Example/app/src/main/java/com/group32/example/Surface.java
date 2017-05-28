package com.group32.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TDLAM123 on 5/27/2017.
 */

public class Surface extends SurfaceView implements SurfaceHolder.Callback{
    private static final int DISTANCE2IMPEDIMENTS=500; //(pixel)
    private int Point=0; //(pixel)
    protected Chibicharacter chibi1;
    protected List<BottomImpediment> Bottoms;// chướng ngại vật bên dưới
    protected List<TopImpediment> Tops;
    protected GameThread gameThread;
    protected boolean GameOver=false;
    public Surface(Context context) {
        super(context);

        // Dảm bảo GameSurface này có thể focus để điều khiển các sự kiện
        this.setFocusable(true);
        // đặt các sự kiện liên quan tới game
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Bitmap chibiBit1= BitmapFactory.decodeResource(this.getResources(),
                R.drawable.chibi1);
        Bitmap tempBottoms=BitmapFactory.decodeResource(this.getResources(),
                R.drawable.image1);
        // Gan nhan vat ban dau la chibit1
        this.chibi1=new Chibicharacter(this,chibiBit1,300,200);
        // tạo chướng ngại vật bottom và top
        this.Bottoms=new ArrayList<BottomImpediment>() ;
        this.Bottoms.add(new BottomImpediment(this,tempBottoms));
        this.Tops = new ArrayList<TopImpediment>();
        this.Tops.add(new TopImpediment(this,tempBottoms));
        // tao thread cho game va start'
        this.gameThread=new GameThread(this,holder);
        this.gameThread.setRunning(true);
        this.gameThread.start();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(!GameOver) {// nếu chưa gameover thì vẽ
            this.chibi1.draw(canvas);
            for (int i = 0; i < this.Bottoms.size(); i++) {
                this.Tops.get(i).draw(canvas);
                this.Bottoms.get(i).draw(canvas);
            }
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setTextSize(50);
            canvas.drawText(String.valueOf(Point), 200, 100, paint);

            int Groundposition = GameObject.screenheight - Impediment.GROUND;

            canvas.drawLine(0, Groundposition, GameObject.screenwidth, Groundposition, paint);
        }
        else { // nếu game over
            try {
                gameThread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setTextSize(100);
            canvas.drawText("Game Over!!\n  "+String.valueOf(Point),
                    GameObject.screenwidth/4, GameObject.screenheight/2, paint);
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

        if (this.Bottoms.get(Bottoms.size()-1).getX()<GameObject.screenwidth-DISTANCE2IMPEDIMENTS ){// mỗi chướng ngại cách nhau một khoảng cách nhất định
            Bitmap tempBottoms = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.image1);
            //Bitmap tempTops=tempBottoms.r
            this.Bottoms.add(new BottomImpediment(this, tempBottoms));
            this.Tops.add(new TopImpediment(this,tempBottoms));
        }
        if (this.Bottoms.get(0).getX()<0-this.Bottoms.get(0).getWidth()) {// nếu đi hết màn hình thì xóa đi
            this.Bottoms.remove(0);
            this.Tops.remove(0);
        }
        
        if(checkPass() && !GameOver ){// tính điểm
            Point++;
        }
        TurnOnFlag();// kiểm tra và bật cờ

        //Xử lí sự kiện khi Chibi chạm vào Chướng ngại vật

        if(CheckCollision()){// Khi có đụng độ xảy ra thì game over
                GameOver=true;
        }

    }

    private void TurnOnFlag() {// nếu chướng ngại đầu tiên bị xóa khỏi màn hình, tức [1] thay [0] thì bật cờ
        // điều này đảm bảo mỗi lần đi qua được 1 chướng ngại thì Point chỉ cộng 1 lần
        if (chibi1.getX() < Bottoms.get(0).getX() + Bottoms.get(0).getWidth()) {
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
        if(this.chibi1.getY()>=GameObject.screenheight-Impediment.GROUND-this.chibi1.getHeight()){
            return true;
        }
        int x=chibi1.getX();
        int y=chibi1.getY();
        int x2=x+chibi1.getWidth();
        int y2=y+chibi1.getHeight();
        // kiểm tra xem chibi có nằm trong 2 "vùng" của chướng ngại đầu tiên hay không
        if(x>=Tops.get(0).getX() && x <=Tops.get(0).getX()+Tops.get(0).getWidth()
                && y>=Tops.get(0).getY()  && y <= Tops.get(0).getY()+Tops.get(0).getHeight()){
            return true;
        }
        if(x2 >= Bottoms.get(0).getX() && x2 <=Bottoms.get(0).getX()+Bottoms.get(0).getWidth()
                && y2 >= Bottoms.get(0).getY()  && y2 <= Bottoms.get(0).getY()+Bottoms.get(0).getHeight()){
            return true;
        }

        return false;
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
                Bitmap tempBottoms=BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.image1);
                // tạo chướng ngại vật bottom và top
                this.Bottoms=new ArrayList<BottomImpediment>() ;
                this.Bottoms.add(new BottomImpediment(this,tempBottoms));
                this.Tops = new ArrayList<TopImpediment>();
                this.Tops.add(new TopImpediment(this,tempBottoms));
                // tao thread cho game va start
                GameOver = false; // chơi lại từ đầu
                chibi1.resetPosition(300,400);
                for (int i = 0; i < this.Bottoms.size(); i++) {
                    this.Tops.get(i).Velocity=0;
                    this.Bottoms.get(i).Velocity=0;
                }
            }
            else{
                this.chibi1.MoveUp();
                for (int i = 0; i < this.Bottoms.size(); i++) {
                    this.Tops.get(i).Velocity=Impediment.VELOCITY;
                    this.Bottoms.get(i).Velocity=Impediment.VELOCITY;
                }

            }
            return true;
        }
        return false;
    }
}
