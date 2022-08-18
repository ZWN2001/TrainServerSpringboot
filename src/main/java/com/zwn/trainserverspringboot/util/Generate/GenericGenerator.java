package com.zwn.trainserverspringboot.util.Generate;

import java.util.Date;
import java.util.Random;

public abstract class GenericGenerator {

    private static Random random = null;

    protected static Random getRandomInstance() {
        if (random == null) {
            random = new Random(new Date().getTime());
        }

        return random;
    }
}