package objects;

import entity.Entity;
import main.GamePanel;

public class Red_Potion extends Entity {

    GamePanel gp;

    public Red_Potion(GamePanel gp) {
        super(gp);
        this.gp = gp;

        type = type_consumable;
        name = "Red Potion";
        value = 5;
        down1 = setup("objects/potion_red", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nHeals your life by " + value + ".";
    }

    public boolean use(Entity entity) {

        boolean status = true;
        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drink the " + name + "!\n" +
                "Your life has been recovered by " + value + ".";
        entity.life += value;
        gp.playSoundEffect(2);
        return status;
    }

}
