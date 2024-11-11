package objects;

import entity.Entity;
import main.GamePanel;

public class Red_Potion extends Entity {

    int healing_value = 5;

    public Red_Potion(GamePanel gp) {
        super(gp);

        type = type_consumable;
        name = "Red Potion";
        down1 = setup("objects/potion_red", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nHeals your life by " + healing_value + ".";
    }

    public void use(Entity entity) {

        gp.gameState = gp.dialogueState;
        gp.ui.currentDialogue = "You drink the " + name + "!\n" +
                "Your life has been recovered by " + healing_value + ".";
        entity.life += healing_value;
        if(gp.player.life > gp.player.maxLife) {
            gp.player.life = gp.player.maxLife;
        }
        gp.playSoundEffect(2);
    }
}
