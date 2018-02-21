package com.github.manolo8.simplemachines.model;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;

public abstract class BluePrint {

    protected Random random;
    protected String name;
    protected Design design;
    protected Producer<? extends Product> producer;
    protected List<ItemStack> buildCost;
    protected ItemStack book;
    protected double price;

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Design getDesign() {
        return design;
    }

    public void setDesign(Design design) {
        this.design = design;
    }

    public Producer<? extends Product> getProducer() {
        return producer;
    }

    public void setProducer(Producer<? extends Product> producer) {
        this.producer = producer;
    }

    public List<ItemStack> getBuildCost() {
        return buildCost;
    }

    public void setBuildCost(List<ItemStack> buildCost) {
        this.buildCost = buildCost;
    }

    public ItemStack getBook() {
        return book.clone();
    }

    public void setBook(ItemStack book) {
        this.book = book;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public abstract Machine newInstance();

    public String getSuper() {
        return "default";
    }
}
