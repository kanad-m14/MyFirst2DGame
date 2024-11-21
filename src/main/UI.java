package main;

import entity.Entity;
import objects.Heart;
import objects.Mana;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {

    GamePanel gp;
    Graphics2D g2;
    Font maruMonica, purisaB;
    BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = " ";
    public int commandNum = 0;
    public int commandEndNum = 0;
    public int slotCol = 0;
    public int slotRow = 0;// Indicates cursor's current position in the inventory window
    int subState = 0;
    boolean fullScreenOn = false;

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

        // MANA
        Entity mana = new Mana(gp);
        crystal_full = mana.image;
        crystal_blank = mana.image2;
    }

    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }

    public void draw(Graphics2D g2) {

        // We do this, so we can use g2 as a method in this class
        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        // GAME STATE
        if(gp.gameState == gp.titleState) {
            message.clear();
            messageCounter.clear();
            drawTitleScreen();
            drawPlayerMoving();
        }

        if(gp.gameState == gp.playState) {
            drawPlayerLife();
            drawMessage();
        }

        if(gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
        }

        if(gp.gameState == gp.dialogueState) {
            drawPlayerLife();
            drawDialogueScreen();
        }

        if(gp.gameState == gp.characterState) {
            drawPlayerLife();
            drawCharacterScreen();
            drawInventory();
        }

        if(gp.gameState == gp.optionState) {
            drawOptionScreen();
        }

        if(gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }

        if(gp.gameState == gp.gameEndState) {
            drawGameEndScreen();
        }
    }

    public void drawPlayerLife() {

        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        // DRAW BLANK LIFE
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

        x = gp.tileSize / 2 - 5;
        y = (int)(gp.tileSize * 2 - 1.5);
        i = 0;

        // DRAW PLAYER MANA
        while(i < gp.player.maxMana) {
            g2.drawImage(crystal_blank, x, y, null);
            i++;
            x += 35;
        }

        x = gp.tileSize / 2 - 5;
        y = (int)(gp.tileSize * 2 - 1.5);
        i = 0;

        while (i < gp.player.mana) {
            g2.drawImage(crystal_full, x, y, null);
            i++;
            x += 35;
        }
    }

//    public void drawMessage() {
//
//        int messageX = gp.tileSize;
//        int messageY = gp.tileSize * 4;
//        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
//
//        for(int i = 0; i < message.size(); i++) {
//
//            if(message.get(i) != null) {
//
//                g2.setColor(Color.black);
//                g2.drawString(message.get(i), messageX + 2, messageY + 2);
//                g2.setColor(Color.white);
//                g2.drawString(message.get(i), messageX, messageY);
//
//                int counter = messageCounter.get(i) + 1; // messageCounter++
//                messageCounter.set(i, counter); // set the counter to the array
//                messageY += 50;
//
//                if(messageCounter.get(i) > 180) {
//                    message.remove(i);
//                    messageCounter.remove(i);
//                }
//            }
//        }
//    }

    public void drawMessage() {
        int messageX = gp.tileSize;
        int messageY = gp.tileSize * 4; // Starting Y position for the first message
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        int alpha = 255;
        // Iterate through the messages
        for (int i = 0; i < message.size(); i++) {
            if (message.get(i) != null) {
                // Calculate the alpha value for fading effect
                alpha = 255;
                 // Fully opaque
                if (messageCounter.get(i) > 180) {
                    alpha = 255 - (messageCounter.get(i) - 180) * (255 / 60); // Fade out over 60 frames
                    if (alpha < 0) {
                        alpha = 0; // Ensure alpha doesn't go below 0
                    }
                }

                // Set the color with the calculated alpha
                g2.setColor(new Color(255, 255, 255, alpha)); // White color with fading alpha
                g2.drawString(message.get(i), messageX, messageY + (i * 50)); // Draw message at its position

                // Increment the counter and update it
                int counter = messageCounter.get(i) + 1; // messageCounter++
                messageCounter.set(i, counter); // set the counter to the array

                // Remove the message if the counter exceeds 240 (180 + 60 for fading out)
                if (messageCounter.get(i) > 240) {
                    message.remove(i);
                    messageCounter.remove(i);
                    i--; // Decrement i to account for the removed message
                }
            }
        }
    }

    public void drawTitleScreen() {

        g2.setColor(new Color(85, 190, 255));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
        String text = "The Indigo Trail";
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
        y += (int) (gp.tileSize * 1.3);
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }


        text = "QUIT";
        x = getXforCenteredText(text);
        y += (int) (gp.tileSize * 1.3);
        g2.drawString(text, x, y);
        if(commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y);
        }

    }

