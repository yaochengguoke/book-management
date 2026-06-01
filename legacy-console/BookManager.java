import java.util.ArrayList;
import java.util.Scanner;

// 图书类
class Book {
    private String id;       // 编号
    private String name;     // 书名
    private String author;   // 作者
    private double price;    // 价格

    // 构造方法
    public Book(String id, String name, String author, double price) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
    }

    // getter & setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    // 显示图书信息
    @Override
    public String toString() {
        return "图书编号：" + id + " | 书名：" + name + " | 作者：" + author + " | 价格：" + price;
    }
}

// 主系统
public class BookManager {
    // 存储所有图书
    private static ArrayList<Book> bookList = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            // 菜单
            System.out.println("\n===== 图书管理系统 =====");
            System.out.println("1. 添加图书");
            System.out.println("2. 查询图书");
            System.out.println("3. 删除图书");
            System.out.println("4. 修改图书");
            System.out.println("5. 显示所有图书");
            System.out.println("0. 退出系统");
            System.out.print("请输入操作序号：");

            int choice = sc.nextInt();
            sc.nextLine(); // 吸收换行

            switch (choice) {
                case 1: addBook(); break;
                case 2: findBook(); break;
                case 3: deleteBook(); break;
                case 4: updateBook(); break;
                case 5: showAllBooks(); break;
                case 0:
                    System.out.println("感谢使用图书管理系统，再见！");
                    return;
                default:
                    System.out.println("输入错误，请重新选择！");
            }
        }
    }

    // 1. 添加图书
    private static void addBook() {
        System.out.println("\n----- 添加图书 -----");
        System.out.print("输入图书编号：");
        String id = sc.nextLine();

        // 判断编号是否重复
        for (Book b : bookList) {
            if (b.getId().equals(id)) {
                System.out.println("该编号已存在！");
                return;
            }
        }

        System.out.print("输入书名：");
        String name = sc.nextLine();
        System.out.print("输入作者：");
        String author = sc.nextLine();
        System.out.print("输入价格：");
        double price = sc.nextDouble();

        bookList.add(new Book(id, name, author, price));
        System.out.println("✅ 添加成功！");
    }

    // 2. 查询图书
    private static void findBook() {
        System.out.println("\n----- 查询图书 -----");
        System.out.print("输入要查询的图书编号：");
        String id = sc.nextLine();

        for (Book b : bookList) {
            if (b.getId().equals(id)) {
                System.out.println(b);
                return;
            }
        }
        System.out.println("❌ 未找到该图书！");
    }

    // 3. 删除图书
    private static void deleteBook() {
        System.out.println("\n----- 删除图书 -----");
        System.out.print("输入要删除的图书编号：");
        String id = sc.nextLine();

        for (Book b : bookList) {
            if (b.getId().equals(id)) {
                bookList.remove(b);
                System.out.println("✅ 删除成功！");
                return;
            }
        }
        System.out.println("❌ 未找到该图书！");
    }

    // 4. 修改图书
    private static void updateBook() {
        System.out.println("\n----- 修改图书 -----");
        System.out.print("输入要修改的图书编号：");
        String id = sc.nextLine();

        for (Book b : bookList) {
            if (b.getId().equals(id)) {
                System.out.print("新书名：");
                b.setName(sc.nextLine());
                System.out.print("新作者：");
                b.setAuthor(sc.nextLine());
                System.out.print("新价格：");
                b.setPrice(sc.nextDouble());
                System.out.println("✅ 修改成功！");
                return;
            }
        }
        System.out.println("❌ 未找到该图书！");
    }

    // 5. 显示所有图书
    private static void showAllBooks() {
        System.out.println("\n----- 所有图书 -----");
        if (bookList.isEmpty()) {
            System.out.println("暂无图书！");
            return;
        }
        for (Book b : bookList) {
            System.out.println(b);
        }
    }
}