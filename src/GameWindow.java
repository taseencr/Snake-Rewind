import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame{
    CardLayout cardLayout;
    JPanel mainPanel;
    SnakeRewind snakeRewindGame;
    Leaderboard leaderboard;

    public GameWindow() throws Exception{
        leaderboard = new Leaderboard();

        setTitle("Snake Rewind");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        HomePanel home = new HomePanel(this);
        snakeRewindGame = new SnakeRewind(this);
        LeaderboardPanel lbPanel = new LeaderboardPanel(this, leaderboard);
        CreditsPanel creditsPanel = new CreditsPanel(this);

        mainPanel.add(home, "HOME");
        mainPanel.add(snakeRewindGame, "Play Game");
        mainPanel.add(lbPanel, "Leaderboard");
        mainPanel.add(creditsPanel, "Credits");

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void showScreen(String name){
        cardLayout.show(mainPanel, name);
        if(name.equals("Play Game")){
            snakeRewindGame.requestFocus();
        }
    }
}
