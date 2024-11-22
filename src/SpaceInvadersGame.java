import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SpaceInvadersGame extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> playerBullets;
    private ArrayList<Bullet> enemyBullets;
    private ArrayList<Explosion> explosions = new ArrayList<>();
    private ArrayList<Star> stars;


    private int score = 0;
    private int lives = 5;
    private Random random;

    public SpaceInvadersGame() {
        this.setFocusable(true); //Menerima input keyboard
        this.setPreferredSize(new Dimension(800, 600));
        this.addKeyListener(this);

        player = new Player(400, 500, 7); // speed player
        enemies = new ArrayList<>();
        playerBullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();
        random = new Random();

        spawnEnemies(10); // Mengspawn 10 enemy diawal

        // Timer utk Loop Game
        timer = new Timer(10, this);
        timer.start();

        stars = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int x = random.nextInt(800);
            int y = random.nextInt(600);
            int speed = random.nextInt(3) + 1; // Kecepatan acak (1-3)
            stars.add(new Star(x, y, speed));
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);

        // Gambar bintang-bintang
        for (Star star : stars) {
            star.draw(g);
        }

        // Menampilkan Skor dan Nyawa
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score + " [Reach 200 pts to WIN]", 10, 20);
        g.drawString("Lives: " + lives, 10, 40);

        player.draw(g);

        // Menggambar peluru
        for (Bullet bullet : playerBullets) {
            bullet.draw(g);
        }
        for (Bullet bullet : enemyBullets) {
            bullet.draw(g);
        }

        // Menggambar musuh
        for (Enemy enemy : enemies) {
            enemy.draw(g);
        }

        // Menggambar ledakan
        for (Explosion explosion : explosions) {
            explosion.draw(g);
        }
    }


    // Metode mengspawn musuh dengan acak dan mempertimbangkan jaraknya
    private void spawnEnemies(int count) {
        for (int i = 0; i < count; i++) {
            int x, y;
            boolean overlap;
            do {
                overlap = false;
                x = random.nextInt(750);
                y = random.nextInt(200);

                for (Enemy enemy : enemies) {
                    if (Math.abs(x - enemy.getX()) < 50 && Math.abs(y - enemy.getY()) < 50) {
                        overlap = true;
                        break;
                    }
                }
            } while (overlap);

            enemies.add(new Enemy(x, y, 1)); // Kecepatan musuh
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (score >= 200) {
            timer.stop();
            showEndScreen(true);
            return;
        }

        if (lives <= 0) {
            timer.stop();
            showEndScreen(false);
            return;
        }

        player.move();

        // Gerakkan dan hapus peluru pemain
        playerBullets.removeIf(bullet -> {
            bullet.move();
            return bullet.getY() < 0;
        });

        // Gerakkan dan hapus peluru musuh
        enemyBullets.removeIf(bullet -> {
            bullet.move();
            if (bullet.collidesWith(player)) {
                lives--;
                explosions.add(new Explosion(player.getX(), player.getY())); // Ledakan saat pemain terkena
                return true;
            }
            return bullet.getY() > 600;
        });

        // Gerakkan musuh dan periksa tabrakan dengan pemain
        for (Enemy enemy : enemies) {
            enemy.move();
            if (enemy.getY() > 600) {
                enemy.resetPosition();
            }
            if (enemy.collidesWith(player)) {
                lives--;
                explosions.add(new Explosion(player.getX(), player.getY())); // Ledakan saat pemain tertabrak
                enemy.resetPosition();
            }
            if (random.nextInt(100) < 2) {
                enemyBullets.add(new Bullet(enemy.getX() + 15, enemy.getY() + 10, 2, false));
            }
        }

        // Gerakkan bintang
        for (Star star : stars) {
            star.move();
        }

        // Hapus musuh yang tertembak oleh peluru dan aktifkan ledakan
        playerBullets.removeIf(bullet -> enemies.removeIf(enemy -> {
            if (bullet.collidesWith(enemy)) {
                score += 10;
                explosions.add(new Explosion(enemy.getX(), enemy.getY())); // Tambahkan ledakan di posisi musuh
                return true;
            }
            return false;
        }));

        // Perbarui ledakan dan hapus yang sudah selesai
        explosions.removeIf(Explosion::isFinished);

        // Spawn musuh baru jika diperlukan
        if (enemies.size() < 5) {
            spawnEnemies(5);
        }

        explosions.forEach(Explosion::update);
        repaint();
    }


    // User input untuk bergerak dan menembak
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            player.setDirection(-1);
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            player.setDirection(1);
        } else if (key == KeyEvent.VK_SPACE) {
            playerBullets.add(new Bullet(player.getX() + 15, player.getY(), 7, true)); // Peluru pemain
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            player.setDirection(0); // Bergerak ke kiri
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            player.setDirection(0); // Bergerak ke kanan
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Abstrak class untuk objek game
    abstract class GameObject {
        protected int x, y, speed;

        public GameObject(int x, int y, int speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }

        public abstract void move();

        public abstract void draw(Graphics g);

        public int getX() { return x; }
        public int getY() { return y; }
    }

    // Player class
    class Player extends GameObject {
        private Image spaceshipImage;
        private int direction = 0;

        public Player(int x, int y, int speed) {
            super(x, y, speed);
            // Meng-load gambar spaceship
            spaceshipImage = new ImageIcon("resources/images/Spaceship.png").getImage();
        }

        public void setDirection(int dir) {
            direction = dir;
        }

        @Override
        public void move() {
            x += direction * speed;
            if (x < 0) x = 0;
            if (x > 760) x = 760;
        }

        @Override
        public void draw(Graphics g) {

            g.drawImage(spaceshipImage, x, y, null);
        }
    }


    // Enemy class
    class Enemy extends GameObject {
        private Image enemyImage;

        public Enemy(int x, int y, int speed) {
            super(x, y, speed);
            // Mengload gambar musuh
            enemyImage = new ImageIcon("resources/images/Enemy.png").getImage(); // Replace with the correct path to your image
        }

        @Override
        public void move() {
            y += speed; // Musuh bergerak konstan ke bawah
        }

        @Override
        public void draw(Graphics g) {
            g.drawImage(enemyImage, x, y, null);
        }

        // Reset musuh ke posisi acak
        public void resetPosition() {
            x = random.nextInt(750);
            y = random.nextInt(200);
        }

        // Metode untuk cek tabrakan
        public boolean collidesWith(Player player) {
            Rectangle enemyRect = new Rectangle(x, y, 32, 19); // Ukuran enemy
            Rectangle playerRect = new Rectangle(player.getX(), player.getY(), 30, 10);
            return enemyRect.intersects(playerRect);
        }
    }


    // Bullet class
    class Bullet extends GameObject {
        private boolean isPlayerBullet;

        public Bullet(int x, int y, int speed, boolean isPlayerBullet) {
            super(x, y, speed);
            this.isPlayerBullet = isPlayerBullet;
        }

        @Override
        public void move() {
            if (isPlayerBullet) {
                y -= speed;  // Player bullet moves up
            } else {
                y += speed;  // Enemy bullet moves down
            }
        }

        @Override
        public void draw(Graphics g) {
            g.setColor(isPlayerBullet ? Color.YELLOW : Color.GREEN);
            g.fillRect(x, y, 5, 10);
        }

        // Metode untuk cek tabrakan dengan objek game lain
        public boolean collidesWith(GameObject object) {
            Rectangle bulletRect = new Rectangle(x, y, 5, 10);
            Rectangle objectRect = new Rectangle(object.getX(), object.getY(), 30, 10);
            return bulletRect.intersects(objectRect);
        }
    }

    class Explosion extends GameObject {
        private int frame = 0;
        private boolean finished = false;
        private Image[] explosionFrames;
        private int delay = 0;  // Counter dari frame delay
        private final int FRAME_DELAY = 5;  // Bisa diadjust untuk speed ledakan

        public Explosion(int x, int y) {
            super(x, y, 0); // Ledakan tidak bergeak jadi speed =0
            explosionFrames = new Image[6]; // 6 frame ledakan
            for (int i = 0; i < 6; i++) {
                explosionFrames[i] = new ImageIcon("resources/images/explosion" + (i + 1) + ".png").getImage();
            }
        }

        @Override
        public void move() {
            // Ledakan tidak bergerak jadi ini kosong
        }

        public void update() {
            delay++;
            if (delay >= FRAME_DELAY) {
                delay = 0;
                frame++;
                if (frame >= explosionFrames.length) {
                    finished = true; // Menandakan ledakan sudah selesai
                }
            }
        }

        public boolean isFinished() {
            return finished;
        }

        @Override
        public void draw(Graphics g) {
            if (!finished) {
                g.drawImage(explosionFrames[frame], x, y, null);
            }
        }
    }



    //Class Bintang
    class Star extends GameObject {
        public Star(int x, int y, int speed) {
            super(x, y, speed);
        }

        @Override
        public void move() {
            y += speed; // Bintang turun ke bawah
            if (y > 600) { // Jika bintang keluar layar, bintang reset dr atas
                y = 0;
                x = new Random().nextInt(800); // Posisi X random
            }
        }

        @Override
        public void draw(Graphics g) {
            g.setColor(Color.WHITE); // Warna bintang
            g.fillOval(x, y, 2, 2); // Ukuran Bintang
        }
    }


    private void showEndScreen(boolean isWin) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Panel untuk end screen
        JPanel endPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Gradien latar belakang
                GradientPaint gradient = new GradientPaint(0, 0, Color.BLACK, 0, getHeight(), Color.DARK_GRAY);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        endPanel.setLayout(new BorderLayout());

        // Pesan kemenangan atau kekalahan
        JLabel message = new JLabel(
                isWin ? "Congratulations! You Win!" : "Game Over! No Lives Left.",
                SwingConstants.CENTER
        );

        message.setFont(new Font("Arial", Font.BOLD, 40));
        message.setForeground(isWin ? Color.GREEN : Color.RED);

        // Tombol untuk restart dan quit
        JButton restartButton = new JButton("Restart");
        JButton quitButton = new JButton("Quit");

        // Tambahkan aksi pada tombol
        restartButton.addActionListener(e -> {
            frame.dispose(); // Tutup frame lama
            SpaceInvadersGame.main(null); // Mulai ulang game
        });
        quitButton.addActionListener(e -> System.exit(0));

        // Panel tombol dengan layout
        JPanel buttons = new JPanel();
        buttons.setBackground(Color.BLACK);
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Jarak antar tombol
        buttons.add(restartButton);
        buttons.add(quitButton);

        // Tambahkan komponen ke panel akhir
        endPanel.add(message, BorderLayout.CENTER);
        endPanel.add(buttons, BorderLayout.SOUTH);

        // Tampilkan panel akhir di frame
        frame.setContentPane(endPanel);
        frame.revalidate();
        frame.repaint();
    }


    // Method memplay game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        SpaceInvadersGame game = new SpaceInvadersGame();
        frame.add(game);
        frame.setVisible(true);
    }
}