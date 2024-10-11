package entity;

import main.GamePanel;
import main.KeyHandler;
import objects.Shield_Wood;
import objects.Sword_Normal;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity{

    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    public boolean attackCanceled = false;

    //constructor
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 10;
        solidArea.y = 20;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 28;
        solidArea.height = 28;

        attackArea.width = 36;
        attackArea.height = 36;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
    }

    // default values of Player's position
    public void setDefaultValues() {

        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        // PLAYER STATUS
        maxLife = 6;
        life = maxLife;
        level = 1;
        strength = 1; // The more strength he has, the more damage he gives
        dexterity = 1; // The more dexterity he has, the less damage he receives
        exp = 0;
        nextLevelExp = 10;
        coin = 0;
        currentWeapon = new Sword_Normal(gp);
        currentShield = new Shield_Wood(gp);
        attack = getAttack(); // The total attack value is decided by the strength and weapon
        defense = getDefense(); // The total defense value is decided by dexterity and shield
    }

    public int getAttack() {
        return attack = strength * currentWeapon.attackValue;
    }

    public int getDefense() {
        return defense = dexterity * currentShield.defenseValue;
    }

    // reads player's action and the image is read and processed
    public void getPlayerImage() {

        up1 = setup("player/boy_up_1", gp.tileSize, gp.tileSize);
        up2 = setup("player/boy_up_2", gp.tileSize, gp.tileSize);
        down1 = setup("player/boy_down_1", gp.tileSize, gp.tileSize);
        down2 = setup("player/boy_down_2", gp.tileSize, gp.tileSize);
        left1 = setup("player/boy_left_1", gp.tileSize, gp.tileSize);
        left2 = setup("player/boy_left_2", gp.tileSize, gp.tileSize);
        right1 = setup("player/boy_right_1", gp.tileSize, gp.tileSize);
        right2 = setup("player/boy_right_2", gp.tileSize, gp.tileSize);
    }

    // loads and scales player attack images
    public void getPlayerAttackImage() {

        attackUp1 = setup("player/boy_attack_up_1", gp.tileSize, gp.tileSize * 2);
        attackUp2 = setup("player/boy_attack_up_2", gp.tileSize, gp.tileSize * 2);
        attackDown1 = setup("player/boy_attack_down_1", gp.tileSize, gp.tileSize * 2);
        attackDown2 = setup("player/boy_attack_down_2", gp.tileSize, gp.tileSize * 2);
        attackLeft1 = setup("player/boy_attack_left_1", gp.tileSize * 2, gp.tileSize);
        attackLeft2 = setup("player/boy_attack_left_2", gp.tileSize * 2, gp.tileSize);
        attackRight1 = setup("player/boy_attack_right_1", gp.tileSize * 2, gp.tileSize);
        attackRight2 = setup("player/boy_attack_right_2", gp.tileSize * 2, gp.tileSize);
    }

    //updates the keyPressed and the player moves accordingly, also checks the collision and sprite counter every frame
    public void update() {

        if(attacking == true) {
            attacking();
        }
        else if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {

            if(keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // CHECK OBJECT COLLISION
            int objectIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objectIndex);

            // CHECK NPC COLLISION
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // CHECK MONSTER COLLISION
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            // CHECK EVENT
            gp.eventHandler.checkEvent();

            // If collisionOn is false, then the player can move
            if(collisionOn == false && keyH.enterPressed == false) {

                switch (direction) {
                    case "up": worldY -= speed; break;
                    case "down": worldY += speed; break;
                    case "left": worldX -= speed; break;
                    case "right": worldX += speed; break;
                }
            }

            if(keyH.enterPressed == true && attackCanceled == false) {
                gp.playSoundEffect(7);
                attacking = true;
                spriteCounter = 0;
            }

            attackCanceled = false;
            keyH.enterPressed = false;

            spriteCounter++;
            if(spriteCounter > 12) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                }
                else if(spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }

        // This needs to be outside of key if statement
        if(invincible == true) {
            invincibleCounter++;
            if(invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    // player attack animation
    public void attacking() {

        spriteCounter++;

        if(spriteCounter <= 5) {
            spriteNum = 1;
        }
        if(spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;

            // Save the current worldX, worldY, solidArea
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust Player's WorldX/Y for the attackArea
            switch (direction) {
                case "up": worldY -= attackArea.height; break;
                case "down": worldY += gp.tileSize; break;
                case "left": worldX -= attackArea.width; break;
                case "right": worldX += gp.tileSize; break;
            }

            // Attack area becomes solid area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check monster collision with updated worldX and worldY and solidArea
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex);

            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        }

        if(spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    // picks up objects
    public void pickUpObject(int i) {

    }

    // interaction with the npc when collision occurs
    public void interactNPC(int i) {

        if(keyH.enterPressed) {

            if(i != 999) {
                attackCanceled = true;
                gp.gameState = gp.dialogueState;
                gp.npc[i].speak();
            }
        }
    }

    public void contactMonster(int i) {

        if(i != 999) {

            if(invincible == false) {
                gp.playSoundEffect(6);
                int damage = gp.monster[i].attack - defense;
                if(damage < 0) {
                    damage = 0;
                }
                life -= damage;
                invincible = true;
            }
        }
    }

    public void damageMonster(int i) {

        if(i != 999) {

            if(gp.monster[i].invincible == false) {

                gp.playSoundEffect(5);
                int damage = attack - gp.monster[i].defense;
                if(damage < 0) {
                    damage = 0;
                }
                gp.monster[i].life -= damage;
                gp.ui.addMessage("Damage " + damage);
                gp.monster[i].invincible = true;
                gp.monster[i].damageReaction();

                if(gp.monster[i].life <= 0) {
                    gp.monster[i].dying = true;
                    gp.ui.addMessage("The " + gp.monster[i].name + " is killed!");
                    gp.ui.addMessage("You have gained " + gp.monster[i].exp + " exp!");
                    exp = exp + gp.monster[i].exp;
                    checkLevelUp();
                }
            }
        }
    }

    public void checkLevelUp() {

        if(exp >= nextLevelExp) {
            level++;
            nextLevelExp += nextLevelExp * 2;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();
            gp.playSoundEffect(8);
            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "You are level " + level + " now!\n";
        }
    }
    // draws players images when moved
    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        switch (direction) {
            case "up":
                if(attacking == false) {
                    if(spriteNum == 1) {
                        image = up1;
                    }
                    if(spriteNum == 2) {
                        image = up2;
                    }
                }
                if(attacking == true) {
                    tempScreenY = screenY - gp.tileSize;
                    if(spriteNum == 1) {
                        image = attackUp1;
                    }
                    if(spriteNum == 2) {
                        image = attackUp2;
                    }
                }
                break;
            case "down":
                if(attacking == false) {
                    if(spriteNum == 1) {
                        image = down1;
                    }
                    if(spriteNum == 2) {
                        image = down2;
                    }
                }
                if(attacking == true) {
                    if(spriteNum == 1) {
                        image = attackDown1;
                    }
                    if(spriteNum == 2) {
                        image = attackDown2;
                    }
                }
                break;
            case "left":
                if(attacking == false) {
                    if(spriteNum == 1) {
                        image = left1;
                    }
                    if(spriteNum == 2) {
                        image = left2;
                    }
                }
                if(attacking == true) {
                    tempScreenX = screenX - gp.tileSize;
                    if(spriteNum == 1) {
                        image = attackLeft1;
                    }
                    if(spriteNum == 2) {
                        image = attackLeft2;
                    }
                }
                break;
            case "right":
                if(attacking == false) {
                    if(spriteNum == 1) {
                        image = right1;
                    }
                    if(spriteNum == 2) {
                        image = right2;
                    }
                }
                if(attacking == true) {
                    if(spriteNum == 1) {
                        image = attackRight1;
                    }
                    if(spriteNum == 2) {
                        image = attackRight2;
                    }
                }
                break;
        }

        if(invincible == true) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, null);

        // Reset alpha
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // DEBUG
        // AttackArea
        tempScreenX = screenX + solidArea.x;
        tempScreenY = screenY + solidArea.y;
        switch(direction) {

            case "up": tempScreenY = screenY - attackArea.height; break;
            case "down": tempScreenY = screenY + gp.tileSize; break;
            case "left": tempScreenX = screenX - attackArea.width; break;
            case "right": tempScreenX = screenX + gp.tileSize; break;
        }

        g2.setColor(Color.red);
        g2.setStroke(new BasicStroke(1));

        g2.drawRect(tempScreenX, tempScreenY, attackArea.width, attackArea.height);
    }
}














