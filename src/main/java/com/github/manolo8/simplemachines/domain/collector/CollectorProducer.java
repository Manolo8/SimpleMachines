package com.github.manolo8.simplemachines.domain.collector;

import com.github.manolo8.simplemachines.model.Producer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.Random;

public class CollectorProducer extends Producer<CollectorProduct> {

    public CollectorProducer(Random random) {
        super(random);
    }

    public CollectorProduct getIfCollectible(BlockState state) {

        for (CollectorProduct product : products) {
            if (product.match(state)) return product;
        }

        return null;
    }
}
