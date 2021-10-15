package com.office_nico.spractice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.office_nico.spractice.domain.Information;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.repository.information.InformationRepository;
import com.office_nico.spractice.repository.user.UserRepository;

@Service
@Transactional
public class InformationService {

	@Autowired InformationRepository informationRepository = null;

	@Autowired UserRepository userRepository = null;
	
	private static final Logger _logger = LoggerFactory.getLogger(InformationService.class);
	
	/**
	 * ページリストの取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param current 現在のページ
	 * @param pageMax 1ページ当たりの表示数
	 * @return ページオブジェクト
	 */
	public Page<Map<String, Object>> page(Logger logger, Long sessionUserId, int start, int length, Sort.Direction dir, String[] order){
		logger = (logger == null ? logger : _logger);

		int pageNumber = start == 0 ? 0 : (start / length);
		Pageable pr = PageRequest.of(pageNumber, length, dir, order);
		Page<Map<String, Object>> page = informationRepository.findByIsDeletedFalse(pr);
		return page;
	}

	
	/**
	 * 情報共有の一覧を取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 */
	public List<Information> list(Logger logger, Long sessionUserId){
		logger = (logger == null ? logger : _logger);
		return informationRepository.findByIsShowDashboardANdIsDeletedFalseAndShowStartedGteTodayOnAndShowEndedOnLteToday(LocalDate.now());
	}
	
	
	/**
	 * レコードの取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param informationid 情報共有ID
	 * @return エンティティ
	 */
	public Information get(Logger logger, Long sessionUserId, Long informationid) {
		logger = (logger == null ? logger : _logger);

		Optional<Information> entity = informationRepository.findById(informationid);

		return entity.get();
	}

	/**
	 * 情報共有の登録
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param information エンティティ
	 * @param 登録されたエンティティ
	 */
	@Transactional
	public Information create(Logger logger, Long sessionUserId, Long postedByUserId, Information information) {
		logger = (logger == null ? logger : _logger);

		if(userRepository.existsById(postedByUserId)) {
			Optional<User> user = userRepository.findById(postedByUserId);
			information.setUser(user.get());
			information.setPostedAt(LocalDateTime.now());
			information.setIsDeleted(false);
			information.setUpdatedBy(sessionUserId);
			information.setCreatedBy(sessionUserId);
			information.setUpdatedAt(LocalDateTime.now());
			information.setCreatedAt(LocalDateTime.now());
			informationRepository.save(information);
		}
		return information;
	}

	/**
	 * 情報共有の更新
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param informationId 情報共有ID
	 * @param information エンティティ
	 * @return 更新されたエンティティ
	 */
	public Information update(Logger logger, Long sessionUserId, Long informationId, Information information) {
		logger = (logger == null ? logger : _logger);

		Optional<Information> entity = informationRepository.findById(informationId);
		if(!entity.isEmpty() && !entity.get().getIsDeleted()) {
			entity.get().setMessage(information.getMessage());
			entity.get().setIsShowDashboard(information.getIsShowDashboard());
			entity.get().setShowStartedOn(information.getShowStartedOn());
			entity.get().setShowEndedOn(information.getShowEndedOn());
			entity.get().setUpdatedBy(sessionUserId);
			entity.get().setUpdatedAt(LocalDateTime.now());
			informationRepository.save(entity.get());
		}

		return entity.get();
	}

	/**
	 * 情報共有の削除
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param informationId 情報共有ID
	 */
	public void delete(Logger logger, Long sessionUserId, Long informationId) {
		logger = (logger == null ? logger : _logger);

		Optional<Information> entity = informationRepository.findById(informationId);
		if(!entity.isEmpty() && !entity.get().getIsDeleted()) {
			entity.get().setIsDeleted(true);
			entity.get().setUpdatedBy(sessionUserId);
			entity.get().setUpdatedAt(LocalDateTime.now());
			informationRepository.save(entity.get());
		}
	}
	
}

