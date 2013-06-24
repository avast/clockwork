package com.avast.clockwork.examples.visitor;

/**
 * User: zslajchrt
 * Date: 6/21/13
 * Time: 2:49 PM
 */
public class Document extends AbstractNode {

    public Document(String name) {
        super(name);
    }

    @Override
    public <D> D visit(Visitor<D> visitor, D data) throws Exception {
        return visitor.accept(this, data);
    }
}
