package com.SonarQubePoc.SonarQubePoc.util;


import java.time.Period;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
public class DifferenceBetweenDatesTest {

    @Autowired
    DifferenceBetweenDates differenceBetweenDates;

    @Test
    public void calculateYearsOfIndependency() {
        //Arrange
        differenceBetweenDates = new DifferenceBetweenDates();
        String independenceDay = "15/09/1821";

        //Act
        Period result = differenceBetweenDates.calculateYearsOfIndependency(independenceDay);

        //Assert
        Assertions.assertEquals(2,result.getDays() );
        Assertions.assertEquals(3,result.getMonths() );
        Assertions.assertEquals(199,result.getYears() );
    }
}