package main;

import objects.Key;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    // used in player class to determine the movement of player
    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;
    GamePanel gp;

    // DEBUG
    public boolean showDebugText = false;

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

        else if(gp.gameState == gp.optionState) {
            optionState(code);
        }

        else if(gp.gameState == gp.gameOverState) {
            gameOverState(code);
        }

        else if(gp.gameState == gp.gameEndState) {
            gameEndState(code);
        }
    }

    public void titleState(int code) {

        if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            gp.playSoundEffect(9);
            if(gp.ui.commandNum == 3) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            gp.playSoundEffect(9);
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
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.pauseState;
            gp.pauseMusic();
        }

        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.characterState;
        }

        if (code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        if (code == KeyEvent.VK_F) {
            shotKeyPressed = true;
        }

        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.optionState;
//            gp.pauseMusic();
        }

        // DEBUG
        if (code == KeyEvent.VK_T) {
            if(!showDebugText) {
                showDebugText = true;
            } else {
                showDebugText = false;
            }
        }

        if (code == KeyEvent.VK_R) {
            gp.tileM.loadMap("maps/worldV2.txt");
        }
    }

    public void pauseState(int code) {

        // Resuming the game
        if (code == KeyEvent.VK_P) {
            gp.gameState = gp.playState;
            gp.resumeMusic();
        }
    }

    public void dialogueState(int code) {

        if(code == KeyEvent.VK_ENTER) {
            gp.gameState = gp.playState;
//            gp.resumeMusic();
        }
    }

    public void characterState(int code) {

        if(code == KeyEvent.VK_C) {
            gp.gameState = gp.playState;
        }

        if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            if(gp.ui.slotRow != 0) {
                gp.ui.slotRow--;
                gp.playSoundEffect(9);
            }
        }

        if(code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            if(gp.ui.slotCol != 0) {
                gp.ui.slotCol--;
                gp.playSoundEffect(9);
            }
        }

        if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            if(gp.ui.slotRow != 3) {
                gp.ui.slotRow++;
                gp.playSoundEffect(9);
            }
        }

        if(code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            if(gp.ui.slotCol != 4) {
                gp.ui.slotCol++;
                gp.playSoundEffect(9);
            }
        }

        if(code == KeyEvent.VK_ENTER) {
            gp.player.selectItem();
        }
    }

    public void gameOverState(int code) {

        if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            gp.ui.commandEndNum++;
            if(gp.ui.commandEndNum == 2) {
                gp.ui.commandEndNum = 0;
            }
            gp.playSoundEffect(9);
        }
        if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            gp.ui.commandEndNum--;
            if(gp.ui.commandEndNum == -1) {
                gp.ui.commandEndNum = 1;
            }
            gp.playSoundEffect(9);
        }
        if(code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandEndNum == 0) {
                gp.gameState = gp.playState;
                gp.retry();
                gp.playMusic(0);
            }
            if(gp.ui.commandEndNum == 1) {
                gp.gameState = gp.titleState;
                gp.restart();
            }
        }
    }

    public void optionState(int code) {

        if(code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
//            gp.resumeMusic();
        }

        if(code == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        int maxCommandNum = 0;
        switch(gp.ui.subState) {
            case 0: maxCommandNum = 5; break;
            case 2: maxCommandNum = 0; break;
            case 3: maxCommandNum = 1; break;
        }

        if(code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            gp.playSoundEffect(9);
            if(gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            gp.playSoundEffect(9);
            if(gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }

        if(code == KeyEvent.VK_A) {
            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 1 && gp.music.volumeScale > 0) {
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                    gp.playSoundEffect(9);
                }
            }

            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 2 && gp.soundEffect.volumeScale > 0) {
                    gp.soundEffect.volumeScale--;
                    gp.playSoundEffect(9);
                }
            }
        }
        else if(code == KeyEvent.VK_D) {
            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 1 && gp.music.volumeScale < 5) {
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSoundEffect(9);
                }
            }

            if(gp.ui.subState == 0) {
                if(gp.ui.commandNum == 2 && gp.soundEffect.volumeScale < 5) {
                    gp.soundEffect.volumeScale++;
                    gp.playSoundEffect(9);
                }
            }
        }

    }

    public void gameEndState(int code) {

        if(code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandEndNum == 0) {
                gp.gameState = gp.titleState;
                gp.restart();
            }
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
        if (code == KeyEvent.VK_F) {
            shotKeyPressed = false;
        }
    }
}
