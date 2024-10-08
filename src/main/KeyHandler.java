package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    // used in player class to determine the movement of player
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
    GamePanel gp;

    // DEBUG
    public boolean checkDrawTime = false;

    public KeyHandler(GamePanel gp) {

        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        // Title State
        if(gp.gameState == gp.titleState) {
            titleState(code);
        }

        // Play State
        else if(gp.gameState == gp.playState) {
            playState(code);
        }

        // Paused State
        else if(gp.gameState == gp.pauseState) {
            pauseState(code);
        }

        // Dialogue State
        else if(gp.gameState == gp.dialogueState) {
            dialogueState(code);
        }

        // Character State
        else if(gp.gameState == gp.characterState) {
            characterState(code);
        }
    }

    public void titleState(int code) {

        if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            if(gp.ui.commandNum == 3) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            if(gp.ui.commandNum == -1) {
                gp.ui.commandNum = 2;
            }
        }
        if(code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.playMusic(0);
            }
            if(gp.ui.commandNum == 1) {
                // load game (add later)
            }
            if(gp.ui.commandNum == 2) {
                System.exit(0);
            }
        }
    }

    public void playState(int code) {

        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }

        // Pausing the Game
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.pauseState;
            gp.stopMusic();
        }

        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.characterState;
        }

        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        // DEBUG
        if (code == KeyEvent.VK_T) {
            if(checkDrawTime == false) {
                checkDrawTime = true;
            } else if (checkDrawTime == true) {
                checkDrawTime = false;
            }
        }
    }

    public void pauseState(int code) {

        // Resuming the game
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
            gp.playMusic(0);
        }
    }

    public void dialogueState(int code) {

        if(code == KeyEvent.VK_ENTER) {
            gp.gameState = gp.playState;
        }
    }

    public void characterState(int code) {

        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = false;
        }
    }
}
