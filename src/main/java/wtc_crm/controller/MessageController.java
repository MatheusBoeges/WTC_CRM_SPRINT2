package wtc_crm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import wtc_crm.model.Message;
import wtc_crm.service.MessageService;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MessageController {

    private final MessageService service;

    // 📩 Enviar mensagem
    @PostMapping
    public Message send(@RequestBody Message message) {

        return service.send(message);
    }

    // 📥 Inbox simples
    @GetMapping("/inbox/{customerId}")
    public List<Message> inbox(
            @PathVariable String customerId
    ) {

        return service.getInbox(customerId);
    }

    // 💬 Conversa completa
    @GetMapping("/conversation/{userId}")
    public List<Message> conversation(
            @PathVariable String userId
    ) {

        return service.getConversation(userId);
    }

    // ✅ DELIVERED
    @PutMapping("/{id}/delivered")
    public Message delivered(
            @PathVariable String id
    ) {

        return service.markAsDelivered(id);
    }

    // 👁️ READ
    @PutMapping("/{id}/read")
    public Message read(
            @PathVariable String id
    ) {

        return service.markAsRead(id);
    }

    // 🚀 Enviar para segmento
    @PostMapping("/segment")
    public List<Message> sendToSegment(
            @RequestParam String tag,
            @RequestBody Message message
    ) {

        return service.sendToSegment(tag, message);
    }
}