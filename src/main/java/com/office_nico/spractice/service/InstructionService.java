package com.office_nico.spractice.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.office_nico.spractice.domain.Course;
import com.office_nico.spractice.domain.VirtualMachine;
import com.office_nico.spractice.repository.CourseRepository;
import com.office_nico.spractice.repository.VirtualMachineRepository;
import com.office_nico.spractice.service.data.Instruction;




/**
 * 受講サービス
 * @author fujisawa
 */
@Service
@Transactional
public class InstructionService {

	@Autowired
	CourseRepository courseRepository = null;

	@Autowired
	VirtualMachineRepository virtualMachineRepository = null;

	public Instruction get(Long userId, String courseName, String virtualMachineName) {

		Instruction instruction = new Instruction();
		instruction.setResult(Instruction.Result.SUCCESS);
		

		// コースの有効性を確認する
		Integer courseCount= courseRepository.countByCourseName(courseName);
		if(courseCount == 0) {
			// 無効の場合は処理終了
			instruction.setResult(Instruction.Result.INVALID_COURSE);
			return instruction;
		}

		// VMの有効性を確認する
		Integer virtualMachineCount = virtualMachineRepository.countByVirtualMachineName(virtualMachineName);
		if(virtualMachineCount == 0) {
			// 無効の場合は処理終了
			instruction.setResult(Instruction.Result.INVALID_VM);
			return instruction;
		}

		// 
		
		return instruction;
	}
}

