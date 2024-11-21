package objects;

import entity.Entity;
import main.GamePanel;

public class Key extends Entity {

    GamePanel gp;
    public Key(GamePanel gp) {
        super(gp);
        this.gp = gp;

        name = "Key";
        type = type_consumable;
        down1 = setup("objects/key", gp.tileSize, gp.tileSize);
        description = "[" + name + "]\nOpen a door.";
    }

    public boolean use(Entity entity) {

        gp.gameState = gp.dialogueState;
        boolean status = false;

        int objIndex = getDetected(entity, gp.obj, "Door");

        if(objIndex != 999) {
            gp.ui.currentDialogue = "You have opened the Door!";
            gp.playSoundEffect(3);
            gp.obj[objIndex] = null;
            status = true;
        }
        else {
            gp.ui.currentDialogue = "What are you doing?";
        }

        return status;
    }
}
