package com.tuyu;

import org.apache.lucene.LucenePackage;
import org.apache.lucene.index.Term;
import org.junit.Test;
import sun.jvm.hotspot.utilities.Bits;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

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
 * tuyu于6/28/18祈祷...
 *
 * @author tuyu
 * @date 6/28/18
 * Stay Hungry, Stay Foolish.
 */
public class HelloTest {

    @Test
    public void testStringIntern() {
        String hello = new String("hello");
        String intern = hello.intern();
        boolean b = hello == intern;
        System.out.println("hello == intern " + b);
        String intern1 = hello.intern();
        boolean b2 = intern == intern1;
        System.out.println("intern == intern1 " + b2);
    }

    @Test
    public void testLocalhost() throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost);
    }

    @Test
    public void testPackage() {
        System.out.println(LucenePackage.get());
    }


    /**
     * 汉诺塔，递归实现
     */
    @Test
    public void testHan() {
        han(3, "A", "C", "B");
    }

    public void han(int num, String from, String to, String other) {
        if (num == 1) {
            System.out.println(from + " --> " + to);
        }else {
            han(num - 1, from, other, to);
            han(1, from, to, other);
            han(num - 1, other, to, from);
        }
    }

    /**
     * Collection 的toArray()方法和toArray(T[] a)的区别
     * <p>
     *     前者，直接返回一个新的数组
     *     后者，如果参数的大小足够容纳collection中所有元素，则返回参数数组的地址，多余的元素置为null，如果大小不够，则返回一个新数组的地址。
     * </p>
     */
    @Test
    public void testToArray() {
        Collection collection = new LinkedList();
        collection.add("string");
        collection.add("hello");
        collection.add("world");
//        Object[] objects = collection.toArray();
//        System.out.println(objects);


//        Object[] hh = new Object[]{"gg", "aa", "22", "dd"};
        Object[] hh = new Object[]{"gg", "aa"};
        System.out.println("hh " + hh + " length is " + hh.length);
        for (Object obj : hh) {
            System.out.println(obj);
        }
        Object[] objects1 = collection.toArray(hh);
        System.out.println("objects1 " + objects1 + " length is " + objects1.length);
        for (Object obj : objects1) {
            System.out.println(obj);
        }
    }

    @Test
    public void testQueue() {
        Queue<String> queue = new LinkedList<>();
        // 添加元素，queue已满，则会抛出IllegalStateException异常
        queue.add("hello");
        // 添加元素，失败则返回false
        queue.offer("world");
        System.out.println(queue);

        // 取出元素，但是不删除
        String element = queue.element();
        System.out.println("element --> " + element);
        System.out.println(queue);

        // 取出元素，但是不删除
        String peek = queue.peek();
        System.out.println("peek --> " + peek);
        System.out.println(queue);

        // 取出元素，并删除
        String poll = queue.poll();
        System.out.println("poll --> " + poll);
        System.out.println(queue);

        // 取出元素，并删除
        String remove = queue.remove();
        System.out.println("remove --> " + remove);
        System.out.println(queue);

        // 如果queue为空，poll将返回null
        String poll1 = queue.poll();
        System.out.println("poll1 --> " + poll1);

        // 如果queue为空，remove将抛出NoSuchElementException
        try {
            String remove1 = queue.remove();
            System.out.println("remove1 --> " + remove1);
            System.out.println(queue);
        } catch (NoSuchElementException e) {
            System.out.println("remove1 --> " + e);
        }

        // 如果queue为空，peek将返回null
        String peek1 = queue.peek();
        System.out.println("peek1 --> " + peek1);
        System.out.println(queue);

        // 如果queue 为空，element将抛出NoSuchElementException
        try {
            String element1 = queue.element();
            System.out.println("element1 --> " + element1);
            System.out.println(queue);
        } catch (NoSuchElementException e) {
            System.out.println("element1 --> " + e);
        }
    }

    @Test
    public void test() {
        System.out.println(longestPalindrome("babad"));
    }
    public String longestPalindrome(String s) {
        if(s == null || s.length() <= 1){
            return s;
        }

        int n = s.length();
        int maxLen = 0;
        String maxPalinStr = s.substring(0, 1);
        String tmp = null;
        for(int i = 0; i < n - 1; i++){
            tmp = getPalindromeString(s, i, i);
            if(tmp.length() > maxLen){
                maxPalinStr = tmp;
                maxLen = tmp.length();
            }
            tmp = getPalindromeString(s, i, i + 1);
            if(tmp.length() > maxLen){
                maxPalinStr = tmp;
                maxLen = tmp.length();
            }
        }
        return maxPalinStr;
    }

    public String getPalindromeString(String s, int begin, int end){

        return "";
    }

    @Test
    public void testUnsafe() {

        Term term = new Term("hello");
        System.out.println(term.hashCode());
        term = new Term("hello");
        System.out.println(term.hashCode());
        System.out.println(new Object().hashCode());
        System.out.println(new Object().hashCode());
    }
}
