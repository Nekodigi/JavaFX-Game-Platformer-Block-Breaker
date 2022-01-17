//REQUIRES SUNNY LAND
//put under "data/SunnyLand" get here:https://assetstore.unity.com/packages/2d/characters/sunny-land-103349

//How to use: Click on palette(left top) to pick. Click grid to make stage.
//[ key to rotate. shift key to make no collider object. Z/X key to set damage when on it.
//press S to save. press L to load stage.

//if you want to test play, press B key.
//Then, arrow key to move. space key to jump.

import java.util.Arrays;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.nio.Buffer;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;


import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PlatformerCreator extends Application {


  Palette palette;
  Stage stage;
  boolean building = true;

  private static final int width = 1152;//1152, 720
  private static final int height = 720;
  private static final float EPSILON = 0.0001f;
  boolean mousePressed;
  MouseButton mouseButton;
  private double mouseX = 0;
  private double mouseY = 0;
  boolean fill;
  boolean stroke;
  int frameCount;

  Canvas canvas;
  GraphicsContext gc;
  

  public static int constrain(int value, int min, int max){
    return Math.min(Math.max(value, min), max); 
  }

  class PVector{
    float x, y, z;

    PVector(){}

    PVector(float x, float y){
      this.x = x;
      this.y = y;
    }

    PVector add(PVector p){
      x += p.x;
      y += p.y;
      z += p.z;
      return this;
    }
  }

  class PImage{
    BufferedImage img;
    Image image;
    int width;
    int height;

    PImage(BufferedImage img){
      this.img = img;
      this.width = img.getWidth();
      this.height = img.getHeight();
      this.image = SwingFXUtils.toFXImage(img, null);;
    }

    PImage get(int x, int y, int w, int h){
      return new PImage(img.getSubimage(x, y, w, h));
    }
  }

  PImage loadImage(String fileName){
    try {
      BufferedImage img = ImageIO.read(new File("data/"+fileName));
      return new PImage(img);
    }catch (IOException e) {
      System.out.println(e);
    }
    return null;
  }

  void image(PImage img, int x, int y){
    gc.drawImage(img.image, x, y);
  }

  void image(PImage img, int x, int y, int w, int h){
    gc.drawImage(img.image, x, y, w, h);
  }

  void rect(int x, int y, int w, int h){
    if(fill)gc.fillRect(x, y, w, h);
    if(stroke)gc.strokeRect(x, y, w, h);
  }

  void noFill(){
    fill = false;
  }

  void fill(float w, float a){
    fill(w, w, w, a);
  }

  void fill(float r, float g, float b, float a){
    fill = true;
    gc.setFill(new Color(r/255.0, g/255.0, b/255.0, a/255.0));
  }

  void noStroke(){
    stroke = false;
  }
  void stroke(float w, float a){//FIXME: ALPHA NOT WORKING NOW!!
    stroke(w, w, w, a);
  }

  void stroke(float r, float g, float b, float a){
    stroke= true;
    gc.setStroke(new Color(r/255.0, g/255.0, b/255.0, a/255.0));
  }

  public void pushMatrix(){
    gc.save();
  }

  public void popMatrix(){
    gc.restore();
  }

  public void translate(float x, float y){
    gc.translate(x, y);
  }

  public void scale(float x, float y){
    gc.scale(x, y);
  }

  public void rotate(float x){
    gc.rotate(x*180/Math.PI);
  }

  public String[] loadStrings(String fileName){
    try{
      File file = new File("data/"+fileName);
      BufferedReader br = new BufferedReader(new FileReader(file));
      //StringBuilder sb = new StringBuilder();
      ArrayList<String> strs =  new ArrayList<String>();
      try{
        String line = br.readLine(); 
        while (line != null) { strs.add(line); line = br.readLine(); }
        br.close();
      }catch(IOException e){System.out.println(e);}

      return strs.toArray(new String[strs.size()]);
    }catch(FileNotFoundException e){System.out.println(e);}
    return null;
  }

  public void saveStrings(String fileName, String[] strs){
    try{
      File file = new File("data/"+fileName);
      PrintWriter pw = new PrintWriter(file);
      for(String str : strs){
        pw.println(str);
      }
      pw.close();
    }catch(IOException e){System.out.println(e);}
  }

  void println(String str){
    System.out.println(str);
  }


  public static void main(String[] args) {
      launch(args);
  }

  Group root;

  @Override
  public void start(javafx.stage.Stage stg) {
    canvas = new Canvas(width, height);
      //stg.setWidth(width);
      //stg.setHeight(height);

      //double xspacing = 35;
      //double yspacing = 35;
      // this.children = children;
      // for (int x = 0; x < 500; x += xspacing) {
      //     for (int y = 0; y < 500 / 2; y += yspacing) {
      //         Block block = new Block(x, y);
      //         children.add(block);
      //         blocks.add(block);
      //     }
      // }

      // children.addAll(ball, bar);
      



      //gc = canvas.getGraphicsContext2D();
      root = new Group();
      root.getChildren().add(canvas);
      stg.setScene(new Scene(root));
      stg.show();
       Timeline timeline= new Timeline(
           new KeyFrame(Duration.millis(1000d/60*4), e->draw()));
      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.play();
      palette = new Palette();
      System.out.println(palette.rawStage);
      stage = new Stage(25*16, 0*16, 1024, 32);
      stage.Load();
      
      player = new Player(0, 300);
  }



  public void draw() {
    root.getChildren().remove(canvas);
    canvas = new Canvas(width, height);
    canvas.setOnMouseMoved(e -> mouseMoved(e));
    canvas.setOnMousePressed(e -> mousePressed(e));
    canvas.setOnMouseReleased(e -> mouseReleased(e));
    canvas.setOnKeyPressed(e -> keyPressed(e));
    canvas.setOnKeyReleased(e -> keyReleased(e));
    canvas.setFocusTraversable(true);

    root.getChildren().add(canvas);
    gc = canvas.getGraphicsContext2D();

    fill(255, 255);
    noStroke();
    rect(0, 0, width, height);

    palette.show();
    stage.show();
  
  
  if(!building){
    for(ObstacleCube tile : obstacles){
        //tile.show();
      }
  }
  
  player.show();
    player.update();
    player.vel.x = tempVelx;
    frameCount++;
    System.gc();
  }

  public void mouseMoved(MouseEvent e){
    mouseX = e.getX();
    mouseY = e.getY();
  }

  public void mousePressed(MouseEvent e){
    mouseButton = e.getButton();
    mousePressed = true;
    palette.pick();
    if(mouseButton == MouseButton.PRIMARY){
      stage.pick();
    }
  }

  public void mouseReleased(MouseEvent e){
    mousePressed = false;
  }

  public void keyPressed(KeyEvent e){
    
    KeyCode keyCode = e.getCode();
    if(keyCode == KeyCode.SHIFT){
      stage.meta();
    }
    //char key = e.getCharacter().charAt(0);
    char key = keyCode.getChar().toLowerCase().charAt(0);
    if(key == '['){
      stage.rot();
    }
    if(key == 's'){
      stage.Save();
    }
    if(key == 'a'){
      stage.vi--;
    }
    if(key == 'd'){
      stage.vi++;
    }
    if(keyCode == KeyCode.UP){
      palette.sj--;
    }
    if(keyCode == KeyCode.DOWN){
      palette.sj++;
    }
    if(keyCode == KeyCode.LEFT){
      palette.si--;
      player.dir = -1;
    }
    if(keyCode == KeyCode.RIGHT){
      palette.si++;
      player.dir = 1;
    }
    if(key == 'b'){
      building = !building;
    }
    
    palette.si = constrain(palette.si, 0, 25-1);
    palette.sj = constrain(palette.sj, 0, 25-1);
    
    stage.vi = constrain(stage.vi, 0, stage.w-32);
    
    if(key == ' ' && player.onGround >= 0){
      
      player.vel.y = -4;
      player.pos.add(player.vel);
    }
    if(keyCode == KeyCode.LEFT){
        tempVelx = -2;
    }else if(keyCode == KeyCode.RIGHT){
      tempVelx = 2;
    }
  }

  public void keyReleased(KeyEvent e){
    KeyCode keyCode = e.getCode();
    if(keyCode == KeyCode.LEFT){
        tempVelx = 0;
    }else if(keyCode == KeyCode.RIGHT){
      tempVelx = 0;
    }
  }

  ArrayList<ObstacleCube> obstacles = new ArrayList<ObstacleCube>();

  class ObstacleCube{
    int x, y;
    int w=16, h=16;
    float bounciness=0;
    
    ObstacleCube(int x, int y){
      this.x = x;
      this.y = y;
    }
    
    public boolean isCollide(Player player){
      float pxl = player.pos.x;
      float pyl = player.pos.y;
      float pxm = player.pos.x+player.w;
      float pym = player.pos.y+player.h;
      return x < pxm && pxl<(x+w) && y < pym && pyl<(y+h);
    }
    
    public int collide(Player player){
      float pxl = player.pos.x;
      float pyl = player.pos.y;
      float pxm = player.pos.x+player.w;
      float pym = player.pos.y+player.h;
      float dxl = Math.abs(x-pxm);
      float dxm = Math.abs((x+w)-pxl);
      float dyl = Math.abs(y-pym);
      float dym = Math.abs((y+h)-pyl);
      
      float[] dists = {dxl, dxm, dyl, dym};
      Arrays.sort(dists);
      //println(dists[0], dists[1], dists[2], dists[3], dxl);
      if(dists[0] == dxl){
        player.pos.x -= dxl+EPSILON;
      }else if(dists[0] == dxm){
        player.pos.x += dxm+EPSILON;
      }else if(dists[0] == dyl){
        player.pos.y -= dyl+EPSILON;
      }else if(dists[0] == dym){
        player.pos.y += dym+EPSILON;
      }
      //player.pos.add(PVector.mult(player.vel, 1.0001));
      if(dists[0] == dxl){
        player.vel.x = -player.vel.x*bounciness;
      }else if(dists[0] == dxm){
        player.vel.x = -player.vel.x*bounciness;
      }else if(dists[0] == dyl){
        player.vel.y = -player.vel.y*bounciness;
      }else{
        player.vel.y = -player.vel.y*bounciness;
      }
      
      if(dists[0] == dxl)return 0;
      else if(dists[0] == dxm)return 1;
      else if(dists[0] == dyl)return 2;
      else return 3;
      
    }
    
    public void show(){
      //rect(x-player.pos.x+camx, y+camy, w, h);
    }
  }
  class Palette{
    PImage rawStage;
    PImage[] tiles = new PImage[25*25];
    int x, y;
    int si, sj;//selected

    Palette(){
      rawStage = loadImage("Sunnyland/artwork/Environment/tileset.png");
      for(int i=0; i<25; i++){
        for(int j=0; j<23; j++){
          tiles[i+j*25] = rawStage.get(i*16, j*16, 16, 16);
        }
      }
    }
    
    
    public void pick(){
      int i=(int)(mouseX/16);
      int j = (int)(mouseY/16);
      if(i < 25 && j < 25){
        this.si = i;
        this.sj = j;
        System.out.println(i+"a"+j);
      }
    }
    
    public int selected(){
      return si+sj*25;
    }
    
    public void show(){
      for(int i=0; i<25; i++){
        for(int j=0; j<23; j++){
          image(tiles[i+j*25], i*16, j*16);
        }
      }
      stroke(255, 0, 0, 255);
      noFill();
      rect(si*16, sj*16, 16, 16);
    } 
  }
  Player player;
  float g = 0.2f;
  float camx = 600;
  float camy = 0;
  float tempVelx = 0;
  PImage back;

  class Player{
    PVector pos, vel = new PVector();
    int onGround = 0;
    float w=16, h=32;//w=w/2
    PImage[] idles = new PImage[4];
    PImage[] runs = new PImage[6];
    PImage[] jumps = new PImage[2];//up=0 down=1
    int fb = 5;//frame between new sprite
    int dir = 1;
    
    Player(float x, float y){
      back = loadImage("Sunnyland/artwork/Environment/back.png");
      String Sprites = "Sunnyland/artwork/Sprites/";
      //println(loadImage("player/idle/player-idle-1.png").width);
      for(int i=1; i<=4; i++){
        idles[i-1] = loadImage(Sprites+"player/idle/player-idle-"+i+".png");
      }
      for(int i=1; i<=6; i++){
        runs[i-1] = loadImage(Sprites+"player/run/player-run-"+i+".png");
      }
      for(int i=1; i<=2; i++){
        jumps[i-1] = loadImage(Sprites+"player/jump/player-jump-"+i+".png");
      }
      this.pos = new PVector(x, y);
    }
    
    public void update(){
      vel.add(new PVector(0, g));
      boolean coli = false;
      for(ObstacleCube tile : obstacles){
        if(tile.isCollide(this)){ 
          int stat = tile.collide(this);
          if(stat == 2)onGround=1;
          coli = true;
        }
      }
      if(!coli)onGround--;
      pos.add(vel);
    }
    
    public void show(){
      //image(back, (int)(0-(player.pos.x/2)%width), 0, width, height);//should show first
      //image(back, (int)(width-(player.pos.x/2)%width), 0, width, height);
      
      pushMatrix();
      if(dir==1){
        translate(camx-w/2, pos.y+camy);
      }else{
        translate(camx-w/2+w*2, pos.y+camy);
        scale( -1, 1 );
      }
      if(onGround >= 0){
        if(vel.x == 0){
          image(idles[(frameCount/fb)%4],0,0, (int)w*2, (int)h);
        }else{
          image(runs[(frameCount/fb)%6],0,0, (int)w*2, (int)h);
        }
      }else{
        if(vel.y > 0){
          image(jumps[1],0,0, (int)w*2, (int)h);
        }else{
          image(jumps[0],0,0, (int)w*2, (int)h);
        }
      }
      popMatrix();
      //rect(camx, pos.y+camy, w, h);
    }
  }
  class Stage{
    int x, y;
    int si, sj;
    int w, h;
    int vi;//viewport offset
    int[][] tiles;
    int[][] metas;//0=enable,1=disable;
    int[][] rots;
    
    Stage(int x, int y, int w, int h){
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      tiles = new int[w][h];
      rots = new int[w][h];
      metas = new int[w][h];
    }
    
    public void pick(){
      if(toIndex())tiles[si][sj] = palette.selected();
    }
    
    public void rot(){
      if(toIndex())rots[si][sj] = (rots[si][sj]+1)%4;;
    }
    
    public void meta(){
      if(toIndex())metas[si][sj] = (1-metas[si][sj]);
    }
    
    public void Load(){
      String[] strss = new String[h];
      strss = loadStrings("stage.txt");
      for(int j=0; j<strss.length; j++){
        String[] strs = strss[j].split(",");
        for(int i=0; i<strs.length; i++){
          String str = strs[i];
          if(str.equals("") || str.equals("\n"))continue;
          String[] datas = str.split("/");
          tiles[i][j] =Integer.parseInt(datas[0]);
          rots[i][j] = Integer.parseInt(datas[1]);
          metas[i][j] = Integer.parseInt(datas[2]);
          if(tiles[i][j]!=0 && metas[i][j] == 0)obstacles.add(new ObstacleCube(i*16, j*16));
        } 
      }
      System.out.println("LOAD");
    }
    
    public void Save(){
      String[] strs = new String[h];
      for(int j=0; j<h; j++){
        String str = "";
        for(int i=0; i<w; i++){
          str += (tiles[i][j])+"/"+(rots[i][j])+"/"+(metas[i][j]);
          
          if(i!=w-1)str += ',';
        } 
        strs[j] = str;
        //println(str);
      }
      saveStrings("stage.txt", strs);
      System.out.println("SAVED");
    }
    
    
    
    public boolean toIndex(){
      int i= (int)((mouseX-x)/16+vi);
      int j = (int)((mouseY-y)/16);
      if(i < 32+vi && j < h && vi<=i&& 0<=j){
        this.si = i;
        this.sj = j;
        return true;
      }
      return false;
    }
    
    public void show(){//SHOULD RESTORE for SHIFT
      stroke(0, 10);
      noFill();
      
      pushMatrix();
      if(building) translate(x, y);
      else translate(-player.pos.x+camx, 0);
      int ilim = 32;
      if(!building)ilim = w;
      for(int i=0; i<ilim; i++){
        for(int j=0; j<h; j++){
          
          pushMatrix();
          translate(i*16+8  , j*16+8);
          rotate((float)(rots[i+vi][j]*Math.PI/2));
          if(tiles[i+vi][j]!=0)  image(palette.tiles[tiles[i+vi][j]], -8, -8);
          if(metas[i+vi][j]==1)fill(255, 0, 0, 100);
          else noFill();
          if(building)rect(-8, -8, 16, 16);
          popMatrix();
        }
      }
      stroke(255, 0, 0, 255);
      noFill();
      rect((si-vi)*16, sj*16, 16, 16);
      popMatrix();
    }
  }
}
