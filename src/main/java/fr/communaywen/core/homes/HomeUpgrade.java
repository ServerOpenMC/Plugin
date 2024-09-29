package fr.communaywen.core.homes;

import lombok.Getter;

@Getter
public enum HomeUpgrade {
    UPGRADE_1(1, 0),
    UPGRADE_2(3, 10000),
    UPGRADE_3(5, 15000),
    UPGRADE_4(7, 20000),
    UPGRADE_5(9, 25000),
    UPGRADE_6(11, 30000),
    UPGRADE_7(13, 35000),
    UPGRADE_8(15, 40000),
    UPGRADE_9(17, 45000),
    UPGRADE_10(19, 50000),
    UPGRADE_11(21, 55000),
    UPGRADE_12(23, 60000),
    UPGRADE_13(25, 65000),

    ;

    private final int homes;
    private final int price;

    HomeUpgrade(int homes, int price) {
        this.homes = homes;
        this.price = price;
    }

}
