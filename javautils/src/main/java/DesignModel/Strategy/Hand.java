package DesignModel.Strategy;

import lombok.Getter;

public class Hand {
    // 表示石头的值
    public static final int HANDVALUE_GUU = 0;
    // 表示剪刀的值
    public static final int HANDVALUE_CHO = 1;
    // 表示布的值
    public static final int HANDVALUE_PAA = 2;

    // 表示猜拳中3种手势的实例
    public static final Hand[] hand = {
        new Hand(HANDVALUE_GUU),
        new Hand(HANDVALUE_CHO),
        new Hand(HANDVALUE_PAA)
    };
    // 表示猜拳中手势所对应的字符串
    private static final String[] name = {
        "石头",
        "剪刀",
        "布"
    };

    // 猜拳中出的手势的值
    private int handValue;
    public Hand(int handValue) {
        this.handValue = handValue;
    }

    // 根据手势的值获取其对应的实例
    public static Hand getHand(int handValue) {
        return hand[handValue];
    }

    // 如果this胜了h则返回true
    public boolean isStrongerThan(Hand h) {
        return fight(h) == 1;
    }

    // 如果this输给了h则返回true
    public boolean isWeakerThan(Hand h) {
        return fight(h) == -1;
    }

    // 计分：平0、胜1、负-1
    private int fight(Hand h) {
        if (this == h) {
            return 0;
        } else if ((this.handValue + 1) % 3 == h.handValue) {
            return 1;
        } else {
            return -1;
        }
    }

    // 转换为手势值所对应的字符串
    public String toString() {
        return name[handValue];
    }
}
