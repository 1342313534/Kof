package kof;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.sound.sampled.*;
import java.io.IOException;

public class MainMenu extends JFrame {
    private static MainMenu instance;

    private JButton startButton;
    private JButton musicButton;
    private Clip clip;
    private boolean isMusicOn = true;
    
 // 版权信息
    private static final String AUTHOR = "罗俊明 潘洪涛";
    private static final String YEAR = "2024";
    private static final String TELEPHONE = "18024213421";
    private static final String EMAIL = "2904562775@qq.com";

    private MainMenu() {
    	printCopyright();
        System.out.println("菜单建立");
        setTitle("kof拳皇");
        setSize(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
        setLocation(Constant.GAME_X, Constant.GAME_Y);
        setLayout(new BorderLayout());

        // 背景面板
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image bg = GameUtil.getImage("kof/images/background.jpg");
                g.drawImage(bg, 0, 0, Constant.GAME_WIDTH,Constant.GAME_HEIGHT,null,null);
            }
        };
        backgroundPanel.setLayout(null);
        add(backgroundPanel, BorderLayout.CENTER);

        addComponents(backgroundPanel);

        setVisible(true);

        playBackgroundMusic();

        // 窗口关闭
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopBackgroundMusic(); // 停止音乐
                System.exit(0);
            }
        });
    }

    public static MainMenu getInstance() {
        if (instance == null) {
            instance = new MainMenu();
        }
        return instance;
    }

    private void addComponents(JPanel backgroundPanel) {
        startButton = new JButton("开始游戏");
        startButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        startButton.setBounds(600, 400, 150, 50);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("游戏开始");
                KofGame mgf = new KofGame();
                mgf.launchFrame();
                stopBackgroundMusic();
                dispose(); // 关闭当前窗口
            }
        });
        backgroundPanel.add(startButton);

        musicButton = new JButton("音乐：开");
        musicButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        musicButton.setBounds(600, 470, 150, 50);
        musicButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleMusic();
            }
        });
        backgroundPanel.add(musicButton);
    }

    private void toggleMusic() {
        isMusicOn = !isMusicOn;
        if (isMusicOn) {
            musicButton.setText("音乐：开");
            System.out.println("音乐：开");
            playBackgroundMusic();
        } else {
            musicButton.setText("音乐：关");
            System.out.println("音乐：关");
            stopBackgroundMusic();
        }
    }

    private void playBackgroundMusic() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/kof/music/background_music.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            JOptionPane.showMessageDialog(this, "无法播放背景音乐：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stopBackgroundMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close(); 
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainMenu.getInstance();
            }
        });
    }
 // 版权信息打印方法
    public void printCopyright() {
        System.out.println( YEAR + "年\n" + "作者：" + AUTHOR + "\n" + "邮箱：" + EMAIL + "\n" + "电话:" + TELEPHONE);
    }
}
