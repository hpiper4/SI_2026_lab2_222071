import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SI2026Lab2Test {

    // Helper method to avoid repetition
    private Library createLibrary() {
        Library library = new Library();

        library.addBook(new Book("Clean Code", "Robert", "Programming"));
        library.addBook(new Book("Effective Java", "Joshua", "Programming"));
        library.addBook(new Book("The Hobbit", "J.R.R. Tolkien", "Fantasy"));

        return library;
    }

    // =========================
    // SEARCH BOOK TESTS
    // =========================

    @Test
    public void searchBookEveryStatementTest_validBookFound() {
        Library library = createLibrary();

        List<Book> result = library.searchBookByTitle("Clean Code");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
    }

    @Test
    public void searchBookEveryStatementTest_notFound() {
        Library library = createLibrary();

        List<Book> result = library.searchBookByTitle("Unknown Book");

        assertNull(result);
    }

    @Test
    public void searchBookEveryStatementTest_emptyTitle() {
        Library library = createLibrary();

        assertThrows(IllegalArgumentException.class, () -> {
            library.searchBookByTitle("");
        });
    }

    // =========================
    // BORROW BOOK TESTS
    // =========================

    @Test
    public void borrowBookEveryBranchTest_emptyInput() {
        Library library = createLibrary();

        assertThrows(IllegalArgumentException.class, () -> {
            library.borrowBook("", "");
        });
    }

    @Test
    public void borrowBookEveryBranchTest_successfulBorrow() {
        Library library = createLibrary();

        library.borrowBook("Clean Code", "Robert");

        assertTrue(
                library.searchBookByTitle("Clean Code") == null
        );
    }

    @Test
    public void borrowBookEveryBranchTest_alreadyBorrowed() {
        Library library = createLibrary();

        library.borrowBook("Clean Code", "Robert");

        assertThrows(RuntimeException.class, () -> {
            library.borrowBook("Clean Code", "Robert");
        });
    }

    @Test
    public void borrowBookEveryBranchTest_notFound() {
        Library library = createLibrary();

        assertThrows(RuntimeException.class, () -> {
            library.borrowBook("Nonexistent", "Unknown");
        });
    }

    // =========================
    // MULTIPLE CONDITION TESTS
    // =========================

    // borrowBook: title.isEmpty() || author.isEmpty()
    @Test
    public void borrowBookMultipleConditionTest() {
        Library library = createLibrary();

        // (T, T)
        assertThrows(IllegalArgumentException.class, () -> {
            library.borrowBook("", "");
        });

        // (T, F)
        assertThrows(IllegalArgumentException.class, () -> {
            library.borrowBook("", "Robert");
        });

        // (F, T)
        assertThrows(IllegalArgumentException.class, () -> {
            library.borrowBook("Clean Code", "");
        });

        // (F, F) -> valid path, should not throw here due to other logic
        assertDoesNotThrow(() -> {
            library.borrowBook("Clean Code", "Robert");
        });
    }

    // searchBookByTitle: book.getTitle().equalsIgnoreCase(title) && !book.isBorrowed()
    @Test
    public void searchBookMultipleConditionTest() {
        Library library = createLibrary();

        // Book exists and not borrowed → (T, T)
        List<Book> result1 = library.searchBookByTitle("Clean Code");
        assertNotNull(result1);

        // Borrow book first
        library.borrowBook("Clean Code", "Robert");

        // Now book is borrowed → (T, F)
        List<Book> result2 = library.searchBookByTitle("Clean Code");
        assertNull(result2);

        // Non-existing title → (F, X)
        List<Book> result3 = library.searchBookByTitle("Unknown");
        assertNull(result3);
    }
}