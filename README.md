clockwork
=========

An adoption of the map-reduce paradigm based on the concept of coroutines to the world of stream data processing.

```java
public class WordSplitter extends Mapper<Long, String, String, Long> {
    @Override
    protected void map(Long lineCounter, String line, Context context) throws Exception {
        Iterable<String> splits = Splitter.on(" ").trimResults().split(line);
        for (String split : splits) {
            emit(split, 1L);
        }
    }
}
```

```java
public class WordCounter extends Reducer<String, Long, String, Long> {
    @Override
    protected void reduce(String word, SuspendableIterator<Long> occurrences, Context context)
            throws SuspendExecution, Exception {
        long counter = 0;
        while (occurrences.hasNext()) {
            counter += occurrences.next();
        }
        emit(word, counter);
    }
}
```
