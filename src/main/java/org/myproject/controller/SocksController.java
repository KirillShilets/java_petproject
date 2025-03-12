package org.myproject.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.myproject.dto.DeleteSocksResponseDto;
import org.myproject.dto.GetAllSocksResponseDto;
import org.myproject.dto.SocksDto;
import org.myproject.entity.Socks;
import org.myproject.entity.enums.Color;
import org.myproject.service.SocksService;
import org.myproject.service.enums.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
public class SocksController {

    private final SocksService socksService;
    private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    public SocksController(SocksService socksService, MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        this.socksService = socksService;
        this.mappingJackson2HttpMessageConverter = mappingJackson2HttpMessageConverter;
    }

    @PostConstruct
    public void setupObjectMapper() {
        mappingJackson2HttpMessageConverter.getObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    @GetMapping("/socks/all")
    public int getSocks() {
        return socksService.getSocks();
    }

    @GetMapping("/socks/{id}")
    public Socks getSocksById(@PathVariable Long id) {
        return socksService.getSocksById(id);
    }

    @PostMapping("/socks/income")
    public Long addSocks(@Valid @RequestBody SocksDto socks) {
        return socksService.addSocks(socks);
    }

    @DeleteMapping("/socks/outcome/{id}")
    public ResponseEntity<DeleteSocksResponseDto> deleteSocksById(@PathVariable Long id) {
        return ResponseEntity.ok(socksService.deleteSocksById(id));
    }

    @GetMapping("/socks")
    public ResponseEntity<GetAllSocksResponseDto> getSocksWithParams
            (@RequestParam Color color, @RequestParam Operation operation, @RequestParam @Min(0) @Max(100) Integer cottonPart) {
        try {
            int socksCount = socksService.getSocksWithParams(color, operation, cottonPart);
            return ResponseEntity.ok().body(new GetAllSocksResponseDto(HttpStatus.OK.value(), "Успешно!", socksCount));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new GetAllSocksResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }
}
