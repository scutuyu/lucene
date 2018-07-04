package com.tuyu;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexFiles {
    private IndexFiles() {
    }

    public static void main(String[] args) {
        String usage = "java org.apache.lucene.demo.IndexFiles [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\nThis indexes the documents in DOCS_PATH, creating a Lucene indexin INDEX_PATH that can be searched with SearchFiles";
        String indexPath = "index";
        String docsPath = null;
        boolean create = true;

        for(int i = 0; i < args.length; ++i) {
            if("-index".equals(args[i])) {
                indexPath = args[i + 1];
                ++i;
            } else if("-docs".equals(args[i])) {
                docsPath = args[i + 1];
                ++i;
            } else if("-update".equals(args[i])) {
                create = false;
            }
        }

        if(docsPath == null) {
            System.err.println("Usage: " + usage);
            System.exit(1);
        }

        Path docDir = Paths.get(docsPath, new String[0]);
        if(!Files.isReadable(docDir)) {
            System.out.println("Document directory '" + docDir.toAbsolutePath() + "' does not exist or is not readable, please check the path");
            System.exit(1);
        }

        Date start = new Date();

        try {
            System.out.println("Indexing to directory '" + indexPath + "'...");
            Directory dir = FSDirectory.open(Paths.get(indexPath, new String[0]));
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            if(create) {
                iwc.setOpenMode(OpenMode.CREATE);
            } else {
                iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
            }

            IndexWriter writer = new IndexWriter(dir, iwc);
            indexDocs(writer, docDir);
            writer.close();
            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");
        } catch (IOException var12) {
            System.out.println(" caught a " + var12.getClass() + "\n with message: " + var12.getMessage());
        }

    }

    static void indexDocs(final IndexWriter writer, Path path) throws IOException {
        if(Files.isDirectory(path, new LinkOption[0])) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        IndexFiles.indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
                    } catch (IOException var4) {
                        ;
                    }

                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            indexDoc(writer, path, Files.getLastModifiedTime(path, new LinkOption[0]).toMillis());
        }

    }

    static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
        InputStream stream = Files.newInputStream(file, new OpenOption[0]);
        Throwable var5 = null;

        try {
            Document doc = new Document();
            Field pathField = new StringField("path", file.toString(), Store.YES);
            doc.add(pathField);
            doc.add(new LongPoint("modified", new long[]{lastModified}));
            doc.add(new TextField("contents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));
            if(writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                System.out.println("adding " + file);
                writer.addDocument(doc);
            } else {
                System.out.println("updating " + file);
                writer.updateDocument(new Term("path", file.toString()), doc);
            }
        } catch (Throwable var15) {
            var5 = var15;
            throw var15;
        } finally {
            if(stream != null) {
                if(var5 != null) {
                    try {
                        stream.close();
                    } catch (Throwable var14) {
                        var5.addSuppressed(var14);
                    }
                } else {
                    stream.close();
                }
            }

        }

    }
}
