package com.medallia.word2vec;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import java.util.List;

/**
 * Provides search functionality
 */
public interface Searcher {
    /**
     * @return true if a word is inside the model's vocabulary.
     */
    boolean contains(Integer word);

    /**
     * @return Raw word vector
     */
    ImmutableList<Double> getRawVector(Integer word) throws UnknownWordException;

    /**
     * @return Top matches to the given word
     */
    List<Match> getMatches(Integer word, int maxMatches) throws UnknownWordException;

    /**
     * @return Top matches to the given vector
     */
    List<Match> getMatches(final double[] vec, int maxNumMatches);

    /**
     * Represents the similarity between two words
     */
    public interface SemanticDifference {
        /**
         * @return Top matches to the given word which share this semantic relationship
         */
        List<Match> getMatches(Integer word, int maxMatches) throws UnknownWordException;
    }

    /**
     * @return {@link SemanticDifference} between the word vectors for the given
     */
    SemanticDifference similarity(Integer s1, Integer s2) throws UnknownWordException;

    /**
     * @return cosine similarity between two words.
     */
    double cosineDistance(Integer s1, Integer s2) throws UnknownWordException;

    /**
     * Represents a match to a search word
     */
    public interface Match {
        /**
         * @return Matching word
         */
        Integer match();

        /**
         * @return Cosine distance of the match
         */
        double distance();

        /**
         * {@link Ordering} which compares {@link Match#distance()}
         */
        Ordering<Match> ORDERING = Ordering.natural().onResultOf(new Function<Match, Double>() {
            @Override
            public Double apply(Match match) {
                return match.distance();
            }
        });
        /**
         * {@link Function} which forwards to {@link #match()}
         */
        Function<Match, Integer> TO_WORD = new Function<Match, Integer>() {
            @Override
            public Integer apply(Match result) {
                return result.match();
            }
        };
    }

    /**
     * Exception when a word is unknown to the {@link Word2VecModel}'s vocabulary
     */
    public static class UnknownWordException extends Exception {
        UnknownWordException(Integer word) {
            super(String.format("Unknown search word '%s'", word));
        }
    }
}
