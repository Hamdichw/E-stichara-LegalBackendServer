package com.esticharalegal.backendServer.repository;

import com.esticharalegal.backendServer.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long>  {
}
