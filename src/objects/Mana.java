package objects;

import entity.Entity;
import main.GamePanel;

public class Mana extends Entity {

    GamePanel gp;

    public Mana(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Mana";
        type = type_pickUpOnly;
        value = 1;
        down1 = setup("objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image = setup("objects/manacrystal_full", gp.tileSize, gp.tileSize);
        image2 = setup("objects/manacrystal_blank", gp.tileSize, gp.tileSize);
    }

    public boolean use(Entity entity) {

        gp.playSoundEffect(2);
        gp.ui.addMessage("Mana + " + value);
        entity.mana += value;

        return true;
    }
}
