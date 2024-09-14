package main;

import entity.Entity;
import objects.Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica, purisaB;
    BufferedImage heart_full, heart_half, heart_blank;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public boolean gameFinished = false;
    public String currentDialogue = " ";
    public int commandNum = 0;

    // Player moving on Title screen
    private String direction = "right";
    private int spriteCounter = 0;
    private int spriteNum = 1;
    private int speed = 4;
    private int playerScreenX = -48;

    public UI(GamePanel gp) {

        this.gp = gp;

        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream("font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getClassLoader().getResourceAsStream("font/Purisa Bold.ttf");
            purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // CREATE HUD OBJECT
        Entity heart = new Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void showMessage(String text) {

        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {

        // We do this, so we can use g2 as a method in this class
        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        // GAME STATE
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
            drawPlayerMoving();
        }

        if(gp.gameState == gp.playState) {
            drawPlayerLife();
        }

        if(gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }

        if(gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }
    }

    public void drawPlayerLife() {

        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        // DRAW MAX LIFE
        while(i < gp.player.maxLife / 2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        // RESET
        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;

        // DRAW CURRENT LIFE
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if(i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }
    }

    public void drawTitleScreen() {

        g2.setColor(new Color(85, 201, 255));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "Blue Boy Adventure";
        int x = getXforCenteredText(text);
        int y = gp.tileSize * 3;

        // SHADOW
        g2.setColor(Color.black);
        g2.drawString(text, x + 4, y + 4);

        // MAIN COLOR
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // BLUE BOY IMAGE
//        x = gp.screenWidth / 2 - (gp.tileSize);
//        y += gp.tileSize * 2 - (gp.tileSize / 2);
//        g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        // MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));

        text = "NEW GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "LOAD GAME";
        x = getXforCenteredText(text);
        y += gp.tileSize * 1.3;
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }


        text = "QUIT";
        x = getXforCenteredText(text);
        y += gp.tileSize * 1.3;
        g2.drawString(text, x, y);
        if(commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y);
        }

    }

    public void drawPlayerMoving() {

        if (direction.equals("right")) {
            playerScreenX += speed;
            if (playerScreenX >= gp.screenWidth) { // Check boundary to change direction
                direction = "left";
            }
        }
        else if (direction.equals("left")) {
            playerScreenX -= speed;
            if (playerScreenX <= -gp.tileSize) { // Check boundary to change direction
                direction = "right";
            }
        }

        spriteCounter++;
        if(spriteCounter % 15 == 0) {
            if(spriteNum == 1) {
                spriteNum = 2;
            }
            else if(spriteNum == 2) {
                spriteNum = 1;
            }
        }

        gp.player.getPlayerImage();
        BufferedImage image = null;
        switch (direction) {
            case "left":
                if(spriteNum == 1) {
                    image = gp.player.left1;
                }
                if(spriteNum == 2) {
                    image = gp.player.left2;
                }
                break;
            case "right":
                if(spriteNum == 1) {
                    image = gp.player.right1;
                }
                if(spriteNum == 2) {
                    image = gp.player.right2;
                }
                break;
        }

        int playerScreenY = 216;
        g2.drawImage(image, playerScreenX, playerScreenY, null);
        if(spriteCounter > 60) {
            spriteCounter = 0;
        }
    }

    public void drawPauseScreen() {

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80f));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;

        g2.drawString(text, x, y);
    }

    public void drawDialogueScreen() {

        // Window
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32f));
        x += gp.tileSize;
        y += gp.tileSize;

        for(String line : currentDialogue.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0, 0,0, 210); // a determines the transparency/ opacity
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25 );
    }

    // Whenever we want to put the text in center, we call this method
    public int getXforCenteredText(String text) {

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;

        return x;
    }
}
