package com.tuyu;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.IOException;

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
 * tuyu于7/4/18祈祷...
 * 测试lucene分词器
 * @author tuyu
 * @date 7/4/18
 * Stay Hungry, Stay Foolish.
 */
public class AnalyzerTest {

    private static final String[] examples = {"The quick brown 1234 fox jumped over the lazy dog!", "XY&Z 15.6 Corporation - xyz@example.com",
            "北京市北京大学", "log4j.rootLogger=debug,consoleAppender,fileAppender\n" +
            "log4j.appender.consoleAppender=org.apache.log4j.ConsoleAppender\n" +
            "log4j.appender.consoleAppender.layout=org.apache.log4j.PatternLayout\n" +
            "log4j.appender.consoleAppender.layout.ConversionPattern=%-d{yyyy-MM-dd HH:mm:ss SSS} ->[%t]--[%-5p]--[%c{1}]--%m%n\n" +
            "\n" +
            "log4j.appender.fileAppender=org.apache.log4j.RollingFileAppender\n" +
            "log4j.appender.fileAppender.File=src/log/test-log.log\n" +
            "log4j.appender.fileAppender.MaxFileSize=1Kb\n" +
            "log4j.appender.fileAppender.MaxBackupIndex=6\n" +
            "log4j.appender.fileAppender.layout=org.apache.log4j.PatternLayout\n" +
            "log4j.appender.fileAppender.layout.ConversionPattern=%-d{yyyy-MM-dd HH:mm:ss SSS}-->[%t]--[%-5p]--[%c{1}]--%m%n"};
    private static final Analyzer[] ANALYZERS = new Analyzer[]{new WhitespaceAnalyzer(), new SimpleAnalyzer(), new StopAnalyzer(), new
            StandardAnalyzer(), new CJKAnalyzer(),  new CJKAnalyzer()};
    @Test
    public void testAnalyzer() throws IOException {
        for (int i = 0; i < ANALYZERS.length; i++) {
            String simpleName = ANALYZERS[i].getClass().getSimpleName();
            for (int j = 0; j < examples.length; j++) {
                TokenStream contents = ANALYZERS[i].tokenStream("contents", examples[j]);
                //TokenStream contents = ANALYZERS[i].tokenStream("contents", new StringReader(examples[j]));
                OffsetAttribute offsetAttribute = contents.addAttribute(OffsetAttribute.class);
                TypeAttribute typeAttribute = contents.addAttribute(TypeAttribute.class);
                contents.reset();
                System.out.println(simpleName + " analyzing : " + examples[j]);
                while (contents.incrementToken()) {
                    String s1 = offsetAttribute.toString();
                    int i1 = offsetAttribute.startOffset();//起始偏移量
                    int i2 = offsetAttribute.endOffset();//结束偏移量
                    System.out.print(s1 + "[" + i1 + "," + i2 + ":" + typeAttribute.type() + "]" + " ");
                }
                contents.end();
                contents.close();
                System.out.println();
                System.out.println();
            }
            System.out.println();
        }
    }
}
