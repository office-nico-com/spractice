package com.office_nico.spractice.repository.completion;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import com.office_nico.spractice.domain.Completion;
import com.office_nico.spractice.service.data.AggregateData;
import com.office_nico.spractice.service.data.CompletionUser;

public interface CompletionDao <T> extends Serializable {

	List<Completion> findByUserIdAndClientIdAndCompletionPointIds(Long userId, Long clientId, List<Long> completionPointIds, Integer limit);

	List<AggregateData> calcWorkTimesByCompletionPointIds(List<Long> completionPointId, Long clientId);

	List<AggregateData> findCompletionCountsByCompletionPointIds(List<Long> completionPointId, Long clientId);

	Integer countUnworkedUserCountByCompletionPointIdAndClientId(Long completionPointId, Long clientId);

	List<CompletionUser> findByCompletionPointIdAndClientIdAndStartedAtNotNullAndEndedAtNotNullAndEndedAtFromAndEndedAtTo(Long completionPointId, Long clientId, LocalDate fd, LocalDate td);

}

