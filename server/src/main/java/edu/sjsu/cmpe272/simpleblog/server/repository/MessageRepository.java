package edu.sjsu.cmpe272.simpleblog.server.repository;


import edu.sjsu.cmpe272.simpleblog.server.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByMessageIdLessThanEqualOrderByMessageIdDesc(Long id, Pageable pageable);

}
