package DesignModel.FactoryMethod;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class IDCardFactory extends Factory {
    @Getter
    private List owners = new ArrayList();

    @Override
    protected Product createProduct(String owner) {
        return new IDCard(owner);
    }

    @Override
    protected void registerProduct(Product product) {
        owners.add(((IDCard)product).getOwner());
    }
}
