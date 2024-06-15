package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;

public class HelloApplication extends Application {

    private Pane heartPane ;
    private Rectangle player ;
    private Rectangle enemyBox ;
    private Pane boxPane;
    private ArrayList<Rectangle> rectangles;
    private static final double SHIFT_AMOUNT = 50;
    private Button runBtn ;
    private Button attackBtn ;
    private boolean flag = false ;
    private int damageNum = 0 ;

    @Override
    public void start(Stage stage) {

        boxPane = new Pane();
        rectangles = new ArrayList<>();

        //set background
        Rectangle backGround = new Rectangle(500 , 700);
        Image bgImage = new Image("F:\\projects\\ground\\demo\\src\\main\\resources\\images\\background.png") ;
        ImagePattern imagePattern = new ImagePattern(bgImage) ;
        backGround.setFill(imagePattern);

        //create player
        player = new Rectangle(195 , 250 , 60 , 100 ) ;
        Image bgPlayer = new Image("F:\\projects\\ground\\demo\\src\\main\\resources\\images\\Warrior.png") ;
        ImagePattern playerImagePattern = new ImagePattern(bgPlayer);
        player.setFill(playerImagePattern);

        //create hearts
        Rectangle hearts ;
        heartPane = new Pane();
        for (int i = 0; i < 3; i++) {
            hearts = new Rectangle((i * 50) , 0 , 50 , 50);
            Image bgHeart = new Image("F:\\projects\\ground\\demo\\src\\main\\resources\\images\\heart.png") ;
            ImagePattern heartImagePattern = new ImagePattern(bgHeart);
            hearts.setFill(heartImagePattern);
            heartPane.getChildren().add(hearts) ;
        }

        //create ground
        int width = 0 ;
        Rectangle ground ;
        Group root = new Group();
        for (int i = 0; i < 10; i++) {
            ground = new Rectangle(width+(i * 50) , 350 , 50 , 50) ;
            Image groundBgImage = new Image("F:\\projects\\ground\\demo\\src\\main\\resources\\images\\brick3.png") ;
            ImagePattern groundImagePattern = new ImagePattern(groundBgImage) ;
            ground.setFill(groundImagePattern);
            boxPane.getChildren().add(ground);
            rectangles.add(ground);
        }

        //create run button
        runBtn = new Button("");
        runBtn.setPrefWidth(100);
        runBtn.setPrefHeight(100);
        runBtn.setLayoutX(125);
        runBtn.setLayoutY(580);

        //run button background
        Image runBtnImage = new Image("F:\\projects\\ground\\demo\\src\\main\\resources\\images\\run.png");
        BackgroundImage runBtnBgImage = new BackgroundImage(runBtnImage , BackgroundRepeat.NO_REPEAT , BackgroundRepeat.NO_REPEAT , BackgroundPosition.CENTER , BackgroundSize.DEFAULT);
        Background background = new Background(runBtnBgImage) ;
        runBtn.setBackground(background);


        //runButton event
        runBtn.setOnMouseClicked(event -> {

            if (boxPane.getChildren().indexOf(enemyBox) < 5)
                flag = false ;

            runBtn.setDisable(true);

            jumpPlayer();

            int randomInt = (int)(Math.random() * 3);

            if (randomInt < 2){addBox();}

            else if (randomInt == 2) {
                if (!flag) {
                    addEnemy();
                }
                else
                    addBox();
            }

            rectangles.remove(0) ;
            boxPane.getChildren().remove(0);

            if (boxPane.getChildren().indexOf(enemyBox) == 4){
                damageNum++ ;
                heartPane.getChildren().remove(3-damageNum);
            }

            if (damageNum == 3){
                gameOverScreen();
            }
        });

        //create attack button
        attackBtn = new Button("");
        attackBtn.setPrefWidth(100);
        attackBtn.setPrefHeight(100);
        attackBtn.setLayoutX(275);
        attackBtn.setLayoutY(580);

        //attack button background
        Image attackBtnImage = new Image("F:\\projects\\ground\\demo\\src\\main\\resources\\images\\attack.png");
        BackgroundImage attackBtnBgImage = new BackgroundImage(attackBtnImage , BackgroundRepeat.NO_REPEAT , BackgroundRepeat.NO_REPEAT , BackgroundPosition.CENTER , BackgroundSize.DEFAULT);
        Background attackBackground = new Background(attackBtnBgImage) ;
        attackBtn.setBackground(attackBackground);

        //attack button event
        attackBtn.setOnMouseClicked(event -> {

            if (boxPane.getChildren().indexOf(enemyBox) == 5 || boxPane.getChildren().indexOf(enemyBox) == 4)
                attackButton();
        });

        //show screen
        Group group = new Group(backGround , heartPane, player , root , boxPane , runBtn , attackBtn) ;
        Scene scene = new Scene(group ,500, 700);
        stage.setTitle("Hoppenhelm!");
        stage.setScene(scene);
        stage.show();
    }

