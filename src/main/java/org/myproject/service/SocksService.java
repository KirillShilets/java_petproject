package org.myproject.service;

import org.myproject.dto.SocksDto;
import org.myproject.entity.Socks;
import org.myproject.entity.enums.Color;
import org.myproject.exception.AdvancedRuntimeException;
import org.myproject.repository.ISocksRepository;
import org.myproject.service.enums.Operation;
import org.myproject.service.specification.SocksSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SocksService {

    @Autowired
    private ISocksRepository socksRepository;

    public List<Socks> getSocks() {
        return socksRepository.findAll();
    }

    public Socks getSocksById(Long id) {
        return socksRepository.findById(id).orElse(null);
    }

    @Transactional
    public Long addSocks(SocksDto socks) {
        try {
            Socks socksEntity = new Socks();
            socksEntity.setColor(socks.getColor());
            socksEntity.setCottonPart(socks.getCottonPart());
            socksEntity.setQuantity(socks.getQuantity());
            return socksRepository.save(socksEntity).getId();
        } catch (Exception e) {
            throw new AdvancedRuntimeException(500,"Ошибка сервера", e.getMessage());
        }

    }

    @Transactional
    public String deleteSocksById(Long id) {
        try {
            socksRepository.deleteById(id);
            return "Носки успешно удалены.";
        } catch (EmptyResultDataAccessException e) {
            return "Носки с указанным id: " + id + "не найдены";
        } catch (Exception e) {
            throw new AdvancedRuntimeException(500,"Ошибка сервера", e.getMessage());
        }
    }

    public int getSocksWithParams(Color color, Operation operation, int cottonPart) {
        Specification<Socks> specification = SocksSpecification.hasColor(color)
                .and(SocksSpecification.hasCottonPart(operation, cottonPart));
        return socksRepository.findAll(specification).size();
    }
}
