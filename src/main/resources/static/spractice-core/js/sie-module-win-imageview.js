// 画像表示モジュール
var SieModuleWinImageview = function(moduleId){
	this.id = moduleId;
	this.title = "画像";
	this.titlebar = "画像";
	this.icon = "sie/img/icon-imageview.png"
	this.isShowDeskTop=true;
	this.isWindowOpen=true;
	this.moduleClass="sie-module-win-imageview";
	this.delayedStart = 0;

	this.view = '<div class="sie-module-win-imageview-body">';
	this.view += '</div>';

	this.image=""


	this.width=500;
	this.height=540;
	this.imageWidth=null;
	this.imageHeight=null;
	this.onCreateEvents=[];
	this.onActiveEvents=[];
	this.onResizeEvents=[];
	this.params=null;
	this.thisWindow=null;
	this.resource="";
	this.$image=null;

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
		var $body = params["window"].find(".sie-module-win-imageview-body");

		var img = new Image();
		img.addEventListener("load", function() {
			var _img= '<img src="';
			_img += img.src;
			_img += '" ';
			if(_self.imageWidth != null){
				_img += ' width="' + _self.imageWidth + '"';
			}
			if(_self.imageHeight != null){
				_img += ' height="' + _self.imageHeight + '"';
			}
			_img += '/>';
			_self.$image = $(_img).appendTo($body);
		}, false);
		img.src = _self.image;
	})

	// リサイズイベント
	this.addOnResizeEvent(function(params){
	})

	// 画像差し替え
	this.replaceImage=function(src, fade){
		var _self=this;
		var img = new Image();
		img.addEventListener("load", function() {
			if(fade == null || fade == true){
				_self.$image.fadeOut(500, function(){
					if(_self.$image != null){
						_self.$image.attr("src", src);
						_self.$image.fadeIn(500);
					}
				});
			}
			else{
				if(_self.$image != null){
					_self.$image.attr("src", src);
				}
			}
		}, false);
		img.src = src;
	}
}

