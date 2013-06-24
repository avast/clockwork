package com.avast.clockwork.examples.visitor;

/**
 * User: zslajchrt
 * Date: 6/21/13
 * Time: 2:52 PM
 */
public class Sample {

    public static void main(String[] args) throws Exception {
        Folder root = loadTree();

        Integer nodeCount = root.visit(new Visitor<Integer>() {
            @Override
            public Integer accept(Folder folder, Integer data) throws Exception {
                return handleNode(folder, data);
            }

            @Override
            public Integer accept(Document document, Integer data) throws Exception {
                return handleNode(document, data);
            }

            @Override
            public Integer accept(Link link, Integer data) throws Exception {
                return handleNode(link, data);
            }

            private Integer handleNode(AbstractNode node, Integer lastCount) {
                System.out.println(node.getName());
                return lastCount + 1;
            }
        }, 0);

        System.out.println("Nodes count " + nodeCount);
    }

    public static void main2(String[] args) throws Exception {
        Folder root = loadTree();

        Integer nodeCount = root.visit(new LoopVisitor<Integer>() {
            @Override
            protected void loop(VisitedNode node) throws Exception {
                while (node != null) {
                    System.out.println(node.getNode().getName());
                    node = yield(node.getData() + 1);
                }
            }
        }, 0);
    }

    public static void main3(String[] args) throws Exception {
        Folder root = loadTree();

        Integer nodeCount = root.visit(new LoopVisitor<Integer>() {
            @Override
            protected void loop(VisitedNode node) throws Exception {
                while (node != null) {

                    // do some general operations on the node

                    switch (node.getNodeType()) {
                        case DOCUMENT:
                            // do some document specific operation
                            break;
                        case FOLDER:
                            // do some folder specific operation
                            break;
                        case LINK:
                            // do some link specific operation
                            break;
                        default:
                            // it is resilient against new node types!
                    }

                    node = yield(node.getData() + 1);
                }
            }
        }, 0);

        System.out.println("Nodes count " + nodeCount);
    }

    private static Folder loadTree() {
        return null;  // todo
    }


}
