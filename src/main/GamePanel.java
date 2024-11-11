package main;

import entity.Entity;
import entity.Player;
import entity.Projectile;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//this class inherits JPanel(subclass of JPanel - all the functions of the JPanel)
public class GamePanel extends JPanel implements Runnable {

    //SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile size (px)
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile size
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768px
    public final int screenHeight = tileSize * maxScreenRow;// 576px

    //WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // FPS
    int FPS = 60;

    // SYSTEM
    TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound soundEffect = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eventHandler = new EventHandler(this);
    Thread gameThread;

    // ENTITY and OBJECT
    public Player player = new Player(this, keyH);
    public Entity obj[] = new Entity[10]; // this represents that we can display up to 10 objects in the game
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();

    // GAME STATE
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;

    //constructor
    public GamePanel() {

        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); //set the size of this class (JPanel)
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    // creating this method, so we can add other setup stuff in this in future
    public void setupGame() {

        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        playMusic(0);
        stopMusic();
        gameState = titleState;
    }

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    // Game loop
    @Override
    public void run() {

        double drawInterval = 1000000000/ FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);

            lastTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if(timer >=1000000000) {
//                System.out.println("FPS = " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // Updating Player Status and NPC status after every frame
    public void update() {

        if(gameState == playState) {
            // Player
            player.update();

            // NPC
            for(int i = 0; i < npc.length; i++) {
                if(npc[i] != null) {
                    npc[i].update();
                }
            }

            // MONSTER
            for(int i = 0; i < monster.length; i++) {
                if(monster[i] != null) {
                    if(monster[i].alive && !monster[i].dying) {
                        monster[i].update();
                    }
                    if(!monster[i].alive) {
                        monster[i] = null;
                    }
                }
            }

            // PROJECTILE
            for(int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    if(projectileList.get(i).alive) {
                        projectileList.get(i).update();
                    }
                    if(!projectileList.get(i).alive) {
                        projectileList.remove(i);
                    }
                }
            }
        }
        if(gameState == pauseState) {
            // nothing for now
        }
    }

    // draws tiles and player images and objects on the game screen
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // DEBUG
        long drawStart = 0;
        if(keyH.showDebugText == true) {
            drawStart = System.nanoTime();
        }

        // TITLE STATE
        if(gameState == titleState) {
            ui.draw(g2);
        }
        else {
            // TILE
            tileM.draw(g2);

            // Adding player to the entity list
            entityList.add(player);

            // Adding npc to the entity list
            for(int i = 0; i < npc.length; i++)
            {
                if(npc[i] != null)
                {
                    entityList.add(npc[i]);
                }
            }

            // Adding objects to the entity list
            for(int i = 0; i < obj.length; i++)
            {
                if(obj[i] != null)
                {
                    entityList.add(obj[i]);
                }
            }

            // Adding monsters to the entity list
            for(int i = 0; i < monster.length; i++)
            {
                if(monster[i] != null)
                {
                    entityList.add(monster[i]);
                }
            }

            // Adding projectiles to the entity list
            for(int i = 0; i < projectileList.size(); i++)
            {
                if(projectileList.get(i) != null)
                {
                    entityList.add(projectileList.get(i));
                }
            }

            // SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {

                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });

            // DRAW ENTITIES
            // The person with lower WorldY will be drawn first, so that its legs do not get drawn above the head of
            // the entity below it
            for(int i =0; i < entityList.size(); i++)
            {
                entityList.get(i).draw(g2);
            }

            // EMPTY ENTITY LIST
            // once the list is drawn one by one, the entity list is again turned back to zero so that
            // it does not get larger and larger with every single time this method is run.
            entityList.clear();

            // UI
            ui.draw(g2);

        }

        // DEBUG
        if(keyH.showDebugText) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            g2.drawString("WorldX: " + player.worldX, x, y);
            y += lineHeight;
            g2.drawString("WorldY: " + player.worldY, x, y);
            y += lineHeight;
            g2.drawString("Col: " + (player.worldX + player.solidArea.x)/ tileSize, x, y);
            y += lineHeight;
            g2.drawString("Row: " + (player.worldY + player.solidArea.y)/ tileSize, x, y);
            y += lineHeight;
            g2.drawString("Draw Time: " + passed, x, y);
        }

        g2.dispose();
    }

    public void playMusic(int i) {

        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {

        music.stop();
    }

    public void playSoundEffect(int i) {

        soundEffect.setFile(i);
        soundEffect.play();
    }
}
