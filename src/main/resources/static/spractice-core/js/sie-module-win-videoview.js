// ビデオ表示モジュール
var SieModuleWinVideoview = function(moduleId){

	this.id = moduleId;
	this.title = "画像";
	this.titlebar = "画像";
	this.icon = "sie/img/icon-videoview.png"
	this.isShowDeskTop=true;
	this.isWindowOpen=true;
	this.moduleClass="sie-module-win-imageview";
	this.delayedStart = 0;

	this.view = '<div class="sie-module-win-videoview-body">';
	this.view += '</div>';

	this.url=""
	this.width=500;
	this.height=540;
	this.videoWidth=null;
	this.videoHeight=null;
	this.onCreateEvents=[];
	this.onActiveEvents=[];
	this.onResizeEvents=[];
	this.params=null;
	this.thisWindow=null;
	this.resource="";
	this.$video=null;
	this.endAction=null;
	this.playbackRate = 1.0;

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
		var $body = params["window"].find(".sie-module-win-videoview-body");

//		video.addEventListener("load", function() {
			var _img= '<video src="' + _self.url + '" ';
			if(_self.videoWidth != null){
				_img += ' width="' + _self.videoWidth + '"';
			}
			if(_self.videoHeight != null){
				_img += ' height="' + _self.videoHeight + '"';
			}
			_img += ' style="display:none;"/>';

			_self.$video = $(_img).appendTo($body);

			_self.$video.get(0).defaultPlaybackRate=_self.playbackRate;

			_self.$video.get(0).addEventListener("loadeddata", function(){
				_self.$video.show();
				_self.$video.get(0).play();
			}, false);

			_self.$video.get(0).addEventListener("ended", function(){
				if(_self.endAction != null){
					_self.endAction();
				}
			}, false);

//		}, false);
//		video.src = _self.url;

	})

	// リサイズイベント
	this.addOnResizeEvent(function(params){
	})

	// 画像差し替え
	this.replaceVideo=function(src, playbackRate){
		if(playbackRate != null){
			_self.$video.get(0).defaultPlaybackRate=playbackRate;
		}
		else{
			_self.$video.get(0).playbackRate=1.0;
		}
		_self.$video.get(0).src = src;
	}
}

