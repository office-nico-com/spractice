package com.office_nico.spractice.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.transaction.Transactional;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import com.office_nico.spractice.domain.Course;
import com.office_nico.spractice.domain.Extension;
import com.office_nico.spractice.domain.VirtualMachine;
import com.office_nico.spractice.extensions.ExtensionManager;
import com.office_nico.spractice.repository.course.CourseRepository;
import com.office_nico.spractice.repository.extension.ExtensionRepository;
import com.office_nico.spractice.repository.virtual_machine.VirtualMachineRepository;
import com.office_nico.spractice.service.data.Instruction;
import com.office_nico.spractice.service.data.SessionData;

/**
 * 受講サービス
 * 
 * @author fujisawa
 */
@Service
@Transactional
public class InstructionService {

	@Autowired 
	ConfigurableApplicationContext ctx = null;
	@Autowired 
	CourseRepository courseRepository = null;
	@Autowired 
	VirtualMachineRepository virtualMachineRepository = null;
	@Autowired 
	ExtensionRepository extensionRepository = null;

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
		for(Extension extension : extensions) {

			// 拡張機能の固有設定を読み込む
			String filePath = "../extensions/" + extension.getExtensionType() + "/extension.properties";
			InputStream in = this.getClass().getResourceAsStream(filePath);
			if (in == null) {
				throw new IllegalArgumentException("property file not found." + filePath);
			}
			Properties props = new Properties();
			try {
				props.load(in);
				extension.setIcon(props.getProperty("icon"));
				extension.setClassName(props.getProperty("javascript.class"));
				String managerClassName = props.getProperty("manager.class");
				
				if(managerClassName != null && managerClassName.length() > 0) {
					ExtensionManager extensionManager = (ExtensionManager)ctx.getBean(Class.forName("com.office_nico.spractice.extensions." + extension.getExtensionType() + "." + managerClassName));
					String specificPropery = extensionManager.getSpecificProperty(extension.getId(), extension.getExtensionName());
					extension.setSpecificProperty(specificPropery);
				}
			}
			catch (IOException | BeansException | ClassNotFoundException e) {
				// TODO:
				throw new RuntimeException(e);
			}
		}
		instruction.getVirtualMachine().setExtensions(extensions);

		return instruction;
	}
}
