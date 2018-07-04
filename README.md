lucene 7.4.0
===

```xml
<dependencies>
    <!-- https://mvnrepository.com/artifact/org.apache.lucene/lucene-core -->
    <dependency>
      <groupId>org.apache.lucene</groupId>
      <artifactId>lucene-core</artifactId>
      <version>7.4.0</version>
    </dependency>

    <dependency>
      <groupId>org.apache.lucene</groupId>
      <artifactId>lucene-queryparser</artifactId>
      <version>7.4.0</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.apache.lucene/lucene-analyzers-common -->
    <dependency>
      <groupId>org.apache.lucene</groupId>
      <artifactId>lucene-analyzers-common</artifactId>
      <version>7.4.0</version>
    </dependency>
</dependencies>
```

# 索引建立过程(索引保存在磁盘)
1. 通过静态工厂方法创建*Directory*对象
2. 创建*IndexWriterConfig*对象
    1. 为`indexWriterConfig`对象设置索引模式，增量索引，还是新索引覆盖旧索引，不指明分词器，默认使用*StandardAnalyzer*
3. 通过`directory`和`indexWriterConfig`创建*IndexWriter*对象
    1. 创建*Document*对象
    2. 为`document`对象添加字段
    3. 将`document`对象添加到`indexWriter`对象中

```java
public class IndexTest{
    public static void main(String[] args){
            String logFile = "src/main/resources/log4j.properties";
            String indexFile = "src/main/resources/index";
            Path logPath = Paths.get(logFile);
            Path indexPath = Paths.get(indexFile);
            Directory directory = FSDirectory.open(indexPath);
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(); // 使用标准的分词器
    
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND); // 使用增量式索引
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
    
            Document document = new Document();
            document.add(new StringField("path", logPath.toString(), Field.Store.YES));
            InputStream inputStream = Files.newInputStream(logPath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String re = null;
            while ((re = bufferedReader.readLine()) != null){
                stringBuilder.append(re);
            }
            document.add(new StringField("content", stringBuilder.toString(), Field.Store.YES));
            document.add(new StringField("author", "tuyud", Field.Store.YES));
            indexWriter.updateDocument(new Term("author"), document);
    
            inputStream.close();
            indexWriter.close();
    }
}
```

# 查询过程(索引保存在磁盘)

1. 通过静态工厂方法创建*IndexReader*对象
2. 使用`indexReader`对象创建*IndexSearcher*对象
3. 创建查询分析器*QueryParser*对象
4. 创建查询对象*Query*
5. 将`query`对象传入`indexSearcher`对象的`search`方法进行查询


```java
public class SearchTest{
    public static void mian(String[] args){
            String indexFile = "src/main/resources/index";
            String queryField = "author";
            String queryString = "tuyu";
    
            Path indexPath = Paths.get(indexFile);
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(indexPath));
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
    
            QueryParser queryParser = new QueryParser(queryField, new StandardAnalyzer());
    
            Query query = queryParser.parse(queryString);
    
            System.out.println("--> " + query);
            ScoreDoc[] scoreDocs = indexSearcher.search(query, 100).scoreDocs;
            for (ScoreDoc s : scoreDocs) {
                System.out.println(" -- > " + indexSearcher.doc(s.doc));
            }
    }
}
```