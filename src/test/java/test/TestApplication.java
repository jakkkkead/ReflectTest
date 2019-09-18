package test;

import com.sun.glass.ui.Application;
import com.web.service.ActiveMQService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class TestApplication {
    @Autowired
    private ActiveMQService activeMQService;
    @Test
    public void testActiveMQ(){
        activeMQService.sendQueMessage();
        activeMQService.sendTopicMessage();
    }
    @Test
    public void test2(){
        System.out.println("kdsjfl;a");
    }

}
