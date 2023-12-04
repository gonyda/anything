package com.bbsk.anything.entity;

import com.bbsk.anything.test.repository.TestRepository;
import com.bbsk.anything.test.entity.Tests;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 해당 설정을 사용하지 않으면 EmbededDatabase를 사용한다
class TestTest {

    @Autowired
    private TestRepository testRepository;

    @DisplayName("jpa 테스트")
    @Test
    void test() {
        Tests test = new Tests();
        test.setName("테스트");

        Tests saveEntity = testRepository.save(test);

        assertEquals(test.getName(), saveEntity.getName());
    }

}