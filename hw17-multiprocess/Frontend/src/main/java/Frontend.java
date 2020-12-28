import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("hw17")
@SpringBootApplication
public class Frontend {
    public static void main(String[] args) {
        SpringApplication.run(Frontend.class, args);
    }
}
