package wtc_crm.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wtc_crm.model.Message;

import java.util.List;

@Repository
public interface MessageRepository
        extends MongoRepository<Message, String> {

    // Inbox simples
    List<Message> findByReceiverId(String receiverId);

    // Conversa completa estilo chat
    List<Message> findBySenderIdOrReceiverId(
            String senderId,
            String receiverId
    );
}