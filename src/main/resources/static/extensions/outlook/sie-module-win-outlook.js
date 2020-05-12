// アウトルックモジュール
var SieModuleWinOutlook = function(moduleId){
	this.id = moduleId;
	this.title = "Outlook";
	this.titlebar = "Outlook";
	this.icon = "sie/img/icon-outlook.png"
	this.isShowDeskTop=true;
	this.isWindowOpen=true;
	this.moduleClass="sie-module-win-outlook";
	this.delayedStart = 1000;

	this.view = '<div class="sie-module-win-outlook-body">';
	this.view += '<div class="outlook-menu-bar">'
	this.view += '<div class="outlook-menu">ファイル</div>';
	this.view += '<div class="outlook-menu"><div class="current"></div>ホーム</div>';
	this.view += '<div class="outlook-menu">送受信</div>';
	this.view += '<div class="outlook-menu">フォルダー</div>';
	this.view += '<div class="outlook-menu">表示</div>';
	this.view += '<div class="outlook-menu">ヘルプ</div>';
	this.view += '<div class="outlook-menu outlook-menu-search"><i class="fa fa-lightbulb-o" aria-hidden="true"></i>実行したい作業を入力してください</div>';

//---------------------

	this.view += '<div class="block1" style="left:8px;">';
	this.view += '<i class="fa fa-envelope" aria-hidden="true" style="top:6px;left:8px;font-size:25px;color:#fff;"></i>';
	this.view += '<i class="fa fa-envelope-o" aria-hidden="true" style="top:6px;left:8px;font-size:25px;color:#5B5B59;"></i>';
	this.view += '<div style="left:4px;top:35px;line-height:15px;font-size:10px;text-align:center;">新しい<br/>メール</div>';
	this.view += '</div>';

	this.view += '<div class="block1" style="left:55px;">';
	this.view += '<i class="fa fa-square" aria-hidden="true" style="top:7px;left:7px;font-size:21px;color:#fff;"></i>';
	this.view += '<i class="fa fa-calendar-o" aria-hidden="true" style="top:5px;left:7px;font-size:22px;color:#5B5B59;"></i>';
	this.view += '<i class="fa fa-envelope" aria-hidden="true" style="top:13px;left:17px;font-size:20px;color:#fff;"></i>';
	this.view += '<i class="fa fa-envelope-o" aria-hidden="true" style="top:13px;left:17px;font-size:20px;color:#5B5B59;"></i>';
	this.view += '<div style="left:4px;top:35px;line-height:15px;font-size:10px;text-align:center;">新しい</div>';
	this.view += '<div style="left:0px;top:50px;line-height:15px;font-size:10px;text-align:left;">アイテム</div>';
	this.view += '<i class="fa fa-sort-desc" aria-hidden="true" style="top:59px;left:16px;font-size:5px;color:#6D6D6D;"></i>';
	this.view += '</div>';

	this.view += '<div style="left:35px;top:113px;line-height:15px;font-size:10px;text-align:left;">新規作成</div>';
	this.view += '<div style="top:34px;left:109px;width:0px;height:90px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------

	this.view += '<div class="block2" style="left:121px;">';
	this.view += '<i class="fa fa-envelope" aria-hidden="true" style="top:3px;left:3px;font-size:11px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-envelope-o" aria-hidden="true" style="top:3px;left:3px;font-size:11px;"></i>';
	this.view += '<i class="fa fa-ban" aria-hidden="true" style="top:7px;left:6px;font-size:11px;color:#EF504E;"></i>';
	this.view += '</div>';

	this.view += '<div class="block2" style="left:121px;top:63px;">';
	this.view += '<i class="fa fa-envelope" aria-hidden="true" style="top:3px;left:3px;font-size:11px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-envelope-o" aria-hidden="true" style="top:3px;left:3px;font-size:11px;"></i>';
	this.view += '<i class="fa fa-times" aria-hidden="true" style="top:7px;left:6px;font-size:11px;color:#EF504E;"></i>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:5px;left:17px;font-size:6px;"></i>';
	this.view += '</div>';

	this.view += '<div class="block2" style="left:121px;top:88px;">';
	this.view += '<i class="fa fa-user" aria-hidden="true" style="top:3px;left:3px;font-size:11px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-user-o" aria-hidden="true" style="top:3px;left:3px;font-size:11px;"></i>';
	this.view += '<i class="fa fa-ban" aria-hidden="true" style="top:7px;left:6px;font-size:11px;color:#EF504E;"></i>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:5px;left:17px;font-size:6px;"></i>';
	this.view += '</div>';

	this.view += '<div class="block1" style="left:155px;">';
	this.view += '<i class="fa fa-trash" aria-hidden="true" style="top:5px;left:8px;font-size:30px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-trash-o" aria-hidden="true" style="top:5px;left:8px;font-size:30px;color:#5B5B59;"></i>';
	this.view += '<div style="left:11px;top:35px;line-height:15px;font-size:10px;text-align:center;">削除</div>';
	this.view += '</div>';

	this.view += '<div class="block1" style="left:202px;">';
	this.view += '<i class="fa fa-archive" aria-hidden="true" style="top:8px;left:6px;font-size:29px;color:#5B5B59;"></i>';
	this.view += '<i class="fa fa-archive" aria-hidden="true" style="top:10px;left:8px;font-size:25px;color:#A1DDAA;"></i>';
	this.view += '<div style="left:11px;top:38px;line-height:15px;font-size:10px;text-align:center;">アー</div>';
	this.view += '<div style="left:8px;top:51px;line-height:15px;font-size:10px;text-align:center;">カイブ</div>';
	this.view += '</div>';

	this.view += '<div style="left:180px;top:113px;line-height:15px;font-size:10px;text-align:left;">削除</div>';
	this.view += '<div style="top:34px;left:256px;width:0px;height:90px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------

	this.view += '<div class="block1" style="left:269px;">';
	this.view += '<i class="fa fa-envelope" aria-hidden="true" style="top:6px;left:8px;font-size:25px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-envelope-o" aria-hidden="true" style="top:6px;left:8px;font-size:25px;color:#5B5B59;"></i>';
	this.view += '<i class="fa fa-reply" aria-hidden="true" style="top:16px;left:5px;font-size:20px;color:#A846B2;"></i>';
	this.view += '<div style="left:11px;top:35px;line-height:15px;font-size:10px;text-align:center;">返信</div>';
	this.view += '</div>';

	this.view += '<div class="block1" style="left:316px;">';
	this.view += '<i class="fa fa-envelope" aria-hidden="true" style="top:6px;left:8px;font-size:25px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-envelope-o" aria-hidden="true" style="top:6px;left:8px;font-size:25px;color:#5B5B59;"></i>';
	this.view += '<i class="fa fa-reply" aria-hidden="true" style="top:16px;left:6px;font-size:20px;color:#A846B2;"></i>';
	this.view += '<i class="fa fa-caret-left" aria-hidden="true" style="top:13px;left:0px;font-size:20px;color:#A846B2;"></i>';
	this.view += '<div style="left:8px;top:38px;line-height:15px;font-size:10px;text-align:center;">全員に</div>';
	this.view += '<div style="left:11px;top:51px;line-height:15px;font-size:10px;text-align:center;">返信</div>';
	this.view += '</div>';

	this.view += '<div class="block1" style="left:363px;">';
	this.view += '<i class="fa fa-envelope" aria-hidden="true" style="top:6px;left:8px;font-size:25px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-envelope-o" aria-hidden="true" style="top:6px;left:8px;font-size:25px;color:#5B5B59;"></i>';
	this.view += '<i class="fa fa-arrow-right" aria-hidden="true" style="top:16px;left:18px;font-size:20px;color:#1E8BCD;"></i>';
	this.view += '<div style="left:11px;top:35px;line-height:15px;font-size:10px;text-align:center;">転送</div>';
	this.view += '</div>';

	this.view += '<div class="block2" style="top:40px;left:410px;height:28px;">';
	this.view += '<i class="fa fa-square" aria-hidden="true" style="top:8px;left:2px;font-size:16px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-calendar-o" aria-hidden="true" style="top:5px;left:2px;font-size:17px;color:#5B5B59;"></i>';
	this.view += '<i class="fa fa-reply" aria-hidden="true" style="top:12px;left:11px;font-size:10px;color:#A846B2;"></i>';
	this.view += '</div>';

	this.view += '<div class="block2" style="top:75px;left:410px;height:28px;">';
	this.view += '<i class="fa fa-square" aria-hidden="true" style="top:8px;left:4px;font-size:11px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-calendar-o" aria-hidden="true" style="top:5px;left:3px;font-size:13px;color:#5B5B59;"></i>';

	this.view += '<i class="fa fa-square" aria-hidden="true" style="top:14px;left:10px;font-size:10px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-tablet" aria-hidden="true" style="top:12px;left:10px;font-size:13px;color:#5B5B59;"></i>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:8px;left:20px;font-size:6px;"></i>';
	this.view += '</div>';

	this.view += '<div style="left:340px;top:113px;line-height:15px;font-size:10px;text-align:left;">返信</div>';
	this.view += '<div style="top:34px;left:450px;width:0px;height:90px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------

	this.view += '<div class="block1 quick" style="left:463px;height:62px;width:137px;">';
	this.view += '</div>';

	this.view += '<div style="left:500px;top:113px;line-height:15px;font-size:10px;text-align:left;">クイック操作</div>';
	this.view += '<div style="top:34px;left:612px;width:0px;height:90px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------

	this.view += '<div class="block2" style="left:625px;width:80px;">';
	this.view += '<i class="fa fa-folder" aria-hidden="true" style="top:2px;left:2px;font-size:15px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-folder-o" aria-hidden="true" style="top:2px;left:2px;font-size:15px;color:#5B5B59;"></i>';
	this.view += '<div style="left:6px;top:0px;line-height:15px;font-size:10px;font-weight:800;color:#3094D0;">↓</div>';
	this.view += '<div style="left:7px;top:0px;line-height:15px;font-size:10px;font-weight:800;color:#3094D0;">↓</div>';
	this.view += '<div style="left:20px;top:2px;line-height:15px;font-size:10px;text-align:center;">移動</div>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:4px;left:43px;font-size:6px;"></i>';
	this.view += '</div>';

	this.view += '<div class="block2" style="left:625px;top:63px;width:80px;">';
	this.view += '<i class="fa fa-folder" aria-hidden="true" style="top:2px;left:2px;font-size:12px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-folder-o" aria-hidden="true" style="top:2px;left:2px;font-size:12px;color:#5B5B59;"></i>';
	this.view += '<i class="fa fa-envelope" aria-hidden="true" style="top:6px;left:6px;font-size:12px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-envelope-o" aria-hidden="true" style="top:6px;left:6px;font-size:12px;color:#5B5B59;"></i>';
	this.view += '<div style="left:20px;top:2px;line-height:15px;font-size:10px;text-align:center;">ルール</div>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:4px;left:50px;font-size:6px;"></i>';
	this.view += '</div>';

	this.view += '<div class="block2" style="left:625px;top:88px;width:80px;">';
	this.view += '<i class="fa fa-file" aria-hidden="true" style="top:2px;left:4px;font-size:14px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-file-archive-o" aria-hidden="true" style="top:2px;left:4px;font-size:14px;color:#7719AA;"></i>';
	this.view += '<div style="left:20px;top:2px;line-height:15px;font-size:10px;text-align:center;">OneNote</div>';
	this.view += '</div>';

	this.view += '<div style="left:655px;top:113px;line-height:15px;font-size:10px;text-align:left;">移動</div>';
	this.view += '<div style="top:34px;left:717px;width:0px;height:90px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------

	this.view += '<div class="block2" style="left:730px;top:48px;width:102px;">';
	this.view += '<i class="fa fa-envelope-open" aria-hidden="true" style="top:3px;left:3px;font-size:12px;color:#FAFAFA;"></i>'
	this.view += '<i class="fa fa-envelope-open-o" aria-hidden="true" style="top:3px;left:3px;font-size:12px;color:#5B5B59;"></i>'
	this.view += '<div style="left:20px;top:2px;line-height:15px;font-size:10px;text-align:center;">未開封/開封済み</div>';
	this.view += '</div>';

	this.view += '<div class="block2" style="left:730px;top:78px;width:102px;">';
	this.view += '<i class="fa fa-flag" aria-hidden="true" style="top:3px;left:3px;font-size:12px;color:#FF9198;"></i>'
	this.view += '<i class="fa fa-flag-o" aria-hidden="true" style="top:3px;left:3px;font-size:12px;color:#5B5B59;"></i>'
	this.view += '<div style="left:20px;top:2px;line-height:15px;font-size:10px;text-align:center;">フラグの設定</div>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:4px;left:80px;font-size:6px;"></i>';
	this.view += '</div>';

	this.view += '<div style="left:770px;top:113px;line-height:15px;font-size:10px;text-align:left;">タグ</div>';
	this.view += '<div style="top:34px;left:844px;width:0px;height:90px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------

	this.view += '<div class="block2" style="left:857px;width:166px;border:solid 1px #C5C5C5;background-color:#FFFFFF;color:#6A6A6A;font-size:10px:padding-left:5px;line-height:18px;">';
	this.view += '&nbsp;ユーザの検索';
	this.view += '</div>';

	this.view += '<div class="block2" style="left:857px;top:63px;width:166px;">';
	this.view += '<i class="fa fa-square" aria-hidden="true" style="top:3px;left:4px;font-size:9px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-square" aria-hidden="true" style="top:5px;left:4px;font-size:9px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-id-badge" aria-hidden="true" style="top:3px;left:4px;font-size:12px;color:#5B5B59;"></i>'
	this.view += '<div style="left:20px;top:2px;line-height:15px;font-size:10px;text-align:center;">アドレス帳</div>';
	this.view += '</div>';

	this.view += '<div class="block2" style="left:857px;top:88px;width:166px;">';
	this.view += '<i class="fa fa-filter" aria-hidden="true" style="top:3px;left:3px;font-size:14px;color:#5B5B59;"></i>';
	this.view += '<i class="fa fa-filter" aria-hidden="true" style="top:3px;left:4px;font-size:14px;color:#5B5B59;"></i>';
	this.view += '<i class="fa fa-filter" aria-hidden="true" style="top:4px;left:5px;font-size:8px;color:#FAFAFA;"></i>';
	this.view += '<div style="left:20px;top:2px;line-height:15px;font-size:10px;text-align:center;">電子メールのフィルタ処理</div>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:4px;left:135px;font-size:6px;"></i>';
	this.view += '</div>';

	this.view += '<div style="left:930px;top:113px;line-height:15px;font-size:10px;text-align:left;">検索</div>';
	this.view += '<div style="top:34px;left:1035px;width:0px;height:90px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------

	this.view += '<div class="block1" style="left:1048px;">';
	this.view += '<i class="fa fa-wifi" aria-hidden="true" style="top:5px;left:16px;font-size:16px;color:#1E8BCD;transform: rotate(90deg);"></i>';
	this.view += '<i class="fa fa-font" aria-hidden="true" style="top:8px;left:9px;font-size:20px;color:#3A3A38;"></i>';
	this.view += '<div style="left:8px;top:38px;line-height:15px;font-size:10px;text-align:center;">音声読</div>';
	this.view += '<div style="left:8px;top:51px;line-height:15px;font-size:10px;text-align:center;">み上げ</div>';
	this.view += '</div>';

	this.view += '<div style="left:1043px;top:113px;line-height:15px;font-size:10px;text-align:left;">音声読み上げ</div>';
	this.view += '<div style="top:34px;left:1102px;width:0px;height:90px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------

	this.view += '<div class="block1" style="left:1115px;width:86px;">';

	this.view += '<i class="fa fa-refresh" aria-hidden="true" style="top:9px;left:36px;font-size:20px;color:#18AB50;"></i>';
	this.view += '<div style="left:1px;top:38px;line-height:15px;font-size:10px;text-align:center;">すべてのフォルダー</div>';
	this.view += '<div style="left:23px;top:51px;line-height:15px;font-size:10px;text-align:center;">を送受信</div>';

	this.view += '</div>';

	this.view += '<div style="left:1140px;top:113px;line-height:15px;font-size:10px;text-align:left;">送受信</div>';
	this.view += '<div style="top:34px;left:1215px;width:0px;height:90px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------

	this.view += '<i class="fa fa-angle-up" aria-hidden="true" style="top:110px;left:1230px;"></i>';

	this.view += '</div>';	// .outlook-menu-bar

	this.view += '<div class="outlook-body">';

	/* ドラッグ可能領域 */
	this.view += '<div id="outlookDragArea2" class="outlook-drag-area2"></div>';

	this.view += '<div class="outlook-folder">';
	this.view += '<div class="outlook-folder-title1"><i class="fa fa-caret-down" aria-hidden="true"></i>お気に入り</div>';

	this.view += '<ul class="outlook-folder-list1">';
	this.view += '<li class="selected">受信トレイ</li>';
	this.view += '<li>下書き</li>';
	this.view += '<li>送信済みアイテム</li>';
	this.view += '<li>削除済みアイテム</li>';
	this.view += '</ul>';

	this.view += '<div class="delim"></div>';

	this.view += '<div class="outlook-folder-title2"><i class="fa fa-caret-down" aria-hidden="true"></i>user.name@maildomain.com</div>';

	this.view += '<ul class="outlook-folder-list2">';
	this.view += '<li>受信トレイ</li>';
	this.view += '<li>削除済みアイテム</li>';
	this.view += '<li>Drafts</li>';
	this.view += '<li>下書き</li>';
	this.view += '<li>送信トレイ</li>';
	this.view += '<li>迷惑メール</li>';
	this.view += '<li>検索フォルダ</li>';
	this.view += '</ul>';

	this.view += '</div>';	// .outlook-folder

	this.view += '<div class="outlook-list">';
	this.view += '<input type="text" placeholder="現在のメールボックスの検索">';
	this.view += '<select><option>現在のメールボックス</option></select>';


	this.view += '<div class="all">すべて</div>';
	this.view += '<div class="notread">未読</div>';
	this.view += '<div class="date">日付&nbsp;<i class="fa fa-chevron-down" aria-hidden="true"></i></div>';
	this.view += '<div class="arrow"><i class="fa fa-long-arrow-up" aria-hidden="true"></i></div>';

	this.view += '<div class="list">';
	this.view += '<ul>';

	for(var i=0; i<10; i++){
		this.view += '<li class="';
		if(i == 0){
			this.view += ' selected ';
		}
		if(i != 0 && i % 3 == 0){
			this.view += ' not-read ';
		}
		this.view += '">';

		this.view += '<div class="not-read-bar"></div>';

		this.view += '<div class="from">Apple &lt;apple_support@0942.com&gt;</div>';
		this.view += '<div class="title">できるだけ早くアカウント情報をご更新ください。</div>';
		this.view += '<div class="body">Apple IDユーザーの皆様、これは非常に重要なメールです。アカウントのログインIPの異常により、一部の情報が失われるため、アカウント情報の更新が完了するまで、一部のアカウント権限をロックさせていただきます。この期間中は、アカウントからアプリを使用してダウンロードすることはできません。手順に従ってアカウント情報をご更新ください。ご理解いただきありがとうございます。ご不便をおかけして申し訳ありません。</div>';

		this.view += '<div class="attachment"><i class="fa fa-paperclip" aria-hidden="true"></i></div>';
		this.view += '<div class="time">（水）15:41</div>';
		this.view += '</li>';
	}

	this.view += '</ul>';
	this.view += '</div>';

	this.view += '</div>';	// .outlook-list

	// メール本文表示領域
	this.view += '<div class="outlook-view"></div>';

	// 区切り１
	this.view += '<div class="outlook-handle1"></div>';
	// 区切り２
	this.view += '<div class="outlook-handle2"></div>';

	this.view += '<div class="outlook-footer">';
	this.view += 'アイテム数: 250';

	this.view += '<div class="button selected" style="margin-right:250px;"><i class="fa fa-columns" aria-hidden="true"></div></i>';
	this.view += '<div class="button" style="margin-right:200px;"><i class="fa fa-book" aria-hidden="true"></div></i>';
	this.view += '<div class="right"></div>';
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
		_self.thisWindow.find('div.titlebar').append('<div class="center-title">受信トレイ - ' + _self.titlebar + ' - Outlook</div>')

		// メッセージの表示
		_self.showMessage();

		// スプラッシュロゴ
		var splashLogo = '<div class="sie-module-win-outlook-splash-logo">';
		splashLogo += '<div class="application-logo"><i class="fa fa-windows" aria-hidden="true"></i></div>';
		splashLogo += '<div class="company">Microsoft</div>';
		splashLogo += '<div class="application-name">Outlook</div>';
		splashLogo += '<div class="message">起動しています．．．</div>';
		splashLogo += '</div>';
		$(splashLogo).appendTo(params['home']).fadeIn("fast" , function(){var __self=this;setTimeout(function(){$(__self).fadeOut("fast", function(){$(this).remove()})}, 1200)});


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
		_self.setObject(params);
	})

	// オブジェクトの位置をセット
	this.setObject=function(params){
		// リサイズ処理など
	}

	this.showMessage=function(){

		var _self = this;
		var html = '';

		html += '<div class="message-head reply">';
		html += '<i class="fa fa-envelope-open" aria-hidden="true" style="top:0px;left:3px;font-size:14px;color:#FAFAFA;"></i>';
		html += '<i class="fa fa-envelope-open-o" aria-hidden="true" style="top:0px;left:3px;font-size:14px;color:#5B5B59;"></i>';
		html += '<i class="fa fa-arrow-left" aria-hidden="true" style="top:5px;left:8px;font-size:12px;color:#A846B2;"></i>';
		html += '<div style="top:0px;left:20px;font-size:12px;color:#666666;">返信</div>';
		html += '</div>';

		html += '<div class="message-head replyall">';
		html += '<i class="fa fa-envelope-open" aria-hidden="true" style="top:0px;left:3px;font-size:14px;color:#FAFAFA;"></i>';
		html += '<i class="fa fa-envelope-open-o" aria-hidden="true" style="top:0px;left:3px;font-size:14px;color:#5B5B59;"></i>';

		html += '<i class="fa fa-envelope-open" aria-hidden="true" style="top:2px;left:5px;font-size:14px;color:#FAFAFA;"></i>';
		html += '<i class="fa fa-envelope-open-o" aria-hidden="true" style="top:2px;left:5px;font-size:14px;color:#5B5B59;"></i>';

		html += '<i class="fa fa-arrow-left" aria-hidden="true" style="top:7px;left:10px;font-size:12px;color:#A846B2;"></i>';
		html += '<div style="top:0px;left:22px;font-size:12px;color:#666666;">全員に返信</div>';
		html += '</div>';

		html += '<div class="message-head transfer">';
		html += '<i class="fa fa-envelope-open" aria-hidden="true" style="top:0px;left:3px;font-size:14px;color:#FAFAFA;"></i>';
		html += '<i class="fa fa-envelope-open-o" aria-hidden="true" style="top:0px;left:3px;font-size:14px;color:#5B5B59;"></i>';
		html += '<i class="fa fa-arrow-right" aria-hidden="true" style="top:5px;left:8px;font-size:12px;color:#4A7DB1;"></i>';
		html += '<div style="top:0px;left:20px;font-size:12px;color:#666666;">返信</div>';
		html += '</div>';

		html += '<div class="message-head user">';
		html += '<i class="fa fa-circle" aria-hidden="true" style="font-size:44px;left:1px;color:#696969;"></i>';
		html += '<i class="fa fa-user-circle" aria-hidden="true" style="font-size:42px;color:#D2D2D2;"></i>';
		html +=  '</div>';

		html += '<div class="message-head from">redmine@backyard1.tv.biz-dev.bbtower.net　｜　fujisawa@office-nico.com</div>';
		html += '<div class="message-head title">[事務処理 #703] システムのの設定変更について</div>';

		html += '<div class="delim"></div>';

		html += '<div class="message-attachment">';


		html += '<div class="attachment" ondblclick="openWord();">';
		html += '<img src="sie/img/icon-word.png" height="46">';
		html += '<div class="info">';
		html += '<span class="file-name">文書1.docx</span><br/><span class="size">12kb</span>';
		html += '</div>';
		html += '</div>';

/*
		html += '<div class="attachment">';
		html += '</div>';
		html += '<div class="attachment">';
		html += '</div>';
		html += '<div class="attachment">';
*/

		html += '</div>';

		html += '<div class="message-body with-attachment">';

		html += '<br/><br/>Apple IDユーザーの皆様、これは非常に重要なメールです。アカウントのログインIPの異常により、一部の情報が失われるため、アカウント情報の更新が完了するまで、一部のアカウント権限をロックさせていただきます。この期間中は、アカウントからアプリを使用してダウンロードすることはできません。手順に従ってアカウント情報をご更新ください。ご理解いただきありがとうございます。ご不便をおかけして申し訳ありません。<br/><br/>';
		html += 'アカウント更新<br/><br/>';
		html += '3営業日以内に情報が更新されない場合、アカウントは完全に廃止されます。<br/><br/>';
		html += 'ご迷惑をかけて、大変申し訳ございません。<br/><br/>';
		html += 'Apple サポートセンター<br/><br/>';

		html += '</div>';

		_self.params["window"].find('.outlook-view').html(html);
	}
}

