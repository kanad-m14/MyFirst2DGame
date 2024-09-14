package objects;

import entity.Entity;
import main.GamePanel;

public class Chest extends Entity {

    public Chest(GamePanel gp) {
        super(gp);

        name = "Chest";
        down1 = setup("objects/chest", gp.tileSize, gp.tileSize);
    }
}

