import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class BlockBreaker extends Application {
    //#region CONSTANT
    public static int width = 600;
    public static int height = 800;
    //#endregion

    //#region VARIABLES
    ArrayList<Ball> balls;
    Bar bar;
    ArrayList<Block> blocks;
    ImageView startIv,gameOverIv,gameClearIv;
    Button startButton,retryButton;
    //ArrayList<Block> blocks = new ArrayList<Block>();
    public static float mouseX = 0;
    //Text text;
    public static int life=3;
    int nBall = 100;

    public static int frameCount = 0;
    Stage stage;
    //#endregion

    Pane pane = new Pane();


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        stage.setTitle("BlockBreaker");
        stage.setWidth(width);
        stage.setHeight(height);      
        this.stage = stage;
        //#region JavaFX setup
        

        Timeline timeline= new Timeline(
        new KeyFrame(Duration.millis(700d/80), e->draw()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        //#endregion

        //#region Graphcis Setup
        startIv = new ImageView("BLOCKBREAKER.png");setImageCenter(startIv);
        gameOverIv = new ImageView("GAMEOVER.png");setImageCenter(gameOverIv);
        gameClearIv = new ImageView("GAMECLEAR.png");setImageCenter(gameClearIv);

        startButton = new Button("START");
        retryButton = new Button("RETRY");
        setButtonCenter(startButton);
        setButtonCenter(retryButton);
        startButton.setOnAction(e -> ballStart());
        retryButton.setOnAction(e -> reset());
        retryButton.setTextFill(Color.WHITE);
        //#endregion

        reset();

        
    }

    public void setImageCenter(ImageView iv){
        int offsety = -100;
        iv.setScaleX(2);iv.setScaleY(2);
        Image img = iv.getImage();
        iv.setX(width/2-img.getWidth()/2);
        iv.setY(height/2-img.getHeight()/2+offsety);
    }

    public void setButtonCenter(Button button){//Note it also change style
        int bw = 200;int bh = 50;
        button.setTranslateX(width/2-bw/2);
        button.setTranslateY(height/2+100-bh/2);
        button.setPrefSize(bw, bh);
        String style = "-fx-font-weight:bolder;-fx-font-size:18px;-fx-background-image:url('Button.png');";
        button.setStyle(style);
        button.setOnAction(e -> ballStart());
        button.setTextFill(Color.WHITE);
    }

    public void gameOver(){
        gameOverIv.setVisible(true);
        retryButton.setVisible(true);
    }

    public void gameClear(){
        gameClearIv.setVisible(true);
        retryButton.setVisible(true);
    }

    public void resetBall(){
        ObservableList<Node> children = pane.getChildren();
        balls = new ArrayList<Ball>();
        
        for(int i=0; i<nBall; i++){
            Ball ball = new Ball();
            balls.add(ball);
            children.add(ball.iv);
        }
    }

    public void reset(){
        pane = new Pane();

        ObservableList<Node> children = pane.getChildren();
        
        pane.setOnMouseMoved(e->mouseMoved(e));
        pane.setOnMousePressed(e->mousePressed(e));
        pane.setStyle("-fx-background-image:url('BG.jpg');");

        stage.setScene(new Scene(pane));
        stage.setResizable(false);
        stage.show();
        
        

        gameClearIv.setVisible(false);
        gameOverIv.setVisible(false);
        retryButton.setVisible(false);
        startIv.setVisible(true);
        startButton.setVisible(true);

        resetBall();
        
        bar = new Bar();

        blocks = new ArrayList<Block>();

        float xs = 65;//x spacing
        float ys = 35;

        for (int i=0; i<width-60; i+=xs) {
            for (int j=0; j<500/2; j+=ys) {
                Block block = new Block(i, j);
                children.add(block.iv);
                blocks.add(block);
            }
        }

        children.addAll(bar.iv, startIv, gameOverIv, gameClearIv, startButton, retryButton);
        startIv.toFront();
        gameOverIv.toFront();
        
    }

    //#region INPUT
    public void mouseMoved(MouseEvent e){
        mouseX=(float)e.getX();
    }

    public void ballStart(){
        startButton.setVisible(false);
        startIv.setVisible(false);
        for(Ball ball : balls)ball.setDir(4, -2);
    }

    public void mousePressed(MouseEvent e){
        if(e.getButton() == MouseButton.PRIMARY && balls.get(0).vel.x == 0 && !startButton.isVisible()){
            for(Ball ball : balls)ball.setDir(4, -2);
        }
    }
    //#endregion

    void draw(){
        boolean deadAll = true;
        boolean isClear = true;
        pane.setStyle("-fx-background-image:url('BG.jpg');-fx-background-position:0px "+(frameCount/10)+"px;");
        for(Ball ball : balls){
            if(bar.isCollide(ball)){
                bar.radialCollide(ball);
            }
            for (Block block:blocks) {
                if (block.isCollide(ball)) {
                    block.radialCollide(ball);
                    block.setActive(false);
                }
            }

            ball.update();
            bar.update();
            if(ball.active)deadAll = false;
        }
        for(Block block : blocks){
            if(block.active)isClear = false;
        }

        if(isClear)gameClear();

        if(deadAll && !isClear){
            BlockBreaker.life=BlockBreaker.life-1;
            
            
            if(BlockBreaker.life > 0){
                resetBall();
            }
            if(BlockBreaker.life==0){
                gameOver();
            }
        }

        frameCount++;
    }

    
}