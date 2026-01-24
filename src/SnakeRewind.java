import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeRewind extends JPanel implements ActionListener, KeyListener{
    class Tiles{
        int x, y;
        Tiles(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    
    GameWindow gameWindow;
    Leaderboard leaderboard;
    int boardWidth = 700;
    int boardHeight = 700;
    int tileSize = 25;

    Tiles snakeHead;
    ArrayList<Tiles> snakeBody;
    Tiles food;
    Random random;
    int velX;
    int velY;
    int score = 0;
    boolean isPaused = false;
    boolean isGameOver = false;
    String playerName = "";
    boolean nameSubmitted = false;

    Timer gameLoop;

    SnakeRewind(GameWindow gameWindow) throws Exception{
        this.gameWindow = gameWindow;
        leaderboard = new Leaderboard();
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        setLayout(null);

        snakeHead = new Tiles(7, 7);
        snakeBody = new ArrayList<Tiles>();
        food = new Tiles(1, 1);
        random = new Random();
        placeFood();

        velX = 0;
        velY = 0;

        gameLoop = new Timer(101, this);
        gameLoop.start();
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        for(int i=0; i<boardWidth/tileSize; i++){
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }

        g.setColor(new Color(40, 40, 40));
        g.fillRect(0, 0, boardWidth, tileSize);
        g.fillRect(0, boardHeight - tileSize, boardWidth, tileSize);
        g.fillRect(0, 0, tileSize, boardHeight);
        g.fillRect(boardWidth - tileSize, 0, tileSize, boardHeight);

        g.setColor(Color.GREEN);  // Snake Head
        g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);
    
        for(int i=0; i<snakeBody.size(); i++){  // Snake Body
            Tiles snakePart = snakeBody.get(i);
            g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
        }
        
        g.setColor(Color.RED);  // Food
        g.fillRoundRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize, 3, 3);
    
        g.setFont(new Font("Verdana", Font.PLAIN, 17));  // Score
        if(!isGameOver){
            g.setColor(Color.GREEN);
            g.drawString(" Score: " + String.valueOf(score), tileSize-16, tileSize);
        }
        
        if(isPaused && !isGameOver){
            drawPauseMenu(g);
        }
        
        if(isGameOver){
            drawGameOverMenu(g);
        }
    }

    private void drawPauseMenu(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, boardWidth, boardHeight);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        int menuWidth = 350;
        int menuHeight = 400;
        int menuX = (boardWidth - menuWidth) / 2;
        int menuY = (boardHeight - menuHeight) / 2;
        
        g2d.setColor(new Color(20, 20, 20));
        g2d.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
        g2d.setColor(new Color(255, 255, 0));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
        
        g2d.setFont(new Font("Verdana", Font.BOLD, 42));
        FontMetrics fm = g2d.getFontMetrics();
        String title = "PAUSED";
        int titleX = menuX + (menuWidth - fm.stringWidth(title)) / 2;
        int titleY = menuY + 60;
        g2d.setColor(new Color(255, 255, 0));
        g2d.drawString(title, titleX, titleY);
        
        int infoY = titleY + 50;
        g2d.setFont(new Font("Verdana", Font.BOLD, 20));
        g2d.setColor(new Color(200, 200, 200));
        String scoreText = "Score: " + score;
        fm = g2d.getFontMetrics();
        int scoreX = menuX + (menuWidth - fm.stringWidth(scoreText)) / 2;
        g2d.drawString(scoreText, scoreX, infoY);
        
        int optionsY = infoY + 80;
        g2d.setFont(new Font("Verdana", Font.PLAIN, 16));
        g2d.setColor(Color.WHITE);
        
        String[] options = {
            "ESC / P - Resume",
            "R - Restart Game",
            "M - Main Menu"
        };
        
        for(int i = 0; i < options.length; i++){
            fm = g2d.getFontMetrics();
            int optionX = menuX + (menuWidth - fm.stringWidth(options[i])) / 2;
            g2d.drawString(options[i], optionX, optionsY + (i * 35));
        }
    }

    private void drawGameOverMenu(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, boardWidth, boardHeight);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        
        int menuWidth = 400;
        int menuHeight = 450;
        int menuX = (boardWidth - menuWidth) / 2;
        int menuY = (boardHeight - menuHeight) / 2;
        
        g2d.setColor(new Color(20, 20, 20));
        g2d.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
        g2d.setColor(new Color(255, 0, 0));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(menuX, menuY, menuWidth, menuHeight, 20, 20);
        
        g2d.setFont(new Font("Verdana", Font.BOLD, 42));
        FontMetrics fm = g2d.getFontMetrics();
        String title = "GAME OVER";
        int titleX = menuX + (menuWidth - fm.stringWidth(title)) / 2;
        int titleY = menuY + 60;
        g2d.setColor(new Color(255, 0, 0));
        g2d.drawString(title, titleX, titleY);
        
        int infoY = titleY + 50;
        g2d.setFont(new Font("Verdana", Font.BOLD, 20));
        g2d.setColor(new Color(200, 200, 200));
        String scoreText = "Score: " + score;
        fm = g2d.getFontMetrics();
        int scoreX = menuX + (menuWidth - fm.stringWidth(scoreText)) / 2;
        g2d.drawString(scoreText, scoreX, infoY);
        
        int nameY = infoY + 50;
        g2d.setFont(new Font("Verdana", Font.PLAIN, 16));
        g2d.setColor(Color.WHITE);
        String nameLabel = "Enter your name:";
        fm = g2d.getFontMetrics();
        int nameLabelX = menuX + (menuWidth - fm.stringWidth(nameLabel)) / 2;
        g2d.drawString(nameLabel, nameLabelX, nameY);
        
        int inputY = nameY + 30;
        int inputWidth = 280;
        int inputHeight = 35;
        int inputX = menuX + (menuWidth - inputWidth) / 2;
        
        g2d.setColor(new Color(40, 40, 40));
        g2d.fillRoundRect(inputX, inputY, inputWidth, inputHeight, 5, 5);
        g2d.setColor(new Color(150, 150, 150));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(inputX, inputY, inputWidth, inputHeight, 5, 5);
        
        g2d.setFont(new Font("Verdana", Font.PLAIN, 18));
        g2d.setColor(Color.WHITE);
        String displayName = playerName.isEmpty() ? "Type here..." : playerName;
        if(playerName.isEmpty()){
            g2d.setColor(new Color(100, 100, 100));
        }
        fm = g2d.getFontMetrics();
        int textX = inputX + (inputWidth - fm.stringWidth(displayName)) / 2;
        int textY = inputY + (inputHeight + fm.getAscent()) / 2 - 2;
        g2d.drawString(displayName, textX, textY);
        
        if(!nameSubmitted){
            int cursorX = textX + (playerName.isEmpty() ? 0 : fm.stringWidth(playerName));
            g2d.setColor(Color.WHITE);
            g2d.fillRect(cursorX, textY - fm.getAscent() + 2, 2, fm.getAscent() - 4);
        }
        
        int optionsY = inputY + inputHeight + 40;
        g2d.setFont(new Font("Verdana", Font.PLAIN, 16));
        g2d.setColor(Color.WHITE);
        
        String[] options;
        if(!nameSubmitted){
            options = new String[]{
                "ENTER - Submit Name"
            };
        } else {
            options = new String[]{
                "ENTER / SPACE - Restart",
                "R - Restart Game",
                "M - Main Menu"
            };
        }
        
        for(int i = 0; i < options.length; i++){
            fm = g2d.getFontMetrics();
            int optionX = menuX + (menuWidth - fm.stringWidth(options[i])) / 2;
            g2d.drawString(options[i], optionX, optionsY + (i * 35));
        }
    }


    public void placeFood(){
        food.x = random.nextInt((boardWidth/tileSize)-2)+1;
        food.y = random.nextInt((boardHeight/tileSize)-2)+1;
    }

    public void move(){
        if(checkCollision(snakeHead, food)){
            snakeBody.add(new Tiles(food.x, food.y));
            score += 10;
            placeFood();
        }

        for(int i=snakeBody.size()-1; i>=0; i--){
            Tiles snakePart = snakeBody.get(i);
            if(i==0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else{
                Tiles prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        snakeHead.x += velX;
        snakeHead.y += velY;

        for(int i=0; i<snakeBody.size(); i++){
            Tiles snakePart = snakeBody.get(i);
            if(checkCollision(snakeHead, snakePart)){
                isGameOver = true;
            }
        }

        if(snakeHead.x<=0 || snakeHead.x>=(boardWidth/tileSize)-1 || snakeHead.y<=0 || snakeHead.y>=(boardHeight/tileSize)-1){
            isGameOver = true;
        }
    }

    public boolean checkCollision(Tiles tile1, Tiles tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void resetPosition(){
        velX = 0;
        velY = 0;
        snakeHead = new Tiles(7, 7);
    }


    public void actionPerformed(ActionEvent e) {
        if(!isPaused && !isGameOver){
            move();
        }
        repaint();
        if(isGameOver){
            gameLoop.stop();
        }
    }
    
    public void keyTyped(KeyEvent e) {
        if(isGameOver && !nameSubmitted){
            char c = e.getKeyChar();
            if(c == '\b'){
                if(playerName.length() > 0){
                    playerName = playerName.substring(0, playerName.length() - 1);
                    repaint();
                }
            } else if(c == '\n' || c == '\r'){
                submitName();
            } else if(Character.isLetterOrDigit(c) || c == ' ' || c == '-' || c == '_'){
                if(playerName.length() < 20){
                    playerName += c;
                    repaint();
                }
            }
        }
    }
    public void keyReleased(KeyEvent e) {

    }
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_P){
            if(!isGameOver){
                togglePause();
            }
            return;
        }

        if(isGameOver){
            if(!nameSubmitted){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    submitName();
                }
                else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    nameSubmitted = true;
                    repaint();
                }
                else if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                    if(playerName.length() > 0){
                        playerName = playerName.substring(0, playerName.length() - 1);
                        repaint();
                    }
                }
            } else {
                if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_R){
                    restartGame();
                }
                else if(e.getKeyCode() == KeyEvent.VK_M && gameWindow != null){
                    returnToMainMenu();
                }
            }
        }

        if(!isPaused && !isGameOver){
            if((e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && velY != 1){
                velX = 0;
                velY = -1;
            }
            else if((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) && velY != -1){
                velX = 0;
                velY = 1;
            }
            else if((e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) && velX != -1){
                velX = 1;
                velY = 0;
            }
            else if((e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) && velX != 1){
                velX = -1;
                velY = 0;
            }
        }
        
        if(isPaused && !isGameOver){
            if(e.getKeyCode() == KeyEvent.VK_R){
                restartGame();
            }
            else if(e.getKeyCode() == KeyEvent.VK_M && gameWindow != null){
                returnToMainMenu();
            }
        }
    }

    private void togglePause(){
        isPaused = !isPaused;
        if(isPaused){
            gameLoop.stop();
        } else {
            gameLoop.start();
        }
        repaint();
    }

    private void submitName(){
        if(!nameSubmitted){
            nameSubmitted = true;
            if(playerName != null && !playerName.trim().isEmpty()){
                try{
                    leaderboard.addScore(playerName.trim(), score);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            repaint();
        }
    }
    
    private void restartGame(){
        gameLoop.stop();
        snakeBody.clear();
        resetPosition();
        score = 0;
        isGameOver = false;
        isPaused = false;
        playerName = "";
        nameSubmitted = false;
        gameLoop.start();
        repaint();
    }
    
    private void returnToMainMenu(){
        if(gameWindow != null){
            isPaused = false;
            gameLoop.stop();
            gameWindow.showScreen("HOME");
        }
    }
}
