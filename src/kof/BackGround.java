package kof;

import java.awt.Graphics;
import java.awt.Image;

public class BackGround {
    private static Image[] backgroundImgs = new Image[8];
    private int backgroundCount = 0;

    static {
        for (int i = 0; i < 8; i++) {
            backgroundImgs[i] = GameUtil.getImage("kof/images/background/background" + (i + 1) + ".png");
            // 预加载图片
            backgroundImgs[i].getWidth(null);
        }
    }

    // 绘制背景
    public void drawBackground(Graphics g) {
        if (backgroundCount >= 8) {
            backgroundCount = 0;
        }
        g.drawImage(backgroundImgs[backgroundCount], 0, 0, Constant.GAME_WIDTH, Constant.GAME_HEIGHT, null);
        backgroundCount++;
    }
}