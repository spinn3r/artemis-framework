package com.spinn3r.artemis.init.example;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 *
 */
public class DefaultThird implements Third {

    private final Provider<Second> secondProvider;

    @Inject
    public DefaultThird(Provider<Second> secondProvider) {
        this.secondProvider = secondProvider;
    }

    @Override
    public Second getSecond() {
        return secondProvider.get();
    }

}
