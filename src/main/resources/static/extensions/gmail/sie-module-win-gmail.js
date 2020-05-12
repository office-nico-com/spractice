// Gmailモジュール
var SieModuleWinGmail = function(moduleId){
	this.id = moduleId;
	this.title = "Gmail";
	this.titlebar = "Gmail";
	this.icon = "sie/img/icon-gmail.png"
	this.isShowDeskTop=true;
	this.isWindowOpen=true;
	this.moduleClass="sie-module-win-gmail";
	this.delayedStart = 0;
	this.backAction=null;
	this.headerIcon = "sie/img/icon-gmail.png"

	this.url="mail.google.com/mail/u/0/?tab=wm&ogbl#inbox"
	this.view = '<div class="sie-module-win-gmail-body">';
	this.view += '<div class="menu-bar">'
	this.view += '<i class="fa fa-arrow-left" aria-hidden="true"></i>';
	this.view += '<i class="fa fa-arrow-right" aria-hidden="true"></i>';
	this.view += '<i class="fa fa-repeat" aria-hidden="true"></i>';
	this.view += '<div class="address-bar">';
	this.view += '<span><i class="fa fa-lock" aria-hidden="true"></i></span>';
	this.view += '<input type="text" value="' + this.url + '"/>';
	this.view += '</div>';
	this.view += '<div class="setting"><i class="fa fa-ellipsis-v" aria-hidden="true"></i></div>';
	this.view += '<i class="fa fa-user-circle" aria-hidden="true"></i>';
	this.view += '<br/>';
	this.view += '</div>';
	this.view += '<div class="gmail-header">'
	this.view += '<div class="main-menu" ><i class="fa fa-bars" aria-hidden="true"></i></div>';
	this.view += '<img src="' + this.headerIcon + '" class="gmail-icon" width="46">';
	this.view += '<div class="service-name">Gmail</div>'
	this.view += '<input type="text" placeholder="メールを検索"/>'

	this.view += '<i class="fa fa-search" aria-hidden="true"></i>';

	this.view += '<div class="gmail-help"><i class="fa fa-question-circle-o" aria-hidden="true"></i></div>'
	this.view += '<div class="gmail-apl"><i class="fa fa-th" aria-hidden="true"></i></div>'
	this.view += '<div class="gmail-user"><i class="fa fa-user-circle" aria-hidden="true"></i></div>'
	this.view += '</div>'; // .gmail-header

	this.view += '<div class="gmail-left">'
	this.view += '<div class="gmail-left-top">'

	this.view += '<div class="button-new">'
	this.view += '<img src="sie/img/sie-module-win-gmail/gmail-new-button.png">';
	this.view += '<div class="text">作成</div>';
	this.view += '</div>'; // .button-new

	this.view += '<div class="folder">';
	this.view += '<ul>';
	this.view += '<li class="selected"><i class="fa fa-inbox" aria-hidden="true"></i><div>受信トレイ</div><span class="mail-count">0</span></li>';
	this.view += '<li><i class="fa fa-star" aria-hidden="true"></i><div>スター付き</div></li>';
	this.view += '<li><i class="fa fa-clock-o" aria-hidden="true"></i><div>スヌーズ中</div></li>';
	this.view += '<li><i class="fa fa-paper-plane" aria-hidden="true"></i><div>送信済み</div></li>';
	this.view += '<li><i class="fa fa-file" aria-hidden="true"></i><div>下書き</div></li>';
	this.view += '<li><i class="fa fa-angle-down" aria-hidden="true"></i><div>もっと見る</div></li>';
	this.view += '</ul>';
	this.view += '</div>'// .folder
	this.view += '</div>'; // .gmail-left-top

	this.view += '<div class="gmail-left-bottom">'
	this.view += '<div class="bottom-header">';

	this.view += '<i class="fa fa-user-circle" aria-hidden="true"></i><div>太郎 ▼</div>'
	this.view += '<i class="fa fa-plus" aria-hidden="true"></i>'
	this.view += '</div>'; // .bottom-header

	this.view += '<i class="fa fa-commenting" aria-hidden="true"></i>';


	this.view += '<div class="message">ハングアウトの連絡先がありません</div>';
	this.view += '<div class="link">ユーザを探す</div>';

	this.view += '</div>'; // .gmail-left-bottom

	this.view += '</div>'; // .gmail-left


	this.view += '<div class="gmail-right-header">'

	this.view += '<div class="header-group-1">'
	this.view += '<i class="fa fa-square-o" aria-hidden="true"></i>';
	this.view += '<span class="arrow1">▼</span>';
	this.view += '<i class="fa fa-repeat" aria-hidden="true"></i>';
	this.view += '<i class="fa fa-ellipsis-v" aria-hidden="true"></i>';
	this.view += '<span class="page">1-50 / 265行</span>';
	this.view += '<span class="left-arrow"><i class="fa fa-angle-left" aria-hidden="true"></i></span>';
	this.view += '<span class="right-arrow"><i class="fa fa-angle-right" aria-hidden="true"></i></span>';
	this.view += '<span class="text">あ</span>';
	this.view += '<span class="arrow2">▼</span>';
	this.view += '<i class="fa fa-cog" aria-hidden="true"></i>';
	this.view += '</div>';

	this.view += '<div class="header-group-2">';
	this.view += '<div class="button-frame back"><i class="fa fa-arrow-left" aria-hidden="true"></i>';
	this.view += '  <div class="description">受信トレイに戻る</div>';
	this.view += '</div>';
	this.view += '<div class="button-frame archive"><i class="fa fa-archive" aria-hidden="true"></i>';
	this.view += '  <div class="description">アーカイブ</div>';
	this.view += '</div>';
	this.view += '<div class="button-frame notify"><i class="fa fa-info-circle" aria-hidden="true"></i>';
	this.view += '  <div class="description">迷惑メールを報告</div>';
	this.view += '</div>';
	this.view += '<div class="button-frame trash"><i class="fa fa-trash" aria-hidden="true"></i>';
	this.view += '  <div class="description">削除</div>';
	this.view += '</div>';
	this.view += '<span class="delim">｜</span>';
	this.view += '<div class="button-frame notread"><i class="fa fa-envelope" aria-hidden="true"></i>';
	this.view += '  <div class="description">未読にする</div>';
	this.view += '</div>';
	this.view += '<div class="button-frame snooze"><i class="fa fa-clock-o" aria-hidden="true"></i>';
	this.view += '  <div class="description">スヌーズ</div>';
	this.view += '</div>';
	this.view += '<span class="delim">｜</span>';
	this.view += '<div class="button-frame move"><i class="fa fa-external-link-square" aria-hidden="true"></i>';
	this.view += '  <div class="description">移動</div>';
	this.view += '</div>';
	this.view += '<div class="button-frame tag"><i class="fa fa-tag" aria-hidden="true"></i>';
	this.view += '  <div class="description">ラベル</div>';
	this.view += '</div>';
	this.view += '<div class="button-frame tag"><i class="fa fa-ellipsis-v" aria-hidden="true"></i></div>';

	this.view += '<div class="button-frame setting"><i class="fa fa-cog" aria-hidden="true"></i></div>';
	this.view += '<span class="arrow2">▼</span>';
	this.view += '<span class="text">あ</span>';
	this.view += '<div class="button-frame arrow-right"><i class="fa fa-angle-right" aria-hidden="true"></i></div>';
	this.view += '<div class="button-frame arrow-left"><i class="fa fa-angle-left" aria-hidden="true"></i></div>';
	this.view += '<span class="page">3 / 265</span>';


	this.view += '</div>'

	this.view += '</div>'; // .gmail-right-header

	this.view += '<div class="gmail-right-body">'

	this.view += '<ul class="mail-list">'

	this.view += '</ul>'

	this.view += '<div class="gmail-footer">';
	this.view += '<div class="capacity">0.07 GB(0%) / 15GB を使用中</div>';
	this.view += '<div class="admin">管理</div>';
	this.view += '<div class="policy">利用規約 · プライバシー · <br/>プログラム ポリシー</div>';
	this.view += '<div class="hisotory">前回のアカウント アクティビティ: 37 分前</div>';
	this.view += '<div class="detail">詳細</div>';
	this.view += '</div>'; // .gmail-footer

	this.view += '</div>'; // .gmail-right-body


	this.view += '<div class="gmail-right-side">'

	this.view += '</div>'; // .gmail-right-side


	this.view += '<div class="gmail-download-footer">'

	this.view += '<div class="gmail-download-footer-close"><i class="fa fa-times" aria-hidden="true"></i></div>'

	this.view += '</div>'; // .gmail-download-footer

	this.view += '</div>'; // .sie-module-win-gmail-body


	this.width=1000;
	this.height=550;
	this.onCreateEvents=[];
	this.onActiveEvents=[];
	this.onResizeEvents=[];
	this.params=null;
	this.thisWindow=null;
	this.resources=[];


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


		var $titlebar = params["window"].find(".titlebar");
		$titlebar.prepend('<div class="tab-menu"><img src="' + _self.icon + '"/>' + _self.titlebar + '<i class="fa fa-times" aria-hidden="true"></i></div>');

		for(var i=0; i<_self.resources.length; i++){

			var li = '<li class="';

			if(_self.resources[i]['is_readed']){
				li += 'readed ';
			}

			if(_self.resources[i]['attachments'] != null && _self.resources[i]['attachments'].length > 0){
				li += 'attachment ';
			}
			li += '" ';
			li += ' data-index="' + i + '" ';
			li += '>';
			li += '<label><input type="checkbox" class="check">';
			li += '<i class="fa fa-square-o" aria-hidden="true"></i>';
			li += '<i class="fa fa-check-square" aria-hidden="true"></i>';
			li += '</label>'
			li += '<i class="fa fa-star-o" aria-hidden="true"></i>';
			li += '<div class="from">' + _self.resources[i]['from'] + '</div>';

			li += '<div class="title">';
			li += _self.resources[i]['title'];

			if(_self.resources[i]['attachments'] != null && _self.resources[i]['attachments'].length > 0){
				li += '<div style="top:35px;border:solid 1px #999;line-height:28px;padding-left:10px;padding-right:10px;border-radius: 15px;color:#666;"><span style="margin-right:5px;font-size:120%;color:' + _self.resources[i]['attachments'][0]['color'] + ';">■</span>' + _self.resources[i]['attachments'][0]['file_name'] + '</div>';
			}
			li += '</div>';

			li += '<div class="recieved_at">';
			if(_self.resources[i]['show_recieved'] == 'date'){
				li += _self.resources[i]['recieved_date'];
			}
			else{
				li += _self.resources[i]['recieved_time'];
			}
			li += '</div>';
			li += '<br style="clear:both;">'
			li += '</li>'
			var mail = $(li).appendTo(params['window'].find('ul.mail-list'));
			mail.click(function(){

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
		}


		var count = 0;
		for(var i=0; i<_self.resources.length; i++){
			if(!_self.resources[i]['is_readed']){
				count++;
			}
		}
		params['window'].find('.mail-count').text(count);

		$(params["window"].find("div.gmail-download-footer-close")).click(function(){
			$(params["window"].find("div.download-box")).remove();
			_self.params["window"].find("div.sie-module-win-gmail-body").removeClass("downloaded");
		});

	})

	this.showMail=function(index){
		var _self = this;

		_self.params["window"].find("div.header-group-1").hide();
		_self.params["window"].find("div.header-group-2").show();
		_self.params["window"].find("ul.mail-list").hide();
		_self.params["window"].find("div.gmail-footer").hide();

		var html = '<div class="mail-block">'
		html += '<div class="title">';
		html += _self.resources[index]['title'];
		html += '</div>';	// .title
		html += '<div class="print">';
		html += '<i class="fa fa-print" aria-hidden="true"></i>';
		html += '<i class="fa fa-external-link" aria-hidden="true"></i>';
		html += '</div>';

		html += '<div class="from">';
		html += '<i class="fa fa-user-circle" aria-hidden="true"></i>';
		html += _self.resources[index]['from'] + ' &lt;' + _self.resources[index]['from_addr'] + '&gt;';
		html += '</div>';	// .from

		html += '<div class="favorite">';
		html += '<span>' + _self.resources[index]['recieved_date'] + ' ' + _self.resources[index]['recieved_time'] + '</span>';
		html += '<i class="fa fa-star-o" aria-hidden="true"></i>';
		html += '<i class="fa fa-reply" aria-hidden="true"></i>';
		html += '<i class="fa fa-ellipsis-v" aria-hidden="true"></i>';
		html += '</div>';

		html += '<div class="to">';
		html += 'To  ' + _self.resources[index]['to'];
		html += '<span>▼</span>' 
		html += '</div>';	// .title

		html += '<div class="body">';
		html += _self.resources[index]['body'];
		html += '</div>';	// .body



		if(_self.resources[index]['attachments'] != null && _self.resources[index]['attachments'].length > 0){
			html += '<hr/>';
			html += '<div class="attachments">';

			for(var i=0; i<_self.resources[index]['attachments'].length; i++){

				html += '<div class="file">';
				html += '<div class="file-body">';
				html += _self.resources[index]['attachments'][i]['preview'];
				html += '</div>';
				html += '<div class="file-footer">';

				html += '<div class="file-type" style="background-color:' + _self.resources[index]['attachments'][i]['color'] + ';">' + _self.resources[index]['attachments'][i]['icon_text'] + '</div>';
				html += '<span class="file-name">' + _self.resources[index]['attachments'][i]['file_name'] + '</span>';

				html += '<p class="init-hide size">' + _self.resources[index]['attachments'][i]['file_size'] + '</p>';

				html += '<div class="init-hide download" data-index="' + index + '" data-attachment="' + i + '"><i class="fa fa-arrow-down" aria-hidden="true"></i>';
				html += '  <div class="description">ダウンロード</div>';
				html += '</div>';
				html += '<div class="init-hide drive"><i class="fa fa-recycle" aria-hidden="true"></i>';
				html += '  <div class="description">ドライブに保存</div>';
				html += '</div>';
				html += '<div class="init-hide edit"><i class="fa fa-pencil" aria-hidden="true"></i>';
				html += '  <div class="description">Googleドキュメントで編集</div>';
				html += '</div>';

				html += '<div class="block1"></div>';
				html += '<div class="block2" style="border-bottom-color:' + _self.resources[index]['attachments'][i]['color'] + ';"></div>';
				html += '<div class="block3"></div>';

				html += '</div>';
				html += '</div>';
			}

			html += '</div>';	// .attachements
		}

		html += '<div class="control">';
		html += '<button><i class="fa fa-reply" aria-hidden="true"></i>返信</button>';
		html += '<button><i class="fa fa-arrow-right" aria-hidden="true"></i>転送</button>';
		html += '</div>';	// .control


		html += '</div>';	// .mail-block

		_self.params["window"].find("div.gmail-right-body").append(html);

		_self.params["window"].find("div.back").click(function(){
			_self.params["window"].find("div.mail-block").remove();
			_self.params["window"].find("div.header-group-1").show();
			_self.params["window"].find("div.header-group-2").hide();
			_self.params["window"].find("ul.mail-list").show();
			_self.params["window"].find("div.gmail-footer").show();

			if(_self.backAction != null){
				_self.backAction();
			}
		});

		_self.params["window"].find("div.mail-block div.file-footer div.download").click(function(){

			var mailIndex = parseInt($(this).data('index'));
			var attachmentIndex = parseInt($(this).data('attachment'));


			_self.params["window"].find("div.sie-module-win-gmail-body").addClass("downloaded");
			var downloadBox = '<div class="download-box">';

			downloadBox += '<img src="' + _self.resources[mailIndex]['attachments'][attachmentIndex]['download_icon'] + '">';
			downloadBox += '<span class="file-name">' + _self.resources[mailIndex]['attachments'][attachmentIndex]['file_name'] + '</span>';
			downloadBox += '<div class="delim"></div>';
			downloadBox += '<i class="fa fa-angle-down" aria-hidden="true"></i>';
			downloadBox += '</div>';

//			_self.params["window"].find("div.gmail-download-footer").prepend(downloadBox);
			var $downloadBox = $(downloadBox).prependTo(_self.params["window"].find("div.gmail-download-footer"));
			if(_self.resources[mailIndex]['attachments'][attachmentIndex]['click_action'] != null){
				$downloadBox.click(function(){_self.resources[mailIndex]['attachments'][attachmentIndex]['click_action']()});
			}

			var download = $('<i class="fa fa-download" aria-hidden="true"></i>').prependTo(_self.params["window"].find("div.gmail-download-footer"));
			download.animate({top:'-30px', opacity:0}, 800, 'linear', function(){$(this).remove();});


			if(_self.resources[mailIndex]['attachments'][attachmentIndex]['download_action'] != null){
				if(_self.resources[mailIndex]['attachments'][attachmentIndex]['download_action_delay'] == null){
					_self.resources[mailIndex]['attachments'][attachmentIndex]['download_action_delay'] = 10;
				}
				setTimeout(function(){
					_self.resources[mailIndex]['attachments'][attachmentIndex]['download_action']();
				}, _self.resources[mailIndex]['attachments'][attachmentIndex]['download_action_delay']);
			}

		});
	}
	
	// 添付ファイルをホバー状態にする
	this.hoverAttachment=function(){
		var _self = this;
		_self.params['window'].find('div.attachments div.file').addClass('hover');
		
	}
	
	// 添付ファイルのホバー状態を解除する
	this.deHoverAttachment=function(){
		var _self = this;
		_self.params['window'].find('div.attachments div.file').removeClass('hover');
		
	}

}

