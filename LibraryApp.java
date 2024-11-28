package lmsmain;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class LibraryApp extends JFrame {
    private ArrayList<Book> books;
    private JList<String> bookList;
    private DefaultListModel<String> listModel;

    public LibraryApp() {
        books = new ArrayList<>();
        listModel = new DefaultListModel<>();
        
        // Add some sample books
        // Format Title, Author
        books.add(new Book("The Pragmatic Programmer", "Andrew Hunt and David Thomas"));
        
        // Update list model
        updateBookList();

        // Set up the frame
        setTitle("Library Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create book list
        bookList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(bookList);
        add(scrollPane, BorderLayout.CENTER);

        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Book");
        JButton checkOutButton = new JButton("Check Out");
        JButton checkInButton = new JButton("Check In");
        
        buttonPanel.add(addButton);
        buttonPanel.add(checkOutButton);
        buttonPanel.add(checkInButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add button listeners with proper error handling
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addBook();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LibraryApp.this,
                        "Error adding book: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        checkOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkOutBook();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LibraryApp.this,
                        "Error checking out book: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        checkInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkInBook();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LibraryApp.this,
                        "Error checking in book: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void updateBookList() {
        listModel.clear();
        for (Book book : books) {
            listModel.addElement(book.toString());
        }
    }

    private void addBook() {
        String title = JOptionPane.showInputDialog(this, "Enter book title:");
        if (title != null && !title.trim().isEmpty()) {
            String author = JOptionPane.showInputDialog(this, "Enter book author:");
            if (author != null && !author.trim().isEmpty()) {
                title = title.trim();
                author = author.trim();
                
                // Check if book already exists
                boolean bookExists = false;
                for (Book book : books) {
                    if (book.getTitle().equalsIgnoreCase(title) && 
                        book.getAuthor().equalsIgnoreCase(author)) {
                        book.incrementQuantity();
                        bookExists = true;
                        JOptionPane.showMessageDialog(this,
                            "Increased quantity of existing book: " + title + " by " + author);
                        break;
                    }
                }
                
                if (!bookExists) {
                    books.add(new Book(title, author));
                    JOptionPane.showMessageDialog(this,
                        "Successfully added: " + title + " by " + author);
                }
                updateBookList();
            }
        }
    }

    private void checkOutBook() {
        int selectedIndex = bookList.getSelectedIndex();
        if (selectedIndex != -1) {
            Book selectedBook = books.get(selectedIndex);
            if (!selectedBook.isCheckedOut() && selectedBook.getQuantity() > 0) {
                selectedBook.checkOut();
                JOptionPane.showMessageDialog(this, 
                    "Successfully checked out: " + selectedBook.getTitle());
                updateBookList();
            } else {
                JOptionPane.showMessageDialog(this,
                    "No copies of this book are available!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a book first!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkInBook() {
        int selectedIndex = bookList.getSelectedIndex();
        if (selectedIndex != -1) {
            Book selectedBook = books.get(selectedIndex);
            selectedBook.checkIn();
            JOptionPane.showMessageDialog(this,
                "Successfully checked in: " + selectedBook.getTitle());
            updateBookList();
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a book first!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryApp app = new LibraryApp();
            app.setVisible(true);
        });
    }
}

class Book {
    private String title;
    private String author;
    private boolean checkedOut;
    private int quantity;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.checkedOut = false;
        this.quantity = 1;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public int getQuantity() {
        return quantity;
    }

    public void incrementQuantity() {
        quantity++;
    }

    public void checkOut() {
        if (quantity > 0) {
            quantity--;
            checkedOut = quantity == 0;
        }
    }

    public void checkIn() {
        quantity++;
        checkedOut = false;
    }

    @Override
    public String toString() {
        return title + " by " + author + 
               " (Available: " + quantity + ")" +
               (checkedOut ? " (All Checked Out)" : "");
    }
}
