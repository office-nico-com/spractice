// Chromeモジュール
var SieModuleWinChrome = function(moduleId){
	this.id = moduleId;
	this.title = "Gmail";
	this.titlebar = "Gmail";
	this.icon = "sie/img/icon-chrome.png"
	this.isShowDeskTop=true;
	this.isWindowOpen=true;
	this.moduleClass="sie-module-win-chrome";
	this.delayedStart = 0;

	this.url="mail.google.com/mail/u/0/?tab=wm&ogbl#inbox"

	this.view = '<div class="sie-module-win-chrome-body">';

	this.width=1000;
	this.height=500;
	this.onCreateEvents=[];
	this.onActiveEvents=[];
	this.onResizeEvents=[];
	this.params=null;
	this.thisWindow=null;
	this.resource="";
	this.backgroundColor="#fff";

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

//		_self.setObject(params);

		var $titlebar = params["window"].find(".titlebar");
//		alert($titlebar.length);
		$titlebar.prepend('<div class="tab-menu"><img src="' + _self.icon + '"/>' + _self.titlebar + '<i class="fa fa-times" aria-hidden="true"></i></div>');

		var head = '';
		head += '<div class="menu-bar">'
		head += '<i class="fa fa-arrow-left" aria-hidden="true"></i>';
		head += '<i class="fa fa-arrow-right" aria-hidden="true"></i>';
		head += '<i class="fa fa-repeat" aria-hidden="true"></i>';
		head += '<div class="address-bar">';
		head += '<span><i class="fa fa-lock" aria-hidden="true"></i></span>';
		head += '<input type="text" value="' + _self.url + '" class="sie-url"/>';
		head += '</div>';
		head += '<div class="setting"><i class="fa fa-ellipsis-v" aria-hidden="true"></i></div>';
		head += '<i class="fa fa-user-circle" aria-hidden="true"></i>';
		head += '<br/>';
		head += '</div>';
		head += '</div>';
		$titlebar.append(head);


		params["window"].find(".sie-module-win-chrome-body").append(_self.resource);
		params["window"].find(".sie-module-win-chrome-body").css("background-color", _self.backgroundColor);

	})

	// 再描画
	this.redraw=function(contents){
		_self.params["window"].find(".sie-module-win-chrome-body").html(contents);
	}

	// 背景色変更
	this.setBackgroundColor=function(color){
		_self.params["window"].find(".sie-module-win-chrome-body").css("background-color", color);
	}

	// URL変更
	this.setUrl=function(url){
		_self.params["window"].find(".sie-url").val(url);
	}

	// 初期化
	this.reflash=function(){
		_self.params["window"].find(".sie-url").val(this.url);
		_self.params["window"].find(".sie-module-win-chrome-body").html(this.resource);
	}

/*
	// リサイズイベント
	this.addOnResizeEvent(function(params){
		_self.setObject(params);
	})

	// オブジェクトの位置をセット
	this.setObject=function(params){

		// 本体部分の表示領域を調整
		var $windowFrame = params["window"].find(".frame");
		var $body = params["window"].find(".note-body");
		var height = $windowFrame.height();
		$body.css({height:to_px(height - 18)});
	}
*/
}

