import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable {

    public static JFrame frame;
    private Thread thread;

    private boolean isRunning = true;
    private final int WIDTH = 160;
    private final int HEIGHT = 120;
    private final int SCALE = 4;

    private BufferedImage image;

    private Spritesheet sheet;
    private BufferedImage[] player;

    public Game(){
        sheet = new Spritesheet("res/spritesheet.png");
        player[0] = sheet.getSprite(0,0, 32, 32);
        player[1] = sheet.getSprite(32,0, 32, 32);
        player[2] = sheet.getSprite(64,0, 32, 32);
        setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        initFrame();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    public void initFrame(){
        frame = new JFrame("My Game");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start(){
        thread = new Thread(this);
        thread.start();

    }

    public synchronized void stop(){
        isRunning = false;
        try {
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        Game game = new Game();
        game.start();
    }

    public void tick() {

    }

    public void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(new Color(50,255,155));
        g.fillRect(0,0, WIDTH, HEIGHT);

        /*Renderização do jogo*/
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0,0,0,100));
        g2.fillRect(0,0,WIDTH, HEIGHT);

        g2.rotate(Math.toRadians(90),50+16,50+16);
        g2.drawImage(player[0], 50, 50, null);

        /****/
        g.dispose();
        g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
        bs.show();
    }

    public void run() {

        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();
        while(isRunning){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1){
                tick();
                render();
                frames++;
                delta--;
            }
            if(System.currentTimeMillis() - timer >= 1000){
                System.out.println("fps "+ frames);
                frames = 0;
                timer+=1000;
            }
        }
        stop();
    }
}
