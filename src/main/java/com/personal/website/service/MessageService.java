package com.personal.website.service;

import com.personal.website.entity.Message;
import com.personal.website.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> findAll() {
        return messageRepository.findAllByOrderByCreatedAtDesc();
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }
}
