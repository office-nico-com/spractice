var SiePlatformWin=function($body){

	this.$body = $body;
	this.$window = $('<div id="main-window" class="main-window"></div>').appendTo($body);
	this.$taskbar = $('<div id="taskbar" class="taskbar"></div>').appendTo(this.$window);
	this.$taskbarBg = $('<div id="taskbar-bg" class="taskbar-bg"></div>').appendTo(this.$taskbar);
	this.$start = $('<div id="start-button" class="start-button"><i class="fa fa-windows" aria-hidden="true"></i></div>').appendTo(this.$taskbar);

	this.cortana = $('<div id="cortana" class="cortana"><i class="fa fa-circle-o" aria-hidden="true"></i></div>').appendTo(this.$taskbar);
	this.searchBox = $('<input type="text" id="search-box" class="search-box" placeholder="ここに入力して検索"/>').appendTo(this.$taskbar);
	this.microphone = $('<div id="microphone" class="microphone"><i class="fa fa-microphone" aria-hidden="true"></i></div>').appendTo(this.$taskbar);
	this.tasktray = $('<div id="tasktray" class="tasktray"></div>').appendTo(this.$taskbar);



	// タスクトレイにアイコン追加
	this.addTaskTray=function($obj){
		var left = -15;
		for(var i=0; i<this.tasktray.children().length; i++){
			left -= this.tasktray.children().eq(i).width();
		}
		var ret = $obj.appendTo(this.tasktray);
		left += (this.tasktray.width() - ret.width());
		ret.css({left:to_px(left)});
		return ret;
	}

	this.tasktrayNotify = this.addTaskTray($('<div class="task-icon"><i class="fa fa-stop" aria-hidden="true"></i></div>'));
	this.tasktrayClock = this.addTaskTray($('<div class="task-icon clock">15:31<br/>2018/11/22</div>'));
	this.clockUpdate=function(){
		var _self = this;
		var current = new Date();
		var year = current.getFullYear();
		var month = current.getMonth()+1;
		var day = current.getDate();
		var hour = current.getHours();
		var minute = current.getMinutes();
		_self.tasktrayClock.html(+ hour + ":" + (('00' + minute).slice(-2)) + "<br/>" + year + "/" + month + "/" + day);
		setTimeout(function(){
			_self.clockUpdate();
		}, 1000 * 20);
	}
	this.clockUpdate();
	this.tasktrayIEM = this.addTaskTray($('<div class="task-icon">A</div>'));



	this.iconCount = 0;

	// Windowのリサイズ *
	this.resizeWindow=function(){
		var _self = this;
		var windowWidth = window.innerWidth;
		var windowHeight = window.innerHeight;
		_self.$taskbar.css('top', to_px(windowHeight - _self.$taskbar.height()))
		_self.alignmentIcon();
		_self.tasktray.css("left", to_px(windowWidth - _self.tasktray.width()));
	}

	// アイコンの追加 *
	this.addIcon=function(title, icon, module_id){
		var _self = this;
		$icon = $('<div class="icon icon-module-id-' + module_id + '"></div>').appendTo(_self.$body);
		$iconBg = $('<div class="icon-bg"></div>').appendTo($icon);
		$image = $('<div class="image"></div>').appendTo($icon);
		$title = $('<div class="title">' + title + '</div>').appendTo($icon);
		$image.css('background-image', "url(" + icon + ")");

		$icon.hover(
			function(){
				$(this).children('.icon-bg').addClass("hover");
			},
			function(){
				$(this).children('.icon-bg').removeClass("hover");
			}
		);

		this.alignmentIcon();
		return $icon;
	}

	// アイコン位置の設定
	this.alignmentIcon=function(){
		var _self = this;
		var margin = 0;
		var top_margin = 10;
		var width = 120;
		var height = 100;
		var display = _self.getDisplaySize();
		var col = 0;
		var row = 0;
		$('.icon').each(function(i, icon) {
			var top = top_margin + (height + margin) * row ;
			if(top + height + margin > display.height){
				row = 0;
				col += 1;
				top = top_margin + (height + margin) * row ;
			}
			var left = (width) * col;
			$(icon).css({top:to_px(top), left:to_px(left), width:to_px(width), height:to_px(height)});
			row++;
		});
	}

	// ウィンドウの作成 *
	this.createWindow=function(windowIndex, module){

		var moduleId = module.id;
		var title = module.titlebar;
		var icon = module.icon;
		var width = module.width;
		var height = module.height;
		var isClosable = module.isClosable;
		var moduleClass = module.moduleClass;
		var delayedStart = module.delayedStart;

		var _self = this;
		var $window = $('<div class="window ' + moduleClass + '"></div>').appendTo(_self.$window).css({"z-index":windowIndex, display:"none"});

		var css = {};
		if(width != null && width!= ''){
			css['width'] = width;
		}
		if(height != null && height!= ''){
			css['height'] = height;
		}
		$window.css(css);
		$window.data('sie-module-id', moduleId);

		// タイトルバー
		$titlebar = $('<div class="titlebar"></div>').appendTo($window);
		var width = $window.width();
		$titlebar.css({width:width});

		$('<img src="' + icon + '"/>').appendTo($titlebar);
		$('<span>' + title + '</span>').appendTo($titlebar);
		var $close = $('<div class="button warn" data-module-id="' + moduleId + '"><i class="fa fa-times" aria-hidden="true"></i></div>').appendTo($titlebar);
		$('<div class="button"><i class="fa fa-window-maximize" aria-hidden="true"></i></div>').appendTo($titlebar);
		// <i class="fa fa-window-restore" aria-hidden="true"></i>
		var $min =  $('<div class="button"><i class="fa fa-window-minimize" aria-hidden="true"></i></i></div>').appendTo($titlebar);
		$close.click(function(){
			 var moduleId = $(this).data("module-id");
			if(isClosable == null || isClosable==true){
				$body.exitModule(parseInt(moduleId));
			}
		});

		// 最小化処理
		$min.click(function(){

			$window.data("last-top", $window.css("top"));
			$window.data("last-left", $window.css("left"));
			$window.data("last-width", $window.css("width"));
			$window.data("last-heigth", $window.css("height"));

			var winHeight = window.innerHeight;
			var top = winHeight - 400;
			$frame.children().hide();
			$window.animate({
				top:to_px(top),
				opacity:'0.0',
				width:'300px'
			}, 
			200, 
			function(){
				$window.hide()
			});
		});

		var top = $titlebar.height();
		var height = $window.height() - $titlebar.height();
		$frame = $('<div class="frame"></div>').appendTo($window).css({top:top, height:height, width:width});

		var offset = {};

		offset.left=0;
		offset.top=0;

		$window
			.draggable({
				cancel:".frame,.resize",
				start:function(e, ui){
					var $frame = ui.helper.children('.frame');
//					$frame.children().hide();
				},
				drag: function(e, ui){
					var move = true;
					var display = _self.getDisplaySize();
					var left = to_num($(this).css("left"));
					offset.left = ui.offset.left;

				},
				stop:function(e, ui){

					var horizontalMargin = 100;
					var verticalMargin = 30

					// 画面内から消える場合は戻す
					// 本当はドラッグを止めたいが、わからない

					var display = _self.getDisplaySize();
					var move = false;
					var left = ui.offset.left;
					var top = ui.offset.top;
					var width = ui.helper.width();
					if(ui.offset.left > display.width - horizontalMargin){
						left = display.width - horizontalMargin;
						move = true;
					}
					else if(-(width - horizontalMargin) > ui.offset.left){
						left = -(width - horizontalMargin);
						move = true;
					}

					if(ui.offset.top < verticalMargin){
						top = verticalMargin;
						move = true;
					}
					else if(ui.offset.top > display.height - verticalMargin){
						top = display.height - verticalMargin;
						move = true;
					}

					if(move){
						ui.helper.animate({ 
										top: to_px(top),
										left: to_px(left)
									}, 200);
					}

					var $frame = ui.helper.children('.frame');
//					$frame.children().show();

				}
			})
			.resizable({
					handles:"all",
					start:function(e, ui){
						var $frame = ui.helper.children('.frame');
						$frame.children().hide();
					},
					resize:function(e, ui){
						var width = ui.helper.width();
						var height = ui.helper.height();
						var $titlebar = ui.helper.children('.titlebar');
						var $frame = ui.helper.children('.frame');

						$titlebar.css({width:to_px(width)});
						$frame.css({width:to_px(width), height:to_px(height - $titlebar.height())});

					},
					stop:function(e, ui){
						var $frame = ui.helper.children('.frame');
						$frame.children().show();
						if($window.resizeEvent != null){
							$window.resizeEvent();
						}
					}
				});
		// 中央に配置

		var random1 =  Math.floor(Math.random() * 11) * 10;
		var random2 = Math.floor(Math.random() * 11) * 10;

		var display = _self.getDisplaySize();
		var left = (display.width - $window.width()) / 2 - 100 + random1;
		if(left < 0){
			left = random1;
		}
		var top = (display.height - $window.height()) / 2 -100 + random2;
		if(top < 0){
			top = random2;
		}

		if(module.top != null){
			top = module.top;
		}
		if(module.left != null){
			left = module.left;
		}
		else if(module.right != null){
			left = display.width - $window.width() - module.right;
		}


		// 透過表示させて、遅延分時間をずらしてopacityを1にする
		// （普通に遅延させるとonCreateEventでサイズの取得ができないくなるため）
		if(delayedStart == null){
			delayedStart = 0;
		}
		$window.css({left:to_px(left), top:to_px(top), position:"absolute", opacity:0}).show();
		$window.delay(delayedStart).animate({opacity:1}, 800);

		// タスクバーに追加
		_self.addTaskbar(moduleId, icon);

		return $window;
	}

	// タスバーにタスクの追加
	this.addTaskbar=function(moduleId, icon){
		var _self = this;

		var left = 30;
		for(var i=0;i < _self.$taskbar.children().length; i++){
			if(!_self.$taskbar.children().eq(i).hasClass('taskbar-bg') && !_self.$taskbar.children().eq(i).hasClass('tasktray')){
				left += to_num(_self.$taskbar.children().eq(i).css('width')) + 1;
			}
		}

		_self.$taskbar.children('.task').removeClass('active');

		var taskLineHeight = 3;
		$task = $('<div class="task active"></div>').appendTo(_self.$taskbar).css({left:to_px(left), 'background-image':'url(' + icon + ')'});
		$task.data('sie-module-id', moduleId);
		$('<div class="line"></div>').appendTo($task).css({'top':to_px(_self.$taskbar.height() - taskLineHeight), 'height': to_px(taskLineHeight)});

		// タスクバーを押された場合のウィンドウのアクティブ化
		$task.click(function(){


			var moduleId = $(this).data('sie-module-id');
			var $windows = _self.$body.find('.window');
			var targetWindows = new Array();
			var minIndex = null;
			for(var i=0; i<$windows.length; i++){
				if($windows.eq(i).data('sie-module-id') == moduleId){
					targetWindows.push($windows.eq(i));
					if(minIndex == null || minIndex > parseInt($windows.eq(i).css("z-index"))){
						minIndex = parseInt($windows.eq(i).css("z-index"));
					}
				}
			}

			var maxIndex = null;
			for(var i=0; i<targetWindows.length; i++){
				var _index = parseInt(targetWindows[i].css("z-index")) - minIndex;
				var index = _self.$body.windowIndex + _index + 1;
				if(targetWindows[i].is(':hidden')){
					var _target = targetWindows[i];
					_target.show();
					_target.animate({
							opacity:'1.0',
							top:_target.data("last-top"),
							left:_target.data("last-left"),
							width:_target.data("last-width"),
							height:_target.data("last-height")
						}, 
						200,
						function(){
							_target.find(".frame").children().show();
						}
					);
				}

				targetWindows[i].css("z-index", index);
				if(maxIndex== null || maxIndex < index){
					maxIndex = index;
				}
			}
			_self.$body.windowIndex = maxIndex;

			var $tasks = _self.$taskbar.children('.task')
			$tasks.removeClass('active');
			$(this).addClass('active');

		});
	}


	// ウィンドウのアクティブ化
	this.activeWindow=function($window){
		var _self = this;
		$(".window.active").removeClass("active");
		$window.addClass("active");

		// タスクバーのアクティブ化
		var moduleId = $window.data('sie-module-id');
		var $tasks = _self.$taskbar.children('.task')
		$tasks.removeClass('active');
		for(var i=0; i<$tasks.length; i++){
			if($tasks.eq(i).data('sie-module-id') == moduleId){
				$tasks.eq(i).addClass('active');
			}
		}
	}


	// モジュールの非表示
	this.hideWindow=function(moduleId){
		var _self = this;
		var $windows = _self.$body.find('.window');
		var topzIndex = 0;
		var topModuleId = null;
		// ウィンドウの削除
		for(var i=0; i<$windows.length; i++){
			if($windows.eq(i).data('sie-module-id') == moduleId){
				$windows.eq(i).hide();;
			}
		}
	}

	// モジュールの再表示
	this.showWindow=function(moduleId){
		var _self = this;
		var $windows = _self.$body.find('.window');
		var topzIndex = 0;
		var topModuleId = null;
		// ウィンドウの削除
		for(var i=0; i<$windows.length; i++){
			if($windows.eq(i).data('sie-module-id') == moduleId){
				$windows.eq(i).show();
			}
		}
	}


	// モジュールの終了
	this.exetModule=function(moduleId){
		var _self = this;
		var $windows = _self.$body.find('.window');
		var topzIndex = 0;
		var topModuleId = null;
		// ウィンドウの削除
		for(var i=0; i<$windows.length; i++){
			if($windows.eq(i).data('sie-module-id') == moduleId){
				$windows.eq(i).remove();
			}
			else{
				if(topzIndex < parseInt($windows.eq(i).css("z-index"))){
					topzIndex = parseInt($windows.eq(i).css("z-index"));
					topModuleId = $windows.eq(i).data('sie-module-id');
				}
			}
		}
		// タスクバーから削除
		var $tasks = _self.$taskbar.children('.task');
		$tasks.removeClass('active');
		for(var i=0; i<$tasks.length; i++){
			if($tasks.eq(i).data('sie-module-id') == moduleId){
				$tasks.eq(i).remove();
			}
			else if($tasks.eq(i).data('sie-module-id') == topModuleId){
				$tasks.eq(i).addClass('active');
			}
		}
		// タスバーの位置を補正
		var left = 30;
		var $tasks = _self.$taskbar.children();
		for(var i=0;i < $tasks.length; i++){
			if(!$tasks.eq(i).hasClass('taskbar-bg') && !$tasks.eq(i).hasClass('tasktray')){
				if($tasks.eq(i).hasClass('task')){
					$tasks.eq(i).animate({'left':to_px(left)}, 300);
				}
				left += to_num($tasks.eq(i).css('width')) + 1;
			}
		}
	}


	// 描画領域のサイズ取得
	this.getDisplaySize=function(){
		// タスクバーの領域を除く
		var width = window.innerWidth;
		var height = window.innerHeight - this.$taskbar.height();
		return {width:width, height:height};
	}


	this.resizeWindow();

}


