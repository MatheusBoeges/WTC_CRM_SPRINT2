package wtc_crm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import wtc_crm.model.Customer;
import wtc_crm.model.Message;
import wtc_crm.model.MessageStatus;
import wtc_crm.repository.CustomerRepository;
import wtc_crm.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository repository;

    private final SimpMessagingTemplate messagingTemplate;

    private final CustomerRepository customerRepository;

    // 📩 Enviar mensagem 1:1
    public Message send(Message message) {

        message.setTimestamp(LocalDateTime.now());

        message.setStatus(MessageStatus.SENT);

        Message saved = repository.save(message);

        log.info(
                "Mensagem enviada de {} para {}",
                message.getSenderId(),
                message.getReceiverId()
        );

        // realtime websocket
        messagingTemplate.convertAndSend(
                "/topic/messages/" + message.getReceiverId(),
                saved
        );

        return saved;
    }

    // 📥 Inbox simples
    public List<Message> getInbox(String customerId) {

        log.info("Buscando inbox do cliente {}", customerId);

        return repository.findByReceiverId(customerId);
    }

    // 💬 Conversa completa
    public List<Message> getConversation(String userId) {

        log.info("Buscando conversa completa do usuário {}", userId);

        return repository.findBySenderIdOrReceiverId(
                userId,
                userId
        );
    }

    // 🚀 Envio por segmento
    public List<Message> sendToSegment(
            String tag,
            Message message
    ) {

        List<Customer> customers =
                customerRepository.findByTagsContaining(tag);

        log.info(
                "Enviando campanha para segmento {} com {} clientes",
                tag,
                customers.size()
        );

        return customers.stream().map(customer -> {

            Message msg = new Message();

            msg.setSenderId(message.getSenderId());

            msg.setReceiverId(customer.getId());

            msg.setContent(message.getContent());

            msg.setTimestamp(LocalDateTime.now());

            message.setStatus(MessageStatus.SENT);

            Message saved = repository.save(msg);

            // realtime websocket
            messagingTemplate.convertAndSend(
                    "/topic/messages/" + customer.getId(),
                    saved
            );

            return saved;

        }).toList();
    }

    // ✅ DELIVERED
    public Message markAsDelivered(String id) {

        Message msg = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Mensagem não encontrada"
                        )
                );

        msg.setStatus(MessageStatus.DELIVERED);

        log.info("Mensagem {} DELIVERED", id);

        return repository.save(msg);
    }

    // 👁️ READ
    public Message markAsRead(String id) {

        Message msg = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Mensagem não encontrada"
                        )
                );

        msg.setStatus(MessageStatus.READ);

        log.info("Mensagem {} READ", id);

        return repository.save(msg);
    }
}