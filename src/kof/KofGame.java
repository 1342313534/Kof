package kof;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;

public class KofGame extends Frame {

    //角色初始化
    BaShenAn baShenAn = new BaShenAn(Constant.PLAYER_X,Constant.PLAYER_Y,
            Constant.PLAYER_WIDTH,Constant.PLAYER_HEIGHT);// 八神庵
    CaoZhiJing caoZhiJing = new CaoZhiJing(Constant.GAME_WIDTH-Constant.PLAYER_X-Constant.PLAYER_WIDTH,
            Constant.PLAYER_Y, Constant.PLAYER_WIDTH,Constant.PLAYER_HEIGHT);;// 草稚京

    GameUtil gameUtil = new GameUtil(baShenAn,caoZhiJing);
    BackGround background = new BackGround();

    Image portrait1 = GameUtil.getImage("kof/images/portrait1.png");// 玩家一头像
    Image portrait2 = GameUtil.getImage("kof/images/portrait2.png");// 玩家二头像

    //绘画
    @Override
    public void paint(Graphics g) {
        Color c =  g.getColor();
        // 背景
        background.drawBackground(g);

        // 出招表
        Font f = new Font("", Font.BOLD, 24);
        g.setFont(f);
        g.setColor(Color.blue);
        g.drawString("A或←左移、S或↓下蹲防御、D或→右移、J或1拳击、K或2腿击、L或3闪避、U或4技能一、I或5技能二",120,60);
        g.setColor(Color.black);
        // 角色血条
        g.drawImage(portrait1,35, 80, 42, 42,null,null);
        g.drawImage(portrait2,1275, 80, 42, 42,null,null);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(10.0f));
        g.drawRect(769,80,502,42);
        g.drawRect(79,80,502,42);
        g.setColor(Color.magenta);
        g.drawString(gameUtil.blood1+"%",595,110);
        g.drawString(gameUtil.blood2+"%",700,110);
        g.setColor(Color.pink);
        g.fillRect(80,81,5*gameUtil.blood1,40);
        g.fillRect(770+(500-5*gameUtil.blood2),81,5*gameUtil.blood2,40);

        // 碰撞检测
        boolean peng = baShenAn.getRect().intersects(caoZhiJing.getRect());
        if (peng){
            System.out.println("***********碰撞***********");
        }
        // 优先级检查
        gameUtil.check(peng);
        // 八神庵
        baShenAn.drawSelf(g,peng);
        // 草稚京
        caoZhiJing.drawSelf(g,peng);
        // ko
        if (gameUtil.blood1 <= 0 || gameUtil.blood2 <= 0){
            gameUtil.ko(g);
        }

        g.setColor(c);
    }

    //初始化窗口
    public void launchFrame() {
        this.setTitle("kof拳皇");
        this.setVisible(true);
        this.setSize(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
        this.setLocation(Constant.GAME_X, Constant.GAME_Y);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        new PaintThread().start();	// 重新启动窗口
        addKeyListener(new KeyMonitor());   // 键盘监听

    }

    class PaintThread extends Thread {
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(55);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //键盘监视
    class KeyMonitor extends KeyAdapter {

        public void keyPressed(java.awt.event.KeyEvent e) {
            if (gameUtil.ko){
                gameUtil.addDirection(e);
            }
            baShenAn.addDirection(e);
            caoZhiJing.addDirection(e);
        }

        public void keyReleased(java.awt.event.KeyEvent e) {
            baShenAn.minusDirection(e);
            caoZhiJing.minusDirection(e);
        }
    }

    private Image offScreenImage = null;

    public void update(Graphics g) {
        if(offScreenImage == null) {
            //窗口宽度和高度
            offScreenImage = this.createImage(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
        }

        Graphics gOff = offScreenImage.getGraphics();
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);
    }

}
