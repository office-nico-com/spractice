// メモ帳モジュール
var SieModuleWinNotepad = function(moduleId){
	this.id = moduleId;
	this.title = "メモ帳";
	this.titlebar = "メモ帳";
	this.icon = "sie/img/icon-notepad.png"
	this.isShowDeskTop=true;
	this.isWindowOpen=true;
	this.moduleClass="sie-module-win-notepad";
	this.delayedStart = 0;

	this.view = '<div class="sie-module-win-notepad-body">';
	this.view += '<div class="note-menu-bar">'
	this.view += '<div class="note-menu note-menu-1">ﾌｧｲﾙ(F)</div>';
	this.view += '<div class="note-menu note-menu-2">編集(E)</div>';
	this.view += '<div class="note-menu note-menu-3">書式(O)</div>';
	this.view += '<div class="note-menu note-menu-4">表示(V)</div>';
	this.view += '<div class="note-menu note-menu-5">ﾍﾙﾌﾟ(H)</div>';
	this.view += '</div>';

	this.view += '<div class="note-body">'
	this.view += '';
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

		var $body = params["window"].find(".note-body");
		$body.html(_self.resource);

	})

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
}

