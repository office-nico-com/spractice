package com.office_nico.spractice.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.office_nico.spractice.domain.Course;
import com.office_nico.spractice.domain.Extension;
import com.office_nico.spractice.domain.VirtualMachine;
import com.office_nico.spractice.repository.course.CourseRepository;
import com.office_nico.spractice.repository.extension.ExtensionRepository;
import com.office_nico.spractice.repository.virtual_machine.VirtualMachineRepository;
import com.office_nico.spractice.service.data.Instruction;

/**
 * 受講サービス
 * 
 * @author fujisawa
 */
@Service
@Transactional
public class InstructionService {

	@Autowired CourseRepository courseRepository = null;
	@Autowired VirtualMachineRepository virtualMachineRepository = null;
	@Autowired ExtensionRepository extensionRepository = null;
	

	public Instruction get(Long organizationId, Long userId, String courseName, String virtualMachineName) {

		Instruction instruction = new Instruction();
		instruction.setResult(Instruction.Result.SUCCESS);

		// コースの有効性を確認する
		List<Course> courses = courseRepository.findByOrganizationIdAndCourseNameAndIsDeletedFalseAndIsInvalidedFalse(organizationId, courseName);
		if (courses.size() == 0) {
			// 無効の場合は処理終了
			instruction.setResult(Instruction.Result.INVALID_COURSE);
			return instruction;
		}
		instruction.setCourse(courses.get(0));

		// VMの有効性を確認する
		List<VirtualMachine> virtualMachines = virtualMachineRepository.findByCourseIdAndVirtualMachineNameAndIsDeletedFalseAndIsInvalidedFalse(instruction.getCourse().getId(), virtualMachineName);
		if (virtualMachines.size() == 0) {
			// 無効の場合は処理終了
			instruction.setResult(Instruction.Result.INVALID_VM);
			return instruction;
		}
		instruction.setVirtualMachine(virtualMachines.get(0));

		// 拡張機能を読み込む
		List<Extension> extensions = extensionRepository.findByVirtualMachineIdAndIsDeletedFalseAndIsInvalidedFalse(instruction.getVirtualMachine().getId());
		System.out.println(extensions.size());
		instruction.getVirtualMachine().setExtensions(extensions);

		
//		Optional<Course> a =courseRepository.getY(courseName);
		
//		Optional<Course> a = courseRepository.getX(courseName);
//
//		if(!a.isEmpty()) {
//			for(int i=0; i < a.get().getVirtualMachines().size(); i++) {
//				 System.out.println(a.get().getVirtualMachines().get(i).getVirtualMachineName());
//			}
//		}
//		

//		VirtualMachine v = virtualMachineRepository.getX();
//		Course c = v.getCourse();
//		if(c != null) {
//			System.out.println(c.getId());
//			System.out.println(c.getCourseName());
//		}

		return instruction;
	}
}
