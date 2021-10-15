package com.office_nico.spractice.service;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.office_nico.spractice.constants.InvalidStatus;
import com.office_nico.spractice.constants.Length;
import com.office_nico.spractice.constants.codes.RoleCode;
import com.office_nico.spractice.constants.codes.UserRegistTypeCode;
import com.office_nico.spractice.domain.Client;
import com.office_nico.spractice.domain.ClientUser;
import com.office_nico.spractice.domain.User;
import com.office_nico.spractice.repository.client.ClientRepository;
import com.office_nico.spractice.repository.client_user.ClientUserRepository;
import com.office_nico.spractice.repository.user.UserRepository;
import com.office_nico.spractice.service.data.CsvImportResult;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository = null;

	@Autowired
	private ClientUserRepository clientUserRepository = null;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRegistTypeCode userRegistTypeCode = null;

	@Autowired
	private InvalidStatus invalidStatus = null;

	@Autowired
	private MessageSource messageSource = null;
	
	@Autowired
	private RoleCode roleCode = null;
	
	@Autowired
	private ClientRepository clientRepository = null;
	
	@Autowired
	private LogService logService = null;
	
	private static final Logger _logger = LoggerFactory.getLogger(UserService.class);

	/**
	 * ページリストの取得
	 * 
	 * @param logger        ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param current       現在のページ
	 * @param pageMax       1ページ当たりの表示数
	 * @return ページオブジェクト
	 */
	public Page<User> page(Logger logger, Long sessionUserId, int start, int length, Sort.Direction dir, String[] order) {
		logger = (logger == null ? logger : _logger);

		int pageNumber = start == 0 ? 0 : (start / length);

		// Pageable pr = PageRequest.of(pageNumber, length, dir, order);

		Pageable pr = PageRequest.of(pageNumber, length, dir, order);
		Page<User> page = userRepository.findByIsDeletedFalse(pr);
		return page;
	}