//    public void drawPlayerMoving() {
//
//        if (direction.equals("right")) {
//            playerScreenX += speed;
//            if (playerScreenX >= gp.screenWidth) { // Check boundary to change direction
//                direction = "left";
//            }
//        }
//        else if (direction.equals("left")) {
//            playerScreenX -= speed;
//            if (playerScreenX <= -gp.tileSize) { // Check boundary to change direction
//                direction = "right";
//            }
//        }
//
//        spriteCounter++;
//        if(spriteCounter % 15 == 0) {
//            if(spriteNum == 1) {
//                spriteNum = 2;
//            }
//            else if(spriteNum == 2) {
//                spriteNum = 1;
//            }
//        }
//
//        gp.player.getPlayerImage();
//        BufferedImage image = null;
//        switch (direction) {
//            case "left":
//                if(spriteNum == 1) {
//                    image = gp.player.left1;
//                }
//                if(spriteNum == 2) {
//                    image = gp.player.left2;
//                }
//                break;
//            case "right":
//                if(spriteNum == 1) {
//                    image = gp.player.right1;
//                }
//                if(spriteNum == 2) {
//                    image = gp.player.right2;
//                }
//                break;
//        }
//
//        int playerScreenY = 216;
//        g2.drawImage(image, playerScreenX, playerScreenY, null);
//        if(spriteCounter > 60) {
//            spriteCounter = 0;
//        }
//    }

    public void drawPlayerMoving() {
        // Define constants for boundary checks and sprite timing
        final int LEFT_BOUNDARY = -gp.tileSize;
        final int RIGHT_BOUNDARY = gp.screenWidth;
        final int SPRITE_CHANGE_RATE = 15;
        final int SPRITE_RESET_LIMIT = 60;

        // Move player based on direction
        if (direction.equals("right")) {
            playerScreenX += speed;
            if (playerScreenX >= RIGHT_BOUNDARY) {
                direction = "left";
            }
        } else if (direction.equals("left")) {
            playerScreenX -= speed;
            if (playerScreenX <= LEFT_BOUNDARY) {
                direction = "right";
            }
        }

        // Update sprite counter and toggle sprite number
        spriteCounter++;
        if (spriteCounter % SPRITE_CHANGE_RATE == 0) {
            spriteNum = (spriteNum == 1) ? 2 : 1; // Toggle spriteNum
        }

        // Select the appropriate image based on direction and spriteNum
        BufferedImage image = null;
        if (direction.equals("left")) {
            image = (spriteNum == 1) ? gp.player.left1 : gp.player.left2;
        } else if (direction.equals("right")) {
            image = (spriteNum == 1) ? gp.player.right1 : gp.player.right2;
        }

        // Draw the player image at the specified Y position
        int playerScreenY = 216;
        g2.drawImage(image, playerScreenX, playerScreenY, null);

        // Reset spriteCounter if it exceeds the limit
        if (spriteCounter > SPRITE_RESET_LIMIT) {
            spriteCounter = 0;
        }
    }

