package com.office_nico.spractice.repository.binary_file_category;

import java.io.Serializable;
import java.util.Optional;

import com.office_nico.spractice.domain.BinaryFileCategory;

public interface BinaryFileCategoryDao <T> extends Serializable {

	public Optional<BinaryFileCategory> getByOrganizationIdOrderByFirstOrderNumber(Long organizationId);

}

