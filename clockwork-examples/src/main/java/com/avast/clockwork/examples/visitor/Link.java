package com.avast.clockwork.examples.visitor;

/**
 * User: zslajchrt
 * Date: 6/21/13
 * Time: 2:52 PM
 */
public class Link extends AbstractNode {

    private final AbstractNode target;

    public Link(String name, AbstractNode target) {
        super(name);
        this.target = target;
    }

    @Override
    public <D> D visit(Visitor<D> visitor, D data) throws Exception {
        return visitor.accept(this, data);
    }
}
