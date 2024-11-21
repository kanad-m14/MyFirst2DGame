package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    public GamePanel gp;

    // IMAGES
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2; // for walk animation
    public BufferedImage image, image2, image3; // for objects
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1,
            attackLeft2, attackRight1, attackRight2; // player attack animation

    // STATE
    public int worldX, worldY;
    public String direction = "down";
    public int spriteNum = 1;
    int dialogueIndex = 0;
    public boolean collisionOn = false; // for entities
    public boolean invincible = false;
    public boolean collision = false; // for objects
    public boolean attacking = false;
    public boolean alive = true;
    public boolean dying = false;
    public boolean hpBarOn = false;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    String dialogues[] = new String[20];

    // CHARACTER ATTRIBUTES
    public String name;
    public int speed;
    public int maxLife;
    public int life;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;

    public Entity currentWeapon;
    public Entity currentShield;

    // ITEM ATTRIBUTES:
    public int value;
    public int attackValue;
    public int defenseValue;
    public String description = "";

    // PROJECTILE ATTRIBUTES
    public int maxMana;
    public int mana;
    public Projectile projectile;
    public int useCost;

    // COUNTERS
    public int actionLockCounter = 0;
    public int invincibleCounter = 0;
    public int spriteCounter = 0;
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;

    // TYPES
    public int type; // 0 is player, 1 is npc, 2 is monster
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_pickUpOnly = 7;
    public final int type_obstacle = 8;
    public final int type_gameEnd = 9;

    public Entity(GamePanel gp) {

        this.gp = gp;
    }

    public int getLeftX() {
        return worldX + solidArea.x;
    }

    public int getRightX() {
        return worldX + solidArea.x + solidArea.width;
    }

    public int getTopY() {
        return worldY + solidArea.y;
    }

    public int getBottomY() {
        return worldY + solidArea.y + solidArea.height;
    }

    public int getCol() {
        return (worldX + solidArea.x)/gp.tileSize;
    }

    public int getRow() {
        return (worldY + solidArea.y)/gp.tileSize;
    }

    public void setAction() {}

    public void damageReaction() {}

    public boolean use(Entity entity) {
        return false;
    }

    public void checkDrop() {}

    public void dropItem(Entity droppedItem) {

        for(int i = 0; i < gp.obj.length; i++) {

            if(gp.obj[i] == null) {
                gp.obj[i] = droppedItem;
                gp.obj[i].worldX = worldX; // dead monster's worldX and worldY
                gp.obj[i].worldY = worldY;
                break;
            }
        }
    }

    // Npc and other entities speaking method
    public void speak() {

        if(dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }

        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
    }

    public void interact() {}

    public int getDetected(Entity user, Entity target[], String targetName) {
        int index = 999;

        // Check the surrounding objects
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();

        switch(user.direction) {
            case "up": nextWorldY = user.getTopY() - user.speed; break;    // change 1 to user.speed
            case "down": nextWorldY = user.getBottomY() + user.speed; break;    // change 1 to user.speed
            case "left": nextWorldX = user.getLeftX() - user.speed; break;    // change 1 to user.speed
            case "right": nextWorldX = user.getRightX() + user.speed; break;    // change 1 to user.speed
        }

        int col = nextWorldX / gp.tileSize;
        int row = nextWorldY / gp.tileSize;

        for(int i = 0; i < target.length; i++) {
            if(target[i] != null) {
                if(target[i].getCol() == col && target[i].getRow() == row && target[i].name.equals(targetName)) {
                    index = i;
                    break;
                }
            }
        }

        return index;
    }

    // updating entity's information every frame
    public void update() {

        setAction();

        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkEntity(this, gp.iTile);
        gp.cChecker.checkObject(this, false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        boolean contactPlayer = gp.cChecker.checkPlayer(this);

        if(this.type == type_monster && contactPlayer == true) {
            damagePlayer(attack);
        }

        // If collisionOn is false, then the NPC can move
        if(collisionOn == false) {

            switch (direction) {
                case "up": worldY -= speed; break;
                case "down": worldY += speed; break;
                case "left": worldX -= speed; break;
                case "right": worldX += speed; break;
            }
        }

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

        // setting invincible state for entities too when they take damage
        if(invincible == true) {
            invincibleCounter++;
            if(invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;

        }
    }

    public void damagePlayer(int attack) {

        if(gp.player.invincible == false) {
            // we can give damage
            gp.playSoundEffect(6);
            int damage = attack * 2 - gp.player.defense;
            if(damage < 0) {
                damage = 0;
            }
            gp.player.life -= damage;
            gp.player.invincible = true;
        }
    }

    public void draw(Graphics2D g2) {

        // setting the NPC's screenX and screenY (similar to object)(check SuperObject class)
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // If the npc is in camera frame, we draw it
        if(worldX + gp.tileSize> gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize< gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize> gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize< gp.player.worldY + gp.player.screenY)
        {

            BufferedImage image = null;
            switch (direction) {
                case "up":
                    if(spriteNum == 1) {
                        image = up1;
                    }
                    if(spriteNum == 2) {
                        image = up2;
                    }
                    break;
                case "down":
                    if(spriteNum == 1) {
                        image = down1;
                    }
                    if(spriteNum == 2) {
                        image = down2;
                    }
                    break;
                case "left":
                    if(spriteNum == 1) {
                        image = left1;
                    }
                    if(spriteNum == 2) {
                        image = left2;
                    }
                    break;
                case "right":
                    if(spriteNum == 1) {
                        image = right1;
                    }
                    if(spriteNum == 2) {
                        image = right2;
                    }
                    break;
            }

            // MONSTER HP BAR
            if(type == type_monster && hpBarOn == true) {

                double oneScale = (double)gp.tileSize / maxLife;
                double hpBarValue = oneScale * life;

                g2.setColor(new Color(35,35,35));
                g2.fillRect(screenX - 1, screenY - 16, gp.tileSize + 2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;
                if(hpBarCounter > 600) {
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            if(invincible == true) {
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.4f);
            }

            if(dying == true) {
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, null);

            changeAlpha(g2, 1f);
        }
    }

    // every 5 frames changing the alpha of the monster for a death effect
    public void dyingAnimation(Graphics2D g2) {

        dyingCounter++;

        int i = 5;

        if(dyingCounter <= i) {
            changeAlpha(g2, 0);
        }
        if(dyingCounter > (i) && dyingCounter <= (i * 2)) {
            changeAlpha(g2, 1);
        }
        if(dyingCounter > (i * 2) && dyingCounter <= (i * 3)) {
            changeAlpha(g2, 0);
        }
        if(dyingCounter > (i * 3) && dyingCounter <= (i * 4)) {
            changeAlpha(g2, 1);
        }
        if(dyingCounter > (i * 4) && dyingCounter <= (i * 5)) {
            changeAlpha(g2, 0);
        }
        if(dyingCounter > (i * 5) && dyingCounter <= (i * 6)) {
            changeAlpha(g2, 1);
        }
        if(dyingCounter > (i * 6) && dyingCounter <= (i * 7)) {
            changeAlpha(g2, 0);
        }
        if(dyingCounter > (i * 7) && dyingCounter <= (i * 8)) {
            changeAlpha(g2, 1);
        }
        if(dyingCounter > i * 8) {
            alive = false;
        }
    }

    // creating a method to change alpha so that we don't have to call the same function again and again
    public void changeAlpha(Graphics2D g2, float alphaValue) {

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    // method to read the entity images
    public BufferedImage setup(String imagePath, int width, int height) {

        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaledImage(image, width, height);

        }catch(IOException e) {
            e.printStackTrace();
        }

        return image;
    }
}
