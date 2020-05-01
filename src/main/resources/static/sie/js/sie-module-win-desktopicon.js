// デスクトップアイコンモジュール
// ウィンドウを開かない
var SieModuleWinDesktopicon = function(moduleId){
	this.id = moduleId;
	this.title = "アイコン"
	this.titlebar = "アイコン";
	this.icon = "sie/img/icon-notepad.png"
	this.isShowDeskTop=true;
	this.isWindowOpen=false;
	this.moduleClass="sie-module-win-desktopicon";
	this.delayedStart = 0;

	this.view += '';

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
}

