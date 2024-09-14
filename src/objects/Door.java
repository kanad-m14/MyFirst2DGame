package objects;

import entity.Entity;
import main.GamePanel;

public class Door extends Entity {

    public Door(GamePanel gp) {
        super(gp);

        name = "Door";
        down1 = setup("objects/door", gp.tileSize, gp.tileSize);
        collision = true;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}

