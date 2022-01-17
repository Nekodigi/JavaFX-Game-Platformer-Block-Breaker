import javafx.scene.image.ImageView;

public class Bar extends Object{
    Bar(){
        super(400, 700, 100, 20, new ImageView("Bar.png"));
    }

    void update(){
        pos.x = BlockBreaker.mouseX;
        super.update();
    }
}
