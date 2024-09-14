package objects;

import entity.Entity;
import main.GamePanel;

public class Boots extends Entity {

    public Boots(GamePanel gp) {
        super(gp);

        name = "Boots";
        down1 = setup("objects/boot", gp.tileSize, gp.tileSize);

    }
}
