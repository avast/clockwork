package com.avast.clockwork.examples.naive;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: zslajchrt
 * Date: 6/13/13
 * Time: 12:07 PM
 */
@Deprecated
public class TotalStat {
    private final Map<String, Map<Integer, AttrStat>> totalStat = Maps.newHashMap();
    private final Map<String, Double> classPriorities;
    private final Map<String, BaseStat> classStats = Maps.newHashMap();

    public TotalStat() {
        classPriorities = Maps.newHashMap();
    }

    public TotalStat(Map<String, Double> classPriorities) {
        this.classPriorities = new HashMap<String, Double>(classPriorities);
    }

    /**
     * The copy constructor
     * @param orig the original statistics
     */
    public TotalStat(TotalStat orig) {
        this.classPriorities = new HashMap<String, Double>(orig.classPriorities);
        this.classStats.putAll(orig.classStats);
        this.totalStat.putAll(orig.totalStat);
    }

    public synchronized void update(TotalStat sourceStat) {
        if (classPriorities.isEmpty()) {
            // Initialize the class priorities if not yet initialized.
            classPriorities.putAll(sourceStat.classPriorities);
        }

        // Merge the class stats
        for (Map.Entry<String, BaseStat> clsStatEntry : sourceStat.classStats.entrySet()) {
            BaseStat thisClsStat = this.classStats.get(clsStatEntry.getKey());
            if (thisClsStat == null) {
                this.classStats.put(clsStatEntry.getKey(), clsStatEntry.getValue());
            } else {
                this.classStats.put(clsStatEntry.getKey(), thisClsStat.update(clsStatEntry.getValue()));
            }
        }

        // Merge the attribute stats
        for (Map.Entry<String, Map<Integer, AttrStat>> totalStatEntry : sourceStat.totalStat.entrySet()) {
            Map<Integer, AttrStat> thisTotalStat = totalStat.get(totalStatEntry.getKey());
            if (thisTotalStat == null) {
                this.totalStat.put(totalStatEntry.getKey(), new HashMap<Integer, AttrStat>(totalStatEntry.getValue()));
            } else {
                // Merge the individual attribute stats
                for (Map.Entry<Integer, AttrStat> attrStatEntry: totalStatEntry.getValue().entrySet()) {
                    AttrStat thisAttrStat = thisTotalStat.get(attrStatEntry.getKey());
                    if (thisAttrStat == null) {
                        thisTotalStat.put(attrStatEntry.getKey(), attrStatEntry.getValue());
                    } else {
                        thisTotalStat.put(attrStatEntry.getKey(), thisAttrStat.update(attrStatEntry.getValue()));
                    }
                }
            }
        }
    }

    public synchronized void update(String key, BaseStat stat) {
        if (stat instanceof AttrStat) {
            int delim = key.indexOf('_');
            String instName = key.substring(0, delim);
            int attrIndex = Integer.parseInt(key.substring(delim + 1));

            Map<Integer, AttrStat> clsStat = totalStat.get(instName);
            if (clsStat == null) {
                clsStat = Maps.newHashMap();
                totalStat.put(instName, clsStat);
            }

            AttrStat attrStat = (AttrStat) stat;
            AttrStat lastAttrStat = clsStat.get(attrIndex);
            if (lastAttrStat == null) {
                clsStat.put(attrIndex, attrStat);
            } else {
                clsStat.put(attrIndex, lastAttrStat.update(attrStat));
            }
        } else {
            String clsName = key.substring("target_".length());
            BaseStat lastClsStat = classStats.get(clsName);
            if (lastClsStat == null) {
                classStats.put(clsName, stat);
            } else {
                classStats.put(clsName, lastClsStat.update(stat));
            }
        }
    }

    /**
     *
     * @return the snapshot of the statistics before it is cleared
     */
    public synchronized TotalStat clear() {
        TotalStat snapshot = new TotalStat(this);
        totalStat.clear();
        classStats.clear();
        return snapshot;
    }

    public Map.Entry<String, Double> guess(List<Number> attributes) {
        double[] dblAttrs = new double[attributes.size()];
        for (int i = 0; i < attributes.size(); i++) {
            Number attr = attributes.get(i);
            dblAttrs[i] = attr.doubleValue();
        }

        return guess(dblAttrs);
    }

    public Map.Entry<String, Double> guess(double... attributes) {
        double maxClsPosterior = 0;
        String maxClass = null;

        for (Map.Entry<String, Map<Integer, AttrStat>> clsStat : totalStat.entrySet()) {

            double clsPosterior = classPriorities.get(clsStat.getKey());

            for (int i = 0; i < attributes.length; i++) {
                AttrStat attrStat = clsStat.getValue().get(i);
                double attribute = attributes[i];
                double attrPosterior = attrStat.getPosterior(attribute);
                clsPosterior *= attrPosterior;
            }

            if (clsPosterior > maxClsPosterior) {
                maxClsPosterior = clsPosterior;
                maxClass = clsStat.getKey();
            }

        }

        return Collections.singletonMap(maxClass, maxClsPosterior).entrySet().iterator().next();
    }

}
