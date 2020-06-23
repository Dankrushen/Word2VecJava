package com.medallia.word2vec.util;


import org.apache.logging.log4j.ThreadContext;

/**
 * Helper to create {@link ThreadContext} for nested diagnostic contexts
 */
public class NDC implements AC {
    private final int size;

    /**
     * Push all the contexts given and pop them when auto-closed
     */
    public static NDC push(String... context) {
        return new NDC(context);
    }

    /**
     * Construct an {@link AutoCloseable} {@link NDC} with the given contexts
     */
    private NDC(String... context) {
        for (String c : context) {
            ThreadContext.push("[" + c + "]");
        }
        this.size = context.length;
    }

    @Override
    public void close() {
        for (int i = 0; i < size; i++) {
            ThreadContext.pop();
        }
    }
}
