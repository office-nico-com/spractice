// イベントビューワモジュール
var SieModuleWinEventviewer = function(moduleId){
	this.id = moduleId;
	this.title = "イベントビューアー";
	this.titlebar = "イベントビューアー";
	this.icon = "sie/img/icon-eventviewer.png"
	this.isShowDeskTop=false;
	this.isWindowOpen=true;
	this.moduleClass="sie-module-win-eventviewer";
	this.delayedStart = 0;

	this.view = '<div class="sie-module-win-eventviewer-body">';
	this.view += '<div class="menu-bar">'
	this.view += '<div class="menu menu-1">ﾌｧｲﾙ(F)</div>';
	this.view += '<div class="menu menu-2">操作(A)</div>';
	this.view += '<div class="menu menu-3">表示(V)</div>';
	this.view += '<div class="menu menu-4">ﾍﾙﾌﾟ(H)</div>';
	this.view += '<div class="menu menu-icon-block">';
	this.view += '<i class="fa fa-arrow-left" aria-hidden="true" style="color:#17A3EE;"></i>';
	this.view += '<i class="fa fa-arrow-right" aria-hidden="true" style="color:#888888;"></i>';
	this.view += '<i style="color:#8C8C8C;">|</i>';
	this.view += '<i class="fa fa-folder" aria-hidden="true" style="color:#F8D786;"></i>';
	this.view += '<i class="fa fa-caret-square-o-left" aria-hidden="true" style="color:#9AA1AA;"></i>';
	this.view += '<i style="color:#8C8C8C;">|</i>';
	this.view += '<i class="fa fa-question-circle" aria-hidden="true" style="color:#7591EE;"></i>';
	this.view += '<i class="fa fa-caret-square-o-right" aria-hidden="true" style="color:#9AA1AA;"></i>';
	this.view += '</div>';
	this.view += '</div>';

	this.view += '<div class="body">'

	// 左バーと移動幅を制限するためのオブジェクト
	this.view += '<div class="left-bar-containment"></div>';
	this.view += '<div class="left-bar"></div>';

	// 右バーと移動幅を制限するためのオブジェクト
	this.view += '<div class="right-bar-containment"></div>';
	this.view += '<div class="right-bar"></div>'

	// 左ウィンドウ
	this.view += '<div class="left-window">'

	this.view += '</div>';



	// 中央ウィンドウ
	this.view += '<div class="center-window">'

	// 中央バーと移動幅を制限するためのオブジェクト
	this.view += '  <div class="center-bar-containment"></div>';
	this.view += '  <div class="center-bar"></div>'


	// 中央タイトル
	this.view += '  <div class="title">';
	this.view += '  管理イベント';
	this.view += '  </div>';

	// 中央上
	this.view += '  <div class="top">';
	this.view += '  </div>';


	// 中央下
	this.view += '  <div class="bottom">';
	this.view += '  </div>';

	this.view += '</div>';


	// 右ウィンドウ
	this.view += '<div class="right-window">'
	this.view += '<div class="title">操作</div>';


	this.view += '<div class="head top">管理イベント</div>';
	this.view += '<div class="item"><div class="menu-icon menu-icon1"></div>保存されたログを開く...</div>';
	this.view += '<div class="item"><div class="menu-icon menu-icon2"></div>カスタムビューの作成...</div>';
	this.view += '<div class="item delim">カスタムビューインポート...</div>';
	this.view += '<div class="item"><div class="menu-icon menu-icon3"></div>現在のカスタムビューをフィルタ...</div>';
	this.view += '<div class="item"><div class="menu-icon menu-icon4"></div>プロパティ</div>';
	this.view += '<div class="item"><div class="menu-icon menu-icon5"></div>検索...</div>';
	this.view += '<div class="item"><div class="menu-icon menu-icon6"></div>カスタムビューのすべてのイベントを名前をつけて保存...</div>';
	this.view += '<div class="item">カスタムビューのエクスポート</div>';
	this.view += '<div class="item">カスタムビューのエクスポート</div>';
	this.view += '<div class="item">カスタムビューのコピー...</div>';
	this.view += '<div class="item delim">このカスタムビューにタスクを設定...</div>';
	this.view += '<div class="item delim">表示</div>';
	this.view += '<div class="item delim"><div class="menu-icon menu-icon7"></div>最新の情報に更新</div>';
	this.view += '<div class="item"><div class="menu-icon menu-icon9"></div>ヘルプ</div>';
	this.view += '<div class="head">イベント</div>';
	this.view += '<div class="item"><div class="menu-icon menu-icon8"></div>イベントのプロパティ</div>';
	this.view += '<div class="item"><div class="menu-icon menu-icon10"></div>このイベントにタスクを設定...</div>';
	this.view += '<div class="item delim"><div class="menu-icon menu-icon11"></div>コピー</div>';
	this.view += '<div class="item"><div class="menu-icon menu-icon12"></div>選択したイベントの保存...</div>';
	this.view += '<div class="item delim"><div class="menu-icon menu-icon7"></div>最新の情報に更新</div>';
	this.view += '<div class="item"><div class="menu-icon menu-icon9"></div>ヘルプ</div>';


	this.view += '</div>';




	this.view += '</div>';

	this.view += '</div>';

	this.width=1000;
	this.height=500;
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
		var $body = params["window"].find(".body");


		var html = '<div class="local"><div class="menu_icon menu_icon1"></div><span>イベントビューアー（ローカル）<span></div>';
		html += '<ul>';

		html += '<li>';
		html += '<i class="fa fa-chevron-right route_menu" aria-hidden="true"></i><div class="menu"><div class="route_menu_text"><div class="menu_icon menu_icon2"></div><span>カスタムビュー</span></div>';
		html += '<div class="sub_menu">';
		html += '<ul><li class="selectable"><div class="menu_icon menu_icon6"></div><span>管理イベント</span></li></ul>';
		html += '</div>';
		html += '</div>';
		html += '</li>';


		html += '<li>';
		html += '<i class="fa fa-chevron-right route_menu" aria-hidden="true"></i><div class="menu"><div class="route_menu_text"><div class="menu_icon menu_icon3"></div><span>Windowsログ</span></div>';
		html += '<div class="sub_menu">';
		html += '<ul>';
		html += '<li><div class="menu_icon menu_icon7"></div><span>Application</span></li>';
		html += '<li><div class="menu_icon menu_icon7"></div><span>セキュリティ</span></li>';
		html += '<li><div class="menu_icon menu_icon8"></div><span>Setup</span></li>';
		html += '<li><div class="menu_icon menu_icon7"></div><span>システム</span></li>';
		html += '<li><div class="menu_icon menu_icon8"></div><span>Forwarded Events</span></li>';
		html += '</ul>';
		html += '</div>';
		html += '</div>';
		html += '</li>';


		html += '<li>';
		html += '<i class="fa fa-chevron-right route_menu" aria-hidden="true"></i><div class="menu"><div class="route_menu_text"><div class="menu_icon menu_icon4"></div><span>アプリケーションとサービスログ</span></div>';
		html += '<div class="sub_menu">';
		html += '<ul>';
		html += '<li><div class="menu_icon menu_icon7"></div><span>Intel</span></li>';
		html += '<li><div class="menu_icon menu_icon7"></div><span>IntelAudio ServiceLg</span></li>';
		html += '<li><div class="menu_icon menu_icon7"></div><span>InternetExplorer</span></li>';
		html += '<li><div class="menu_icon menu_icon7"></div><span>Microsoft</span></li>';
		html += '<li><div class="menu_icon menu_icon7"></div><span>OpenSSH</span></li>';
		html += '<li><div class="menu_icon menu_icon7"></div><span>Windows Azure</span></li>';
		html += '<li><div class="menu_icon menu_icon7"></div><span>Windows PowerShell</span></li>';
		html += '<li><div class="menu_icon menu_icon7"></div><span>キー管理サービス</span></li>';
		html += '<li><div class="menu_icon menu_icon7"></div><span>ハードウェアイベント</span></li>';
		html += '</ul>';
		html += '</div>';
		html += '</div>';
		html += '</li>';

		html += '<li>';
		html += '<i class="fa fa-chevron-right" aria-hidden="true" style="color:#fff;"></i><div class="menu_icon menu_icon5"></div><span>サブスクリプション</span>';
		html += '</li>';
		html += '</ul>';
		var $leftWin = params["window"].find("div.left-window");
		$leftWin.html(html);


		$leftWin.find("i.route_menu").click(function(e){
			if($(this).hasClass("selected")){
				$(this).removeClass("selected");
				$(this).parent().find(".sub_menu").hide();
			}
			else{
				$(this).addClass("selected");
				$(this).parent().find(".sub_menu").show();
			}
		});
		$leftWin.find("i.route_menu").click();

		$leftWin.find(".selectable").click(function(){
			if($(this).hasClass("selected")){
				$(this).removeClass("selected");
			}
			else{
				$(this).addClass("selected");
				_self.showEventLog($body);
			}
		});
		$leftWin.find(".selectable").click();


		var startLeft1 = null;
		_self.thisWindow.find('div.left-bar').draggable({
			axis: "x",
			containment: '.left-bar-containment',		// 見えない領域を作るべきか
			start:function(e, ui){
				_self.thisWindow.find('div.left-window,div.center-window,div.right-window').css({'opacity':0});
				_self.thisWindow.find('div.left-bar').addClass('dragging');
				startLeft1 = ui.offset['left'];
			},
			stop:function(e, ui){
				_self.thisWindow.find('div.left-bar').removeClass('dragging');
				var stopLeft = ui.offset['left'];
				var distance = stopLeft - startLeft1;
				var item2left = to_num(_self.thisWindow.find('div.center-window').css('left')) + distance;

				var item1width = to_num(_self.thisWindow.find('div.left-window').css('width')) + distance;
				var item2width = to_num(_self.thisWindow.find('div.center-window').css('width')) - distance;

				_self.thisWindow.find('div.left-window').css({'width':to_px(item1width)});
				_self.thisWindow.find('div.center-window').css({'left':to_px(item2left), 'width':to_px(item2width)});

				_self.thisWindow.find('div.left-window,div.center-window,div.right-window').css({'opacity':1});
			}
		});

		var startLeft2 = null;
		_self.thisWindow.find('div.right-bar').draggable({
			axis: "x",
			containment: '.right-bar-containment',		// 見えない領域を作るべきか
			start:function(e, ui){
				_self.thisWindow.find('div.left-window,div.center-window,div.right-window').css({'opacity':0});
				_self.thisWindow.find('div.right-bar').addClass('dragging');
				startLeft2 = ui.offset['left'];
			},
			stop:function(e, ui){
				_self.thisWindow.find('div.right-bar').removeClass('dragging');
				var stopLeft = ui.offset['left'];
				var distance = stopLeft - startLeft2;
				var item2left = to_num(_self.thisWindow.find('div.right-window').css('left')) + distance;

				var item1width = to_num(_self.thisWindow.find('div.center-window').css('width')) + distance;
				var item2width = to_num(_self.thisWindow.find('div.right-window').css('width')) - distance;

				_self.thisWindow.find('div.center-window').css({'width':to_px(item1width)});
				_self.thisWindow.find('div.right-window').css({'left':to_px(item2left), 'width':to_px(item2width)});

				_self.thisWindow.find('div.left-window,div.center-window,div.right-window').css({'opacity':1});
			}
		});

		var startTop1 = null;
		_self.thisWindow.find('div.center-bar').draggable({
			axis: "y",
			containment: '.center-bar-containment',		// 見えない領域を作るべきか
			start:function(e, ui){
				_self.thisWindow.find('div.top,div.bottom').css({'opacity':0});
				_self.thisWindow.find('div.center-bar').addClass('dragging');
				startTop1 = ui.offset['top'];
			},
			stop:function(e, ui){
				_self.thisWindow.find('div.center-bar').removeClass('dragging');
				var stopTop = ui.offset['top'];
				var distance = stopTop - startTop1;
				var item2top = to_num(_self.thisWindow.find('div.center-window div.bottom').css('top')) + distance;

				var item1height = to_num(_self.thisWindow.find('div.center-window div.top').css('height')) + distance;
				var item2height = to_num(_self.thisWindow.find('div.center-window div.bottom').css('height')) - distance;

				_self.thisWindow.find('div.center-window div.top').css({'height':to_px(item1height)});
				_self.thisWindow.find('div.center-window div.bottom').css({'top':to_px(item2top), 'height':to_px(item2height)});

				_self.thisWindow.find('div.top,div.bottom').css({'opacity':1});
			}
		});
	})

	this.showEventLog=function($body){
		$body.find("div.center-window div.top").append('<div class="event_log_title"><i class="fa fa-filter" aria-hidden="true"></i>イベント数:9648</div>');
		var html = '';
		html += '<div class="log_list">';
		html += '<table>';
		html += '<tr>';
		html += '<th>レベル</th>';
		html += '<th>日付と時刻</th>';
		html += '<th>ソース</th>';
		html += '<th class="text-right">イベントID</th>';
		html += '<th>タスクのカテゴリ</th>';
		html += '<th class="hide">名前</th>';
		html += '<th class="hide">キーワード</th>';
		html += '<th class="hide">ユーザー</th>';
		html += '<th class="hide">コンピュータ</th>';
		html += '<th class="hide">オペレートコード</th>';
		html += '<th class="hide">全般</th>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_serv"></div><span>重大<span></td>';
		html += '<td>2021/07/27 10:10:03</td>';
		html += '<td>Karnel-Powwer</td>';
		html += '<td class="text-right">41</td>';
		html += '<td>(63)</td>';
		html += '<td class="hide">システム</th>';
		html += '<td class="hide">(70368744177664),(2)</td>';
		html += '<td class="hide">SYSTEM</td>';
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';
		html += '<td class="hide">情報</td>';
		html += '<td class="hide">システムは正常にシャットダウンする前に再起動しました。このエラーは、システムの応答の停止、クラッシュ、または予期しない電源の遮断により発生する可能性があります。</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_error"></div><span>エラー<span></td>';
		html += '<td>2021/07/27 10:07:03</td>';
		html += '<td>Application Error</td>';
		html += '<td class="text-right">1000</td>';
		html += '<td>(100)</td>';
		html += '<td class="hide">Application</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">N/A</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">障害が発生しているアプリケーション名: backgroundTaskHost.exe、バージョン: 10.0.19041.546、タイム スタンプ: 0x1d3a15e7<br/>障害が発生しているモジュール名: CalendarApp.Gui.Win10.dll、バージョン: 1.0.0.0、タイム スタンプ: 0x60eb226e<br/>例外コード: 0xc00000fd<br/>障害オフセット: 0x0000000000d3a673<br/>障害が発生しているプロセス ID: 0x262c<br/>障害が発生しているアプリケーションの開始時刻: 0x01d7828c24c18454<br/>障害が発生しているアプリケーション パス: C:\WINDOWS\system32\backgroundTaskHost.exe<br/>障害が発生しているモジュール パス: C:\Program Files\WindowsApps\64885BlueEdge.OneCalendar_2021.703.2.0_x64__8kea50m9krsh2\CalendarApp.Gui.Win10.dll<br/>レポート ID: 6f430399-479b-48fc-96bb-48fa1848d5d9<br/>障害が発生しているパッケージの完全な名前: 64885BlueEdge.OneCalendar_2021.703.2.0_x64__8kea50m9krsh2<br/>障害が発生しているパッケージに関連するアプリケーション ID: App</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_warn"></div><span>警告<span></td>';
		html += '<td>2021/07/27 9:57:34</td>';
		html += '<td>DustributedCOM</td>';
		html += '<td class="text-right">10016</td>';
		html += '<td>なし</td>';
		html += '<td class="hide">システム</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">DESKTOP-P0P3ERS\\usr</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">コンピューターの既定 のアクセス許可の設定では、CLSID <br/>{C2F03A33-21F5-47FA-B4BB-156362A2F239}<br/> および APPID <br/>{316CDED5-E4AE-4B15-9113-7055D84DCC97}<br/> の COM サーバー アプリケーションに対するローカルアクティブ化のアクセス許可を、アプリケーション コンテナー Microsoft.Windows.ShellExperienceHost_10.0.19041.1023_neutral_neutral_cw5n1h2txyewy SID (S-1-15-2-155514346-2573954481-755741238-1654018636-1233331829-3075935687-2861478708) で実行中のアドレス LocalHost (LRPC 使用) のユーザー DESKTOP-P0P3ERS\\fujisawa SID (S-1-5-21-1311239280-1613070088-2410843011-1001) に与えることはできません。このセキュリティ アクセス許可は、コンポーネント サービス管理ツールを使って変更できます。</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_warn"></div><span>警告<span></td>';
		html += '<td>2021/07/27 9:37:06</td>';
		html += '<td>VMnetDHCP</td>';
		html += '<td class="text-right">1</td>';
		html += '<td>なし</td>';
		html += '<td class="hide">システム</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">N/A</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">dispatch: Timeout waiting for input data</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_warn"></div><span>警告<span></td>';
		html += '<td>2021/07/27 9:27:47</td>';
		html += '<td>DustributedCOM</td>';
		html += '<td class="text-right">10016</td>';
		html += '<td>なし</td>';
		html += '<td class="hide">システム</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">DESKTOP-P0P3ERS\\usr</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">コンピューターの既定 のアクセス許可の設定では、CLSID <br/>{C2F03A33-21F5-47FA-B4BB-156362A2F239}<br/> および APPID <br/>{316CDED5-E4AE-4B15-9113-7055D84DCC97}<br/> の COM サーバー アプリケーションに対するローカルアクティブ化のアクセス許可を、アプリケーション コンテナー Microsoft.Windows.ShellExperienceHost_10.0.19041.1023_neutral_neutral_cw5n1h2txyewy SID (S-1-15-2-155514346-2573954481-755741238-1654018636-1233331829-3075935687-2861478708) で実行中のアドレス LocalHost (LRPC 使用) のユーザー DESKTOP-P0P3ERS\\fujisawa SID (S-1-5-21-1311239280-1613070088-2410843011-1001) に与えることはできません。このセキュリティ アクセス許可は、コンポーネント サービス管理ツールを使って変更できます。</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_warn"></div><span>警告<span></td>';
		html += '<td>2021/07/27 9:27:40</td>';
		html += '<td>DustributedCOM</td>';
		html += '<td class="text-right">10016</td>';
		html += '<td>なし</td>';
		html += '<td class="hide">システム</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">DESKTOP-P0P3ERS\\usr</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">コンピューターの既定 のアクセス許可の設定では、CLSID <br/>{C2F03A33-21F5-47FA-B4BB-156362A2F239}<br/> および APPID <br/>{316CDED5-E4AE-4B15-9113-7055D84DCC97}<br/> の COM サーバー アプリケーションに対するローカルアクティブ化のアクセス許可を、アプリケーション コンテナー Microsoft.Windows.ShellExperienceHost_10.0.19041.1023_neutral_neutral_cw5n1h2txyewy SID (S-1-15-2-155514346-2573954481-755741238-1654018636-1233331829-3075935687-2861478708) で実行中のアドレス LocalHost (LRPC 使用) のユーザー DESKTOP-P0P3ERS\\fujisawa SID (S-1-5-21-1311239280-1613070088-2410843011-1001) に与えることはできません。このセキュリティ アクセス許可は、コンポーネント サービス管理ツールを使って変更できます。</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_warn"></div><span>警告<span></td>';
		html += '<td>2021/07/27 9:27:32</td>';
		html += '<td>DustributedCOM</td>';
		html += '<td class="text-right">10016</td>';
		html += '<td>なし</td>';
		html += '<td class="hide">システム</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">DESKTOP-P0P3ERS\\usr</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">コンピューターの既定 のアクセス許可の設定では、CLSID <br/>{C2F03A33-21F5-47FA-B4BB-156362A2F239}<br/> および APPID <br/>{316CDED5-E4AE-4B15-9113-7055D84DCC97}<br/> の COM サーバー アプリケーションに対するローカルアクティブ化のアクセス許可を、アプリケーション コンテナー Microsoft.Windows.ShellExperienceHost_10.0.19041.1023_neutral_neutral_cw5n1h2txyewy SID (S-1-15-2-155514346-2573954481-755741238-1654018636-1233331829-3075935687-2861478708) で実行中のアドレス LocalHost (LRPC 使用) のユーザー DESKTOP-P0P3ERS\\fujisawa SID (S-1-5-21-1311239280-1613070088-2410843011-1001) に与えることはできません。このセキュリティ アクセス許可は、コンポーネント サービス管理ツールを使って変更できます。</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_warn"></div><span>警告<span></td>';
		html += '<td>2021/07/27 9:20:09</td>';
		html += '<td>DustributedCOM</td>';
		html += '<td class="text-right">10016</td>';
		html += '<td>なし</td>';
		html += '<td class="hide">システム</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">DESKTOP-P0P3ERS\\usr</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">コンピューターの既定 のアクセス許可の設定では、CLSID <br/>{C2F03A33-21F5-47FA-B4BB-156362A2F239}<br/> および APPID <br/>{316CDED5-E4AE-4B15-9113-7055D84DCC97}<br/> の COM サーバー アプリケーションに対するローカルアクティブ化のアクセス許可を、アプリケーション コンテナー Microsoft.Windows.ShellExperienceHost_10.0.19041.1023_neutral_neutral_cw5n1h2txyewy SID (S-1-15-2-155514346-2573954481-755741238-1654018636-1233331829-3075935687-2861478708) で実行中のアドレス LocalHost (LRPC 使用) のユーザー DESKTOP-P0P3ERS\\fujisawa SID (S-1-5-21-1311239280-1613070088-2410843011-1001) に与えることはできません。このセキュリティ アクセス許可は、コンポーネント サービス管理ツールを使って変更できます。</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_error"></div><span>エラー<span></td>';
		html += '<td>2021/07/27 9:07:03</td>';
		html += '<td>Application Error</td>';
		html += '<td class="text-right">1000</td>';
		html += '<td>(100)</td>';
		html += '<td class="hide">Application</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">N/A</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">障害が発生しているアプリケーション名: backgroundTaskHost.exe、バージョン: 10.0.19041.546、タイム スタンプ: 0x1d3a15e7<br/>障害が発生しているモジュール名: CalendarApp.Gui.Win10.dll、バージョン: 1.0.0.0、タイム スタンプ: 0x60eb226e<br/>例外コード: 0xc00000fd<br/>障害オフセット: 0x0000000000d3a673<br/>障害が発生しているプロセス ID: 0x262c<br/>障害が発生しているアプリケーションの開始時刻: 0x01d7828c24c18454<br/>障害が発生しているアプリケーション パス: C:\\WINDOWS\\system32\\backgroundTaskHost.exe<br/>障害が発生しているモジュール パス: C:\\Program Files\\WindowsApps\\64885BlueEdge.OneCalendar_2021.703.2.0_x64__8kea50m9krsh2\\CalendarApp.Gui.Win10.dll<br/>レポート ID: 6f430399-479b-48fc-96bb-48fa1848d5d9<br/>障害が発生しているパッケージの完全な名前: 64885BlueEdge.OneCalendar_2021.703.2.0_x64__8kea50m9krsh2<br/>障害が発生しているパッケージに関連するアプリケーション ID: App</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_error"></div><span>エラー<span></td>';
		html += '<td>2021/07/27 9:01:44</td>';
		html += '<td>Karnel-EventTracing</td>';
		html += '<td class="text-right">3</td>';
		html += '<td>Session</td>';
		html += '<td class="hide">Microsoft-Windows-Kernel-EventTracing/Admin</th>';		// 名前
		html += '<td class="hide">Session</td>';	// キーワード
		html += '<td class="hide">SYSTEM</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">Stop</td>';		// オペレートコード
		html += '<td class="hide">セッション "PerfDiag Logger" が次のエラーで停止しました: 0xC0000188</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_warn"></div><span>警告<span></td>';
		html += '<td>2021/07/27 9:01:44</td>';
		html += '<td>Karnel-EventTracing</td>';
		html += '<td class="text-right">4</td>';
		html += '<td>Logging</td>';
		html += '<td class="hide">システム</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">DESKTOP-P0P3ERS\\usr</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">コンピューターの既定 のアクセス許可の設定では、CLSID <br/>{C2F03A33-21F5-47FA-B4BB-156362A2F239}<br/> および APPID <br/>{316CDED5-E4AE-4B15-9113-7055D84DCC97}<br/> の COM サーバー アプリケーションに対するローカルアクティブ化のアクセス許可を、アプリケーション コンテナー Microsoft.Windows.ShellExperienceHost_10.0.19041.1023_neutral_neutral_cw5n1h2txyewy SID (S-1-15-2-155514346-2573954481-755741238-1654018636-1233331829-3075935687-2861478708) で実行中のアドレス LocalHost (LRPC 使用) のユーザー DESKTOP-P0P3ERS\\fujisawa SID (S-1-5-21-1311239280-1613070088-2410843011-1001) に与えることはできません。このセキュリティ アクセス許可は、コンポーネント サービス管理ツールを使って変更できます。</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_warn"></div><span>警告<span></td>';
		html += '<td>2021/07/27 8:57:04</td>';
		html += '<td>DustributedCOM</td>';
		html += '<td class="text-right">10016</td>';
		html += '<td>なし</td>';
		html += '<td class="hide">システム</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">DESKTOP-P0P3ERS\\usr</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">コンピューターの既定 のアクセス許可の設定では、CLSID <br/>{C2F03A33-21F5-47FA-B4BB-156362A2F239}<br/> および APPID <br/>{316CDED5-E4AE-4B15-9113-7055D84DCC97}<br/> の COM サーバー アプリケーションに対するローカルアクティブ化のアクセス許可を、アプリケーション コンテナー Microsoft.Windows.ShellExperienceHost_10.0.19041.1023_neutral_neutral_cw5n1h2txyewy SID (S-1-15-2-155514346-2573954481-755741238-1654018636-1233331829-3075935687-2861478708) で実行中のアドレス LocalHost (LRPC 使用) のユーザー DESKTOP-P0P3ERS\\fujisawa SID (S-1-5-21-1311239280-1613070088-2410843011-1001) に与えることはできません。このセキュリティ アクセス許可は、コンポーネント サービス管理ツールを使って変更できます。</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_error"></div><span>エラー<span></td>';
		html += '<td>2021/07/27 8:56:59</td>';
		html += '<td>Application Error</td>';
		html += '<td class="text-right">1000</td>';
		html += '<td>(100)</td>';
		html += '<td class="hide">Application</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">N/A</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">障害が発生しているアプリケーション名: backgroundTaskHost.exe、バージョン: 10.0.19041.546、タイム スタンプ: 0x1d3a15e7<br/>障害が発生しているモジュール名: CalendarApp.Gui.Win10.dll、バージョン: 1.0.0.0、タイム スタンプ: 0x60eb226e<br/>例外コード: 0xc00000fd<br/>障害オフセット: 0x0000000000d3a673<br/>障害が発生しているプロセス ID: 0x262c<br/>障害が発生しているアプリケーションの開始時刻: 0x01d7828c24c18454<br/>障害が発生しているアプリケーション パス: C:\\WINDOWS\\system32\\backgroundTaskHost.exe<br/>障害が発生しているモジュール パス: C:\\Program Files\\WindowsApps\\64885BlueEdge.OneCalendar_2021.703.2.0_x64__8kea50m9krsh2\\CalendarApp.Gui.Win10.dll<br/>レポート ID: 6f430399-479b-48fc-96bb-48fa1848d5d9<br/>障害が発生しているパッケージの完全な名前: 64885BlueEdge.OneCalendar_2021.703.2.0_x64__8kea50m9krsh2<br/>障害が発生しているパッケージに関連するアプリケーション ID: App</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_error"></div><span>エラー<span></td>';
		html += '<td>2021/07/27 8:56:24</td>';
		html += '<td>Application Error</td>';
		html += '<td class="text-right">1000</td>';
		html += '<td>(100)</td>';
		html += '<td class="hide">Application</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">N/A</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">障害が発生しているアプリケーション名: backgroundTaskHost.exe、バージョン: 10.0.19041.546、タイム スタンプ: 0x1d3a15e7<br/>障害が発生しているモジュール名: CalendarApp.Gui.Win10.dll、バージョン: 1.0.0.0、タイム スタンプ: 0x60eb226e<br/>例外コード: 0xc00000fd<br/>障害オフセット: 0x0000000000d3a673<br/>障害が発生しているプロセス ID: 0x262c<br/>障害が発生しているアプリケーションの開始時刻: 0x01d7828c24c18454<br/>障害が発生しているアプリケーション パス: C:\\WINDOWS\\system32\\backgroundTaskHost.exe<br/>障害が発生しているモジュール パス: C:\\Program Files\\WindowsApps\\64885BlueEdge.OneCalendar_2021.703.2.0_x64__8kea50m9krsh2\\CalendarApp.Gui.Win10.dll<br/>レポート ID: 6f430399-479b-48fc-96bb-48fa1848d5d9<br/>障害が発生しているパッケージの完全な名前: 64885BlueEdge.OneCalendar_2021.703.2.0_x64__8kea50m9krsh2<br/>障害が発生しているパッケージに関連するアプリケーション ID: App</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_warn"></div><span>警告<span></td>';
		html += '<td>2021/07/27 8:54:32</td>';
		html += '<td>DustributedCOM</td>';
		html += '<td class="text-right">10016</td>';
		html += '<td>なし</td>';
		html += '<td class="hide">システム</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">DESKTOP-P0P3ERS\\usr</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">コンピューターの既定 のアクセス許可の設定では、CLSID <br/>{C2F03A33-21F5-47FA-B4BB-156362A2F239}<br/> および APPID <br/>{316CDED5-E4AE-4B15-9113-7055D84DCC97}<br/> の COM サーバー アプリケーションに対するローカルアクティブ化のアクセス許可を、アプリケーション コンテナー Microsoft.Windows.ShellExperienceHost_10.0.19041.1023_neutral_neutral_cw5n1h2txyewy SID (S-1-15-2-155514346-2573954481-755741238-1654018636-1233331829-3075935687-2861478708) で実行中のアドレス LocalHost (LRPC 使用) のユーザー DESKTOP-P0P3ERS\\fujisawa SID (S-1-5-21-1311239280-1613070088-2410843011-1001) に与えることはできません。このセキュリティ アクセス許可は、コンポーネント サービス管理ツールを使って変更できます。</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_warn"></div><span>警告<span></td>';
		html += '<td>2021/07/27 8:53:28</td>';
		html += '<td>DustributedCOM</td>';
		html += '<td class="text-right">10016</td>';
		html += '<td>なし</td>';
		html += '<td class="hide">システム</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">DESKTOP-P0P3ERS\\usr</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">コンピューターの既定 のアクセス許可の設定では、CLSID <br/>{C2F03A33-21F5-47FA-B4BB-156362A2F239}<br/> および APPID <br/>{316CDED5-E4AE-4B15-9113-7055D84DCC97}<br/> の COM サーバー アプリケーションに対するローカルアクティブ化のアクセス許可を、アプリケーション コンテナー Microsoft.Windows.ShellExperienceHost_10.0.19041.1023_neutral_neutral_cw5n1h2txyewy SID (S-1-15-2-155514346-2573954481-755741238-1654018636-1233331829-3075935687-2861478708) で実行中のアドレス LocalHost (LRPC 使用) のユーザー DESKTOP-P0P3ERS\\fujisawa SID (S-1-5-21-1311239280-1613070088-2410843011-1001) に与えることはできません。このセキュリティ アクセス許可は、コンポーネント サービス管理ツールを使って変更できます。</td>';
		html += '<tr>';


		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_error"></div><span>エラー<span></td>';
		html += '<td>2021/07/27 8:52:14</td>';
		html += '<td>Application Error</td>';
		html += '<td class="text-right">1000</td>';
		html += '<td>(100)</td>';
		html += '<td class="hide">Application</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">N/A</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">障害が発生しているアプリケーション名: backgroundTaskHost.exe、バージョン: 10.0.19041.546、タイム スタンプ: 0x1d3a15e7<br/>障害が発生しているモジュール名: CalendarApp.Gui.Win10.dll、バージョン: 1.0.0.0、タイム スタンプ: 0x60eb226e<br/>例外コード: 0xc00000fd<br/>障害オフセット: 0x0000000000d3a673<br/>障害が発生しているプロセス ID: 0x262c<br/>障害が発生しているアプリケーションの開始時刻: 0x01d7828c24c18454<br/>障害が発生しているアプリケーション パス: C:\\WINDOWS\\system32\\backgroundTaskHost.exe<br/>障害が発生しているモジュール パス: C:\\Program Files\\WindowsApps\\64885BlueEdge.OneCalendar_2021.703.2.0_x64__8kea50m9krsh2\\CalendarApp.Gui.Win10.dll<br/>レポート ID: 6f430399-479b-48fc-96bb-48fa1848d5d9<br/>障害が発生しているパッケージの完全な名前: 64885BlueEdge.OneCalendar_2021.703.2.0_x64__8kea50m9krsh2<br/>障害が発生しているパッケージに関連するアプリケーション ID: App</td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_error"></div><span>エラー<span></td>';
		html += '<td>2021/07/27 8:50:24</td>';
		html += '<td>DPTF</td>';
		html += '<td class="text-right">17</td>';
		html += '<td>なし</td>';
		html += '<td class="hide">Application</th>';		// 名前
		html += '<td class="hide"></td>';	// キーワード
		html += '<td class="hide">LOCAL SERVICE</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">ESIF(8.7.10600.20700) TYPE: ERROR MODULE: ACTION FUNC: EsifActWriteLogHandler FILE: esif_uf_action.c LINE: 789 TIME: 48792135 ms<br/><br/>UPE_SOCWC:[CalculateProcessorPkgC0@socwc_telemetry.c#454]<48792135 ms>: Error: Total Processor Pkg CState Residency count = 0x10F9D40B3D6 > TSC Delta = 0x10F45D7272D </td>';
		html += '<tr>';

		html += '<tr class="selectable">';
		html += '<td><div class="level_icon level_icon_error"></div><span>エラー<span></td>';
		html += '<td>2021/07/27 8:49:54</td>';
		html += '<td>DustributedCOM</td>';
		html += '<td class="text-right">10010</td>';
		html += '<td>(100)</td>';
		html += '<td class="hide">システム</th>';		// 名前
		html += '<td class="hide">クラシック</td>';	// キーワード
		html += '<td class="hide">SYSTEM</td>';		// ユーザー
		html += '<td class="hide">DESKTOP-P0P3ERS</td>';	// コンピュータ
		html += '<td class="hide">情報</td>';		// オペレートコード
		html += '<td class="hide">サーバー {AB93B6F1-BE76-4185-A488-A9001B105B94} は、必要なタイムアウト期間内に DCOM に登録しませんでした。 </td>';
		html += '<tr>';

		html += "</table>";
		html += "</div>";

		$body.find("div.center-window div.top").append(html);
		$body.find("div.center-window div.top table tr.selectable").click(function(){
			$('div.center-window div.top table tr').removeClass('selected');
			$(this).addClass("selected");
			_self.showLogDetail($body, $(this));
		});

		$body.find("div.center-window div.top table tr.selectable").eq(0).click();
	}

	this.showLogDetail=function($body, $target){

		$body.find("div.center-window div.bottom").empty();

		var source = $target.find('td').eq(2).text();
		var eventId = $target.find('td').eq(3).text();

		var html = '';
		html += '<div class="detail_title">イベント' + eventId + ', ' + source + '<div class="del">×</div></div>';
		$body.find("div.center-window div.bottom").append(html);

		html = '';


		html += '<div class="deital_box">';
		html += '<div class="deital_box_inner">';
		html += '<div class="deital_box_message">';
		html += $target.find('td').eq(10).html();
		html += '</div>';

		html += '<div class="deital_box_items">';
		html += '<table>';

		html += '<tr>';
		html += '<td>ログの名前(M):';
		html += '</td>';
		html += '<td colspan="3">';
		html += $target.find('td').eq(5).text();
		html += '</td>';
		html += '</tr>';

		html += '<tr>';
		html += '<td>ソース(S):';
		html += '</td>';
		html += '<td>';
		html += $target.find('td').eq(2).text();
		html += '</td>';
		html += '<td>ログの日付(D):';
		html += '</td>';
		html += '<td>';
		html += $target.find('td').eq(1).text();
		html += '</td>';
		html += '</tr>';

		html += '<tr>';
		html += '<td>イベントID(E):';
		html += '</td>';
		html += '<td>';
		html += $target.find('td').eq(3).text();
		html += '</td>';
		html += '<td>タスクのカテゴリ(Y):';
		html += '</td>';
		html += '<td>';
		html += $target.find('td').eq(4).text();
		html += '</td>';
		html += '</tr>';

		html += '<tr>';
		html += '<td>レベル(L):';
		html += '</td>';
		html += '<td>';
		html += $target.find('td').eq(0).text();
		html += '</td>';
		html += '<td>キーワード(K):';
		html += '</td>';
		html += '<td>';
		html += $target.find('td').eq(6).text();
		html += '</td>';
		html += '</tr>';

		html += '<tr>';
		html += '<td>ユーザー(U):';
		html += '</td>';
		html += '<td>';
		html += $target.find('td').eq(7).text();
		html += '</td>';
		html += '<td>コンピューター(R):';
		html += '</td>';
		html += '<td>';
		html += $target.find('td').eq(8).text();
		html += '</td>';
		html += '</tr>';

		html += '<tr>';
		html += '<td>オペレートコード(O):';
		html += '</td>';
		html += '<td>';
		html += $target.find('td').eq(9).text();
		html += '</td>';
		html += '<td>';
		html += '</td>';
		html += '<td>';
		html += '</td>';
		html += '</tr>';

		html += '<tr>';
		html += '<td>詳細情報(I):';
		html += '</td>';
		html += '<td class="log_help">イベント ログのヘルプ';
		html += '</td>';
		html += '<td>';
		html += '</td>';
		html += '<td>';
		html += '</td>';
		html += '</tr>';

		html += '</table>';


		html += '</div>';

		html += '</div>';
		html += '</div>';

		html += '<div class="tab1">全般</div>';
		html += '<div class="tab2">詳細</div>';

		$body.find("div.center-window div.bottom").append(html);


	}


	// リサイズイベント
	this.addOnResizeEvent(function(params){
		_self.setObject(params);
	});


	this.setObject=function(params){

		var $body = params["window"].find(".body");

		// 左ウィンドウ、left:変更なし、width:変更なし、height:変更なし

		// 中ウィンドウ、left:変更なし、width:再計算、height:変更なし
		// widthは、全体の幅 - (left win width + right win width);
		var leftWidth = to_num(_self.thisWindow.find('div.left-window').css('width'));
		var rightWidth = to_num(_self.thisWindow.find('div.right-window').css('width'));
		var winWidth = $body.width();

		_self.thisWindow.find('div.center-window').css({width : to_px(winWidth - (leftWidth + rightWidth) - 10)});


		// 中ウィンドウ上、top:変更なし、height:変更なし

		// 中ウィンドウ下、top:変更なし、height:再計算
		var centerHeight = to_num( _self.thisWindow.find('div.center-window').css('height'));
		var centerTitleHeight = to_num( _self.thisWindow.find('div.center-window div.title').css('height'));
		var centerTopHeight = to_num( _self.thisWindow.find('div.center-window div.top').css('height'));
		_self.thisWindow.find('div.center-window div.bottom').css({height : to_px(centerHeight - (centerTitleHeight + centerTopHeight + 2 + 5))});

		// 右ウィンドウ、left:再計算、width:変更なし、height:変更なし
		_self.thisWindow.find('div.right-window').css({left : to_px(winWidth - rightWidth)});

		// 左バー
		// 変更なし

		// 右バー
		// left:再計算
		_self.thisWindow.find('div.right-bar').css({left : to_px(winWidth - rightWidth - 5)});

		// 中央バー
		// top:再変更なし

	}


}

