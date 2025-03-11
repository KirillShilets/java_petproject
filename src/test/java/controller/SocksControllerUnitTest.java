package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.myproject.Application;
import org.myproject.dto.DeleteSocksResponseDto;
import org.myproject.dto.SocksDto;
import org.myproject.entity.Socks;
import org.myproject.entity.enums.Color;
import org.myproject.exception.AdvancedRuntimeException;
import org.myproject.service.SocksService;
import org.myproject.service.enums.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class SocksControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    @Autowired
    private SocksService socksService;

    @Autowired
    private ObjectMapper objectMapper;
    private SocksDto validSocksDto;

    @BeforeEach
    void setUp() {
        validSocksDto = new SocksDto(Color.red, 50, 100);
    }

    @Test
    void testGetSocks_ReturnsCorrectCount() throws Exception {
        when(socksService.getSocks()).thenReturn(5);
        mockMvc.perform(get("/api/socks/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(socksService).getSocks();
    }

    @Test
    void testGetSocksById_Found() throws Exception {
        Socks sock = new Socks();
        sock.setId(1L);

        when(socksService.getSocksById(1L)).thenReturn(sock);
        mockMvc.perform(get("/api/socks/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(socksService).getSocksById(1L);
    }

    @Test
    void testGetSocksById_NotFound() throws Exception {
        when(socksService.getSocksById(1L)).thenThrow(new AdvancedRuntimeException(404,"Носки не найдены", "Not Found"));
        mockMvc.perform(get("/api/socks/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Носки не найдены"));

        verify(socksService).getSocksById(1L);
    }

    @Test
    void testAddSocks_Success() throws Exception {
        when(socksService.addSocks(any(SocksDto.class))).thenReturn(1L);
        ResultActions result = mockMvc.perform(post("/api/socks/income")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSocksDto)));

        result.andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(socksService).addSocks(any(SocksDto.class));
    }

    @Test
    void testAddSocks_InvalidInput() throws Exception {
        SocksDto invalidSocksDto = new SocksDto(null, -10, -50);
        mockMvc.perform(post("/api/socks/income")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidSocksDto)))
                .andExpect(status().isBadRequest());

        verify(socksService, never()).addSocks(any(SocksDto.class));
    }

    @Test
    void testDeleteSocksById_Success() throws Exception {
        DeleteSocksResponseDto response = new DeleteSocksResponseDto("Носки успешно удалены", 200);
        when(socksService.deleteSocksById(1L)).thenReturn(response);
        mockMvc.perform(delete("/api/socks/outcome/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Носки успешно удалены"));

        verify(socksService).deleteSocksById(1L);
    }

    @Test
    void testDeleteSocksById_NotFound() throws Exception {
        when(socksService.deleteSocksById(1L)).thenThrow(new AdvancedRuntimeException(404,"Таких носков не существует", "Not Found"));
        mockMvc.perform(delete("/api/socks/outcome/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Таких носков не существует"));

        verify(socksService).deleteSocksById(1L);
    }

    @Test
    void testGetSocksWithParams_Success() throws Exception {
        when(socksService.getSocksWithParams(eq(Color.red), eq(Operation.equal), eq(50))).thenReturn(3);
        mockMvc.perform(get("/api/socks")
                        .param("color", "red")
                        .param("operation", "equal")
                        .param("cottonPart", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Успешно!"))
                .andExpect(jsonPath("$.size").value(3));

        verify(socksService).getSocksWithParams(eq(Color.red), eq(Operation.equal), eq(50));
    }

    @Test
    void testGetSocksWithParams_Exception() throws Exception {
        when(socksService.getSocksWithParams(eq(Color.red), eq(Operation.equal), eq(50)))
                .thenThrow(new AdvancedRuntimeException(500, "Internal Server Error", "Ошибка при получении носков"));
        mockMvc.perform(get("/api/socks")
                        .param("color", "red")
                        .param("operation", "equal")
                        .param("cottonPart", "50"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.statusCode").value(500))
                .andExpect(jsonPath("$.message").value("Ошибка при получении носков"));

        verify(socksService).getSocksWithParams(eq(Color.red), eq(Operation.equal), eq(50));
    }
}