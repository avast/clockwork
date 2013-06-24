package com.avast.clockwork.examples.prodcon;

import org.w3c.dom.Document;

import java.io.File;
import java.util.Iterator;
import java.util.Queue;

/**
 * User: zslajchrt
 * Date: 6/21/13
 * Time: 4:51 PM
 */
public class Producer extends Coroutine {

    private final Queue<Document> queue;
    private final Consumer consumer;

    public Producer(Queue<Document> queue, Consumer consumer) {
        this.queue = queue;
        this.consumer = consumer;
    }

    public void run(Iterator<File> sources) {
        while (sources.hasNext()) {
            Document doc = parseFile(sources.next());
            queue.add(doc);
            yield(consumer);
        }
    }

    private Document parseFile(File next) {
        // todo
        return null;
    }

    private void produce(Document doc) {
        // todo
    }

}
