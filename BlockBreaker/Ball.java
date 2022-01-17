import javafx.scene.image.ImageView;

class Ball extends Object{
    Ball(){
        super(300, 680, 20, 20, 0, 0, new ImageView("Balli.png"));
        speed = 5;
    }

    void update(){
        if(!active)return;
        if(pos.x<0||pos.x>BlockBreaker.width){
            vel.x=-vel.x;
        }   
        if(pos.y<0){//TEMP BALL bounce top
            vel.y=-vel.y;
        } 
        else if(pos.y>BlockBreaker.height){
            setActive(false);
            pos.x = 300;
            pos.y = 680;
            vel = new PVector(0, 0);
        }  
        super.update();
    }
}