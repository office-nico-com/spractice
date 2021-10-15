package com.office_nico.spractice.repository.information;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.office_nico.spractice.domain.Information;

public interface InformationDao <T> extends Serializable {

	List<Information> findByIsShowDashboardANdIsDeletedFalseAndShowStartedGteTodayOnAndShowEndedOnLteToday(LocalDate today);
}

