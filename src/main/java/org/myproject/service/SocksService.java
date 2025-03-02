package org.myproject.service;

import org.myproject.dto.SocksDto;
import org.myproject.entity.Socks;
import org.myproject.entity.enums.Color;
import org.myproject.repository.ISocksRepository;
import org.myproject.service.enums.Operation;
import org.myproject.service.specification.SocksSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    public Long addSocks(SocksDto socks) {
        Socks socksEntity = new Socks();
        socksEntity.setColor(socks.getColor());
        socksEntity.setCottonPart(socks.getCottonPart());
        socksEntity.setQuantity(socks.getQuantity());
        return socksRepository.save(socksEntity).getId();
    }

    public String deleteSocksById(Long id) {
        socksRepository.deleteById(id);
        return socksRepository.existsById(id) ? "Socks delete" : "Socks not delete";
    }

    public List<Socks> getSocksWithParams(Color color, Operation operation, int cottonPart) {
        Specification<Socks> spec = SocksSpecification.hasColor(color)
                .and(SocksSpecification.hasCottonPart(operation, cottonPart));
        List<Socks> socks = socksRepository.findAll(spec);
        return socks;
    }
}
