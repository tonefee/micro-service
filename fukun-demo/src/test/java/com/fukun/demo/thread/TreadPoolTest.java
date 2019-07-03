package com.fukun.demo.thread;

import com.fukun.demo.FukunDemoApplication;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@ActiveProfiles("local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {FukunDemoApplication.class})
public class TreadPoolTest {

    @Test
    public void contextLoads1() {
        System.out.println("测试中2");
        TestCase.assertEquals(1, 1);
    }

}
