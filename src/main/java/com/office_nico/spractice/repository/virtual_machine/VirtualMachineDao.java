package com.office_nico.spractice.repository.virtual_machine;

import java.io.Serializable;

import com.office_nico.spractice.domain.VirtualMachine;

public interface VirtualMachineDao <T> extends Serializable {
	public VirtualMachine getX();
}

