package com.example.md_back.service;

import com.example.md_back.dto.WordDto;
import com.example.md_back.mappers.WordMapper;
import com.example.md_back.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class WordService {

    @Autowired
    private WordMapper wordMapper;

    @Transactional
    public void insertWord(Approval approval) {
        Word word = new Word();
        word.approvalToWord(approval);
        wordMapper.insertWord(word);
    }

    @Transactional
    public void updateWord(Approval approval) {
        Word word = wordMapper.getWordById(approval.getTargetId());
        if (word == null) throw new IllegalArgumentException("단어 수정 실패 : 단어를 찾을 수 없습니다.");
        else if (word.isDeleteStatus()) throw new IllegalArgumentException("단어 삭제 실패 : 이미 삭제된 단어입니다.");

        word.approvalToWord(approval);
        wordMapper.updateWord(word);
    }

    @Transactional
    public void deleteWord(Approval approval) {
        Word word = wordMapper.getWordById(approval.getTargetId());
        if (word == null) throw new IllegalArgumentException("단어 삭제 실패 : 단어를 찾을 수 없습니다.");
        else if (word.isDeleteStatus()) throw new IllegalArgumentException("단어 삭제 실패 : 이미 삭제된 단어입니다.");

        // show related Terms
        word.approvalToWord(approval);
        wordMapper.deleteWord(word);
        // change deleteStatus True
    }


    @Transactional
    public Approval dtoToApproval(User user, WordDto wordDto, int targetId) {  // UPDATE
        Word word = wordMapper.getWordById(targetId);
        if (wordDto.getEngName() == null)throw new IllegalArgumentException("결재 생성 실패 : 영문명이 공백입니다.");
        if (word == null) throw new IllegalArgumentException("결재 생성 실패 : 단어를 찾을 수 없습니다.");
        else if (word.isDeleteStatus()) throw new IllegalArgumentException("결재 생성 실패 : 이미 삭제된 단어입니다.");

        Approval approval = new Approval();
        approval.setCreateUser(user);
        approval.setTargetId(targetId);
        approval.setApprovalType(ApprovalType.UPDATE);
        approval.setWordType(WordType.WORD);
        // headers

        if (Objects.equals(word.getEngName(), wordDto.getEngName())) approval.setEngName(null);
        else approval.setEngName(wordDto.getEngName());

        if (Objects.equals(word.getKorName(), wordDto.getKorName())) approval.setKorName(null);
        else approval.setKorName(wordDto.getKorName());

        if (Objects.equals(word.getShortName(), wordDto.getShortName())) approval.setShortName(null);
        else approval.setShortName(wordDto.getShortName());

        if (Objects.equals(word.getMeaning(), wordDto.getMeaning())) approval.setMeaning(null);
        else approval.setMeaning(wordDto.getMeaning());
        // body
        return approval;
    }

    @Transactional
    public Approval dtoToApproval(User user, WordDto wordDto) {  // CREATE
        if (wordDto.getEngName() == null)throw new IllegalArgumentException("결재 생성 실패 : 영문명이 공백입니다.");
        Approval approval = new Approval();
        approval.setCreateUser(user);
        approval.setTargetId(0);
        approval.setApprovalType(ApprovalType.CREATE);
        approval.setWordType(WordType.WORD);
        // headers
        approval.setEngName(wordDto.getEngName());
        approval.setKorName(wordDto.getKorName());
        approval.setShortName(wordDto.getShortName());
        approval.setMeaning(wordDto.getMeaning());
        // body
        return approval;
    }

    @Transactional
    public Approval dtoToApproval(User user, int targetId) { // DELETE
        Word word = wordMapper.getWordById(targetId);
        if (word == null) throw new IllegalArgumentException("결재 생성 실패 : 단어가 존재하지 않습니다.");
        else if (word.isDeleteStatus()) throw new IllegalArgumentException("결재 생성 실패 : 이미 삭제된 단어입니다.");

        Approval approval = new Approval();
        approval.setCreateUser(user);
        approval.setTargetId(targetId);
        approval.setApprovalType(ApprovalType.DELETE);
        approval.setWordType(WordType.WORD);
        // headers
        return approval;
    }

    @Transactional
    public void deleteWordDB(int wordId) {
        wordMapper.deleteWordDB(wordId);
    }

    @Transactional(readOnly = true)
    public Word findById(int wordId) {
        return wordMapper.getWordById(wordId);
    }

    @Transactional(readOnly = true)
    public List<Word> findByName(String name) {
        return wordMapper.getWordsByName(name);
    }

    @Transactional(readOnly = true)
    public List<Word> getWordListByUserId(int userId) {
        return wordMapper.getWordListByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Word> getWords() {
        return wordMapper.getWords();
    }
}
