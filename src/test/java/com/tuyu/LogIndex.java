package com.tuyu;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <pre>
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //             佛祖保佑       永无BUG     永不修改                   //
 * ////////////////////////////////////////////////////////////////////
 * </pre>
 * <p>
 * tuyu于7/3/18祈祷...
 * 将src/main/resources/log4j.properties文件建立索引
 *
 * @author tuyu
 * @date 7/3/18
 * Stay Hungry, Stay Foolish.
 */
public class LogIndex {
    public static void main(String[] args) throws IOException {
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
//        document.add(new TextField("content", new BufferedReader(new InputStreamReader(inputStream))));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String re = null;
        while ((re = bufferedReader.readLine()) != null){
            stringBuilder.append(re);
        }
        document.add(new StringField("content", stringBuilder.toString(), Field.Store.YES));
        document.add(new StringField("author", "tuyu", Field.Store.YES));
//        indexWriter.addDocument(document);
        indexWriter.updateDocument(new Term("author"), document);

        inputStream.close();
        indexWriter.close();
    }
}
