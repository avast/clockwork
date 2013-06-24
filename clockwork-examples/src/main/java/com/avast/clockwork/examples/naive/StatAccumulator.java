package com.avast.clockwork.examples.naive;

import com.avast.clockwork.TableAccumulator;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* User: zslajchrt
* Date: 6/13/13
* Time: 2:52 PM
*/
public class StatAccumulator extends TableAccumulator<String, BaseStat> {

    private final Map<String, Double> classPriors;

    public StatAccumulator(Map<String, Double> classPriors) {
        super(null, new StatReducer());
        this.classPriors = new HashMap<String, Double>(classPriors);
    }

    public Map.Entry<String, Double> guess(List<Number> attributes) {
        double[] dblAttrs = new double[attributes.size()];
        for (int i = 0; i < attributes.size(); i++) {
            Number attr = attributes.get(i);
            dblAttrs[i] = attr.doubleValue();
        }

        return guess(dblAttrs);
    }

    public synchronized Map.Entry<String, Double> guess(double... attributes) {
        double maxClsPosterior = 0;
        String maxClass = null;

        // Iterate through the class priorities table, which gives us the set of the classes
        for (Map.Entry<String, Double> classPriorEntry : classPriors.entrySet()) {
            final String className = classPriorEntry.getKey();

            // Filter the attribute stats concerning the current class
            Map<String, BaseStat> classAttrTableOnly = Maps.filterEntries(table, new Predicate<Map.Entry<String, BaseStat>>() {
                @Override
                public boolean apply(Map.Entry<String, BaseStat> input) {
                    return input.getKey().startsWith(className);
                }
            });

            // Initialize the class posterior member
            double clsPosterior = classPriorEntry.getValue();

            // Calculate the class posterior member by iterating all class attribute stats
            for (Map.Entry<String, BaseStat> statEntry : classAttrTableOnly.entrySet()) {
                AttrStat attrStat = (AttrStat) statEntry.getValue();

                String attrKey = statEntry.getKey();
                int delimPos = attrKey.indexOf('_');
                int attrIndex = Integer.parseInt(attrKey.substring(delimPos + 1));

                double attribute = attributes[attrIndex];
                double attrPosterior = attrStat.getPosterior(attribute);
                clsPosterior *= attrPosterior;
            }

            // Determine the maximum class posterior and the corresponding class name
            if (clsPosterior > maxClsPosterior) {
                maxClsPosterior = clsPosterior;
                maxClass = className;
            }
        }

        // Return the pair consisting of the winning class name and its posterior number
        return Collections.singletonMap(maxClass, maxClsPosterior).entrySet().iterator().next();
    }

}