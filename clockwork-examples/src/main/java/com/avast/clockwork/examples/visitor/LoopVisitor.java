package com.avast.clockwork.examples.visitor;

import java.util.Iterator;

/**
 * User: zslajchrt
 * Date: 6/21/13
 * Time: 3:00 PM
 */
public abstract class LoopVisitor<D> implements Visitor<D> {

    public enum NodeType {
        FOLDER, DOCUMENT, LINK
    }

    public class VisitedNode {
        private final AbstractNode node;
        private final NodeType nodeType;
        private final D data;

        public VisitedNode(AbstractNode node, NodeType nodeType, D data) {
            this.node = node;
            this.nodeType = nodeType;
            this.data = data;
        }

        public AbstractNode getNode() {
            return node;
        }

        public NodeType getNodeType() {
            return nodeType;
        }

        public D getData() {
            return data;
        }
    }

    @Override
    public D accept(Folder folder, D data) throws Exception {
        return emit(NodeType.FOLDER, folder, data);
    }

    @Override
    public D accept(Document document, D data) throws Exception {
        return emit(NodeType.DOCUMENT, document, data);
    }

    @Override
    public D accept(Link link, D data) throws Exception {
        return emit(NodeType.LINK, link, data);
    }

    private D emit(NodeType nodeType, AbstractNode node, D data) {
        return null; // todo
    }

    protected abstract void loop(VisitedNode node) throws Exception;

    protected VisitedNode yield(D data) throws Exception {
        return null; // todo
    }
}
