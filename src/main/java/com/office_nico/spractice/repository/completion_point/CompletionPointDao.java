package com.office_nico.spractice.repository.completion_point;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

import com.office_nico.spractice.domain.CompletionPoint;

public interface CompletionPointDao <T> extends Serializable {

	List<CompletionPoint> findByUserIdAndClientIdAndCompletionPointIds(Long userId, Long clientId, List<Long> completionPointIds);

	Page<CompletionPoint> findCompletionPointsBySearchKeywordAndIsDeletedFalse(String searchKeyword, String[] orders, String direction, Integer offset, Integer limit);

}

