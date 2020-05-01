var SieApiGuide=function($_body, _themeNo, _logger){

	this.$body = $_body;
	this.isExec=false;
	this.scenarioWindow=null;
	this.scenarioWindowInner=null;
	this.scenarioWindowClose=null
	this.scenarios = {};
	this.currentScriptId = 0;
	this.wait = 15;
	this.newLineWait = 20;
	this.controlWait = 30;
	this.defaultHeight=300;
	this.currentHeight=300;
	this.close=false;
	this.windowAnimate=350;
	this.themeNo = _themeNo;
	this.logger = _logger;

	// シナリオのセット
	this.setMessage=function(scenarioId, message, controls, option){
		if(option == null){
			option = {};
		}
		this.scenarios[scenarioId] = {	message:message, 
										controls:controls, 
										beforeAction:option["beforeAction"], 
										beforeActionDelay:option["beforeActionDelay"], 
										afterAction:option["afterAction"], 
										afterActionDelay:option["afterActionDelay"], 
										delay:option["delay"], 
										height:option["height"], 
										width:option["width"]}
	}

	// シナリオの実行
	this.exec=function(scenarioId){
		var _self = this;
		if(!_self.isExec){
			// ウィンドウの描画
			_self.drawWindow();
			// シナリオの実行
			_self.start(scenarioId);
		}
		this.isExec=true;
	}

	// シナリオの中断
	this.stop=function(){
		var _self = this;
		if(_self.isExec){

		}
		this.isExec=false;
	}

	// ウィンドウの描画
	this.drawWindow=function(){
		var _self = this;
		
		var margin = 50;
		var windowHeight = 300;
		var windowWidth = $(window).width() - (margin * 2);

		_self.scenarioWindow=$('<div class="sie-api-scenario-window"></div>').prependTo(_self.$body);
		_self.scenarioWindowInner=$('<div class="sie-api-scenario-window-inner"></div>').prependTo(_self.$body);
		_self.scenarioWindowClose=$('<div class="sie-api-scenario-window-close">×ガイダンスを10秒閉じる</div>').prependTo(_self.$body);

		if(_self.scenarios != null && _self.scenarios.length > 1){
		}

		_self.resizeWindow();
		$(window).resize(function() {
			_self.resizeWindow();
		});


		_self.scenarioWindowClose.click(function(){
			if(!_self.close){
				_self.close=true;

				// ウィンドウの最小化
				_self.scenarioWindowInner.hide();
				_self.scenarioWindow.animate({
					width:to_px(200), 
					height:to_px(50),
					top:to_px(46),
					left:to_px($(window).width() - 200 - 55)
				}, _self.windowAnimate);

				_self.scenarioWindowClose.text("∇ガイダンスを開く").animate({
					top:to_px(60),
					left:to_px($(window).width() - 180 - 50)
				}, _self.windowAnimate);

				setTimeout(function(){
					if(_self.close){
						_self.windowOpen();
					}
				}, 10000);

			}
			else{
				_self.windowOpen();
			}
			
		});
	}
	
	// ウィンドウを開く
	this.windowOpen=function(){
		var _self = this;
		_self.close=false;
                                         
		_self.scenarioWindowClose.text("×ガイダンスを10秒閉じる");
		_self.resizeWindow(true);

		setTimeout(function(){
			_self.scenarioWindowInner.show();
		}, _self.windowAnimate);
	}

	// ウィンドウのリサイズ
	this.resizeWindow=function(animate){

		var _self = this;
		if(!_self.close){
			var marginLeft = 150;
			var marginBottom = 80;
			var windowHeight = _self.currentHeight;
			if(_self.currentHeight == 'full'){
				windowHeight = $(window).height() - 150;
			}
			var windowWidth = $(window).width() - (marginLeft * 2);
			var border = 2;
			var padding = 4;

			if(animate == null || animate == false){
				_self.scenarioWindow.css({
											width: to_px(windowWidth), 
											height: to_px(windowHeight),
											left: to_px(marginLeft),
											top: to_px($(window).height() - windowHeight - marginBottom)
									});

				_self.scenarioWindowInner.css({
											width: to_px(windowWidth - (padding * 2) ), 
											height: to_px(windowHeight - (padding * 2)),
											left: to_px(marginLeft + padding),
											top: to_px($(window).height() - windowHeight - marginBottom + padding)
										});

				var closeWidth = _self.scenarioWindowClose.width();
				_self.scenarioWindowClose.css({
											left:to_px(windowWidth + marginLeft - closeWidth), 
											top: to_px($(window).height() - windowHeight - marginBottom - 20)
										})
			}
			else{
				_self.scenarioWindow.animate({
											width: to_px(windowWidth), 
											height: to_px(windowHeight),
											left: to_px(marginLeft),
											top: to_px($(window).height() - windowHeight - marginBottom)
									}, _self.windowAnimate);

				_self.scenarioWindowInner.animate({
											width: to_px(windowWidth - (padding * 2) ), 
											height: to_px(windowHeight - (padding * 2)),
											left: to_px(marginLeft + padding),
											top: to_px($(window).height() - windowHeight - marginBottom + padding)
										}, _self.windowAnimate);

				var closeWidth = _self.scenarioWindowClose.width();
				_self.scenarioWindowClose.animate({
											left:to_px(windowWidth + marginLeft - closeWidth), 
											top: to_px($(window).height() - windowHeight - marginBottom - 20)
										}, _self.windowAnimate)
			}
		}
		else{
				_self.scenarioWindow.css({
					left:to_px($(window).width() - 260 - 55)
				});

				_self.scenarioWindowClose.css({
					left:to_px($(window).width() - 240 - 50)
				});
		}
	}

	// ウィンドウの高さ変更
	this.resizeWindowHeight=function(){
		var _self = this;
		var marginLeft = 150;
		var marginBottom = 80;
		var windowHeight = _self.currentHeight;
		if(_self.currentHeight == 'full'){
			windowHeight = $(window).height() - 150;
		}
		var windowWidth = $(window).width() - (marginLeft * 2);
		var border = 2;
		var padding = 4;

		_self.scenarioWindow.animate({
									height: to_px(windowHeight),
									top: to_px($(window).height() - windowHeight - marginBottom)
								}, _self.windowAnimate);

		_self.scenarioWindowInner.animate({
									height: to_px(windowHeight - (padding * 2)),
									top: to_px($(window).height() - windowHeight - marginBottom + padding)
								}, _self.windowAnimate);

		var closeWidth = _self.scenarioWindowClose.width();
		_self.scenarioWindowClose.animate({
									left:to_px(windowWidth + marginLeft - closeWidth), 
									top: to_px($(window).height() - windowHeight - marginBottom - 20)
								}, _self.windowAnimate)

	}

	// 文字列の描画
	this.start=function(scenarioId){
		var _self = this;
		var scenario = this.scenarios[scenarioId];
		if(scenario == null){
			alert("シナリオが設定されていません。");
			return;
		}
		_self.setText(scenarioId, scenario);
	}

	// シナリオ遷移
	this.next=function(id){
		var _self = this;
		var scenario = this.scenarios[id];
		if(scenario == null){
			alert("シナリオが設定されていません。");
			return;
		}
		_self.setText(id, scenario);
	}

	// ウィンドウの削除
	this.destroyWindow=function(){
		var _self = this;
		_self.scenarioWindow.remove();
		_self.scenarioWindow = null;
	}

	// 文字の設定
	this.setText=function(scenarioId, scenario){
		var _self = this;

		// ログの出力
		if(_self.logger != null){
			_self.logger.put(_self.themeNo, scenarioId);
		}

		// キーを生成してインスタンスに保持
		// 同時に描画が走った場合でも、キーが一致していない場合は描画しない
		_self.currentScriptId = scriptId = this.getRand();

		// 描画文字列をクリア
		_self.scenarioWindowInner.html('');

		// ウィンドウの高さを設定
		if(scenario['height'] != null){
			_self.currentHeight = scenario['height'];
		}
		else{
			_self.currentHeight = _self.defaultHeight;
		}
		_self.resizeWindowHeight();

		// beforeActionスクリプトの実行

		if(scenario['beforeAction'] != null){
			if(scenario['beforeActionDelay'] == null){
				scenario['beforeActionDelay'] = 10;
			}

			var action = function(_scriptId){
				setTimeout(function(){
					if(_scriptId == _self.currentScriptId){
						scenario['beforeAction']();
					}
				}, scenario['beforeActionDelay']);
			};
			action(scriptId);
		}

		// テキストの描画
		if(scenario['delay'] != null){
			_self.hide();
			// 描画待ちあり
			var action = function(_scriptId){
				setTimeout(function(){
					_self.show();
					_self._setText(scenarioId, scenario, _scriptId);
				}, scenario['delay']);
			};
			action(scriptId);
		}
		else{
			this._setText(scenarioId, scenario, scriptId);
		}
	}


	this._setText=function(scenarioId, scenario, scriptId){

		var _self = this;
		var list = this.toList(scenario['message']);
		
		var append = true;
		var str = "";
		var count = 1;
		var gt_count = 0;
		var isLast=false;
		for(var i=0; i<list.length; i++){

			if(i == list.length - 1){
				isLast = true;
			}

			if(list[i] == "<" || append == false){
				str += list[i];
				append = false;
				if(list[i] == '>'){
					gt_count++;
				}
				if(gt_count == 2){
					append = true;
				}
			}
			else{
				str = list[i];
			}

			if(append){


				if(str == '\n'){
					count+=_self.newLineWait;
				}

				this.appendChr(str, count, scriptId, isLast, scenario['afterAction'], scenario['afterActionDelay']);
				count++;
				append = true;
				str = "";
				gt_count=0;
			}
		}

		var j = count + _self.controlWait;
		if(scenario['controls'] != null && scenario['controls'].length > 0){
			var controls = [];
			for(var i=0; i < scenario['controls'].length; i++){
				if(scenario['controls'][i]['next'] != null && scenario['controls'][i]['next'] != ''){

					if(scenario['controls'][i]['next'] == 'url' && scenario['controls'][i]['url'] != null){
						// URLの表示
						//var open='0'; TODO:
						//if(scenario['controls'][i]['open'] != null && (scenario['controls'][i]['open'] == '1' || scenario['controls'][i]['open'] == '2')){
						//	open = scenario['controls'][i]['open'];
						//}
						var html = '<a href="' + scenario['controls'][i]['url'] + '" ';
						if(scenario['controls'][i]['openWindow'] != null && scenario['controls'][i]['openWindow']==true){
							html += "target='_blank'";
						}
						html += '">' + scenario['controls'][i]['label'] + '</a>';


						// $a = $('<a href="' + scenario['controls'][i]['url'] + '">' + scenario['controls'][i]['label'] + '</a>');
						var $a = $(html);
						controls.push($a);
					}
					else if(scenario['controls'][i]['next'] == 'script' && scenario['controls'][i]['script'] != null){
						var $a = $('<a href="javascript:void(0);">' + scenario['controls'][i]['label'] + '</a>');
						var index = i;
						var _script = scenario['controls'][i]['script'];
						// $a.click(function(){_script()});

						var action = function($_a, __script){
							$_a.click(function(){__script()});
						}
						action($a, _script);

						controls.push($a);
					}
					else {
						// 次のシナリオへの遷移
						var $a = $('<a href="javascript:void(0);">' + scenario['controls'][i]['label'] + '</a>');
						var index = i;
						$a.click(function(){_self.next(scenario['controls'][index]['next'])});
						controls.push($a);
					}
				}
			}
			_self.addControl(controls, j, scriptId);
		}

	}

	// 文字の追加
	this.appendChr=function(chr, count, scriptId, isLast, afterAction, afterActionDelay){

		var _self = this;
		var chr = _self.escape(chr);
		if(chr != null){
			setTimeout(function(){
				if(scriptId == _self.currentScriptId){
					_self.scenarioWindowInner.append('<span>' + chr + '</span>');
					_self.scenarioWindowInner.scrollTop(_self.scenarioWindowInner.height());
				}

				if(isLast){
					if(afterAction != null){
						if(afterActionDelay == null){
							afterActionDelay = 100;
						}
						setTimeout(function(){
							if(scriptId == _self.currentScriptId){
								afterAction();
							}
						}, afterActionDelay);
					}
				}
			}, count * _self.wait);
		}
	}

	// コントロールの追加
	this.addControl=function(controls, count, scriptId){
		var _self = this;
		setTimeout(function(){
			if(scriptId == _self.currentScriptId){
//				_self.scenarioWindowInner.append(control);
				var $control = $('<div class="control"></div>').appendTo(_self.scenarioWindowInner);
				for(var i=0; i<controls.length; i++){
					$span = $('<span></span>')
					$span.append(controls[i]);
					$control.append($span);
				}
				_self.scenarioWindowInner.scrollTop(_self.scenarioWindowInner.height() + 10000);
			}
		}, count * _self.wait);
	}

	// 文字のエスケープ
	this.escape=function(str){

		if(str != null){
			// 改行コードを/nに統一
			str = str.replace(/\r\n/g, "\n");
			str = str.replace(/\r/g, "");
			// BRに変換
			str = str.replace(/\n/g, "<br/>");
		}
		return str;
	}

	this.toList=function(str){
		return str.match(/[\uD800-\uDBFF][\uDC00-\uDFFF]|[^\uD800-\uDFFF]/g) || []
	}

	this.getRand=function(){
		var min = 10000 ;
		var max = 99999 ;
		var a = Math.floor( Math.random() * (max + 1 - min) ) + min ;
		return a;
	}

	this.delay=function(time){
		var _self = this;
		this.hide();
		setTimeout(function(){
			_self.show();
		}, time);
	}


	this.hide=function(){
		if(this.scenarioWindow != null){
			this.scenarioWindow.hide();
			this.scenarioWindowInner.hide();
			this.scenarioWindowClose.hide();
		}
	}

	this.show=function(){
		this.scenarioWindow.show();
		this.scenarioWindowInner.show();
		this.scenarioWindowClose.show();
	}

}

