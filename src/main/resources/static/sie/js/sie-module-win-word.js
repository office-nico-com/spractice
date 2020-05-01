// ワードモジュール
var SieModuleWinWord = function(moduleId){
	this.id = moduleId;
	this.title = "Word";
	this.titlebar = "Word";
	this.icon = "sie/img/icon-word.png"
	this.isShowDeskTop=true;
	this.isWindowOpen=true;
	this.moduleClass="sie-module-win-word";
	this.delayedStart = 1000;

	this.scriptBar=null;

	this.view = '<div class="sie-module-win-word-body">';
	this.view += '<div class="word-menu-bar">'
	this.view += '<div class="word-menu">ファイル</div>';
	this.view += '<div class="word-menu"><div class="current"></div>ホーム</div>';
	this.view += '<div class="word-menu">挿入</div>';
	this.view += '<div class="word-menu">描画</div>';
	this.view += '<div class="word-menu">デザイン</div>';
	this.view += '<div class="word-menu">レイアウト</div>';
	this.view += '<div class="word-menu">参考資料</div>';
	this.view += '<div class="word-menu">差し込み文章</div>';
	this.view += '<div class="word-menu">校閲</div>';
	this.view += '<div class="word-menu">表示</div>';
	this.view += '<div class="word-menu">ヘルプ</div>';
	this.view += '<div class="word-menu word-menu-search"><i class="fa fa-search" aria-hidden="true"></i>実行したい作業を入力してください</div>';

	this.view += '<i class="fa fa-file-o" aria-hidden="true" style="top:43px;left:32px;font-size:30px;color:#ED8733;"></i>';
	this.view += '<i class="fa fa-file" aria-hidden="true" style="top:56px;left:42px;font-size:22px;color:#FAFAFA;"></i>';
	this.view += '<i class="fa fa-file-o" aria-hidden="true" style="top:56px;left:42px;font-size:22px;color:#3A3A38;"></i>';

	this.view += '<div style="border-top:solid 1px #D2D0CE;width:50px;top:86px;left:22px;">貼り付け</div>';
	this.view += '<div style="width:40px;top:100px;left:22px;text-align:center;font-size:2px;color:#6d6d6d;">▼</div>';

	this.view += '<i class="fa fa-scissors" aria-hidden="true" style="top:43px;font-size:14px;left:90px;transform:rotate(-90deg);color:#A9A8A8;"></i>';
	this.view += '<i class="fa fa-files-o" aria-hidden="true" style="top:75px;font-size:14px;left:90px;color:#A9A8A8;"></i>';
	this.view += '<i class="fa fa-paint-brush" aria-hidden="true" style="top:107px;font-size:14px;left:90px;color:#EE9245;"></i>';

	this.view += '<div style="top:140px;left:16px;">クリップボード</div>';
	this.view += '<i class="fa fa-external-link" aria-hidden="true" style="top:145px;left:116px;font-size:8px;transform: scale(1, -1);color:#7E7E7E;"></i>';
	this.view += '<div style="top:34px;left:130px;width:0px;height:120px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------

	this.view += '<select style="position:absolute;top:49px;left:142px;font-size:11px;width:200px;"><option>游明朝 (本文のフォント - 日本語)</option><option>游ゴシック Light (見出しのフォント)</option></select>';
	this.view += '<select style="position:absolute;top:49px;left:342px;font-size:11px;"><option>10.5</option><option>11</option><option>12</option></select>';

	this.view += '<i class="fa fa-font" aria-hidden="true" style="top:53px;left:400px;"></i>';
	this.view += '<i class="fa fa-angle-up" aria-hidden="true" style="top:49px;left:411px;font-size:6px;"></i>';

	this.view += '<i class="fa fa-font" aria-hidden="true" style="top:53px;left:430px;font-size:11px;"></i>';
	this.view += '<i class="fa fa-angle-down" aria-hidden="true" style="top:49px;left:439px;font-size:6px;"></i>';

	this.view += '<div style="top:50px;left:458px;font-size:15px;">Aa</div>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:55px;left:478px;font-size:6px;"></i>';

	this.view += '<i class="fa fa-font" aria-hidden="true" style="top:50px;left:495px;font-size:18px;"></i>';
	this.view += '<div style="top:60px;left:505px;width:8px;height:8px;background-color:#F3F2F1;"></div>';
	this.view += '<i class="fa fa-eraser" aria-hidden="true" style="top:55px;left:503px;font-size:12px;color:#9E46A7;"></i>';


	this.view += '<div style="top:45px;left:533px;font-size:9px;">ア</div>';
	this.view += '<i class="fa fa-align-center" aria-hidden="true" style="top:58px;left:533px;font-size:9px;"></i>';

	this.view += '<div style="top:50px;left:561px;font-size:15px;border:solid 1px #000;width:18px;height:18px;text-align:center;line-height:18px;">A</div>';

	this.view += '<i class="fa fa-bold" aria-hidden="true" style="top:95px;left:160px;"></i>';
	this.view += '<i class="fa fa-italic" aria-hidden="true" style="top:95px;left:200px;"></i>';
	this.view += '<i class="fa fa-underline" aria-hidden="true" style="top:95px;left:240px;"></i>';
	this.view += '<i class="fa fa-strikethrough" aria-hidden="true" style="top:95px;left:280px;"></i>';
	this.view += '<i class="fa fa-subscript" aria-hidden="true" style="top:95px;left:320px;"></i>';
	this.view += '<i class="fa fa-superscript" aria-hidden="true" style="top:95px;left:360px;"></i>';

	// 薄いので重ねる
	this.view += '<i class="fa fa-font" aria-hidden="true" style="top:95px;left:400px;text-shadow: #0D6CB7 0 0 3px;color:#F3F2F1;"></i>';
	this.view += '<i class="fa fa-font" aria-hidden="true" style="top:95px;left:400px;text-shadow: #0D6CB7 0 0 3px;color:#F3F2F1;"></i>';
	this.view += '<i class="fa fa-font" aria-hidden="true" style="top:95px;left:400px;text-shadow: #0D6CB7 0 0 3px;color:#F3F2F1;"></i>';
	this.view += '<i class="fa fa-font" aria-hidden="true" style="top:95px;left:400px;text-shadow: #0D6CB7 0 0 3px;color:#F3F2F1;"></i>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:95px;left:415px;font-size:6px;"></i>';

	this.view += '<i class="fa fa-pencil" aria-hidden="true" style="top:95px;left:440px;font-size:12px;"></i>';
	this.view += '<div style="top:107px;left:437px;background-color:yellow;width:18px;height:5px;"></div>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:95px;left:457px;font-size:6px;"></i>';

	this.view += '<i class="fa fa-font" aria-hidden="true" style="top:95px;left:480px;font-size:12px;"></i>';
	this.view += '<div style="top:107px;left:477px;background-color:red;width:18px;height:5px;"></div>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:95px;left:497px;font-size:6px;"></i>';

	this.view += '<div style="top:95px;left:520px;font-size:15px;background-color:#B3B3B3;width:18px;height:18px;text-align:center;line-height:18px;">A</div>';

	this.view += '<div style="top:95px;left:560px;font-size:15px;border:solid 1px #000;width:20px;height:20px;text-align:center;line-height:20px;font-size:12px;border-radius:10px;-webkit-border-radius:10px;-moz-border-radius:10px;">字</div>';

	this.view += '<div style="top:140px;left:350px;">フォント</div>';
	this.view += '<i class="fa fa-external-link" aria-hidden="true" style="top:145px;left:586px;font-size:8px;transform: scale(1, -1);color:#7E7E7E;"></i>';
	this.view += '<div style="top:34px;left:600px;width:0px;height:120px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------

	this.view += '<i class="fa fa-list-ul" aria-hidden="true" style="top:43px;left:620px;"></i>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:43px;left:640px;font-size:6px;"></i>';

	this.view += '<i class="fa fa-list-ol" aria-hidden="true" style="top:43px;left:660px;"></i>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:43px;left:680px;font-size:6px;"></i>';


	this.view += '<i class="fa fa-align-left" aria-hidden="true" style="top:43px;left:700px;transform:rotate(180deg);"></i>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:43px;left:720px;font-size:6px;"></i>';

	this.view += '<i class="fa fa-outdent" aria-hidden="true" style="top:43px;left:740px;"></i>';

	this.view += '<i class="fa fa-outdent" aria-hidden="true" style="top:43px;left:780px;transform:rotate(180deg);"></i>';

	this.view += '<i class="fa fa-align-left" aria-hidden="true" style="top:80px;left:620px;"></i>';
	this.view += '<i class="fa fa-align-center" aria-hidden="true" style="top:80px;left:660px;"></i>';
	this.view += '<i class="fa fa-align-right" aria-hidden="true" style="top:80px;left:700px;"></i>';
	this.view += '<i class="fa fa-align-justify" aria-hidden="true" style="top:70px;left:730px;background-color:#B3B3B3;display:block;padding:10px;"></i>';

	this.view += '<i class="fa fa-text-width" aria-hidden="true" style="top:80px;left:780px;"></i>';

	this.view += '<i class="fa fa-text-height" aria-hidden="true" style="top:80px;left:820px;"></i>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:80px;left:840px;font-size:6px;"></i>';

	this.view += '<i class="fa fa-flask" aria-hidden="true" style="top:117px;left:620px;"></i>';
	this.view += '<i class="fa fa-table" aria-hidden="true" style="top:117px;left:660px;"></i>';

	this.view += '<i class="fa fa-arrows-h" aria-hidden="true" style="top:112px;left:700px;font-size:9px;transform:scaleX(2);"></i>';
	this.view += '<i class="fa fa-font" aria-hidden="true" style="top:118px;left:700px;font-size:9px;transform:scaleX(2);"></i>';
	this.view += '<i class="fa fa-caret-down" aria-hidden="true" style="top:117px;left:720px;font-size:6px;"></i>';

	this.view += '<i class="fa fa-sort-alpha-desc" aria-hidden="true" style="top:117px;left:740px;"></i>';

	this.view += '<i class="fa fa-exchange" aria-hidden="true" style="top:117px;left:780px;"></i>';

	this.view += '<div style="top:140px;left:720px;">段落</div>';
	this.view += '<i class="fa fa-external-link" aria-hidden="true" style="top:145px;left:851px;font-size:8px;transform: scale(1, -1);color:#7E7E7E;"></i>';
	this.view += '<div style="top:34px;left:865px;width:0px;height:120px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------

	this.view += '<div class="text-style" style="top:60px;left:880px;"></div>';

	this.view += '<div style="top:140px;left:980px;">スタイル</div>';
	this.view += '<i class="fa fa-external-link" aria-hidden="true" style="top:145px;left:1126px;font-size:8px;transform: scale(1, -1);color:#7E7E7E;"></i>';
	this.view += '<div style="top:34px;left:1140px;width:0px;height:120px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------


	this.view += '<i class="fa fa-search" aria-hidden="true" style="top:23px;left:1155px;display:block;padding:10px;border:solid 1px #C6C6C6;width:40px;height:40px;line-height:20px;text-align:center;font-size:20px;background-color:#FCFCFB;color:#464644;"></i>';
	this.view += '<div style="border-top:solid 0px #D2D0CE;top:88px;left:1163px;">編集</div>';
	this.view += '<div style="width:40px;top:102px;left:1156px;text-align:center;font-size:2px;color:#6d6d6d;">▼</div>';
	this.view += '<div style="top:34px;left:1210px;width:0px;height:120px;border-right:solid 1px #B3B0AD;"></div>';

//---------------------

	this.view += '<i class="fa fa-microphone" aria-hidden="true" style="top:55px;left:1240px;font-size:30px;color:#0063B1;"></i>';
	this.view += '<div style="border-top:solid 0px #D2D0CE;top:90px;left:1220px;width:50px; word-wrap: break-word;word-break : break-all;">ディクテー<br/>ション</div>';
	this.view += '<div style="width:40px;top:110px;left:1242px;text-align:center;font-size:2px;color:#6d6d6d;">▼</div>';
	this.view += '<div style="top:34px;left:1290px;width:0px;height:120px;border-right:solid 1px #B3B0AD;"></div>';
	this.view += '<div style="top:140px;left:1235px;">音声</div>';

//---------------------
	this.view += '<i class="fa fa-angle-up" aria-hidden="true" style="top:145px;left:1300px;"></i>';
	this.view += '</div>';




	this.view += '<div class="word-body">'
	this.view += '<div class="word-body-page">'
	this.view += '</div>';	// .word-body-page

	this.view += '</div>';	// .word-body

	this.view += '<div class="word-body-script-bar"></div>';

	this.view += '</div>';	// .sie-module-win-word-body



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

		if(_self.scriptBar != null && _self.scriptBar != ''){
			params["window"].find(".word-body-script-bar").html(_self.scriptBar);
			setTimeout(function(){
				params["window"].find(".sie-module-win-word-body").addClass("script-on");
			}, 1000);
		}

		// タイトルバーのセット
		_self.thisWindow.find('div.titlebar').append('<div class="center-title">' + _self.titlebar + ' - Word</div>')

		
		// ワード本文のセット
		_self.thisWindow.find('.word-body-page').html(_self.contents);

		// スプラッシュロゴ
		var splashLogo = '<div class="sie-module-win-word-splash-logo">';
		splashLogo += '<div class="application-logo"><div style="top:8px;height:10px;width:10px;background-color:#fff;"></div><div style="top:8px;left:12px;height:10px;width:10px;background-color:#fff;"></div><div style="top:20px;height:10px;width:10px;background-color:#fff;"></div><div style="top:20px;left:12px;height:10px;width:10px;background-color:#fff;"></div></div>';
		splashLogo += '<div class="company">Microsoft</div>';
		splashLogo += '<div class="application-name">Word</div>';
		splashLogo += '<div class="message">起動しています．．．</div>';
		splashLogo += '</div>';
		$(splashLogo).appendTo(params['home']).fadeIn("fast" , function(){var __self=this;setTimeout(function(){$(__self).fadeOut("fast", function(){$(this).remove()})}, 1200)});
	})

	// リサイズイベント
	this.addOnResizeEvent(function(params){
		_self.setObject(params);
	})

	// オブジェクトの位置をセット
	this.setObject=function(params){
		// リサイズなど
	}

	// スクリプトバーを非表示
	this.hideScriptBar=function(){
		var _self = this;
		_self.params["window"].find(".sie-module-win-word-body").removeClass("script-on");
	}
}

