package com.avast.clockwork.examples.visitor;

/**
 * User: zslajchrt
 * Date: 6/21/13
 * Time: 2:44 PM
 */
public abstract class AbstractNode {

    private final String name;

    protected AbstractNode(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract <D> D visit(Visitor<D> visitor, D data) throws Exception;

}
