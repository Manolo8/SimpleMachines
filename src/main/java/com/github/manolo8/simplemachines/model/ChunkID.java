package com.github.manolo8.simplemachines.model;

import java.util.UUID;

//Usado para verificar se há alguma máquina na chunk
//Melhorar a eficiência, assim terá menas requisições no mysql
//IMPORTANTE! '-'
public class ChunkID {

    private UUID world;
    private int x;
    private int z;
    private int quantity;

    public ChunkID(int x, int z, UUID world) {
    }

    public ChunkID() {
    }

    public boolean match(int x, int z, UUID world) {
        return this.x == x && this.z == z && this.world.equals(world);
    }

    public void setWorld(UUID world) {
        this.world = world;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
