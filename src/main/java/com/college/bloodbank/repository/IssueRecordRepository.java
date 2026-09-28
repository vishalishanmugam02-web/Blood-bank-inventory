package com.college.bloodbank.repository;
import com.college.bloodbank.entity.IssueRecord;
import org.springframework.data.jpa.repository.JpaRepository;
public interface IssueRecordRepository extends JpaRepository<IssueRecord, Long> {}
