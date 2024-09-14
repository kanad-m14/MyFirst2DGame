package main;

import java.awt.*;
import java.awt.image.BufferedImage;

// used with debug
public class UtilityTool {

    // SCALE A BUFFERED IMAGE BEFOREHAND AND NOT RUN IT IN THE LOOP AGAIN AND AGAIN
    public BufferedImage scaledImage(BufferedImage original, int width, int height) {

        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);

        g2.dispose();

        return scaledImage;
    }
}
