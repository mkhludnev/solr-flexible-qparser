# solr-flexible-qparser
Hooking up Lucene's flexible query parser to evaluate intervals

Naturally it requires Lucene 9.1, we only have Solr 9.1 with Lucene 9.3 that's a minimum requirement. 

Declare it in solrconfig.xml as described in guide

```
  <queryParser name="flex" class="org.apache.solr.flexibleqp.FlexibleQP"/>
```

This jar should be deployed as a Solr Plugin into some lib folder along aside with lucne-queryparser:9.3 or above. Take it here https://repo1.maven.org/maven2/org/apache/lucene/lucene-queryparser/9.3.0/lucene-queryparser-9.3.0.jar

Example iterval query 
```q={!flex}unstemmed_text:fn:maxgaps(4 fn:unordered(charge account))```

Find more interval syntax examples in 
 * https://lucene.apache.org/core/9_3_0/queryparser/org/apache/lucene/queryparser/flexible/standard/StandardQueryParser.html
 * https://lucene.apache.org/core/9_3_0/queryparser/org/apache/lucene/queryparser/flexible/standard/nodes/intervalfn/package-summary.html

see also 
 * https://issues.apache.org/jira/browse/LUCENE-10223
 * https://issues.apache.org/jira/browse/SOLR-13764
