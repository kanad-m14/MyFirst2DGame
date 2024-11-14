package objects;

import entity.Entity;
import main.GamePanel;

public class Mana extends Entity {

    public Mana(GamePanel gp) {
        super(gp);

        name = "Mana";
        image = setup("objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image2 = setup("objects/manacrystal_blank", gp.tileSize, gp.tileSize);
    }
}
