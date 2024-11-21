package main;

import Monster.GreenSlime;
import entity.NPC_OldMan;
import objects.*;
import tile_interactive.DryTree;
import tile_interactive.InteractiveTile;

import java.util.Arrays;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {

        this.gp = gp;
    }

    // setting the position of objects on the map
    public void setObject() {

        Arrays.fill(gp.obj, null);

        int i = 0;
        gp.obj[i] = new Axe(gp);
        gp.obj[i].worldX = gp.tileSize * 33;
        gp.obj[i].worldY = gp.tileSize * 7;
        i++;

        gp.obj[i] = new Door(gp);
        gp.obj[i].worldX = gp.tileSize * 10;
        gp.obj[i].worldY = gp.tileSize * 12;
        i++;

        gp.obj[i] = new Chest(gp, new Key(gp));
        gp.obj[i].worldX = gp.tileSize * 30;
        gp.obj[i].worldY = gp.tileSize * 28;
        i++;

        gp.obj[i] = new BlueHeart(gp);
        gp.obj[i].worldX = gp.tileSize * 10;
        gp.obj[i].worldY = gp.tileSize * 9;
        i++;

        gp.obj[i] = new Chest(gp, new Red_Potion(gp));
        gp.obj[i].worldX = gp.tileSize * 14;
        gp.obj[i].worldY = gp.tileSize * 39;
    }

    // setting position of the npc at the start of the game
    public void setNPC() {

        gp.npc[0] = new NPC_OldMan(gp);
        gp.npc[0].worldX = gp.tileSize * 21;
        gp.npc[0].worldY = gp.tileSize * 21;

    }

    public void setMonster() {

        int i = 0;

        gp.monster[i] = new GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 23;
        gp.monster[i].worldY = gp.tileSize * 36;
        i++;

        gp.monster[i] = new GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 23;
        gp.monster[i].worldY = gp.tileSize * 37;
        i++;

        gp.monster[i] = new GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 34;
        gp.monster[i].worldY = gp.tileSize * 42;
        i++;

        gp.monster[i] = new GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 38;
        gp.monster[i].worldY = gp.tileSize * 41;
        i++;

        gp.monster[i] = new GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 23;
        gp.monster[i].worldY = gp.tileSize * 40;
        i++;

        gp.monster[i] = new GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 34;
        gp.monster[i].worldY = gp.tileSize * 8;
        i++;

        gp.monster[i] = new GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 39;
        gp.monster[i].worldY = gp.tileSize * 7;
        i++;

        gp.monster[i] = new GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 41;
        gp.monster[i].worldY = gp.tileSize * 12;
        i++;

//        gp.monster[i] = new GreenSlime(gp);
//        gp.monster[i].worldX = gp.tileSize * 9;
//        gp.monster[i].worldY = gp.tileSize * 30;
//        i++;
//
//        gp.monster[i] = new GreenSlime(gp);
//        gp.monster[i].worldX = gp.tileSize * 11;
//        gp.monster[i].worldY = gp.tileSize * 33;
//        i++;
//
//        gp.monster[i] = new GreenSlime(gp);
//        gp.monster[i].worldX = gp.tileSize * 14;
//        gp.monster[i].worldY = gp.tileSize * 32;
//        i++;

        gp.monster[i] = new GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 12;
        gp.monster[i].worldY = gp.tileSize * 29;
        i++;

        gp.monster[i] = new GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 32;
        gp.monster[i].worldY = gp.tileSize * 21;
        i++;

        gp.monster[i] = new GreenSlime(gp);
        gp.monster[i].worldX = gp.tileSize * 38;
        gp.monster[i].worldY = gp.tileSize * 25;
        i++;
    }

    public void setInteractiveTile() {

        int i = 0;
        gp.iTile[i] = new DryTree(gp, 27, 12); i++;
        gp.iTile[i] = new DryTree(gp, 28, 12); i++;
        gp.iTile[i] = new DryTree(gp, 29, 12); i++;
        gp.iTile[i] = new DryTree(gp, 30, 12); i++;
        gp.iTile[i] = new DryTree(gp, 31, 12); i++;
        gp.iTile[i] = new DryTree(gp, 32, 12); i++;
        gp.iTile[i] = new DryTree(gp, 33, 12); i++;
        gp.iTile[i] = new DryTree(gp, 29, 20); i++;
        gp.iTile[i] = new DryTree(gp, 29, 21); i++;
        gp.iTile[i] = new DryTree(gp, 29, 22); i++;
        gp.iTile[i] = new DryTree(gp, 25, 29); i++;
        gp.iTile[i] = new DryTree(gp, 26, 29); i++;
        gp.iTile[i] = new DryTree(gp, 26, 30); i++;
        gp.iTile[i] = new DryTree(gp, 26, 31); i++;
        gp.iTile[i] = new DryTree(gp, 27, 31); i++;
        gp.iTile[i] = new DryTree(gp, 28, 31); i++;
        gp.iTile[i] = new DryTree(gp, 28, 30); i++;
        gp.iTile[i] = new DryTree(gp, 14, 28); i++;
        gp.iTile[i] = new DryTree(gp, 14, 40); i++;
        gp.iTile[i] = new DryTree(gp, 15, 40); i++;
        gp.iTile[i] = new DryTree(gp, 16, 40); i++;
        gp.iTile[i] = new DryTree(gp, 17, 40); i++;
        gp.iTile[i] = new DryTree(gp, 8, 28); i++;

    }
}
