package com.example.md_back.service;

import com.example.md_back.dto.CodeDto;
import com.example.md_back.dto.DomainDto;
import com.example.md_back.mappers.CodeMapper;
import com.example.md_back.mappers.DomainMapper;
import com.example.md_back.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class DomainService {

    @Autowired
    private DomainMapper domainMapper;

    @Autowired
    private CodeMapper codeMapper;

    @Transactional
    public void insertDomain(Approval approval) {
        Domain domain = new Domain();
        domain.approvalToDomain(approval);
        domainMapper.insertDomain(domain);
    }

    @Transactional
    public void updateDomain(Approval approval) {
        Domain domain = domainMapper.getDomainById(approval.getTargetId());
        if (domain == null) {
            System.out.println("도메인 수정 실패 : 도메인을 찾을 수 없습니다.");
            return;
        }
        domain.approvalToDomain(approval);
        domainMapper.updateDomain(domain);
    }

    @Transactional
    public void deleteDomain(Approval approval) {
        Domain domain = domainMapper.getDomainById(approval.getTargetId());
        if (domain == null) {
            System.out.println("도메인 삭제 실패 : 도메인을 찾을 수 없습니다.");
            return;
        }
        domain.approvalToDomain(approval);
        domainMapper.deleteDomain(domain);
    }

    @Transactional
    public void deleteDomainDB(int domainId) {
        domainMapper.deleteDomainDB(domainId);
    }

    @Transactional
    public void addCode(Approval approval) {
        Domain domain = domainMapper.getDomainById(approval.getSlaveId());
        if (domain == null) {
            System.out.println("코드 추가 실패 : 도메인을 찾을 수 없습니다.");
            return;
        }
        Code code = new Code();
        code.approvalToCode(approval);
        codeMapper.insertCode(code);
        domain.setModifyUser(approval.getCreateUser());
        domain.setModifyDate(approval.getCreateDate());
        domainMapper.updateDomain(domain);
    }

    @Transactional
    public void updateCode(Approval approval) {
        Domain domain = domainMapper.getDomainById(approval.getSlaveId());
        if (domain == null) {
            System.out.println("코드 수정 실패 : 도메인을 찾을 수 없습니다.");
            return;
        }
        Code code = codeMapper.getCodeById(approval.getTargetId());
        if (code == null) {
            System.out.println("코드 수정 실패 : 코드를 찾을 수 없습니다.");
            return;
        }
        code.approvalToCode(approval);
        domain.setModifyUser(approval.getCreateUser());
        domain.setModifyDate(approval.getCreateDate());
        codeMapper.updateCode(code);
        domainMapper.updateDomain(domain);
    }

    @Transactional
    public void deleteCode(Approval approval) {
        Domain domain = domainMapper.getDomainById(approval.getSlaveId());
        if (domain == null) {
            System.out.println("코드 삭제 실패 : 도메인을 찾을 수 없습니다.");
            return;
        }
        domain.setModifyUser(approval.getCreateUser());
        domain.setModifyDate(approval.getCreateDate());
        codeMapper.deleteCode(approval.getTargetId());
        domainMapper.updateDomain(domain);
    }

    public Approval dtoToApproval(User user, DomainDto domainDto, int targetId) {  // UPDATE DOMAIN
        Domain domain = domainMapper.getDomainById(targetId);
        if (domain == null) {
            System.out.println("결재 추가 실패 : 도메인을 찾을 수 없습니다.");
            return null;
        }
        Approval approval = new Approval();
        approval.setCreateUser(user);
        approval.setTargetId(targetId);
        approval.setApprovalType(ApprovalType.UPDATE);
        approval.setWordType(WordType.DOMAIN);
        // headers


        if (Objects.equals(domain.getEngName(), domainDto.getEngName())) approval.setEngName(null);
        else approval.setEngName(domainDto.getEngName());

        if (Objects.equals(domain.getKorName(), domainDto.getKorName())) approval.setKorName(null);
        else approval.setKorName(domainDto.getKorName());

        if (Objects.equals(domain.getShortName(), domainDto.getShortName())) approval.setShortName(null);
        else approval.setShortName(domainDto.getShortName());

        if (Objects.equals(domain.getMeaning(), domainDto.getMeaning())) approval.setMeaning(null);
        else approval.setMeaning(domainDto.getMeaning());
        // body
        return approval;
    }

    public Approval dtoToApproval(User user, CodeDto codeDto, int targetId, int slaveId) {  // UPDATE CODE
        Domain domain = domainMapper.getDomainById(slaveId);
        if (domain == null) {
            System.out.println("결재 추가 실패 : 도메인을 찾을 수 없습니다.");
            return null;
        }

        Code code = codeMapper.getCodeById(targetId);
        if(code == null){
            System.out.println("결재 추가 실패 : 코드를 찾을 수 없습니다.");
            return null;
        }

        Approval approval = new Approval();
        approval.setCreateUser(user);
        approval.setTargetId(targetId);
        approval.setApprovalType(ApprovalType.UPDATE);
        approval.setWordType(WordType.CODE);
        // headers

        if (Objects.equals(code.getEngName(), codeDto.getEngName())) approval.setEngName(null);
        else approval.setEngName(codeDto.getEngName());

        if (Objects.equals(code.getKorName(), codeDto.getKorName())) approval.setKorName(null);
        else approval.setKorName(codeDto.getKorName());

        if (Objects.equals(code.getShortName(), codeDto.getShortName())) approval.setShortName(null);
        else approval.setShortName(codeDto.getShortName());
        // body

        // 도메인에 수정(body=null)인 Approval 필요?
        return approval;
    }

    public Approval dtoToApproval(User user, DomainDto domainDto) { // CREATE DOMAIN
        Approval approval = new Approval();
        approval.setCreateUser(user);
        approval.setTargetId(0);
        approval.setApprovalType(ApprovalType.CREATE);
        approval.setWordType(WordType.DOMAIN);
        // headers

        approval.setEngName(domainDto.getEngName());
        approval.setKorName(domainDto.getKorName());
        approval.setShortName(domainDto.getShortName());
        approval.setMeaning(domainDto.getMeaning());
        // body
        return approval;
    }

    public Approval dtoToApproval(User user, CodeDto codeDto, int slaveId) { // CREATE CODE
        Approval approval = new Approval();
        approval.setCreateUser(user);
        approval.setTargetId(0);
        approval.setSlaveId(slaveId);

        approval.setApprovalType(ApprovalType.CREATE);
        approval.setWordType(WordType.CODE);
        // headers

        approval.setEngName(codeDto.getEngName());
        approval.setKorName(codeDto.getKorName());
        approval.setShortName(codeDto.getShortName());
        // body
        return approval;
    }


    public Approval dtoToApproval(User user, int targetId) { // DELETE DOMAIN
        Approval approval = new Approval();
        approval.setCreateUser(user);
        approval.setTargetId(targetId);
        approval.setApprovalType(ApprovalType.DELETE);
        approval.setWordType(WordType.DOMAIN);
        // headers
        return approval;
    }

    public Approval dtoToApproval(User user, int targetId, int slaveId) { // DELETE CODE
        Approval approval = new Approval();
        approval.setCreateUser(user);
        approval.setTargetId(targetId);
        approval.setSlaveId(slaveId);
        approval.setApprovalType(ApprovalType.DELETE);
        approval.setWordType(WordType.CODE);
        // headers
        return approval;
    }


    @Transactional(readOnly = true)
    public Domain findById(int domainId) {
        return domainMapper.getDomainById(domainId);
    }

    @Transactional(readOnly = true)
    public List<Domain> findByName(String name) {
        return domainMapper.getDomainsByName(name);
    }

    @Transactional(readOnly = true)
    public Code findByIdCode(int codeId) {
        return codeMapper.getCodeById(codeId);
    }

    @Transactional(readOnly = true)
    public List<Domain> getDomainListByUserid(int userId) {
        return domainMapper.getDomainsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Domain> getDomains() {
        return domainMapper.getDomains();
    }
}
