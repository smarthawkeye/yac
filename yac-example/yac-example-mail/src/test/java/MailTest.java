import io.github.smarthawkeye.core.mail.core.MailTemplate;
import com.yac.example.mail.MailApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @ClassName MailTest
 * @Description 描述
 * @Author xiaoya - https://github.com/an0701/ya-java
 * @Date 2022/8/26 13:20
 * @Version V0.1.0
 */
@SpringBootTest(classes = MailApplication.class)
public class MailTest {
    @Resource
    MailTemplate template;
    @Test
    void send(){
        try {
           // template.sendSimpleMail("an0701@163.com","abcasdfaf","asdfasdfasdfadsf");

          //  template.sendSimpleMail("coderzzg@163.com","abcasdfaf","asdfasdfasdfadsf");

            template.sendSimpleMail("278729535@qq.com","abcasdfaf","asdfasdfasdfadsf");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
