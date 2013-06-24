package com.avast.clockwork.examples.iterator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * User: zslajchrt
 * Date: 6/21/13
 * Time: 3:58 PM
 */
@SuppressWarnings("unchecked")

public class JoiningIterators {

    public static void main(String[] args) {

        Iterator<String> iterator1 = Arrays.asList("a", "b", "c").iterator();
        Iterator<String> iterator2 = Collections.<String>emptyList().iterator();
        Iterator<String> iterator3 = Arrays.asList("x").iterator();

        Iterator<String> joinedIterators = new CoJoinedIterators<String>(
                iterator1,
                iterator2,
                iterator3
        );

        while (joinedIterators.hasNext()) {
            String next = joinedIterators.next();
            System.out.println(next);
        }
    }

}
