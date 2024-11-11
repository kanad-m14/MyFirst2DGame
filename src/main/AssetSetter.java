package main;

import Monster.GreenSlime;
import entity.NPC_OldMan;
import objects.Axe;
import objects.Key;
import objects.Red_Potion;
import objects.Shield_Blue;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {

        this.gp = gp;
    }

    // setting the position of objects on the map
    public void setObject() {

        gp.obj[0] = new Key(gp);
        gp.obj[0].worldX = gp.tileSize * 25;
        gp.obj[0].worldY = gp.tileSize * 19;

        gp.obj[1] = new Axe(gp);
        gp.obj[1].worldX = gp.tileSize * 33;
        gp.obj[1].worldY = gp.tileSize * 21;

        gp.obj[2] = new Shield_Blue(gp);
        gp.obj[2].worldX = gp.tileSize * 36;
        gp.obj[2].worldY = gp.tileSize * 21;

        gp.obj[3] = new Red_Potion(gp);
        gp.obj[3].worldX = gp.tileSize * 38;
        gp.obj[3].worldY = gp.tileSize * 14;

    }

    // setting position of the npc at the start of the game
    public void setNPC() {

        gp.npc[0] = new NPC_OldMan(gp);
        gp.npc[0].worldX = gp.tileSize * 21;
        gp.npc[0].worldY = gp.tileSize * 21;

    }

    public void setMonster() {

        gp.monster[0] = new GreenSlime(gp);
        gp.monster[0].worldX = gp.tileSize * 23;
        gp.monster[0].worldY = gp.tileSize * 36;

        gp.monster[1] = new GreenSlime(gp);
        gp.monster[1].worldX = gp.tileSize * 23;
        gp.monster[1].worldY = gp.tileSize * 37;

        gp.monster[2] = new GreenSlime(gp);
        gp.monster[2].worldX = gp.tileSize * 34;
        gp.monster[2].worldY = gp.tileSize * 42;

        gp.monster[3] = new GreenSlime(gp);
        gp.monster[3].worldX = gp.tileSize * 38;
        gp.monster[3].worldY = gp.tileSize * 41;

        gp.monster[4] = new GreenSlime(gp);
        gp.monster[4].worldX = gp.tileSize * 23;
        gp.monster[4].worldY = gp.tileSize * 40;


    }
}