//    public void drawPauseScreen() {
//
//        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80f));
//        String text = "PAUSED";
//        int x = getXforCenteredText(text);
//        int y = gp.screenHeight/2;
//
//        g2.drawString(text, x, y);
//    }

    public void drawPauseScreen() {
        // Draw a semi-transparent dark overlay
        g2.setColor(new Color(0, 0, 0, 150)); // Black with 150 alpha for transparency
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        // Draw a decorative border (optional)
        g2.setColor(Color.white);
        g2.drawRect(50, 50, gp.screenWidth - 100, gp.screenHeight - 100); // Simple border

        // Set font for the pause text
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80f));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight / 2 - 50; // Slightly above center

        // Draw the pause text
        g2.setColor(Color.white);
        g2.drawString(text, x, y);

        // Set font for additional instructions
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40f));
        String resumeText = "Press 'ESC' to Resume";
        x = getXforCenteredText(resumeText);
        int yInstructions = y + 60; // Position below the pause text
        g2.drawString(resumeText, x, yInstructions);

        // Optionally, add more instructions
        String menuText = "Press 'M' for Menu";
        x = getXforCenteredText(menuText);
        int yMenu = yInstructions + 40; // Position below the resume text
        g2.drawString(menuText, x, yMenu);

        // Optionally, you can add some pixel art or decorative elements here
        // For example: g2.drawImage(pixelArtImage, xPosition, yPosition, null);
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

    public void drawCharacterScreen() {

        // Create a frame
        final int frameX = gp.tileSize;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize * 5;
        final int frameHeight = gp.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Text
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        int textX = frameX + 20;
        int textY = frameY + gp.tileSize;
        final int lineHeight = 37;

        // NAMES
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Mana", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Coin", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Shield", textX, textY);

        // Values
        int tailX = frameX + frameWidth - 30;
        textY = frameY + gp.tileSize; // Resetting the textY
        String value;

        value = String.valueOf(gp.player.level);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.strength);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.attack);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.defense);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.exp + "/" + gp.player.nextLevelExp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gp.player.coin);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight - 3;

        g2.drawImage(gp.player.currentWeapon.down1, tailX - 40, textY - 14, null);
        textY += gp.tileSize + 2;

        g2.drawImage(gp.player.currentShield.down1, tailX - 40, textY - 14, null);
    }

    public void drawGameOverScreen() {

        // Draw the background for the Game Over screen
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        g2.setColor(Color.BLACK);
        text = "Game Over";
        x = getXforCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text, x + 4, y + 4);

        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));

        text = "RETRY";
        x = getXforCenteredText(text);
        y += (int) (gp.tileSize * 3.4);
        g2.drawString(text, x, y);
        if(commandEndNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "BACK TO TITLE SCREEN";
        x = getXforCenteredText(text);
        y += (int) (gp.tileSize * 1.3);
        g2.drawString(text, x, y);
        if(commandEndNum == 1) {
            g2.drawString(">", x - gp.tileSize, y);
        }

    }

    public void drawGameEndScreen() {

        // Draw the background for the Game Over screen
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        g2.setColor(Color.BLACK);
        text = "Congratulations";
        x = getXforCenteredText(text);
        y = gp.tileSize * 4;
        g2.drawString(text, x + 4, y + 4);

        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));

        text = "BACK TO TITLE SCREEN";
        x = getXforCenteredText(text);
        y += (int) (gp.tileSize * 5);
        g2.drawString(text, x, y);
        g2.drawString(">", x - gp.tileSize, y);
    }

    public void drawOptionScreen() {

        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32f));

        // SUB WINDOW
        int frameX = gp.tileSize * 4;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 8;
        int frameHeight = gp.tileSize * 10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch(subState) {
            case 0: optionsTop(frameX, frameY); break;
            case 1: break;
            case 2: controlSubState(frameX, frameY); break;
            case 3: endGame(frameX, frameY); break;
        }

        gp.keyH.enterPressed = false;
    }

    public void optionsTop(int frameX, int frameY) {

        int textX;
        int textY;

        // STRING
        String text = "Options";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        // FULL SCREEN
        textX = frameX + gp.tileSize;
        textY += gp.tileSize * 2;
        text = "Full Screen";
        g2.drawString(text, textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX - 24, textY);
            if(gp.keyH.enterPressed) {
                fullScreenOn = !fullScreenOn;
            }
        }

        // MUSIC
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if(commandNum == 1) {
            g2.drawString(">", textX - 24, textY);
        }

        // SOUND EFFECT
        textY += gp.tileSize;
        g2.drawString("SE", textX, textY);
        if(commandNum == 2) {
            g2.drawString(">", textX - 24, textY);
        }

        // CONTROLS
        textY += gp.tileSize;
        g2.drawString("Controls", textX, textY);
        if(commandNum == 3) {
            g2.drawString(">", textX - 24, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 2;
                commandNum = 0;
            }
        }

        // QUIT
        textY += gp.tileSize;
        g2.drawString("Quit", textX, textY);
        if(commandNum == 4) {
            g2.drawString(">", textX - 24, textY);
            if(gp.keyH.enterPressed) {
                subState = 3;
                commandNum = 0;
            }
        }

        // BACK
        textY += gp.tileSize * 2;
        g2.drawString("Back", textX, textY);
        if(commandNum == 5) {
            g2.drawString(">", textX - 24, textY);
            if(gp.keyH.enterPressed) {
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }

        // FULL SCREEN CHECKBOX
        textX = (int)(frameX + gp.tileSize * 4.5);
        textY = frameY + gp.tileSize * 2 + 27;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, 24, 24);
        if(fullScreenOn) {
            g2.fillRect(textX, textY, 24, 24);
        }

        // MUSIC VOLUME BOX
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);

        // SOUND EFFECT BOX
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        int soundEffectWidth = 24 * gp.soundEffect.volumeScale;
        g2.fillRect(textX, textY, soundEffectWidth, 24);

    }

    public void controlSubState(int frameX, int frameY) {

        int textX;
        int textY;

        // STRING
        String text = "Controls";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);

        textX = frameX + gp.tileSize - 10;
        textY += gp.tileSize;
        g2.drawString("Move", textX, textY); textY += gp.tileSize;
        g2.drawString("Attack/Confirm", textX, textY); textY += gp.tileSize;
        g2.drawString("Shoot", textX, textY); textY += gp.tileSize;
        g2.drawString("Character Screen", textX, textY); textY += gp.tileSize;
        g2.drawString("Pause Screen", textX, textY); textY += gp.tileSize;
        g2.drawString("Use Item", textX, textY);

        textX = frameX + gp.tileSize * 6 - 24;
        textY = frameY + gp.tileSize * 2;
        g2.drawString("WASD", textX, textY); textY += gp.tileSize;
        g2.drawString("Enter", textX, textY); textY += gp.tileSize;
        g2.drawString("F", textX, textY); textY += gp.tileSize;
        g2.drawString("C", textX, textY); textY += gp.tileSize;
        g2.drawString("P", textX, textY); textY += gp.tileSize;
        g2.drawString("Enter", textX, textY);

        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize * 9;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX - 24, textY);
            if(gp.keyH.enterPressed) {
                subState = 0;
                commandNum = 3;
            }
        }
    }

    public void endGame(int frameX, int frameY) {

        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize * 3;

        currentDialogue = "Quit the Game and \nreturn to the Title Screen?";

        for(String line: currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        // YES
        String text = "Yes";
        textX = getXforCenteredText(text);
        textY += gp.tileSize * 3;
        g2.drawString(text, textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX - 24, textY);
            if(gp.keyH.enterPressed) {
                subState = 0;
                gp.gameState = gp.titleState;
                gp.stopMusic();
                gp.restart();
            }
        }

        text = "No";
        textX = getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if(commandNum == 1) {
            g2.drawString(">", textX - 24, textY);
            if(gp.keyH.enterPressed) {
                subState = 0;
                commandNum = 4;
            }
        }
    }

    public void drawInventory() {

        // FRAME
        int frameX = gp.tileSize * 9;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize * 6;
        int frameHeight = gp.tileSize * 5;
        drawSubWindow(frameX,frameY,frameWidth,frameHeight);

        // SORT
        final int slotXStart = frameX + 20;
        final int slotYStart = frameY + 20;
        int slotX = slotXStart;
        int slotY = slotYStart;
        int slotSize = gp.tileSize + 3;

        // DRAW PLAYER ITEMS
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 5 && (j + (i * 5)) < gp.player.inventory.size(); j++)
            {
                if(gp.player.inventory.get(j + (i * 5)) == gp.player.currentWeapon ||
                        gp.player.inventory.get(j + (i * 5)) == gp.player.currentShield)
                {
                    g2.setColor(new Color(240,190,90));
                    g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize,10,10);
                }

                g2.drawImage(gp.player.inventory.get(j + (i * 5)).down1, slotX, slotY, null);
                slotX += slotSize;
            }
            slotX = slotXStart;
            slotY += slotSize;
        }

