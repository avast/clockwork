clockwork
=========

An adoption of the map-reduce paradigm based on the concept of coroutines to the world of stream data processing.

If you have some experience in writing offline jobs for Hadoop or other map-reduce frameworks you will find Clockwork
very familiar since it uses the same programming model. The important difference is that, in contrast to Hadoop, Clockwork
is primarily designed for writing online tasks like processing text stream data (like [Twitter firehose](https://dev.twitter.com/docs/streaming-apis/streams/public)),
while thinking in the map-reduce way.

See the project [wiki](https://github.com/avast-open/clockwork/wiki "Clockwork wiki") for more information.

The following code demonstrates how the classical map-reduce introductory example - The Word Count - can be rewritten
to Clockwork. The original Hadoop example can be found here: [Hadoop WordCount tutorial](http://www.cloudera.com/content/cloudera-content/cloudera-docs/HadoopTutorial/CDH4/Hadoop-Tutorial/ht_topic_5.html)

###Mapper

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

###Reducer

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

###Creating the execution pipeline

```java
Execution<Long, String, String, Long> execution = Execution.newBuilder()
    .mapper(new WordSplitter())
    .reducer(new WordCounter())
    .accumulator(new TableAccumulator<String, Long>())
    .build();
```

###Feeding the execution pipeline

```java
long counter = 0;
BufferedReader reader =
    new BufferedReader(new FileReader(fileName));
String line;
while ((line = reader.readLine()) != null) {
    execution.emit(counter++, line);
}

execution.close();
```
