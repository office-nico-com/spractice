package com.office_nico.spractice.repository.binary_file;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.office_nico.spractice.domain.BinaryFile;

@Repository
@Transactional
public interface BinaryFileRepository extends JpaRepository<BinaryFile,Long>, BinaryFileDao<BinaryFile> {

}
