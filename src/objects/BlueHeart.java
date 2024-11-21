package objects;

import entity.Entity;
import main.GamePanel;

public class BlueHeart extends Entity {

    GamePanel gp;

    public BlueHeart(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Blue Heart";
        type = type_gameEnd;
        down1 = setup("objects/blueheart", gp.tileSize, gp.tileSize);
        collision = true;

        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

}
