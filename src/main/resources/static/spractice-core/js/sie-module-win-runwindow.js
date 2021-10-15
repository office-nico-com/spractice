// メモ帳モジュール
var SieModuleWinRunwindow = function(moduleId){
	this.id = moduleId;
	this.title = "ファイル名を指定して実行";
	this.titlebar = "ファイル名を指定して実行";
	this.icon = "sie/img/icon-runwindow.png"
	this.isShowDeskTop=false;
	this.isWindowOpen=true;
	this.isClosable=false;
	this.moduleClass="sie-module-win-notepad";
	this.delayedStart = 0;

	this.view = '<div class="sie-module-win-runwindow-body">';



	this.view += '</div>';

	this.height=200;
	this.width=480;
	this.left=10;
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


		var html = '';
		html += '<div class="runicon"></div>';
		html += '<div class="label">実行するプログラム名、または開くフォルダーやドキュメント名、インターネットリソース名を入力してください。</div>';
		html += '<div class="input">名前(O):&nbsp;<input type="text" style=""></div>';
		html += '<div class="control"></div>';
		html += '<i class="fa fa-chevron-down" aria-hidden="true"></i>';

		html += '<button class="button1">OK</button>';
		html += '<button class="button2">キャンセル</button>';
		html += '<button class="button3">参照(B)</button>';


		var $body = params["window"].find('.sie-module-win-runwindow-body');
		$body.html(html);

		params["window"].find(".titlebar .button").eq(1).hide();
		params["window"].find(".titlebar .button").eq(2).hide();

		params["window"].css({"left":"20px;", "margin-bottom":"50px","bottom":"0", "top":""});

	})

	// リサイズイベント
	this.addOnResizeEvent(function(params){

	})

	this.setCommand=function(command){
		_self.thisWindow.find("div.sie-module-win-runwindow-body div.input input").val(command);
	}
}