//	public Page<User> page(int start, int length, Sort.Direction dir, String[] orders){
//		
//		int pageNumber = start == 0 ? 0 : (start / length);
//		
//		String order = "";
//		for(String orderField : orders) {
//			if(order.length() > 0) {
//				order += ",";
//			}
//			order += orderField + " " + dir;
//		}
//		
//		Page<User> page = new Page<>();
//		List<Map<String, Object>> list = userRepository.findByIsDeletedFalse(Sort.by(order), length, start);
//		page.set(list, User.class);
//		Long count = userRepository.countByIsDeletedFalse();
//		page.setTotalElements(count);
//		return page;
//	}

	/**
	 * レコードの取得
	 * 
	 * @param logger        ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param userId        ユーザーID
	 * @return エンティティ
	 */
	public User get(Logger logger, Long sessionUserId, Long userId) {
		logger = (logger == null ? logger : _logger);

		User user = userRepository.findTopById(userId);
		
		return user;
	}

	/**
	 * ユーザーの登録
	 * 
	 * @param logger         ロガー
	 * @param sessionUserId  セッションユーザーID
	 * @param user           エンティティ
	 * @param clientUserList 所属しているクライアントのリスト
	 * @param 登録されたエンティティ
	 */
	@Transactional
	public User create(Logger logger, Long sessionUserId, User user, List<ClientUser> clientUserList) {
		logger = (logger == null ? logger : _logger);

		user.setPasswd(passwordEncoder.encode(user.getPasswd()));

		user.setIsDeleted(false);
		user.setUpdatedBy(sessionUserId);
		user.setCreatedBy(sessionUserId);
		user.setUpdatedAt(LocalDateTime.now());
		user.setCreatedAt(LocalDateTime.now());

		userRepository.save(user);

		// クライアントユーザーの登録
		for (ClientUser clientUser : clientUserList) {
			clientUser.setUser(user);
			clientUser.setUpdatedBy(sessionUserId);
			clientUser.setCreatedBy(sessionUserId);
			clientUser.setUpdatedAt(LocalDateTime.now());
			clientUser.setCreatedAt(LocalDateTime.now());
			clientUserRepository.save(clientUser);
		}

		return user;
	}

	/**
	 * ユーザーの更新
	 * 
	 * @param logger         ロガー
	 * @param sessionUserId  セッションユーザーID
	 * @param userId         ユーザーID
	 * @param user           エンティティ
	 * @param clientUserList 所属しているクライアントのリスト
	 * @return 更新されたエンティティ
	 */
	public User update(Logger logger, Long sessionUserId, Long userId, User user, List<ClientUser> clientUserList) {
		logger = (logger == null ? logger : _logger);

		Optional<User> entity = userRepository.findById(userId);
		if (!entity.isEmpty() && !entity.get().getIsDeleted()) {
			entity.get().overwrite(user);
			if (user.getPasswd() != null && user.getPasswd().length() > 0) {
				entity.get().setPasswd(passwordEncoder.encode(user.getPasswd()));
			}
			entity.get().setUpdatedBy(sessionUserId);
			entity.get().setUpdatedAt(LocalDateTime.now());
			userRepository.save(entity.get());
		}

		// クライアント-ユーザーの処理
		// DBにあって指定されていない場合は削除
		// 権限が更新されている場合は更新
		// DBになくて指定されている場合は追加
		List<ClientUser> clientUsers = clientUserRepository.findByUserId(userId);
		for (ClientUser clientUser : clientUsers) {
			boolean find = false;
			for (int i = clientUserList.size() - 1; i == 0; i--) {
				ClientUser clientUser2 = clientUserList.get(i);
				if (clientUser.getClient().getId().equals(clientUser2.getClient().getId())) {
					find = true;
					if (!clientUser.getRoleCode().equals(clientUser2.getRoleCode())) {
						clientUser.setRoleCode(clientUser2.getRoleCode());
						clientUser.setUpdatedBy(sessionUserId);
						clientUser.setUpdatedAt(LocalDateTime.now());
						clientUserRepository.save(clientUser);
					}
					clientUserList.remove(i);
					break;
				}
			}
			if (!find) {
				clientUserRepository.delete(clientUser);
			}
		}
		for (ClientUser clientUser : clientUserList) {
			clientUser.setUser(user);
			clientUser.setUpdatedBy(sessionUserId);
			clientUser.setCreatedBy(sessionUserId);
			clientUser.setUpdatedAt(LocalDateTime.now());
			clientUser.setCreatedAt(LocalDateTime.now());
			clientUserRepository.save(clientUser);
		}
		return entity.get();
	}

	/**
	 * ユーザーの削除
	 * 
	 * @param logger        ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param userId        ユーザーID
	 */
	public void delete(Logger logger, Long sessionUserId, Long userId) {
		logger = (logger == null ? logger : _logger);

		Optional<User> entity = userRepository.findById(userId);
		if (!entity.isEmpty() && !entity.get().getIsDeleted()) {
			entity.get().setIsDeleted(true);
			entity.get().setUpdatedBy(sessionUserId);
			entity.get().setUpdatedAt(LocalDateTime.now());
		}
	}

	/**
	 * ユーザーの重複チェック
	 * 
	 * @param logger        ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param account       アカウント
	 * @return true：重複、false：重複なし
	 */
	public Boolean isDuplidate(Logger logger, Long sessionUserId, String account) {
		logger = (logger == null ? logger : _logger);

		Boolean ret = false;
		if (userRepository.countByIsDeletedFalseAndAccount(account) > 0L) {
			ret = true;
		}
		return ret;
	}

	/**
	 * ユーザーの重複チェック
	 * 
	 * @param logger        ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param account       アカウント
	 * @param exclusionId   除外ID
	 * @return true：重複、false：重複なし
	 */
	public Boolean isDuplidate(Logger logger, Long sessionUserId, String account, Long exclusionId) {
		logger = (logger == null ? logger : _logger);

		Boolean ret = false;
		if (userRepository.countByIsDeletedFalseAndAccountAndIdNot(account, exclusionId) > 0L) {
			ret = true;
		}
		return ret;
	}

	/**
	 * 所属しているクライアントの一覧を返す
	 * 
	 * @param logger        ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param userId        クライアントID
	 * @return クライアントリスト
	 */
	public List<ClientUser> getAffiliatedClients(Logger logger, Long sessionUserId, Long userId) {
		logger = (logger == null ? logger : _logger);

		return clientUserRepository.findClientByUserId(userId);
	}

	/**
	 * アカウントから有効なユーザー情報の取得
	 * 
	 * @param logger        ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param account       アカウント
	 * @return エンティティ
	 */
	public User getValidByAccount(Logger logger, Long sessionUserId, String acccount) {
		logger = (logger == null ? logger : _logger);

		User entity = userRepository.findTopByAccountAndIsDeletedFalseAndIsInvalidedFalse(acccount);

		return entity;
	}

	/**
	 * ユーザーの自動登録（更新）
	 * 
	 * @param logger        ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId      クライアントID
	 * @param clientKeycode クライアントキーコード
	 * @param account       アカウント
	 * @param email         メールアドレス
	 * @param name          名前
	 * @param kana          カナ
	 */
	public User autoSave(Logger logger, Long sessionUserId, Client client, String account, String email, String name, String kana) {
		logger = (logger == null ? logger : _logger);

		String familyName = null;
		String givenName = null;
		String familyNameKana = null;
		String givenNameKana = null;

		// 名前を分割
		if (name != null) {
			String[] names = name.split("[　 /]", 0);
			if (names.length >= 2) {
				familyName = names[0];
				givenName = names[1];
			}
			else {
				familyName = name;
				givenName = "";
			}
		}
		if (kana != null) {
			String[] kanas = kana.split("[　 /]", 0);
			if (kanas.length >= 2) {
				familyNameKana = kanas[0];
				givenNameKana = kanas[1];
			}
			else {
				familyNameKana = kana;
				givenNameKana = "";
			}
		}

		String _account = messageSource.getMessage("user.auto.save.account", new String[] { account, client.getClientKeycode() }, Locale.getDefault());

		// insert or update
		User user = userRepository.findTopByAccountAndIsDeletedFalseAndIsInvalidedFalse(_account);
		if (user == null) {
			user = new User();
			user.setAccount(_account);
			user.setPasswd("****");
			user.setRegisteredFromCode(userRegistTypeCode.systemLink);
			user.setIsAdmin(false);
			user.setIsInvalided(invalidStatus.Valid);
			user.setIsDeleted(false);
			user.setCreatedBy(0L);
			user.setCreatedAt(LocalDateTime.now());
		}

		if (email != null) {
			user.setEmail(email);
		}
		else if (user.getEmail() == null) {
			// 勝手に生成
			user.setEmail(messageSource.getMessage("user.auto.save.emal", new String[] { account, client.getClientKeycode() }, Locale.getDefault()));
		}

		if (familyName != null) {
			user.setFamilyName(familyName);
		}
		else if (user.getFamilyName() == null) {
			// 勝手に生成
			user.setFamilyName(messageSource.getMessage("user.auto.save.family.name", new String[] {}, Locale.getDefault()));
		}

		if (givenName != null) {
			user.setGivenName(givenName);
		}
		else if (user.getGivenName() == null) {
			// 勝手に生成
			user.setGivenName(messageSource.getMessage("user.auto.save.given.name", new String[] {}, Locale.getDefault()));
		}

		if (familyNameKana != null) {
			user.setFamilyNameKana(familyNameKana);
		}
		else if (user.getFamilyNameKana() == null) {
			// 勝手に生成
			user.setFamilyNameKana(messageSource.getMessage("user.auto.save.family.name.kana", new String[] {}, Locale.getDefault()));
		}

		if (givenNameKana != null) {
			user.setGivenNameKana(givenNameKana);
		}
		else if (user.getGivenNameKana() == null) {
			// 勝手に生成
			user.setGivenNameKana(messageSource.getMessage("user.auto.save.given.name.kana", new String[] {}, Locale.getDefault()));
		}
		user.setUpdatedBy(0L);
		user.setUpdatedAt(LocalDateTime.now());
		userRepository.save(user);

		boolean find = false;
		List<ClientUser> clientUsers = clientUserRepository.findByUserId(user.getId());
		for (ClientUser clientUser : clientUsers) {
			if (clientUser.getClient().getId().equals(client.getId())) {
				find = true;
			}
		}
		if (!find) {
			ClientUser clientUser = new ClientUser();
			clientUser.setUser(user);
			clientUser.setClient(client);
			clientUser.setRoleCode(Short.valueOf("1"));
			clientUser.setCreatedBy(0L);
			clientUser.setCreatedAt(LocalDateTime.now());
			clientUser.setUpdatedBy(0L);
			clientUser.setUpdatedAt(LocalDateTime.now());
			clientUserRepository.save(clientUser);
			user.getClientUsers().add(clientUser);
		}
		return user;
	}

	/**
	 * ユーザーの更新
	 * 
	 * @param logger        ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param userId        ユーザーID
	 * @param user          エンティティ
	 * @return 更新されたエンティティ
	 */
	public User updateProfile(Logger logger, Long sessionUserId, Long userId, User user) {
		logger = (logger == null ? logger : _logger);

		Optional<User> entity = userRepository.findById(userId);
		if (!entity.isEmpty() && !entity.get().getIsDeleted()) {
			entity.get().setFamilyName(user.getFamilyName());
			entity.get().setGivenName(user.getGivenName());
			entity.get().setFamilyNameKana(user.getFamilyNameKana());
			entity.get().setGivenNameKana(user.getGivenNameKana());
			entity.get().setEmail(user.getEmail());
			entity.get().setUpdatedBy(sessionUserId);
			entity.get().setUpdatedAt(LocalDateTime.now());
			userRepository.save(entity.get());
		}
		return entity.get();
	}

	/**
	 * ユーザーの更新
	 * 
	 * @param logger        ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param userId        ユーザーID
	 * @param password        パスワード
	 * @return 更新されたエンティティ
	 */
	public User updatePassword(Logger logger, Long sessionUserId, Long userId, String password) {
		logger = (logger == null ? logger : _logger);

		Optional<User> entity = userRepository.findById(userId);
		if (!entity.isEmpty() && !entity.get().getIsDeleted()) {
			entity.get().setPasswd(passwordEncoder.encode(password));
			entity.get().setUpdatedBy(sessionUserId);
			entity.get().setUpdatedAt(LocalDateTime.now());
			userRepository.save(entity.get());
		}
		return entity.get();
	}
	
	/**
	 * 権限コードの取得
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param clientId クライアントID
	 * @param userId ユーザID
	 * @return 権限コード
	 */
	public Short getRoleCode(Logger logger, Long sessionUserId, Long clientId, Long userId) {
		Short ret = roleCode.user;
		ClientUser clientUser = clientUserRepository.findTopByClientIdAndUserId(clientId, userId);
		if(clientUser != null) {
			ret = clientUser.getRoleCode();
		}
		return ret;
	}

	
	/**
	 * ユーザーの一括登録
	 * @param logger ロガー
	 * @param sessionUserId セッションユーザーID
	 * @param fileName ファイル名
	 * @param data データ
	 * @return 取り込み結果
	 */
	public CsvImportResult bulkSave(Logger logger, Long sessionUserId, String fileName, String data) {
		
		CsvImportResult ret = new CsvImportResult();
		
		try {
			CsvParserSettings settings = new CsvParserSettings();
			settings.setMaxColumns(2000);
			CsvParser parser = new CsvParser(settings);

			// 同一アカウントはスキップ
			// 
			try(StringReader sr = new StringReader(data)){
				List<String[]> rows = parser.parseAll(sr);
				for(int i= 0; i < rows.size(); i++) {
					
					User user = new User();
					ClientUser clientUser = new ClientUser();
					
					if(i == 0) {
						// 一行目はヘッダー行のため、読み飛ばす
						continue;
					}
					
					String[] row = rows.get(i);
					if(row.length < 10) {
						// カラム不足
						String message = messageSource.getMessage("message.input.error.csv.shortage.column.1", new String[] {String.valueOf(row.length)}, Locale.getDefault());
						ret.addMessage(createMessage(i+1, 0, message));
						continue;
					}

					// 登録元(9)
					// ・必須
					// ・1 or 2
					int col = 9;
					if(row[col] == null || row[col].length() == 0) {
						String message = messageSource.getMessage("message.input.error.csv.require.1", null, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else if(!row[col].matches("[+-]?\\d*(\\.\\d+)?")) {
						String message = messageSource.getMessage("message.input.error.csv.number.1", null, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else if(Integer.parseInt(row[col]) != 1 && Integer.parseInt(row[col]) != 2) {
						String message = messageSource.getMessage("message.input.error.csv.number.2", null, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else {
						user.setRegisteredFromCode(Short.parseShort(row[col]));
					}

					// 所属クライアントコード(7)
					// ・必須
					// ・有効性チェック
					col = 7;
					if(row[col] == null || row[col].length() == 0) {
						String message = messageSource.getMessage("message.input.error.csv.require.1", null, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else {
						Client client = clientRepository.findTopByClientKeycodeAndIsDeletedFalseAndIsInvalidedFalse(row[col]);
						if(client == null){
							String message = messageSource.getMessage("message.input.error.csv.invalid.1", null, Locale.getDefault());
							ret.addMessage(createMessage(i+1, col+1, message));
						}
						else {
							clientUser.setClient(client);
						}
					}

					
					// アカウント
					// ・必須
					// ・文字数
					// ・半角英数記号
					// ・登録元がシステム連携の場合はサフィックスにクライアントコードを付与
					// ・重複はスキップ
					col = 0;
					if(row[col] == null || row[col].length() == 0) {
						String message = messageSource.getMessage("message.input.error.csv.require.1", null, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else if(row[col].length() > Length.account) {
						String message = messageSource.getMessage("message.input.error.csv.length.1", new String[] {String.valueOf(Length.account)}, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else if(!row[col].matches("^[a-zA-Z0-9@\\+\\-\\.\\[\\]\\*_<>:!\\|]*$")) {
						String message = messageSource.getMessage("message.input.error.csv.invalid.1", null, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else {
						if(!ret.hasError()) {
							String account = row[col];
							if(user.getRegisteredFromCode().equals(userRegistTypeCode.systemLink)) {
								account = messageSource.getMessage("user.auto.save.account", new String[] { account, row[7] }, Locale.getDefault());
							}
							
							if(userRepository.findTopByAccountAndIsDeletedFalse(account) != null) {
								// 既に登録済みの場合はskip;
								continue;
							}
							user.setAccount(account);
						}
					}
					
					
					// パスワード
					// ・登録元がマニュアルの場合は必須
					// ・文字数
					// ・半角英数アルファベット
					col = 1;
					String password = "****";
					if(!ret.hasError() && user.getRegisteredFromCode().equals(userRegistTypeCode.manual)) {
						if(row[col] == null || row[col].length() == 0) {
							String message = messageSource.getMessage("message.input.error.csv.require.1", null, Locale.getDefault());
							ret.addMessage(createMessage(i+1, col+1, message));
						}
						else if(row[col].length() > Length.passwd || row[col].length() < Length.passwdMin) {
							String message = messageSource.getMessage("message.input.error.csv.length.2", new String[] {String.valueOf(Length.passwdMin), String.valueOf(Length.passwd)}, Locale.getDefault());
							ret.addMessage(createMessage(i+1, col+1, message));
						}
						else if(!row[col].matches("^[a-zA-Z0-9@\\+\\-\\.\\[\\]\\*_<>:!\\|]*$")) {
							String message = messageSource.getMessage("message.input.error.csv.invalid.1", null, Locale.getDefault());
							ret.addMessage(createMessage(i+1, col+1, message));
						}
						else {
							password = passwordEncoder.encode(row[col]);
						}
					}
					user.setPasswd(password);

					// 姓
					// 任意
					// ・文字数チェック
					col = 2;
					String str = messageSource.getMessage("user.auto.save.family.name", new String[] {}, Locale.getDefault());
					if(row[col] == null && row[col].length() > 0 && row[col].length() > Length.familyName) {
						String message = messageSource.getMessage("message.input.error.csv.length.1", new String[] {String.valueOf(Length.familyName)}, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else {
						str = row[col];
					}
					user.setFamilyName(str);
					
					// 名
					// 任意
					// ・文字数チェック
					col = 3;
					str = messageSource.getMessage("user.auto.save.given.name", new String[] {}, Locale.getDefault());
					if(row[col] == null && row[col].length() > 0 && row[col].length() > Length.givenName) {
						String message = messageSource.getMessage("message.input.error.csv.length.1", new String[] {String.valueOf(Length.givenName)}, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else {
						str = row[col];
					}
					user.setGivenName(str);
					
					// 性（カナ）
					// 任意
					// ・文字数チェック
					// ・カタカナ
					col = 4;
					str = messageSource.getMessage("user.auto.save.family.name.kana", new String[] {}, Locale.getDefault());
					if(row[col] == null && row[col].length() > 0 && row[col].length() > Length.familyNameKana) {
						String message = messageSource.getMessage("message.input.error.csv.length.1", new String[] {String.valueOf(Length.familyNameKana)}, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else if(row[col] != null && !row[col].matches("^[ァ-タダ-ヶー０-９]*$")) {
						String message = messageSource.getMessage("message.input.error.csv.invalid.1", null, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else {
						str = row[col];
					}
					user.setFamilyNameKana(str);

					// 名（カナ）
					// 任意
					// ・文字数チェック
					// ・カタカナ
					col = 5;
					str = messageSource.getMessage("user.auto.save.given.name.kana", new String[] {}, Locale.getDefault());
					if(row[col] == null && row[col].length() > 0 && row[col].length() > Length.givenNameKana) {
						String message = messageSource.getMessage("message.input.error.csv.length.1", new String[] {String.valueOf(Length.givenNameKana)}, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else if(row[col] != null && !row[col].matches("^[ァ-タダ-ヶー０-９]*$")) {
						String message = messageSource.getMessage("message.input.error.csv.invalid.1", null, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else {
						str = row[col];
					}
					user.setGivenNameKana(str);
					
					// Email
					// ・指定されていない場合は自動生成
					// ・メールアドレス
					col = 6;
					if(!ret.hasError()) {
						str = messageSource.getMessage("user.auto.save.emal", new String[] { row[0], row[7] }, Locale.getDefault());
						if(row[col] == null && row[col].length() > 0 && row[col].length() > Length.email) {
							String message = messageSource.getMessage("message.input.error.csv.length.1", new String[] {String.valueOf(Length.email)}, Locale.getDefault());
							ret.addMessage(createMessage(i+1, col+1, message));
						}
						else if(row[col] != null && !row[col].matches("^([a-zA-Z0-9])+([a-zA-Z0-9._-])*@([a-zA-Z0-9_-])+([a-zA-Z0-9._-]+)+$")) {
							String message = messageSource.getMessage("message.input.error.csv.invalid.1", null, Locale.getDefault());
							ret.addMessage(createMessage(i+1, col+1, message));
						}
						else {
							str = row[col];
						}
						user.setEmail(str);
					}
					// 権限
					// ・必須
					// ・1 or 2
					col = 8;
					Short roleCode = 0;
					if(row[col] == null || row[col].length() == 0) {
						String message = messageSource.getMessage("message.input.error.csv.require.1", null, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else if(!row[col].matches("[+-]?\\d*(\\.\\d+)?")) {
						String message = messageSource.getMessage("message.input.error.csv.number.1", null, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else if(Integer.parseInt(row[col]) != 1 && Integer.parseInt(row[col]) != 2) {
						String message = messageSource.getMessage("message.input.error.csv.number.2", null, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else {
						roleCode = Short.valueOf(row[col]);
					}
					
					// 説明
					// 任意
					// 文字数チェック
					col = 9;
					if(row[col] == null && row[col].length() > 0 && row[col].length() > Length.description) {
						String message = messageSource.getMessage("message.input.error.csv.length.1", new String[] {String.valueOf(Length.description)}, Locale.getDefault());
						ret.addMessage(createMessage(i+1, col+1, message));
					}
					else {
						user.setDescription(row[col]);
					}
					if(!ret.hasError()) {
						user.setIsAdmin(false);
						user.setIsInvalided(invalidStatus.Valid);
						user.setIsDeleted(false);
						user.setCreatedBy(sessionUserId);
						user.setCreatedAt(LocalDateTime.now());
						user.setUpdatedBy(sessionUserId);
						user.setUpdatedAt(LocalDateTime.now());
						userRepository.save(user);
						clientUser.setUser(user);
						clientUser.setRoleCode(roleCode);
						clientUser.setCreatedBy(sessionUserId);
						clientUser.setCreatedAt(LocalDateTime.now());
						clientUser.setUpdatedBy(sessionUserId);
						clientUser.setUpdatedAt(LocalDateTime.now());
						clientUserRepository.save(clientUser);
					}
				}
			}
		}
		catch(Throwable e) {
			String message = messageSource.getMessage("message.system.error.regist.data.1", null, Locale.JAPAN);
			ret.setResult(CsvImportResult.RESULT_FAIL);
			ret.addMessage(message);
			logService.error(logger, null, e);
		}
		return ret;
	}

	private String createMessage(int row, int col, String message) {
		String position = messageSource.getMessage("message.input.error.csv.position", new String[]{String.valueOf(row), String.valueOf(col)}, Locale.getDefault());
		String ret = position + message;
		return ret;
	}
	
}
