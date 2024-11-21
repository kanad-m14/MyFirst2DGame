package objects;

import entity.Entity;
import main.GamePanel;

public class Heart extends Entity {

    GamePanel gp;

    public Heart(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Heart";
        type = type_pickUpOnly;
        value = 2;
        down1 = setup("objects/heart_full", gp.tileSize, gp.tileSize);
        image = setup("objects/heart_full", gp.tileSize, gp.tileSize);
        image2 = setup("objects/heart_half", gp.tileSize, gp.tileSize);
        image3 = setup("objects/heart_blank", gp.tileSize, gp.tileSize);
    }

    public boolean use(Entity entity) {

        boolean status = true;
        gp.playSoundEffect(2);
        gp.ui.addMessage("Life + " + value);
        entity.life += value;

        return status;
    }


}
