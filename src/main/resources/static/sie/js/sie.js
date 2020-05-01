$.fn.extend(new function(){

	// モジュールのリスト
	this.modules = {};
	this.windows = new Array();
	this.windowIndex = 100;

	this.sie=function(platform){
		var _self = this;
		var command = 'this.platform = new SiePlatform' + platform.charAt(0).toUpperCase() + platform.slice(1) + "(this)";
		eval(command);

		$(window).resize(function(){
			_self.platform.resizeWindow();
		});

		// 背景のセット
		this.setBackground=function(url, options){
			this.platform.$window.css({"background-image": 'url("' + url + '")'});
			if(options != null){
				this.platform.$window.css(options);
			}
		}


		// モジュールのセット
		this.setModule=function(module){
			var _self = this;
			if(module == null || module.id == null){
				alert("Please specify the module !");
				return;
			}
			_self.modules[module.id.toString()]=module;

			if(module.isShowDeskTop){
				var $icon = _self.platform.addIcon(module.title, module.icon, module.id);
				$icon.data("sie-module-id", module.id);
				$icon.data("sie-module-title", module.title);
				var _w = "";
				if(module.width != null){
					_w = module.width;
				}
				var _h = "";
				if(module.height != null){
					_h = module.height;
				}
				$icon.data("sie-module-width", _w);
				$icon.data("sie-module-height", _h);
				$icon.data("sie-module-icon", module.icon);
				$icon.dblclick(function(e){

					var clickedModuleId = $(this).data("sie-module-id");
/*
					var clickedModuleTitle = $(this).data("sie-module-title");
					var clickedModuleWidth = $(this).data("sie-module-width");
					var clickedModuleHeight = $(this).data("sie-module-height");
					var clickedModuleIcon = $(this).data("sie-module-icon");
*/
					_self.openWindow(clickedModuleId);

				});
			}
		}

		// ウィンドウのオープン
		this.openWindow=function(moduleId){
			var _self = this;
			var _module = null;
			// すでに開かれているWindowを検索
			for(var i=0; i<_self.windows.length; i++){
				if(_self.windows[i]["moduleId"] == parseInt(moduleId)){
					break;
				}
			}

			// ウィンドウが作成されていない場合は、新しいWindowの表示
			// ウィンドウが作成されている場合は、クリックされたウィンドウをアクティブ化
			if(i >= _self.windows.length){
				if(_self.modules[moduleId.toString()] != null){
					var _module = _self.modules[moduleId.toString()];
					var $window = _self._createWindow(_module);
				}
			}
			else{
				_self.windowIndex++;
				_self.windows[i]["window"].css({"z-index":_self.windowIndex});
				_self.platform.activeWindow(_self.windows[i]["window"]);
				var $window = _self.windows[i]["window"];
			}

			// ウィンドウアクティブイベント
			if(_self.modules[moduleId.toString()].onActiveEvents != null){
				for(var i=0; i < _self.modules[moduleId.toString()].onActiveEvents.length; i++){
					_self.modules[moduleId.toString()].onActiveEvents[i](_self.eventParams($window));
				}
			}

			// 画面が選択される場合があるので、選択を解除する
			window.getSelection().empty();
		}


		// ウィンドウの生成
		this._createWindow=function(module){
			var _self = this;
			var $window = null;

			if(module.isWindowOpen==null || module.isWindowOpen==true){

				$window = _self.platform.createWindow(_self.windowIndex, module);
				_self.platform.activeWindow($window);
				_self.windows.push({moduleId:module.id, window:$window});
				$window.mousedown(function(){
					_self.windowIndex++;
					$(this).css({"z-index":_self.windowIndex});
					_self.platform.activeWindow($(this));
				});

				// viewのセット
				$window.children(".frame").html(module.view);

				// リサイズイベントのバインド
				$window.resizeEvent=function(){
					if(_self.modules[module.id.toString()].onResizeEvents != null){
						for(var i=0; i < _self.modules[module.id.toString()].onResizeEvents.length; i++){
							_self.modules[module.id.toString()].onResizeEvents[i](_self.eventParams($window));
						}
					}
				};
			}

			// ウィンドウクリエイトイベントの実行
			if(_self.modules[module.id.toString()].onCreateEvents != null){
				for(var i=0; i < _self.modules[module.id.toString()].onCreateEvents.length; i++){
					_self.modules[module.id.toString()].onCreateEvents[i](_self.eventParams($window));
				}
			}

			return $window;
		}

		//  イベント発生時のパラメータを生成する
		this.eventParams=function($window){
			var _self = this;
			var params = {};
			params["desktop"] = _self;
			if($window != null && $window.length > 0){
				params["window"] = $window;
				params["titlebar"] = $window.children(".titlebar");
				params["view"] = $window.children(".frame");
				params["mainWindow"] = _self;		// deprecation
				params["home"] = _self;
			}
			return params;
		}

		return this;
	}

	// モジュールの終了
	this.exitModule=function(moduleId){
		var _self = this;
		for(var i=0; i<_self.windows.length; i++){
			if(_self.windows[i]["moduleId"] == moduleId){
				_self.windows.splice(i, 1);
//				_self.windows[i]["window"].remove();
			}
		}
		_self.platform.exetModule(moduleId);
	}
});

function to_px(num){
	return num + "px";
}

function to_num(px){
	return parseInt(px);
}