    // making player movable
    public void jumpPlayer() {

        double originalY = player.getLayoutY();
        double newY = originalY - 40;  // move player up 50px

        // upper animation
        Timeline moveTimeline = new Timeline(
                new KeyFrame(Duration.millis(125), // animation time
                        new KeyValue(player.layoutYProperty(), newY))
        );

        // downer animation
        Timeline returnTimeline = new Timeline(
                new KeyFrame(Duration.millis(125), // animation time
                        new KeyValue(player.layoutYProperty(), originalY))
        );

        // making continues animation
        SequentialTransition sequentialTransition = new SequentialTransition(moveTimeline, returnTimeline);
        sequentialTransition.play();

    }

    // add box
    public void addBox() {
        // Create a new rectangle with specific properties
        Rectangle StandardBox = new Rectangle(500 , 350 , 50, 50);
        Image newBox = new Image("F:\\projects\\ground\\demo\\src\\main\\resources\\images\\brick2.png") ;
        ImagePattern newBoxImagePattern = new ImagePattern(newBox) ;
        StandardBox.setFill(newBoxImagePattern);

        // Add the new rectangle to the pane and list
        boxPane.getChildren().add(StandardBox);
        rectangles.add(StandardBox);

        // shift existing rectangles to the left
        for (Rectangle rect : rectangles) {
            rect.setLayoutX(rect.getLayoutX() - SHIFT_AMOUNT);
        }
        runBtn.setDisable(false);
    }

    //add enemy
    public void addEnemy() {

        //enemy created
        flag = true ;

        // Create a new rectangle with specific properties
        enemyBox = new Rectangle(500 , 300 , 50, 50);
        Image newBox = new Image("F:\\projects\\ground\\demo\\src\\main\\resources\\images\\ghost.png") ;
        ImagePattern newBoxImagePattern = new ImagePattern(newBox) ;
        enemyBox.setFill(newBoxImagePattern);

        Rectangle enemyGroundBox = new Rectangle(500 , 350 , 50, 50);
        Image enemyGround = new Image("F:\\projects\\ground\\demo\\src\\main\\resources\\images\\brick2.png") ;
        ImagePattern enemyGroundImagePattern = new ImagePattern(enemyGround) ;
        enemyGroundBox.setFill(enemyGroundImagePattern);

        // Add the new rectangle to the pane and list
        boxPane.getChildren().add(enemyBox);
        rectangles.add(enemyBox);
        boxPane.getChildren().add(enemyGroundBox);
        rectangles.add(enemyGroundBox);

        // Shift existing rectangles to the left
        for (Rectangle rect : rectangles) {
            rect.setLayoutX(rect.getLayoutX() - SHIFT_AMOUNT);
        }

        //disable run button
        runBtn.setDisable(false);
    }

    //attack button event
    public void attackButton(){

        if (boxPane.getChildren().indexOf(enemyBox) == 5){

            boxPane.getChildren().remove(5);
            flag = false ;
        }
        else if (boxPane.getChildren().indexOf(enemyBox) == 4) {

            boxPane.getChildren().remove(4);
            flag = false ;
        }
    }

    //show gameOver screen & stop the game
    public void gameOverScreen(){

        //make gameOver screen
        Rectangle gameOver = new Rectangle(500 , 700) ;
        Image gameOverBg = new Image("F:\\projects\\ground\\demo\\src\\main\\resources\\images\\gameover.png");
        ImagePattern gameOverImagePattern = new ImagePattern(gameOverBg) ;
        gameOver.setFill(gameOverImagePattern);

        //show gameOver screen & stop the game
        Group GameOver = new Group(gameOver) ;
        Scene gameOverScreen = new Scene(GameOver ,500, 700);
        Stage stage = new Stage();
        stage.setTitle("GameOver!");
        stage.setScene(gameOverScreen);
        stage.show();
        runBtn.setDisable(true);
        attackBtn.setDisable(true);
    }

    //main function
    public static void main(String[] args) {
        launch();
    }
}