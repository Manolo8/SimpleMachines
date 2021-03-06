package com.github.manolo8.simplemachines.model;

import java.util.List;
import java.util.Random;

public class Producer<T extends Product> {

    protected Random random;
    protected List<T> products;

    public Producer(Random random) {
        this.random = random;
    }

    public void setProducts(List<T> products) {
        this.products = products;
    }

    public List<T> getProducts() {
        return products;
    }

    public T getNextProduct() {
        return products.get(random.nextInt(products.size()));
    }
}
