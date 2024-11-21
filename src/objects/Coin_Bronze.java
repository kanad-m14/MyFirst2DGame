package objects;

import entity.Entity;
import main.GamePanel;

public class Coin_Bronze extends Entity {

    GamePanel gp;

    public Coin_Bronze(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_pickUpOnly;
        name = "Bronze Coin";
        value = 1;
        down1 = setup("objects/coin_bronze", gp.tileSize, gp.tileSize);
    }

    public boolean use(Entity entity) {

       boolean status = true;

       gp.playSoundEffect(1);
       gp.ui.addMessage("Coin + " + value);
       entity.coin += value;

       return status;
    }


}
