package com.avast.clockwork.examples.visitor;

import java.util.List;

/**
 * User: zslajchrt
 * Date: 6/21/13
 * Time: 2:46 PM
 */
public class Folder extends AbstractNode {

    private final List<AbstractNode> children;

    public Folder(String name, List<AbstractNode> children) {
        super(name);
        this.children = children;
    }

    private <D> D visitChildren(Visitor<D> visitor, D data) throws Exception {
        D lastData = data;
        for (AbstractNode child : children) {
            lastData = child.visit(visitor, lastData);
        }
        return lastData;
    }

    @Override
    public <D> D visit(Visitor<D> visitor, D data) throws Exception {
        return visitChildren(visitor, visitor.accept(this, data));
    }
}
