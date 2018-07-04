package com.tuyu;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
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
 * 通过lucene进行全文检索
 *
 * @author tuyu
 * @date 7/3/18
 * Stay Hungry, Stay Foolish.
 */
public class LogSearch {
    public static void main(String[] args) throws IOException, ParseException {

        String indexFile = "src/main/resources/index";
        String queryField = "author";
        String queryString = "tuyu";

        Path indexPath = Paths.get(indexFile);
        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(indexPath));
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        QueryParser queryParser = new QueryParser(queryField, new StandardAnalyzer());

        Query query = queryParser.parse(queryString);

//        query.ad

//        ScoreDoc[] scoreDocs = indexSearcher.search(query, 100).scoreDocs;
//        query = new TermQuery(new Term(queryField, queryString));
        System.out.println("--> " + query);
        ScoreDoc[] scoreDocs = indexSearcher.search(query, 100).scoreDocs;
        for (ScoreDoc s : scoreDocs) {
            System.out.println(" -- > " + indexSearcher.doc(s.doc));
        }

    }
}
