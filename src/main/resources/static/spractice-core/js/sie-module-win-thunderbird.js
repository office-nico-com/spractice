// アウトルックモジュール
var SieModuleWinThunderbird = function(moduleId, _account){
	this.id = moduleId;
	this.title = "Thunderbird";
	this.titlebar = "Thunderbird";
	this.icon = "sie/img/icon-thunderbird.png"
	this.isShowDeskTop=true;
	this.isWindowOpen=true;
	this.initSelect=false;
	this.moduleClass="sie-module-win-thunderbird";
	this.delayedStart = 0;
	this.account = 'user.name@maildomain.com';
	if(_account != null){
		this.account = _account;
	}

	this.view = '<div class="sie-module-win-body">';

	this.view += '<div class="menu-bar">'

	this.view += '<div class="download"><i class="fa fa-download" aria-hidden="true"></i>受信&nbsp;&nbsp;|&nbsp;&nbsp;<i class="fa fa-angle-down" aria-hidden="true"></i></div>'
	this.view += '<div class="create"><i class="fa fa-pencil" aria-hidden="true"></i>作成</div>'
	this.view += '<div class="chat"><i class="fa fa-comment-o" aria-hidden="true"></i>チャット</div>'
	this.view += '<div class="address"><i class="fa fa-address-book-o" aria-hidden="true"></i>アドレス帳</div>'
	this.view += '<div class="delim"></div>'
	this.view += '<div class="tag"><i class="fa fa-tag" aria-hidden="true"></i></i>タグ<i class="fa fa-angle-down" aria-hidden="true"></i></div>'
	this.view += '<div class="filter"><i class="fa fa-filter" aria-hidden="true"></i>クイックフィルター</div>'
	this.view += '<div class="menubar"><i class="fa fa-bars" aria-hidden="true"></i>&nbsp;</div>';
	this.view += '<input type="text" class="searchbox" placeholder="検索<Ctl + K>"/>';

	this.view += '</div>';	// .menu-bar

	this.view += '<div class="mail-body">';

	/* ドラッグ可能領域 */
	this.view += '<div id="dragArea2" class="drag-area2"></div>';

	this.view += '<div class="folder">';
	this.view += '<div class="folder-title1"><i class="fa fa-angle-down" aria-hidden="true"></i><i class="fa fa-envelope-o" aria-hidden="true"></i>' + this.account + '</div>';

	this.view += '<ul class="folder-list1">';
	this.view += '<li class="selected"><i class="fa fa-hdd-o" aria-hidden="true"></i>受信トレイ(614)</li>';
	this.view += '<li><i class="fa fa-file-text-o" aria-hidden="true"></i>下書き</li>';
	this.view += '<li><i class="fa fa-paper-plane-o" aria-hidden="true"></i>送信済みトレイ</li>';
	this.view += '<li><i class="fa fa-trash-o" aria-hidden="true"></i>ごみ箱</li>';
	this.view += '<li><i class="fa fa-folder-o" aria-hidden="true"></i>下書き</li>';
	this.view += '<li><i class="fa fa-folder-o" aria-hidden="true"></i>削除済みアイテム(280)</li>';
	this.view += '<li><i class="fa fa-folder-o" aria-hidden="true"></i>送信済みアイテム</li>';
	this.view += '<li><i class="fa fa-folder-o" aria-hidden="true"></i>迷惑メール</li>';
	this.view += '</ul>';


	this.view += '<div class="folder-title2"><i class="fa fa-angle-down" aria-hidden="true"></i><i class="fa fa-folder" aria-hidden="true"></i>ローカルフォルダー</div>';

	this.view += '<ul class="folder-list2">';
	this.view += '<li><i class="fa fa-trash-o" aria-hidden="true"></i>ごみ箱</li>';
	this.view += '<li><i class="fa fa-hdd-o" aria-hidden="true"></i>送信トレイ</li>';
	this.view += '</ul>';

	this.view += '</div>';	// .folder


	// メールの一覧
	this.view += '<div class="mail-list">';
	this.view += '<table class="mail-list">';
	this.view += '<thead><tr>';
	this.view += '<th class="th1">‡</td>';
	this.view += '<th class="th2">★</td>';
	this.view += '<th class="th3"><i class="fa fa-paperclip" aria-hidden="true"></i></td>';
	this.view += '<th class="th4">件名</td>';
	this.view += '<th class="th5"><i class="fa fa-link" aria-hidden="true"></i></td>';
	this.view += '<th class="th6">通信相手</td>';
	this.view += '<th class="th7"><i class="fa fa-fire" aria-hidden="true"></i></td>';
	this.view += '<th class="th8">通信日時</td>';
	this.view += '</tr></thead>';
	this.view += '<tbody>';
	this.view += '</tbody>';
	this.view += '</table>';
	this.view += '</div>';	// .mail-list


	// メール本文表示領域
	this.view += '<div class="mail-view">';
	this.view += '<div class="init-view">';
	this.view += '<img src="sie/img/sie-module-win-thunderbird/large_icon.png" width="100" style="top:50px;left:50px;">';
	this.view += '<div style="color:#2666A6;font-size:40px;top:65px;left:160px;">Thunderbird</div>';
	this.view += '<div style="color:#373D5C;font-size:20px;top:110px;left:160px;">へようこそ</div>';

	this.view += '<div style="color:#373D5C;font-size:18px;top:160px;left:50px;color:#4F96DD;"><i class="fa fa-heart" aria-hidden="true" style="color:#FF0039;"></i>&nbsp;Thunderbirdへ寄付</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:200px;left:50px;">Thunderbirdは、オープンソースでクロスプラットフォームを特徴と、ビジネスでも個</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:220px;left:50px;">人でも無料で利用できる人気のメール・カレンダークライアントです。私たちはそのセ</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:240px;left:50px;">キュリティを守りつつ、さらに改善していきたいと考えています。皆さんからの寄付に</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:260px;left:50px;">よって、開発者の雇用、インフラストラクチャの購入、改良の継続が可能となります。</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:290px;left:50px;"><span style="font-weight:bold;">Thunderbirdはあなたのようなユーザーに支えられています！Thunderbirdを気に入っ</span></div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:310px;left:50px;"><span style="font-weight:bold;">ているなら、ぜひ寄付を検討してください。</span>Thuderbirdを存続可能にするためあなたが</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:330px;left:50px;">できる最善の方法は<span style="color:#0A96DD;">寄付することです。&gt;&gt;</span></div>';

	this.view += '<div style="color:#373D5C;font-size:18px;top:370px;left:50px;color:#4F96DD;"><i class="fa fa-handshake-o" aria-hidden="true" style="color:#ED00B5"></i>&nbsp;協力する</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:410px;left:50px;">Thunderbirdはオープンソースプロジェクトです。つまり誰でも、アイデア、デザイン、</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:430px;left:50px;">コード、あるいは他のユーザーを助ける時間を提供できます。Thuderbirdユーザーをサ</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:450px;left:50px;">ポートするチームの一員となって、Thuderbirdを素晴らしい製品にしてください。<span style="color:#0A96DD;">参加</span></div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:470px;left:50px;"><span style="color:#0A96DD;">する&gt;&gt;</span></div>';

	this.view += '<div style="color:#373D5C;font-size:18px;top:510px;left:50px;color:#4F96DD;"><i class="fa fa-life-ring" aria-hidden="true" style="color:#0060DF"></i>&nbsp;サポートを受ける</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:550px;left:50px;">Thunderbirdについて何かサポートが必要ですか？質問をしたり、ナレッジベース記事を</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:570px;left:50px;">読んでThunderbirdの使い方を学んだりできる<span style="color:#0A96DD;">サポートページ</span>を用意しています。</div>';

	this.view += '<div style="color:#373D5C;font-size:18px;top:610px;left:50px;color:#4F96DD;"><i class="fa fa-bug" aria-hidden="true" style="color:#058B00"></i>&nbsp;バグを報告する</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:650px;left:50px;">Thunderbirdに何か問題を見たけたようですか？私たちが解決できるよう<span style="color:#0A96DD;">バグ報告を作成</span></div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:670px;left:50px;">してください。すべてのユーザーにとってTunderbirdがより良い製品となるよう、バグ</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:690px;left:50px;">の特定と修正にご協力を。<br/><br/></div>';


	this.view += '<div style="color:#373D5C;font-size:18px;top:170px;left:680px;">私たちが寄付を必要とする理由</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:210px;left:680px;">Thunderbirdは、以前のようにMozillaから資金を得ていません。幸い、製品の維持とさら</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:230px;left:680px;">なる開発を支えている活発なコミュニティがありますが、長期的な存続のため、プロジェ</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:250px;left:680px;">クトは信金を必要としています。</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:280px;left:680px;">Thuderbirdの資金は、アプリケーションを何百万人ものユーザーへ届けるために維持し</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:300px;left:680px;">なければならない相当な規模のインフラストラクチャと人件費に使われます。</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:320px;left:680px;">Thuderbirdが存続し進化を続けられるよう、私たちはあなたの支援を必要としており、</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:340px;left:680px;">この場で寄付をお願いしたいと思います。皆さんからの寄付はすべて直接Thunderbirdの</div>';
	this.view += '<div style="color:#373D5C;font-size:16px;top:360px;left:680px;">開発とインフラストラクチャに充てられます。</div>';


	this.view += '</div>';
	this.view += '</div>';
	


	// 区切り１
	this.view += '<div class="handle1"></div>';
	// 区切り２
	this.view += '<div class="handle2"></div>';

	this.view += '<div class="footer">';

	this.view += '<div class="footer1"><i class="fa fa-wifi" aria-hidden="true"></i></div>';
	this.view += '<span class="link-url"></span>';
	this.view += '<div class="footer2"><i class="fa fa-calendar" aria-hidden="true"></i>Todayペイン</div>';
	this.view += '<div class="footer3">合計:604</div>';
	this.view += '<div class="footer4">未読数:10</div>';

	this.view += '</div>';	// footer


	this.view += '</div>';
	this.view += '</div>';

	this.width=1000;
	this.height=700;
	this.onCreateEvents=[];
	this.onActiveEvents=[];
	this.onResizeEvents=[];
	this.params=null;
	this.thisWindow=null;
	this.resource="";


	// ウィンドウが初回表示時に1回だけ実行されるイベント
	this.addOnCreateEvent=function(event){
		this.onCreateEvents.push(event)
	}

	// ウィンドウが全面に表示された場合に実行されるイベントの追加
	this.addOnActiveEvent=function(event){
		this.onActiveEvents.push(event)
	}

	// ウィンドウがリサイズされた場合に実行されるイベントの追加
	this.addOnResizeEvent=function(event){
		this.onResizeEvents.push(event);
	}


	// 初期処理
	var _self = this

	this.addOnCreateEvent(function(params){

		_self.thisWindow=params["window"];

		_self.params = params;

		_self.setObject(params);

		// タイトルバーのセット
		var html = "";
		html += '<div class="delim"></div>';
		html += '<div class="menu menu1">ファイル(F)</div>';
		html += '<div class="menu menu2">編集(E)</div>';
		html += '<div class="menu menu3">表示(V)</div>';
		html += '<div class="menu menu4">移動(G)</div>';
		html += '<div class="menu menu5">ﾒｯｾｰｼﾞ(M)</div>';
		html += '<div class="menu menu6">予定とToDo(N)</div>';
		html += '<div class="menu menu7">ﾂｰﾙ(T)</div>';
		html += '<div class="menu menu8">ﾍﾙﾌﾟ(H)</div>';
		html += '<div class="tab"><i class="fa fa-hdd-o" aria-hidden="true"></i>受信トレイ</div>';

		_self.thisWindow.find('div.titlebar').append(html);

		var firstMail = null;
		for(var i=0; i<_self.resources.length; i++){

			var mail = '';
			mail += '<tr class="mail ';

			if(_self.resources[i]['id'] != null){
				mail += ' mail_' + _self.resources[i]['id'];
			}
			if(_self.initSelect && i == 0){
				mail += ' selected ';
			}

			if(!_self.resources[i]['is_readed']){
				mail += ' not-read ';
			}

			if(_self.resources[i]['attachments'] != null && _self.resources[i]['attachments'].length > 0){
				mail += ' attachment ';
			}
			mail += '" ';

			mail += ' data-index="' + i + '" ';
			mail += '>';
			mail += '<td class="td1"></td>';
			mail += '<td class="td2">☆</td>';
			mail += '<td class="td3">'
			if(_self.resources[i]['attachments'] != null && _self.resources[i]['attachments'].length > 0){
				mail += '<i class="fa fa-paperclip" aria-hidden="true">';
			}
			mail += '</td>';
			mail += '<td class="td4"><div>' +  _self.resources[i]['title'] + '</div></td>';
			mail += '<td class="td5">●</td>';
			mail += '<td class="td6"><div>' + _self.resources[i]['from'] + '</div></td>';
			mail += '<td class="td7"><i class="fa fa-fire" aria-hidden="true"></i></td>';

			if(_self.resources[i]['show_recieved'] == 'date'){
				mail += '<td class="td8">' + _self.resources[i]['recieved_date'] + "&nbsp;"+ _self.resources[i]['recieved_time'] + '</td>';
			}
			else{
				mail += '<td class="td8">' + _self.resources[i]['recieved_time'] + '</td>';
			}

			mail += '</tr>';

			var $mail = $(mail).appendTo(params['window'].find('div.mail-list table tbody'));


			$mail.click(function(){
				$('div.mail-list table tbody tr.selected').removeClass('selected');
				$(this).addClass('selected');

				var index = parseInt($(this).data('index'));
				if(_self.resources[index]['is_enable_open']){
					_self.showMail(index);
					if(_self.resources[index]['open_action'] != null){
						if(_self.resources[index]['open_action_delay'] == null){
							_self.resources[index]['open_action_delay'] = 100;
						}
						setTimeout(function(){
							_self.resources[index]['open_action']();
						}, _self.resources[index]['open_action_delay']);
					}
				}
			});
			if(firstMail == null){
				firstMail = mail;
			}
		}

		// メールリストのサイズ設定
		_self.setMailListSize();


		// メッセージの表示
		if(firstMail != null && _self.initSelect){
			firstMail.click();
		}

		var startTop = null;
		// 枠のドラッグ制御
		_self.thisWindow.find('div.handle2').draggable({
																	axis: "y",
																	containment: '#dragArea2',
																	start:function(e, ui){
																				_self.thisWindow.find('div.mail-list,div.mail-view').css({'opacity':0});
																				startTop = ui.offset['top'];
																			},
																	stop:function(e, ui){
																				var stopTop = ui.offset['top'];
																				var distance = stopTop - startTop;
																				var listTop = to_num(_self.thisWindow.find('div.mail-list').css('top')) + distance;
																				var viewTop = to_num(_self.thisWindow.find('div.mail-view').css('top')) + distance;

																				var listHeight = to_num(_self.thisWindow.find('div.mail-list').css('height')) + distance;
																				var viewHeight = to_num(_self.thisWindow.find('div.mail-view').css('height')) - distance;

																				_self.thisWindow.find('div.mail-list').css({'height':to_px(listHeight)});

																				_self.thisWindow.find('div.mail-view').css({'top':to_px(viewTop), 'height':to_px(viewHeight)});
																				_self.thisWindow.find('div.mail-list,div.mail-view').css({'opacity':1});
																				// メールリストのサイズ設定
																				_self.setMailListSize();
																			}

															});


	})

	// リサイズイベント
	this.addOnResizeEvent(function(params){
		_self.setObject(params);
	})

	// オブジェクトの位置をセット
	this.setObject=function(params){
		// メールリストのサイズ設定
		_self.setMailListSize();
	}


	this.setMailListSize=function(){
		var _self = this;

		setTimeout(function(){
			var width = _self.thisWindow.find('div.mail-list').width();
			var tableWidth = width < 800 ? 800 : width;

			var height1 = _self.thisWindow.find('div.mail-list').height();
			var height2 = _self.thisWindow.find('div.mail-list table thead').height();

			_self.thisWindow.find('div.mail-list table').css({'width' : to_px(tableWidth)});
			_self.thisWindow.find('div.mail-list table tbody').css({'height' : to_px(height1 - height2)});
			_self.thisWindow.find('div.mail-list table th.th1,div.mail-list table td.td1').css({'width':'20px'});
			_self.thisWindow.find('div.mail-list table th.th2,div.mail-list table td.td2').css({'width':'20px'});
			_self.thisWindow.find('div.mail-list table th.th3,div.mail-list table td.td3').css({'width':'20px'});
			_self.thisWindow.find('div.mail-list table th.th4').css({'width': to_px(tableWidth - 480)});
			_self.thisWindow.find('div.mail-list table td.td4').css({'width': to_px(tableWidth - 480)});
			_self.thisWindow.find('div.mail-list table td.td4 div').css({'width': to_px(tableWidth - 490)});
			_self.thisWindow.find('div.mail-list table th.th5,div.mail-list table td.td5').css({'width':'20px'});
			_self.thisWindow.find('div.mail-list table th.th6,div.mail-list table td.td6').css({'width':'230px'});
			_self.thisWindow.find('div.mail-list table td.td6 div').css({'width':'220px'});
			_self.thisWindow.find('div.mail-list table th.th7,div.mail-list table td.td7').css({'width':'20px'});
			_self.thisWindow.find('div.mail-list table th.th8').css({'width': '150px'});
			_self.thisWindow.find('div.mail-list table td.td8').css({'width': '125px'});
			_self.thisWindow.find('div.mail-list table td.td8 div').css({'width': '115px'});
		}, 10);
	}

	this.showMail=function(index){

		var _self = this;
		var html = '';

		html += '<div class="message-head">';

		html += '<div class="from-title">差出人</div>';
		html += '<div class="from-body">' + _self.resources[index]['from'] + '＜' + _self.resources[index]['from_addr'] + '＞' + '</div>';
		html += '<div class="subject-title">件名</div>';
		html += '<div class="subject-body">' + _self.resources[index]['title'] + '</div>';
		html += '<div class="to-title">宛先</div>';
		html += '<div class="to-body">' + _self.resources[index]['to'] + '</div>';


		html += '<div class="button button-reply"><i class="fa fa-reply" aria-hidden="true"></i>返信</div>';
		html += '<div class="button button-reply-all"><i class="fa fa-reply-all" aria-hidden="true"></i></i>全員に返信</div>';
		html += '<div class="button button-reply-all-down"><i class="fa fa-angle-down" aria-hidden="true"></i></div>';
		html += '<div class="button button-transfer"><i class="fa fa-reply" aria-hidden="true"></i>転送</div>';
		html += '<div class="button button-archive"><i class="fa fa-archive" aria-hidden="true"></i>アーカイブ</div>';
		html += '<div class="button button-fire"><i class="fa fa-fire" aria-hidden="true"></i>迷惑メールを付ける</div>';
		html += '<div class="button button-trash"><i class="fa fa-trash-o" aria-hidden="true"></i>削除</div>';
		html += '<div class="button button-other">その他<i class="fa fa-angle-down" aria-hidden="true"></i></div>';


		html += '<div class="receive-time">' +   _self.resources[index]['recieved_date'] + " " + _self.resources[index]['recieved_time'] + '</div>';

		html += '</div>';
		html += '<div class="message-body ';
		if(_self.resources[index]['attachments'] != null && _self.resources[index]['attachments'].length > 0){
			html += 'attachment';
		}
		html += '">';
		html += _self.resources[index]['body'];
		html += '</div>';
		if(_self.resources[index]['attachments'] != null && _self.resources[index]['attachments'].length > 0){
			html += '<div class="attachment-head">';
			html += '<i class="fa fa-angle-down" aria-hidden="true"></i><i class="fa fa-paperclip" aria-hidden="true"></i>添付ファイル:&nbsp;';
			html += _self.resources[index]['attachments'][0]['file_name'];
			html += '<span>' + _self.resources[index]['attachments'][0]['file_size'] + '</span>';
			html += '<div class="save-down"><i class="fa fa-angle-down" aria-hidden="true"></i>&nbsp;</div><div class="save"><i class="fa fa-file-o" aria-hidden="true"></i>保存</div>';
			html += '</div>';
			html += '<div class="attachment-body">';

			html += '<div class="dl" data-index="' + index + '" data-attachment="' + 0 + '">';
			html += '<img src="' + _self.resources[index]['attachments'][0]['icon'] + '"/>';
			html += _self.resources[index]['attachments'][0]['file_name'];
			html += '<span>' + _self.resources[index]['attachments'][0]['file_size'] + '</span>';
			html += '</div>';
			html += '</div>';
		}

		_self.params["window"].find('.mail-view').html(html);

		_self.params["window"].find("div.dl").dblclick(function(){
			var mailIndex = parseInt($(this).data('index'));
			var attachmentIndex = parseInt($(this).data('attachment'));

			if(_self.resources[mailIndex]['attachments'][attachmentIndex]['click_action'] != null){
				_self.resources[mailIndex]['attachments'][attachmentIndex]['click_action']();
			}
		});
	}

	// リンクの表示
	this.showLink=function(label){
		_self.thisWindow.find('span.link-url').text(label);
	}
	this.hideLink=function(){
		_self.thisWindow.find('span.link-url').text('');
	}


	// 既読
	this.setReaded=function(id, readed){
		var _self = this;
		if(readed){
			if(_self.thisWindow != null){
				_self.thisWindow.find('.mail_' + id).removeClass('not-read');
			}
			for(var i=0; i<_self.resources.length; i++){
				if(_self.resources[i]['id'] == id){
					_self.resources[i].is_readed=true;
					break;
				}
			}
		}
		else{
			if(_self.thisWindow != null){
				_self.thisWindow.find('.mail_' + id).addClass('not-read');
			}
			for(var i=0; i<_self.resources.length; i++){
				if(_self.resources[i]['id'] == id){
					_self.resources[i].is_readed=false;
					break;
				}
			}
		}
	}

	// 既読の確認
	this.isReaded=function(id){
		var _self = this;
		var ret = false;
		for(var i=0; i<_self.resources.length; i++){
			if(_self.resources[i]['id'] == id){
				ret = _self.resources[i].is_readed;
				break;
			}
		}
		return ret;
	}


}

