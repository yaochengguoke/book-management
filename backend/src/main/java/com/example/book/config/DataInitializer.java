package com.example.book.config;

import com.example.book.entity.*;
import com.example.book.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepo, BookRepository bookRepo, PasswordEncoder encoder) {
        return args -> {
            if (!userRepo.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin"));
                admin.setRole("ADMIN");
                userRepo.save(admin);

                User user = new User();
                user.setUsername("reader");
                user.setPassword(encoder.encode("123456"));
                user.setRole("USER");
                userRepo.save(user);
            }

            if (bookRepo.count() == 0) {
                String[][] books = {
                    {"Java编程思想","Bruce Eckel","9787111459835","108","15","技术","https://img3.doubanio.com/view/subject/l/public/s27243442.jpg","Java领域经典之作"},
                    {"Spring实战","Craig Walls","9787115531315","89","10","技术","https://img1.doubanio.com/view/subject/l/public/s33791559.jpg","Spring框架从入门到精通"},
                    {"深入理解计算机系统","Randal E.Bryant","9787111544937","139","8","技术","https://img2.doubanio.com/view/subject/l/public/s29119723.jpg","CSAPP计算机科学殿堂级教材"},
                    {"软件测试与质量控制","Ron Patton","9787111342908","69","20","技术","https://img9.doubanio.com/view/subject/l/public/s4468484.jpg","软件测试工程师必读"},
                    {"百年孤独","加西亚·马尔克斯","9787544253994","55","5","文学","https://img1.doubanio.com/view/subject/l/public/s27603648.jpg","魔幻现实主义不朽经典"},
                    {"三体","刘慈欣","9787536692930","45","12","文学","https://img3.doubanio.com/view/subject/l/public/s27628263.jpg","中国科幻文学里程碑"},
                    {"高等数学","同济大学","9787040396638","49","30","教材","https://img2.doubanio.com/view/subject/l/public/s28276923.jpg","理工科数学基础教材"},
                };
                for (String[] b : books) {
                    Book book = new Book();
                    book.setTitle(b[0]); book.setAuthor(b[1]); book.setIsbn(b[2]);
                    book.setPrice(Double.parseDouble(b[3])); book.setQuantity(Integer.parseInt(b[4]));
                    book.setCategory(b[5]); book.setCoverImage(b[6]); book.setPublisher(b[7]);
                    book.setDescription(b.length > 8 ? b[8] : "");
                    bookRepo.save(book);
                }
            }
        };
    }
}
