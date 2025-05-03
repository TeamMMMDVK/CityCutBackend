package com.example.citycutbackend.Test030525;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IAmTestingMyselfTest {
    IAmTestingMyself testObject;

    @BeforeEach
    public void setUp() {
        testObject = new IAmTestingMyself();
        testObject.setName("I am a test object");
        testObject.setNum(2);
        testObject.setNum2(5);
    }

    @Test
    void mathing() {
        //Arrange


        //Act
        int expected = 7; // We set this
        int actual = testObject.mathing();
//        System.out.println(expected);
//        System.out.println(actual);

        //Assert
        Assertions.assertEquals(expected, actual);

    }

    @Test
    void getName() {
        //Arrange

        //Act
        String expectedName = "I am a test object";

        //Assert
        Assertions.assertEquals(expectedName, testObject.getName());


    }

    @Test
    void getNum() {
        //Arrange

        //Act
        int expectedNum = 2;

        //Assert
        assertEquals(expectedNum, testObject.getNum());

    }

    @Test
    void getNum2() {
        //Arrange

        //Act
        int expectedNum = 5;

        //Assert
        assertEquals(expectedNum, testObject.getNum2());
    }
    @Test
    void yetAnotherTest() {
        String str = "Hello";

        assertEquals("Hello", str);
    }
}