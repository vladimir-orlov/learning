package com.vorlov;

import com.vorlov.book.config.EsConfig;
import com.vorlov.book.model.Book;
import com.vorlov.book.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = EsConfig.class)
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Before
    public void before() {
        esTemplate.deleteIndex(Book.class);
        esTemplate.createIndex(Book.class);
        esTemplate.putMapping(Book.class);
        esTemplate.refresh(Book.class);
    }

    @Test
    public void testSave() {

        Book book = new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
        Book testBook = bookService.save(book);

        assertNotNull(testBook.getId());
        assertEquals(testBook.getTitle(), book.getTitle());
        assertEquals(testBook.getAuthor(), book.getAuthor());
        assertEquals(testBook.getReleaseDate(), book.getReleaseDate());

    }

    @Test
    public void testFindOne() {

        Book book = new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
        bookService.save(book);

        Book testBook = bookService.findOne(book.getId());

        assertNotNull(testBook.getId());
        assertEquals(testBook.getTitle(), book.getTitle());
        assertEquals(testBook.getAuthor(), book.getAuthor());
        assertEquals(testBook.getReleaseDate(), book.getReleaseDate());

    }

    @Test
    public void testFindByTitle() {

        Book book = new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
        bookService.save(book);

        List<Book> byTitle = bookService.findByTitle(book.getTitle());
        assertThat(byTitle.size(), is(1));
    }

    @Test
    public void testFindByAuthor() {

        List<Book> bookList = new ArrayList<>();

        bookList.add(new Book("1001", "Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017"));
        bookList.add(new Book("1002", "Apache Lucene Basics", "Rambabu Posa", "13-MAR-2017"));
        bookList.add(new Book("1003", "Apache Solr Basics", "Rambabu Posa", "21-MAR-2017"));
        bookList.add(new Book("1007", "Spring Data + ElasticSearch", "Rambabu Posa", "01-APR-2017"));
        bookList.add(new Book("1008", "Spring Boot + MongoDB", "Mkyong", "25-FEB-2017"));

        for (Book book : bookList) {
            bookService.save(book);
        }

        Page<Book> byAuthor = bookService.findByAuthor("Rambabu Posa", new PageRequest(0, 10));
        assertThat(byAuthor.getTotalElements(), is(4L));

        Page<Book> byAuthor2 = bookService.findByAuthor("Mkyong", new PageRequest(0, 10));
        assertThat(byAuthor2.getTotalElements(), is(1L));

    }

    @Test
    public void testDelete() {

        Book book = new Book("Elasticsearch Basics", "Rambabu Posa", "23-FEB-2017");
        book = bookService.save(book);
        bookService.delete(book);
        Book testBook = bookService.findOne(book.getId());
        assertNull(testBook);
    }

    @Test
    public void testFindByRegex(){
        addPushkinBooks();

//        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withFilter(regexpQuery("title", ".*pokoy.*")).build();
        final SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(regexpQuery("title", ".*pokoy.*")).build();
        final List<Book> books = esTemplate.queryForList(searchQuery, Book.class);

        assertEquals(1, books.size());
    }

    private void addPushkinBooks() {
        createBook("Arap Petra Velikogo", "Alexander Pushkin", "23-FEB-1828");
        createBook("Povesti pokoynogo Ivana Petrovicha Belkina", "Alexander Pushkin", "23-FEB-1831");
        createBook("Pikovaa dama", "Alexander Pushkin", "23-FEB-1834");
        createBook("Kirjali", "Alexander Pushkin", "23-FEB-1834");
        createBook("Istoria Pugachyova", "Alexander Pushkin", "23-FEB-1834");
        createBook("Puteshestvie v Arzrum", "Alexander Pushkin", "23-FEB-1836");
        createBook("Roslavlyov", "Alexander Pushkin", "23-FEB-1836");
        createBook("Istoria sela Goryuhina", "Alexander Pushkin", "23-FEB-1837");
        createBook("Egypetskie nochi", "Alexander Pushkin", "23-FEB-1837");
        createBook("Dubrovsky", "Alexander Pushkin", "23-FEB-1841");
    }

    private void createBook(String title, String author, String releaseDate) {
        bookService.save(new Book(title, author, releaseDate));
    }
}
