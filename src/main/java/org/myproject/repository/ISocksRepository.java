package org.myproject.repository;

import org.myproject.entity.Socks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ISocksRepository extends JpaRepository<Socks, Long>, JpaSpecificationExecutor<Socks> {

}
