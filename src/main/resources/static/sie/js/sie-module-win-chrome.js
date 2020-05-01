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

//		_self.setObject(params);

		var $titlebar = params["window"].find(".titlebar");
//		alert($titlebar.length);
		$titlebar.prepend('<div class="tab-menu"><img src="' + _self.icon + '"/>' + _self.titlebar + '<i class="fa fa-times" aria-hidden="true"></i></div>');
//		$body.html(_self.resource);

	})

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

