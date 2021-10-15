// アウトルックモジュール
var SieModuleWinOutlookCom = function(moduleId){
	this.id = moduleId;
	this.title = "Outlook";
	this.titlebar = "Outlook";
	this.icon = "sie/img/icon-outlook.png"
	this.isShowDeskTop=true;
	this.isWindowOpen=true;
	this.moduleClass="sie-module-win-outlook-com";
	this.delayedStart = 0;
	this.initSelect = true;

	this.view = '<div class="sie-module-win-outlook-com-body">';

//---------------------
	// browser header
	this.url="outlook.live.com/mail/0/inbox/id/EwLTQyNTktODc4MS0wMAItMDAKABAAQQkADAwATE0ODEwLTQyNTktODc4MS0wMAItM"
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

//---------------------
	// outlook header
	this.view += '<div class="outlook-link-popup"></div>'
	this.view += '<div class="outlook-header">'


	this.view += '<div class="top">'

	this.view += '<div class="main-menu" >';
	this.view += '<div class="circle" style="top:15px;left:15px;"></div>';
	this.view += '<div class="circle" style="top:15px;left:22px;"></div>';
	this.view += '<div class="circle" style="top:15px;left:29px;"></div>';

	this.view += '<div class="circle" style="top:22px;left:15px;"></div>';
	this.view += '<div class="circle" style="top:22px;left:22px;"></div>';
	this.view += '<div class="circle" style="top:22px;left:29px;"></div>';

	this.view += '<div class="circle" style="top:29px;left:15px;"></div>';
	this.view += '<div class="circle" style="top:29px;left:22px;"></div>';
	this.view += '<div class="circle" style="top:29px;left:29px;"></div>';
	this.view += '</div>';

	this.view += '<input type="text" placeholder="検索"/>'
	this.view += '<i class="fa fa-search" aria-hidden="true"></i>';

	this.view += '<div class="skype"><i class="fa fa-skype" aria-hidden="true"></i></div>';
	this.view += '<div class="calendar"><i class="fa fa-calendar-check-o" aria-hidden="true"></i></div>';
	this.view += '<div class="cog"><i class="fa fa-cog" aria-hidden="true"></i></div>';
	this.view += '<div class="help"><i class="fa fa-question" aria-hidden="true"></i></div>';
	this.view += '<div class="news"><i class="fa fa-paper-plane" aria-hidden="true"></i></div>';
	this.view += '<div class="user"><i class="fa fa-user-circle-o" aria-hidden="true"></i>';
	this.view += '<i class="fa fa-user-circle" aria-hidden="true"></i>';
	this.view += '</div>';

	this.view += '</div>'

	this.view += '<div class="bottom">'
	this.view += '<div class="bar"><i class="fa fa-bars" aria-hidden="true"></i></div>'; 
	this.view += '<div class="create">新しいメッセージ</div>'; 

	this.view += '<div class="control outlook_com_delete" style="left:200px;"><i class="fa fa-trash-o" aria-hidden="true"></i>削除</div>'; 
	this.view += '<div class="control outlook_com_archive" style="left:260px;"><i class="fa fa-archive" aria-hidden="true"></i>アーカイブ</div>'; 
	this.view += '<div class="control outlook_com_notify" style="left:365px;"><i class="fa fa-ban" aria-hidden="true"></i>迷惑メール</div>'; 
	this.view += '<div class="control" style="left:470px;"><i class="fa fa-spoon" aria-hidden="true" style="transform: rotate(-135deg);"></i>一括処理</div>'; 
	this.view += '<div class="control" style="left:550px;"><i class="fa fa-sign-in" aria-hidden="true"></i>移動<i class="fa fa-angle-down" aria-hidden="true" style="margin-left:5px;"></i></div>'; 
	this.view += '<div class="control" style="left:630px;"><i class="fa fa-tag" aria-hidden="true"></i>分類<i class="fa fa-angle-down" aria-hidden="true" style="margin-left:5px;"></i></div>'; 
	this.view += '<div class="control" style="left:710px;"><i class="fa fa-clock-o" aria-hidden="true"></i>分類<i class="fa fa-angle-down" aria-hidden="true" style="margin-left:5px;"></i></div>'; 
	this.view += '<div class="control" style="left:790px;"><i class="fa fa-undo" aria-hidden="true" style="color:#A19F9D;"></i>やり直し</div>'; 
	this.view += '<div class="control" style="left:880px;"><i class="fa fa-ellipsis-h" aria-hidden="true"></i></div>'; 

	this.view += '</div>'; 
	this.view += '</div>'; 


	this.view += '<div class="outlook-body">';

	/* ドラッグ可能領域 */
	this.view += '<div id="outlookDragArea2" class="outlook-drag-area2"></div>';

	this.view += '<div class="outlook-folder">';
	this.view += '<div class="outlook-folder-title1"><i class="fa fa-angle-down" aria-hidden="true"></i>お気に入り</div>';

	this.view += '<ul class="outlook-folder-list1">';
	this.view += '<li class="selected"><i class="fa fa-hdd-o" aria-hidden="true"></i>受信トレイ</li>';
	this.view += '<li><i class="fa fa-pencil" aria-hidden="true"></i>下書き</li>';
	this.view += '<li><i class="fa fa-paper-plane-o" aria-hidden="true"></i>送信済みアイテム</li>';
	this.view += '<li><i class="fa fa-trash" aria-hidden="true"></i>削除済みアイテム</li>';
	this.view += '<li style="color:#102D62;padding-left:42px;">お気に入り追加</li>';
	this.view += '</ul>';

	this.view += '<div class="outlook-folder-title2"><i class="fa fa-angle-right" aria-hidden="true"></i>フォルダー</div>';
	this.view += '<div class="outlook-folder-title3"><i class="fa fa-angle-right" aria-hidden="true"></i>グループ</div>';



	this.view += '</div>';	// .outlook-folder

	this.view += '<div class="outlook-list">';

	this.view += '<div class="check"><i class="fa fa-check-circle-o" aria-hidden="true"></i></div>';

	this.view += '<div class="all"><i class="fa fa-inbox" aria-hidden="true"></i>優先</div>';

	this.view += '<div class="notread"><i class="fa fa-envelope-o" aria-hidden="true"></i>その他</div>';
	this.view += '<div class="filter">フィルター<i class="fa fa-chevron-down" aria-hidden="true"></i></div>';

	this.view += '<div class="list">';
	this.view += '<ul></ul>';
	this.view += '</div>';

	this.view += '</div>';	// .outlook-list

	// メール本文表示領域
	this.view += '<div class="outlook-view"></div>';

	// 区切り１
	this.view += '<div class="outlook-handle1"></div>';
	// 区切り２
	this.view += '<div class="outlook-handle2"></div>';

	this.view += '<div class="outlook-footer">';

	this.view += '</div>';	// .outlook-footer


	this.view += '</div>';

	this.view += '</div>';

	this.width=1000;
	this.height=700;
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


		// タイトルバーのセット
		var $titlebar = params["window"].find(".titlebar");
		$titlebar.prepend('<div class="tab-menu"><img src="' + _self.icon + '"/>' + _self.titlebar + '<i class="fa fa-times" aria-hidden="true"></i></div>');


		var firstMail = null;
		for(var i=0; i<_self.resources.length; i++){

			var li = '<li class="';

			if(_self.resources[i]['id'] != null){
				li += ' mail_' + _self.resources[i]['id'];
			}

			if(_self.initSelect && i == 0){
				li += ' selected ';
			}

			if(!_self.resources[i]['is_readed']){
				li += ' not-read ';
			}

			if(_self.resources[i]['attachments'] != null && _self.resources[i]['attachments'].length > 0){
				li += ' attachment ';
			}
			li += '" ';
			li += ' data-index="' + i + '" ';
			li += '>';

			li += '<div class="not-read-bar"></div>';

			li += '<div class="user_icon">';
			li += '<i class="fa fa-user-circle-o" aria-hidden="true"></i>';
			li += '<i class="fa fa-user-circle" aria-hidden="true"></i>';
			li += '</div>';

			li += '<div class="from" style="position:relative;overflow:visible;">' + _self.resources[i]['from'];


			li += '<div style="" class="from_addr">' + _self.resources[i]['from_addr'] + '</div>';

			li += '</div>';
			li += '<div class="title">' + _self.resources[i]['title'] + '</div>';
			li += '<div class="body">' + _self.resources[i]['body'] + '</div>';

			if(_self.resources[i]['attachments'] != null && _self.resources[i]['attachments'].length > 0){
				li += '<div class="attachment"><i class="fa fa-paperclip" aria-hidden="true"></i></div>';
			}
			li += '<div class="time">';
			if(_self.resources[i]['show_recieved'] == 'date'){
				li += _self.resources[i]['recieved_date'];
			}
			else{
				li += _self.resources[i]['recieved_time'];
			}
			li += '</div>';

			if(_self.resources[i]['attachments'] != null && _self.resources[i]['attachments'].length > 0){
				li += '<div class="attachment_box">';
				li += '<img src="' + _self.resources[i]['attachments'][0]['icon'] + '" height="18">';
				li += _self.resources[i]['attachments'][0]['file_name'];
				li += '</div>'
			}

			li += '</li>';
			var mail = $(li).appendTo(params['window'].find('div.outlook-list div.list ul'));
			mail.click(function(){

				$('div.outlook-list div.list ul li.selected').removeClass('selected');
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

		// メッセージの表示
		if(firstMail != null && _self.initSelect){
			firstMail.click();
		}

		var startLeft = null;
		// 枠のドラッグ制御
		_self.thisWindow.find('div.outlook-handle2').draggable({
																	axis: "x",
																	containment: '#outlookDragArea2',
																	start:function(e, ui){
																				_self.thisWindow.find('div.outlook-list,div.outlook-view').css({'opacity':0});
																				startLeft = ui.offset['left'];
																			},
																	stop:function(e, ui){
																				var stopLeft = ui.offset['left'];
																				var distance = stopLeft - startLeft;
																				var listLeft = to_num(_self.thisWindow.find('div.outlook-list').css('left')) + distance;
																				var viewLeft = to_num(_self.thisWindow.find('div.outlook-view').css('left')) + distance;

																				var listWidth = to_num(_self.thisWindow.find('div.outlook-list').css('width')) + distance;
																				var viewWidth = to_num(_self.thisWindow.find('div.outlook-view').css('width')) - distance;

																				_self.thisWindow.find('div.outlook-list').css({'width':to_px(listWidth)});

																				_self.thisWindow.find('div.outlook-view').css({'left':to_px(viewLeft), 'width':to_px(viewWidth)});
																				_self.thisWindow.find('div.outlook-list,div.outlook-view').css({'opacity':1});
																			}



															});



	})

	// リサイズイベント
	this.addOnResizeEvent(function(params){
	})


	this.showMail=function(index){

		var _self = this;
		var html = '';


		html += '<div class="message-head title">' + _self.resources[index]['title'] + '</div>';

		html += '</div>';


		html += '<div class="message-body">';

		html += '<div class="user_icon">';
		html += '<i class="fa fa-user-circle-o" aria-hidden="true"></i>';
		html += '<i class="fa fa-user-circle" aria-hidden="true"></i>';
		html += '</div>';

		html += '<div class="from">' + _self.resources[index]['from'] + '&lt;' + _self.resources[index]['from_addr'] + '&gt;';

		// detail
		html += '<div class="detail">';
		html += '<div class="icon">';
		html += '<i class="fa fa-user-circle-o" aria-hidden="true"></i>';
		html += '<i class="fa fa-user-circle" aria-hidden="true"></i>';
		html += '</div>';
		html += '<span class="name">' + _self.resources[index]['from'] + '<span class="star">☆</span></span></i><br/><br/>';
		html += '<br/><span style="padding-left:10px;"><i class="fa fa-envelope-o" aria-hidden="true" style="margin-right:5px;color:#1A4B8A;"></i>メールを送信</span>';
		html += '<i class="fa fa-comment-o" aria-hidden="true" style="margin-left:20px;color:#1A4B8A;"></i>';
		html += '<i class="fa fa-linkedin" aria-hidden="true" style="margin-left:20px;color:#2F77B2"></i>';
		html += '<i class="fa fa-ellipsis-h" aria-hidden="true" style="margin-left:50px;color:#839EC0;"></i>';
		html += '<br/>';
		html += '<hr style="width:100%;"/>';
		html += '<br/><span class="addr" style="font-weight:bold;">連絡先 <span style="color:#1E4F8C;">＞</span></span><br/>';
		html += '<br/><span class="addr" style="font-size:80%;color:#1E4F8C;">' + _self.resources[index]['from_addr'] + '</span><br/>';
		html += '<br/><span class="addr" style="font-size:80%;color:#1A4B8A;font-weight:bold;">その他を表示</span><br/><br/>';
		html += '</div>';
		html += '</div>';
		// detail

		html += '<div class="date">' + _self.resources[index]['recieved_date'] + ' ' + _self.resources[index]['recieved_time'] + '</div>';
		html += '<div class="to">宛先：' + _self.resources[index]['to'] + '</div>';

		html += '<div class="return1"><i class="fa fa-arrow-left" aria-hidden="true"></i></div>';

		html += '<div class="return2"><i class="fa fa-long-arrow-left" aria-hidden="true"></i></div>';
		html += '<div class="return3"><i class="fa fa-long-arrow-left" aria-hidden="true"></i></div>';

		html += '<div class="forward"><i class="fa fa-arrow-right" aria-hidden="true"></i></div>';

		html += '<div class="menu"><i class="fa fa-ellipsis-h" aria-hidden="true"></i></div>';

		// 複数ファイルに未対応、、、すまぬ
		if(_self.resources[index]['attachments'] != null && _self.resources[index]['attachments'].length > 0){
			html += '<div class="attachment">';
			html += '<img src="' + _self.resources[index]['attachments'][0]['icon'] + '" height="40">';
			html += '<div class="dl" data-index="' + index + '" data-attachment="' + 0 + '"><i class="fa fa-download" aria-hidden="true"></i></div>';
			html += '<div class="menu"><i class="fa fa-chevron-down" aria-hidden="true"></i></div>';


			html += '<div class="info">';
			html += '<p class="file-name">' + _self.resources[index]['attachments'][0]['file_name'] + '</p><p class="size">' + _self.resources[index]['attachments'][0]['file_size'] + '</p>';
			html += '</div>';
			html += '</div>';
		}

		html += '<div class="message ';
		if(_self.resources[index]['attachments'] != null && _self.resources[index]['attachments'].length > 0){
			html += ' with-attachment ';
		}
		html += '">'
		html += _self.resources[index]['body'];
		html += '</div>';

		html += '</div>';

		_self.params["window"].find('.outlook-view').html(html);




		_self.params["window"].find("div.dl").click(function(){


			var mailIndex = parseInt($(this).data('index'));
			var attachmentIndex = parseInt($(this).data('attachment'));


			_self.params["window"].find("div.outlook-body").addClass("download");
			var downloadBox = '<div class="download-box">';

			downloadBox += '<img src="' + _self.resources[mailIndex]['attachments'][attachmentIndex]['download_icon'] + '">';
			downloadBox += '<span class="file-name">' + _self.resources[mailIndex]['attachments'][attachmentIndex]['file_name'] + '</span>';
			downloadBox += '<div class="delim"></div>';
			downloadBox += '<i class="fa fa-angle-down" aria-hidden="true"></i>';
			downloadBox += '</div>';

			var $downloadBox = $(downloadBox).prependTo(_self.params["window"].find("div.outlook-footer"));
			if(_self.resources[mailIndex]['attachments'][attachmentIndex]['click_action'] != null){
				$downloadBox.click(function(){_self.resources[mailIndex]['attachments'][attachmentIndex]['click_action']()});
			}

			var download = $('<i class="fa fa-download" aria-hidden="true"></i>').prependTo(_self.params["window"].find("div.outlook-footer"));
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

	// リンクの表示
	this.showLink=function(label){
		_self.thisWindow.find('div.outlook-link-popup').text(label);
		_self.thisWindow.find('div.outlook-link-popup').fadeIn("fast");
	}
	this.hideLink=function(){
		_self.thisWindow.find('div.outlook-link-popup').text('');
		_self.thisWindow.find('div.outlook-link-popup').hide();
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

	// ダウンロードのクリア
	this.clearDownloadFile=function(){
		_self.params["window"].find("div.outlook-footer").empty();
		_self.params["window"].find("div.outlook-body").removeClass("download");
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

