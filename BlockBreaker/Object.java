import java.util.Arrays;

import javafx.scene.image.ImageView;

class Object{
    PVector pos, vel, size;
    float speed;
    ImageView iv;
    float bounciness = 1;
    boolean active=true;

    Object(float x, float y, float w, float h, ImageView iv){this(x, y, w, h, 0, 0, iv);}

    Object(float x, float y, float w, float h, float vx, float vy, ImageView iv){
        pos = new PVector(x ,y);
        size = new PVector(w, h);
        vel = new PVector(vx, vy);
        this.iv = iv;
        iv.setFitWidth(size.x);
        iv.setFitHeight(size.y);
        iv.setX(pos.x);
        iv.setY(pos.y);
    }

    void update(){
        pos.add(vel);
        iv.setX(pos.x);
        iv.setY(pos.y);
    }

    public void setActive(boolean v){
        active = v;
        iv.setVisible(v);
    }

    public void setDir(float x, float y){
        vel = new PVector(x, y);
        vel.setMag(speed);
    }

    public boolean isCollide(Object t){
        float x = pos.x;
        float y = pos.y;
        float xm = pos.x+size.x;
        float ym = pos.y+size.y;
        float _x = t.pos.x;
        float _y = t.pos.y;
        float _xm = t.pos.x+t.size.x;
        float _ym = t.pos.y+t.size.y;
        return x < _xm && _x<xm && y < _ym && _y<ym;
    }

    //#region Update Target Veloocity
    public void radialCollide(Object t){//t away from this object
        if(!active)return;
        PVector diff = t.pos.clone().sub(pos);
        t.vel = diff.setMag(t.speed);
    }
      
      float EPSILON = 0.001f;

    public int collide(Object t){
        if(!active)return -1;
        float x = pos.x;
        float y = pos.y;
        float xm = pos.x+size.x;
        float ym = pos.y+size.y;
        float _x = t.pos.x;
        float _y = t.pos.y;
        float _xm = t.pos.x+t.size.x;
        float _ym = t.pos.y+t.size.y;
        float dxl = Math.abs(x-_xm);
        float dxm = Math.abs(xm-_x);
        float dyl = Math.abs(y-_ym);
        float dym = Math.abs(ym-_y);
        
        float[] dists = {dxl, dxm, dyl, dym};
        Arrays.sort(dists);
        //println(dists[0], dists[1], dists[2], dists[3], dxl);
        if(dists[0] == dxl){
            t.pos.x -= dxl+EPSILON;
        }else if(dists[0] == dxm){
            t.pos.x += dxm+EPSILON;
        }else if(dists[0] == dyl){
            t.pos.y -= dyl+EPSILON;
        }else if(dists[0] == dym){
            t.pos.y += dym+EPSILON;
        }
        //player.pos.add(PVector.mult(player.vel, 1.0001));
        if(dists[0] == dxl){
            t.vel.x = -t.vel.x*bounciness;
        }else if(dists[0] == dxm){
            t.vel.x = -t.vel.x*bounciness;
        }else if(dists[0] == dyl){
            t.vel.y = -t.vel.y*bounciness;
        }else{
            t.vel.y = -t.vel.y*bounciness;
        }
        
        if(dists[0] == dxl)return 0;
        else if(dists[0] == dxm)return 1;
        else if(dists[0] == dyl)return 2;
        else return 3;
    }
}