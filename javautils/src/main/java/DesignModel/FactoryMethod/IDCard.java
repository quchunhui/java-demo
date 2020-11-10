package DesignModel.FactoryMethod;

import lombok.Getter;

public class IDCard extends Product {
    @Getter
    private String owner;

    IDCard(String owner) {
        System.out.println("制作 " + owner + " 的ID卡。");
        this.owner = owner;
    }

    @Override
    public void use() {
        System.out.println("使用 " + owner + " 的ID卡。");
    }
}
