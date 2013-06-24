package com.avast.clockwork.examples.prodcon;

import org.w3c.dom.Document;

import java.util.Iterator;

/**
 * User: zslajchrt
 * Date: 6/21/13
 * Time: 4:51 PM
 */
public class Consumer extends Coroutine {

    public void run(Iterator<Document> documents) {
        while (documents.hasNext()) {
            consume(documents.next());
        }
    }

    private void consume(Document next) {
        // todo
    }

}
