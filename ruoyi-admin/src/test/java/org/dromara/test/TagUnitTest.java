package org.dromara.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 标签单元测试案例
 *
 * @author Lion Li
 */
@SpringBootTest
@DisplayName("标签单元测试案例")
public class TagUnitTest {

    @Tag("dev")
    @DisplayName("测试 @Tag dev")
    @Test
    public void testTagDev() {
        System.out.println("dev");
    }

    @Tag("prod")
    @DisplayName("测试 @Tag prod")
    @Test
    public void testTagProd() {
        System.out.println("prod");
    }

    @Tag("local")
    @DisplayName("测试 @Tag local")
    @Test
    public void testTagLocal() {
        System.out.println("local");
    }

    @Tag("exclude")
    @DisplayName("测试 @Tag exclude")
    @Test
    public void testTagExclude() {
        System.out.println("exclude");
    }

    @BeforeEach
    public void testBeforeEach() {
        System.out.println("@BeforeEach ==================");
    }

    @AfterEach
    public void testAfterEach() {
        System.out.println("@AfterEach ==================");
    }


}
