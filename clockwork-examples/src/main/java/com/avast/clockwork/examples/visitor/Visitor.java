package com.avast.clockwork.examples.visitor;

/**
 * User: zslajchrt
 * Date: 6/21/13
 * Time: 2:45 PM
 */
public interface Visitor<D> {

    D accept(Folder folder, D data) throws Exception;

    D accept(Document document, D data) throws Exception;

    D accept(Link link, D data) throws Exception;

}
