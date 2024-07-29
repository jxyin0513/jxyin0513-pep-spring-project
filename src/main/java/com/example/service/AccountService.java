package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.BadRequestException;
import com.example.exception.ConflictException;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account addAccount(Account account){
        List<Account> accounts = accountRepository.findAllByUsername(account.getUsername());
        if(accounts.size()!=0){
            throw new ConflictException("Try another username.");
        }else if(account.getUsername().length()==0 || account.getPassword().length()<4){
            throw new BadRequestException("Username password format is incorrect.");
        }else{
            return accountRepository.save(account);
        }
    }

    public Account logInAccount(Account account){
        List<Account> accounts = accountRepository.findAllByUsername(account.getUsername());
        for(Account accnt:accounts){
            if(accnt.getPassword().equals(account.getPassword())){
                return accnt; 
            }
        }
        throw new UnauthorizedException("Username or password doesn't match.");
    }
}
