package org.myproject.unit.service;

import org.junit.jupiter.api.Test;
import org.myproject.Application;
import org.myproject.dto.DeleteSocksResponseDto;
import org.myproject.dto.SocksDto;
import org.myproject.entity.Socks;
import org.myproject.entity.enums.Color;
import org.myproject.exception.AdvancedRuntimeException;
import org.myproject.repository.ISocksRepository;
import org.myproject.service.SocksService;
import org.myproject.service.enums.Operation;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class SocksServiceUnitTest {

    @InjectMocks
    private SocksService socksService;

    @Mock
    private ISocksRepository socksRepository;

    @Test
    void testGetSocks_ReturnsCorrectCount() {
        List<Socks> sockList = Arrays.asList(new Socks(), new Socks());
        when(socksRepository.findAll()).thenReturn(sockList);
        int count = socksService.getSocks();

        assertEquals(2, count, "Количество носков должно быть равно 2");
        verify(socksRepository).findAll();
    }

    @Test
    void testGetSocksById_Found() {
        Socks sock = new Socks();
        sock.setId(1L);
        when(socksRepository.findById(1L)).thenReturn(Optional.of(sock));
        Socks result = socksService.getSocksById(1L);

        assertNotNull(result, "Найденный объект не должен быть null");
        assertEquals(1L, result.getId(), "id носков должен быть 1");
        verify(socksRepository).findById(1L);
    }

    @Test
    void testGetSocksById_NotFound() {
        when(socksRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EmptyResultDataAccessException.class, () -> {
            socksService.getSocksById(1L);
        }, "Должно быть выброшено исключение EmptyResultDataAccessException");

        verify(socksRepository).findById(1L);
    }

    @Test
    void testAddSocks_Success() {
        SocksDto dto = new SocksDto(Color.red, 50, 100);
        Socks savedSock = new Socks();
        savedSock.setId(1L);
        when(socksRepository.save(any(Socks.class))).thenReturn(savedSock);

        Long returnedId = socksService.addSocks(dto);

        assertEquals(1L, returnedId, "Возвращаемый ID должен быть равен 1");
        verify(socksRepository).save(any(Socks.class));
    }

    @Test
    void testAddSocks_ExceptionThrow() {
        SocksDto dto = new SocksDto(Color.red, 50,100);
        when(socksRepository.save(any(Socks.class))).thenThrow(new RuntimeException());

        AdvancedRuntimeException ex = assertThrows(AdvancedRuntimeException.class, () -> {
            socksService.addSocks(dto);
        }, "Должно быть выброшено AdvancedRuntimeException");

        assertEquals(500, ex.getCode());

        verify(socksRepository).save(any(Socks.class));
    }

    @Test
    void testDeleteSocksById_Success() {
        Long sockId = 1L;
        when(socksRepository.existsById(sockId)).thenReturn(true);

        DeleteSocksResponseDto response = socksService.deleteSocksById(sockId);

        assertNotNull(response, "Ответ не должен быть null");
        assertEquals("Носки успешно удалены", response.getMessage(), "Сообщение об успешном удалении не совпадает");
        assertEquals(200, response.getCode(), "Статус должен быть HTTP 200");

        verify(socksRepository).existsById(sockId);
        verify(socksRepository).deleteById(sockId);
    }

    @Test
    void testDeleteSocksById_NotFound() {
        Long sockId = 1L;
        when(socksRepository.existsById(sockId)).thenReturn(false);

        AdvancedRuntimeException ex = assertThrows(AdvancedRuntimeException.class, () -> {
            socksService.deleteSocksById(sockId);
        }, "Должно быть выброшено AdvancedRuntimeException");

        assertEquals(404, ex.getCode());
        verify(socksRepository).existsById(sockId);
        verify(socksRepository, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteSocksById_ExceptionInDelete() {
        Long sockId = 1L;
        when(socksRepository.existsById(sockId)).thenReturn(true);
        doThrow(new RuntimeException("Delete error")).when(socksRepository).deleteById(sockId);

        AdvancedRuntimeException ex = assertThrows(AdvancedRuntimeException.class, () -> {
            socksService.deleteSocksById(sockId);
        }, "Должно быть выброшено AdvancedRuntimeException");

        assertEquals(500, ex.getCode());
        verify(socksRepository).existsById(sockId);
        verify(socksRepository).deleteById(sockId);
    }

    @Test
    void testGetSocksWithParams_Success() {
        Color color = Color.red;
        Operation operation = Operation.equal;
        int cottonPart = 80;

        List<Socks> sockList = Collections.singletonList(new Socks());
        when(socksRepository.findAll(any(Specification.class))).thenReturn(sockList);

        int count = socksService.getSocksWithParams(color, operation, cottonPart);

        assertEquals(1, count, "Количество найденных записей должно быть равно 1");
        verify(socksRepository).findAll(any(Specification.class));
    }
}