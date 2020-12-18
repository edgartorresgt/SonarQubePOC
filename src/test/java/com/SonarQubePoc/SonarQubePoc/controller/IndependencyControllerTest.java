package com.SonarQubePoc.SonarQubePoc.controller;

import com.SonarQubePoc.SonarQubePoc.models.Country;
import com.SonarQubePoc.SonarQubePoc.models.CountryResponse;
import com.SonarQubePoc.SonarQubePoc.repositories.CountryRepository;
import com.SonarQubePoc.SonarQubePoc.util.DifferenceBetweenDates;
import java.util.Optional;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@AutoConfigureMockMvc
public class IndependencyControllerTest {
    @BeforeEach
    public void initEach(){
        System.out.println("BeforeEach initEach() method called");
    }
    @Autowired
    CountryResponse countryResponse;
    @Autowired
    Optional<Country> country;

    CountryRepository countryRepositoryMock = Mockito.mock(CountryRepository.class);

    @Autowired
    DifferenceBetweenDates differenceBetweenDates = new DifferenceBetweenDates();

    @Autowired
    IndependencyController independencyController = new IndependencyController(countryRepositoryMock, differenceBetweenDates);

    @Test
    public void getCountryDetailsWithValidCountryCode() {
        //Arrange
        ResponseEntity<CountryResponse> respuestaServicio;
        Country mockCountry = new Country();
        mockCountry.setIsoCode("GT");
        mockCountry.setCountryIdependenceDate("15/09/1821");
        mockCountry.setCountryId((long) 1);
        mockCountry.setCountryName("Guatemala");
        mockCountry.setCountryCapital("Ciudad de Guatemala");
        Mockito.when(countryRepositoryMock.findCountryByIsoCode("GT")).thenReturn(mockCountry);

        //Act
        respuestaServicio = independencyController.getCountryDetails("GT");

        //Assert
        Assertions.assertEquals("Guatemala",respuestaServicio.getBody().getCountryName());
    }

    @Test
    public void getCountryDetailsWithInvalidCountryCode() {
        //Arrange
        ResponseEntity<CountryResponse> respuestaServicio;

        //Act
        respuestaServicio = independencyController.getCountryDetails("IT");

        //Assert
        Assertions.assertNull(respuestaServicio.getBody().getCountryName());
    }


}