//        for(int i = 0; i < gp.player.inventory.size(); i++)
//        {
//            g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
//
//            slotX += slotSize;
//
//            if(i == 4 || i == 9 || i == 14 || i == 20)
//            {
//                slotX = slotXStart;
//                slotY += slotSize;
//            }
//        }

        // CURSOR
        int cursorX = slotXStart + (slotSize * slotCol);
        int cursorY = slotYStart + (slotSize * slotRow);
        int cursorWidth = gp.tileSize;
        int cursorHeight = gp.tileSize;

        // DRAW CURSOR
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

        // DESCRIPTION FRAME
        int dFrameX = frameX;
        int dFrameY = frameY + frameHeight;
        int dFrameWidth = frameWidth;
        int dFrameHeight = gp.tileSize * 3 + 20;


        //DRAW DESCRIPTION TEXT
        int textX = dFrameX + 20;
        int textY = dFrameY + gp.tileSize;
        g2.setFont(g2.getFont().deriveFont(28F));

        int itemIndex = getItemIndexOnSlot();

        if(itemIndex < gp.player.inventory.size())
        {
            drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
            for(String line: gp.player.inventory.get(itemIndex).description.split("\n"))
            {
                g2.drawString(line, textX, textY);
                textY += 32;
            }
        }
    }

    public int getItemIndexOnSlot() {
        int itemIndex = slotCol + (slotRow * 5);
        return itemIndex;
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

    public int getXforAlignToRightText(String text, int tailX) {

        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;

        return x;
    }
}
