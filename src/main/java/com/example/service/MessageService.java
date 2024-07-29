package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.BadRequestException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;
    
    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message addMessage(Message message){
        Optional<Account> account = accountRepository.findById(message.getPostedBy());
        if(account.isPresent() && message.getMessageText().length()>0 && message.getMessageText().length()<=255){
            return messageRepository.save(message);
        }
        throw new BadRequestException("Can't create new message.");
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId){
        return messageRepository.findById(messageId).orElse(null);
    }

    public Integer deleteMessageById(int messageId){
        Optional<Message> message = messageRepository.findById(messageId);
        if(message.isPresent()){
            messageRepository.delete(message.get());
            return 1;
        }else{
            return null;
        }
    }
    
    public Integer patchMessageById(int messageId, Message message){
        Optional<Message> msg = messageRepository.findById(messageId);
        if(msg.isPresent() && message.getMessageText().length()>0 && message.getMessageText().length()<=255){
            msg.get().setMessageText(message.getMessageText());
            if(message.getPostedBy()!=null){
                msg.get().setPostedBy(message.getPostedBy());
            }
            if(message.getTimePostedEpoch() != null){
                msg.get().setTimePostedEpoch(message.getTimePostedEpoch());
            }
            messageRepository.save(msg.get());
            return 1;
        }else{
            throw new BadRequestException("Can't update message, try again.");
        }
    }

    public List<Message> getAllMessagesByAccountId(int accountId){
        return messageRepository.findAllByPostedBy(accountId);
    }

}
