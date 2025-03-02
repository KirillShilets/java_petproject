package org.myproject.service.specification;

import org.myproject.entity.Socks;
import org.myproject.entity.enums.Color;
import org.myproject.service.enums.Operation;
import org.springframework.data.jpa.domain.Specification;

public class SocksSpecification {

    public static Specification<Socks> hasColor(Color color) {
        return (root, query, criteriaBuilder) -> {
            if (color == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("color"), color);
        };
    }

    public static Specification<Socks> hasCottonPart(Operation operation, int cottonPart) {
        return (root, query, criteriaBuilder) -> {
            if (operation == null) {
                return criteriaBuilder.conjunction();
            }

            switch (operation) {
                case equal:
                    return criteriaBuilder.equal(root.get("cottonPart"), cottonPart);
                case moreThan:
                    return criteriaBuilder.greaterThan(root.get("cottonPart"), cottonPart);
                case lessThan:
                    return criteriaBuilder.lessThan(root.get("cottonPart"), cottonPart);
                default:
                    return criteriaBuilder.conjunction();
            }
        };
    }
}