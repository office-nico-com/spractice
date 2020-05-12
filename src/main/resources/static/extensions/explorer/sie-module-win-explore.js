// エクスプローラーモジュール
var SieModuleWinExplore = function(moduleId){
	this.id = moduleId;
	this.title = "Explore"
	this.titlebar = "Explore";
	this.icon = "sie/img/icon-explorer.png"
	this.isShowDeskTop=true;
	this.isWindowOpen=true;
	this.isClosable=true;
	this.moduleClass="sie-module-win-explore";
	this.delayedStart = 0;

	this.view = '<div class="sie-module-win-explore-body">';
	this.view += '<div class="exp-header">';
	this.view += '<div class="exp-menu exp-tab2">';

	this.view += '<div class="exp-menu-block exp-pin">クイックアクセス<br/>にピン留めする</div>';
	this.view += '<div class="exp-menu-block exp-copy">コピー</div>';
	this.view += '<div class="exp-menu-block exp-paste">貼り付け</div>';
	this.view += '<div class="exp-menu-block exp-cut">切り取り</div>';
	this.view += '<div class="exp-menu-block exp-pathcopy">パスのコピー</div>';
	this.view += '<div class="exp-menu-block exp-shortcut">ショートカットの貼り付け</div>';
	this.view += '<div class="exp-menu-block exp-header-group-label" style="left:0px;width:355px">クリップボード</div>';
	this.view += '<div class="exp-menu-block exp-delim" style="left:355px;"></div>';

	this.view += '<div class="exp-menu-block exp-moveto">移動先<br><span>▼</span></div>';
	this.view += '<div class="exp-menu-block exp-copyto">コピー先<br><span>▼</span></div>';
	this.view += '<div class="exp-menu-block exp-header-group-label" style="left:356px;width:200px">整理</div>';
	this.view += '<div class="exp-menu-block exp-delim" style="left:460px;top:20px;height:55px;"></div>';

	this.view += '<div class="exp-menu-block exp-delete">削除<br><span>▼</span></div>';
	this.view += '<div class="exp-menu-block exp-rename">名前の<br>変更</div>';
	this.view += '<div class="exp-menu-block exp-header-group-label" style="left:557px;width:185px">新規</div>';
	this.view += '<div class="exp-menu-block exp-delim" style="left:555px;"></div>';

	this.view += '<div class="exp-menu-block exp-newfolder">新しい<br>フォルダー</div>';
	this.view += '<div class="exp-menu-block exp-newitem">新しいアイテム<span>▼</span></div>';
	this.view += '<div class="exp-menu-block exp-newshortcut">ショートカット<span>▼</span></div>';
	this.view += '<div class="exp-menu-block exp-delim" style="left:740px;"></div>';
	this.view += '<div class="exp-menu-block exp-property">プロパティ<br><span>▼</span></div>';
	this.view += '<div class="exp-menu-block exp-open">開く<span>▼</span></div>';
	this.view += '<div class="exp-menu-block exp-edit">編集</div>';
	this.view += '<div class="exp-menu-block exp-history">履歴</div>';
	this.view += '<div class="exp-menu-block exp-header-group-label" style="left:743px;width:129px">開く</div>';
	this.view += '<div class="exp-menu-block exp-delim" style="left:870px;"></div>';

	this.view += '<div class="exp-menu-block exp-selectall">すべて選択</div>';
	this.view += '<div class="exp-menu-block exp-selectcancel">選択解除</div>';
	this.view += '<div class="exp-menu-block exp-selectchange">選択の切り替え</div>';
	this.view += '<div class="exp-menu-block exp-header-group-label" style="left:873px;width:112px">選択</div>';
	this.view += '</div>';


	this.view += '<div class="exp-menu exp-tab3" style="display:none;">';
	this.view += '<div class="exp-menu-block exp-share">共有</div>';
	this.view += '<div class="exp-menu-block exp-email">電子<br>メール</div>';
	this.view += '<div class="exp-menu-block exp-zip">Zip</div>';
	this.view += '<div class="exp-menu-block exp-write">ディスクに書き込む</div>';
	this.view += '<div class="exp-menu-block exp-print">印刷</div>';
	this.view += '<div class="exp-menu-block exp-fax">FAX</div>';
	this.view += '<div class="exp-menu-block exp-header-group-label" style="left:0px;width:285px">送信</div>';

	this.view += '<div class="exp-menu-block exp-delim" style="left:285px;"></div>';
	this.view += '<div class="exp-menu-block exp-access">特定のユーザー</div>';
	this.view += '<div class="exp-menu-block exp-lock">アクセスを<br/>削除する</div>';
	this.view += '<div class="exp-menu-block exp-delim" style="left:490px;"></div>';
	this.view += '<div class="exp-menu-block exp-securite">セキュリティの<br/>詳細</div>';
	this.view += '<div class="exp-menu-block exp-header-group-label" style="left:286px;width:205px">共有</div>';
	this.view += '<div class="exp-menu-block exp-delim" style="left:585px;"></div>';
	this.view += '</div>';


	this.view += '<div class="exp-menu exp-tab4" style="display:none;">';
	this.view += '<div class="exp-menu-block exp-navi">ナビケーション<br/>ウィンドウ<span>▼</span></div>';
	this.view += '<div class="exp-menu-block exp-preview">プレビューウィンドウ</div>';
	this.view += '<div class="exp-menu-block exp-detail">詳細ウィンドウ</div>';
	this.view += '<div class="exp-menu-block exp-header-group-label" style="left:0px;width:230px">ペイン</div>';
	this.view += '<div class="exp-menu-block exp-delim" style="left:230px;"></div>';

	this.view += '<div class="exp-menu-block exp-layoutbg"></div>';
	this.view += '<div class="exp-menu-block exp-layout-1">特大アイコン</div>';
	this.view += '<div class="exp-menu-block exp-layout-2">小アイコン</div>';
	this.view += '<div class="exp-menu-block exp-layout-3">並べて表示</div>';
	this.view += '<div class="exp-menu-block exp-layout-4">大アイコン</div>';
	this.view += '<div class="exp-menu-block exp-layout-5">一覧</div>';
	this.view += '<div class="exp-menu-block exp-layout-6">コンテンツ</div>';
	this.view += '<div class="exp-menu-block exp-layout-7">中アイコン</div>';
	this.view += '<div class="exp-menu-block exp-layout-8 exp-layout-selected">詳細</div>';
	this.view += '<div class="exp-menu-block exp-header-group-label" style="left:231px;width:305px">レイアウト</div>';
	this.view += '<div class="exp-menu-block exp-delim" style="left:535px;"></div>';



	this.view += '<div class="exp-menu-block exp-sort">並べ替え<br/><span>▼</span></div>';
	this.view += '<div class="exp-menu-block exp-group">グループ化▼</div>';
	this.view += '<div class="exp-menu-block exp-row">列の追加▼</div>';
	this.view += '<div class="exp-menu-block exp-autoresize">すべての列のサイズを自動的に変更する</div>';
	this.view += '<div class="exp-menu-block exp-header-group-label" style="left:535px;width:285px">現在のビュー</div>';
	this.view += '<div class="exp-menu-block exp-delim" style="left:820px;"></div>';

	this.view += '<div class="exp-menu-block exp-view1"><label><input type="checkbox" name="view1" value="1" checked>項目チェックボックス</label></div>';
	this.view += '<div class="exp-menu-block exp-view2"><label><input type="checkbox" name="view2" value="1" class="show-extension">ファイル名拡張子</label></div>';
	this.view += '<div class="exp-menu-block exp-view3"><label><input type="checkbox" name="view3" value="1">隠しファイル</label></div>';
	this.view += '<div class="exp-menu-block exp-hide">選択した項目を<br/>表示しない</div>';
	this.view += '<div class="exp-menu-block exp-header-group-label" style="left:820px;width:235px">表示/非表示</div>';
	this.view += '<div class="exp-menu-block exp-delim" style="left:1055px;"></div>';

	this.view += '<div class="exp-menu-block exp-option">オプション</div>';
	this.view += '</div>';



	this.view += '<div class="tab-group">';
	this.view += '<div class="tab tab1">ﾌｧｲﾙ</div>';
	this.view += '<div class="tab tab2 selected">ﾎｰﾑ</div>';
	this.view += '<div class="tab tab3">共有</div>';
	this.view += '<div class="tab tab4">表示</div>';
	this.view += '</div>';
	this.view += '<div class="body">';
	this.view += '<div class="exp-address">';
	this.view += '<i class="fa fa-arrow-left" aria-hidden="true"></i>';
	this.view += '<i class="fa fa-arrow-right" aria-hidden="true"></i>';
	this.view += '<i class="fa fa-arrow-up" aria-hidden="true"></i>';
	this.view += '<input type="text" class="exp-address-input">';
	this.view += '<input type="text" class="exp-search-input">';
	this.view += '<i class="fa fa-angle-down" aria-hidden="true"></i>';
	this.view += '<i class="fa fa-repeat" aria-hidden="true"></i></i>';
	this.view += '<i class="fa fa-search" aria-hidden="true"></i>';
	this.view += '</div>';
	this.view += '<div class="exp-left"></div>';
	this.view += '<div class="exp-right"></div>';



	this.view += '</div>';
	this.view += '</div>';

	this.width=1000;
	this.height=500;
	this.onCreateEvents=[];
	this.onActiveEvents=[];
	this.onResizeEvents=[];
	this.params=null;
	this.thisWindow=null;
	this.leftWindow=null;
	this.rightWindow=null;
	this.treeItemHeight=24;		// 左側ツリーの1オブジェクトあたりの表示高さ
	this.treeItemTopPadding=10;	// 上部のパディング
	this.detailViewHeight=24;
	this.home=null;	// ホーム画面

	// 描画リソースの定義
/*
	this.resources=[
		{
			id:"100",
			type: "folder",
			name:"Desktop", 
			icon:"sie/img/sie-module-win-explore/icon-pc.png", 
			children:[
				{	id:"100100", 
					type: "file",
					name:"Excel.zip", 
					icon:"sie/img/sie-module-win-explore/icon-word.png",
					click:null,
					dblClick:null,
					children:[]
				},
				{	id:"100200", 
					type: "folder",
					name:"テスト1", 
					icon:"sie/img/sie-module-win-explore/icon-explorer.png",
					children:[
						{	id:"100200100", 
							type: "file",
							name:"Excel2.zip", 
							icon:"sie/img/sie-module-win-explore/icon-word.png",
							click:null,
							dblClick:null,
							children:[]
						}
					]
				},
				{	id:"100300", 
					type: "folder",
					name:"テスト2", 
					icon:"sie/img/sie-module-win-explore/icon-explorer.png",
					children:[
						{	id:"100300100", 
							type: "folder",
							name:"フォルダテスト", 
							icon:"sie/img/sie-module-win-explore/icon-word.png",
							click:null,
							dblClick:null,
							children:[]
						}
					]
				}
			]
		},
		{	id:"200", 
			type: "folder",
			name:"PC", 
			icon:"sie/img/sie-module-win-explore/icon-explorer.png", 
			click:null,
			dblClick:null,
			children:[]
		},
		{	id:"300", 
			type: "folder",
			name:"ネットワーク", 
			icon:"sie/img/sie-module-win-explore/icon-explorer.png", 
			click:null,
			dblClick:null,
			children:[]
		}
	]
*/

	this.resources=[
		{	id:"200", 
			type: "folder",
			name:"PC", 
			icon:"sie/img/sie-module-win-explore/icon-pc.png", 
			click:null,
			dblclick:null,
			selected:true,
			children:[
				{	id:"100100", 
					type: "file",
					name:"Excel.zip", 
					icon:"sie/img/sie-module-win-explore/icon-word.png",
					kind:"圧縮ファイル",
					updated:"2019/01/30 15:17",
					size:"102KB",
					click:null,
					dblclick:function(id){alert("zzzzzzzzzzzz")},
					children:[]
				},
				{	id:"100200", 
					type: "folder",
					name:"テスト1", 
					icon:"sie/img/sie-module-win-explore/icon-explorer.png",
					kind:"ファイルフォルダー",
					updated:"2019/01/31 12:19",
					size:"",
					children:[
						{	id:"100200100", 
							type: "file",
							name:"Excel2.zip", 
							icon:"sie/img/sie-module-win-explore/icon-word.png",
							kind:"Microsoft Excel",
							updated:"2019/01/01 9:21",
							size:"",
							click:null,
							dblclick:null,
							children:[]
						}
					]
				},
				{	id:"100300", 
					type: "folder",
					name:"テスト2", 
					icon:"sie/img/sie-module-win-explore/icon-explorer.png",
					kind:"ファイルフォルダー",
					updated:"2019/01/31 12:19",
					size:"",
					children:[
						{	id:"100300100", 
							type: "folder",	
							name:"フォルダテスト", 
							icon:"sie/img/sie-module-win-explore/icon-word.png",
							kind:"ファイルフォルダー",
							updated:"2019/01/30 15:17",
							size:"12MB",
							click:null,
							dblclick:null,
							children:[]
						}
					]
				}
			]
		},
		{	id:"300", 
			type: "folder",
			name:"ネットワーク", 
			icon:"sie/img/sie-module-win-explore/icon-network.png", 
			kind:"ファイルフォルダー",
			updated:"",
			size:"",
			click:null,
			dblclick:null,
			children:[]
		}
	]

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

		_self.home=params["home"];
		_self.thisWindow=params["window"];
		_self.params = params;

		_self.setObject(params);

		// イベントをバインド
		params["window"].find(".tab2").click(function(){
			params["window"].find(".exp-tab2").show();
			params["window"].find(".exp-tab3").hide();
			params["window"].find(".exp-tab4").hide();
			params["window"].find(".tab2").addClass("selected")
			params["window"].find(".tab3").removeClass("selected")
			params["window"].find(".tab4").removeClass("selected")
		});

		params["window"].find(".tab3").click(function(){
			params["window"].find(".exp-tab2").hide();
			params["window"].find(".exp-tab3").show();
			params["window"].find(".exp-tab4").hide();
			params["window"].find(".tab2").removeClass("selected")
			params["window"].find(".tab3").addClass("selected")
			params["window"].find(".tab4").removeClass("selected")
		});

		params["window"].find(".tab4").click(function(){
			params["window"].find(".exp-tab2").hide();
			params["window"].find(".exp-tab3").hide();
			params["window"].find(".exp-tab4").show();
			params["window"].find(".tab2").removeClass("selected")
			params["window"].find(".tab3").removeClass("selected")
			params["window"].find(".tab4").addClass("selected")
		});

		// リソースの描画
		_self.drawResources();

		// ファイルの拡張子表示判定
		_self.thisWindow.find('.show-extension').click(function(){

			if($(this).prop('checked')){
				_self.thisWindow.find("span.extension").show();
			}
			else{
				_self.thisWindow.find("span.extension").hide();
			}
		});

	})

	this.addOnResizeEvent(function(params){
		_self.setObject(params);
	})

	// オブジェクトの位置をセット
	this.setObject=function(params){

		// 本体部分の表示領域を調整
		var $windowFrame = params["window"].find(".frame");
		var $header = params["window"].find(".header");
		var $body = params["window"].find(".body");
		var height = $windowFrame.height();
		$body.css({height:to_px(height - 125)});

		var $left = $body.find(".exp-left");
		_self.leftWindow = $left;
		var $right = $body.find(".exp-right");
		_self.rightWindow = $right;
		var w = $body.width() - $left.width();

		$right.css({width: to_px(w), height:to_px(height - 120 - 35 - 35)});	//  win高 - ヘッダー高 - アドレスバー高 - フッター部高
		$left.css({height:to_px(height - 120 - 35 - 35)});	//  win高 - ヘッダー高 - アドレスバー高 - フッター部高

		var input_left = 90;
		var input_serach_width = 200;
		var input_delim = 30;
		var right_margin = 10;

		var $address = $body.find(".exp-address-input");
		var $search = $body.find(".exp-search-input");
		var $fa_down = $body.find(".fa-angle-down");
		var $fa_repeat = $body.find(".fa-repeat");
		var $fa_search = $body.find(".fa-search");

		var address_width = $body.width() - input_left - input_serach_width - right_margin - input_delim;
		if(address_width < 400){
			address_width = 400;
		}
		$address.css({width:to_px(address_width)});
		var search_left = address_width + input_left + input_delim;
		$search.css({left:to_px(search_left)});

		$fa_down.css({left:to_px(address_width + input_left - 20)});
		$fa_repeat.css({left:to_px(address_width + input_left)});
		$fa_search.css({left:to_px(search_left + input_serach_width - 20)});
	}

	// リソースの描画を行う
	this.drawResources=function(){

		var _self = this;
		var selcted = null;
		// 左側に一覧を表示
		for(var i=0; i<_self.resources.length; i++){
			if(_self.resources[i]["type"] != 'file'){
				var $item = $('<div class="exp-tree resource-id-'+ _self.resources[i]["id"] + '" data-resource-id="' + _self.resources[i]["id"] + '" data-resource-level="0">' + _self.resources[i]["name"] + '</div>').appendTo(_self.leftWindow);
				$item.css({top:to_px((_self.treeItemHeight) * i + _self.treeItemTopPadding)});
				$item.prepend('<img src="' +  _self.resources[i]["icon"] + '"/>');

				// 子のフォルダ数をカウント
				var childFolderCount = 0;
				if(_self.resources[i]["children"] != null){
					for(var j=0; j<_self.resources[i]["children"].length; j++){
						if(_self.resources[i]["children"][j]["type"] != "file"){
							childFolderCount++;
						}
					}
				}

				// 子にフォルダがあった場合は矢印を表示する
				if(childFolderCount > 0){
					$item.prepend('<i class="fa fa-chevron-right" aria-hidden="true"></i>');
				}
				else{
					$item.prepend('<i class="fa fa-square fa-none" aria-hidden="true"></i>');
				}
				// クリックイベント
				var _bind = function($item, __resource){
					// 矢印
					$item.children("i").click(function(){
						_self.clickTreeResourceHead($item);
					});
					// 文字列
					$item.click(function(e){
						if(e.target.tagName.toLowerCase() != 'i'){
							_self.clickTreeResourceText($item);
						}
						// 追加のイベントを発火
						if(__resource['click'] != null){
							__resource['click']('left', __resource["id"], _self.params);
						}
					});
					// 文字列
					$item.dblclick(function(e){
						// 追加のイベントを発火
						if(__resource['dblclick'] != null){
							__resource['dblclick']('left', __resource["id"], _self.params);
						}
					});
				}
				_bind($item, _self.resources[i]);


				if(_self.resources[i]["selected"] != null && _self.resources[i]["selected"]){
					selcted = $item;
				}
			}
		}
		// 初期選択
		if(selcted != null){
			selcted.click();
		}
	}

	// 矢印アイコンクリックイベント
	this.clickTreeResourceHead=function($item){
		var _self = this;
		//alert($item.data("resource-id"));
		var id = $item.data("resource-id");
		$i = $item.children("i");
		if($i.hasClass("fa-chevron-right")){
			$i.removeClass("fa-chevron-right");
			$i.addClass("fa-chevron-down");
			// 下の階層を追加
			var resouce = _self.getResouce(_self.resources, id);
			if(resouce != null){
				if(resouce["children"] != null && resouce["children"].length > 0){

					var baseTop = to_num($item.css("top")) + _self.treeItemHeight;
					var lastTop = null;
					var lastAddItem = null;
					var level = parseInt($item.data("resource-level")) + 1;

					// ディレクトリの数を数える
					var count = 0;
					for(var j=0; j < resouce["children"].length; j++){
						if(resouce["children"][j]["type"] != "file"){
							count++;
						}
					}

					for(var i=resouce["children"].length - 1; i>=0; i--){
						var _resource = resouce["children"][i];
						if(_resource["type"] != 'file'){

							var $addItem = $('<div class="exp-tree resource-id-'+ _resource["id"] + '" data-resource-id="' + _resource["id"] + '" data-resource-level="' + level + '" style="padding-left:' + to_px(level * 10) + '">' + _resource["name"] + '</div>').insertAfter($item);
							_lastTop = _self.treeItemHeight * (count - 1) + baseTop;
							if(lastAddItem == null){
								lastAddItem = $addItem;
								lastTop = _lastTop;
							}
							$addItem.css({top:to_px(_lastTop)});

							$addItem.prepend('<img src="' +  _resource["icon"] + '"/>');


							// 子のフォルダ数をカウント
							var childFolderCount = 0;
							if(_resource["children"] != null){
								for(var j=0; j<_resource["children"].length; j++){
									if(_resource["children"][j]["type"] != "file"){
										childFolderCount++;
									}
								}
							}

							// 子にフォルダがあった場合は矢印を表示する
							if(childFolderCount > 0){
								$addItem.prepend('<i class="fa fa-chevron-right" aria-hidden="true"></i>');
							}
							else{
								$addItem.prepend('<i class="fa fa-square fa-none" aria-hidden="true"></i>');
							}

							// クリックイベント
							var _bind = function($item, __resource){
								// 矢印
								$item.children("i").click(function(){
									_self.clickTreeResourceHead($item);
								});
								// 文字列
								$item.click(function(e){
									if(e.target.tagName.toLowerCase() != 'i'){
										_self.clickTreeResourceText($item);
									}
									// 追加のイベントを発火
									if(__resource['click'] != null){
										__resource['click']('left', __resource["id"], _self.params);
									}
								});

								// 文字列
								$item.dblclick(function(e){
									// 追加のイベントを発火
									if(__resource['dblclick'] != null){
										__resource['dblclick']('left', __resource["id"], _self.params);
									}
								});
							}
							_bind($addItem, _resource);
							count--;
						}
					}

					// さらに下のオブジェクトの位置を移動する
					if(lastAddItem != null){
						var baseTop = lastTop + _self.treeItemHeight;
						var i=0;
						var _item = lastAddItem;
						while(1){
							_item = _item.next();
							if(_item.length == 0){
								break;
							}
							_item.css({'top' : to_px(baseTop + _self.treeItemHeight * i) })
							i++;
						}
					}
				}
			}
		}
		else if($i.hasClass("fa-chevron-down")){
			$i.addClass("fa-chevron-right");
			$i.removeClass("fa-chevron-down");
			// 下の階層を削除
			var removeList = [];
			var currentLevel = parseInt($item.data("resource-level"));
			var _item = $item;
			var _lastItem = null;
			while(1){
				_item = _item.next();
				if(_item.length == 0){
					break;
				}
				var level = parseInt(_item.data("resource-level"));
				if(level > currentLevel){
					removeList.push(_item);
					_lastItem = _item;
				}
				else{
					break;
				}
			}
			// 次のアイテムを保持
			var nextItem = _lastItem.next();
			for(var i=0; i<removeList.length; i++){
				removeList[i].remove();
			}
			// さらに下のオブジェクトの位置を移動する
			if(nextItem.length > 0){
				var i=0;
				var baseTop = to_num($item.css("top")) + _self.treeItemHeight;
				while(1){
					nextItem.css({'top' : to_px(baseTop + _self.treeItemHeight * i) })
					nextItem = nextItem.next();
					if(nextItem.length == 0){
						break;
					}
					i++;
				}
			}
		}
	}

	// ツリー選択イベント
	this.clickTreeResourceText=function($item){
		var _self = this;
		_self.leftWindow.find(".exp-tree").removeClass('selected');
		$item.addClass("selected");
		// 右側ペインに描画


		// 右側ペインにオブジェクト描画
		var id = $item.data("resource-id");
		var resouce = _self.getResouce(_self.resources, id);

		// 右側ペインの描画
		_self.drawRightWindow(resouce);
	}


	this.drawRightWindow=function(resouce){

		var _self=this;
			_self.rightWindow.empty();

		$table = $('<table></table>').appendTo(_self.rightWindow);
		$tr = $('<tr></tr>').appendTo($table);
		$('<th class="checkbox"><input type="checkbox"/></th>').appendTo($tr);
		$('<th class="name">名前</th>').appendTo($tr);
		$('<th>更新日時</th>').appendTo($tr);
		$('<th>種類</th>').appendTo($tr);
		$('<th>サイズ</th>').appendTo($tr);
		// 先にフォルダを描画して次にファイルを描画する
		for(var i=0; i<resouce["children"].length; i++){
			if(resouce["children"][i]["type"]  == 'folder'){
				_self.drawRightLine($table, resouce["children"][i]);
			}
		}

		for(var i=0; i<resouce["children"].length; i++){
			if(resouce["children"][i]["type"]  == 'file'){
				_self.drawRightLine($table, resouce["children"][i]);
			}
		}
	}


	// 右側ペインの行描画
	this.drawRightLine=function($table, resource){
		var _self = this;
		var $tr = $('<tr class="list resource-id-' + resource["id"] + '" data-resource-id="' + resource["id"] + '"></tr>').appendTo($table);
		$('<td class="checkbox"></td>').appendTo($tr);

		var _name = resource['name'];
		var match = resource['name'].match(/\.([^/.]+)$/);
		if (match) {
			var ext = match[0];
			var _body = resource['name'].replace(/\.([^/.]+)$/, '');
			_name = _body + '<span class="extension">' + ext + '</span>';
		}

		var $name = $('<td class="name">' + _name + '</td>').appendTo($tr);
		$name.prepend('<img src="' + resource['icon'] + '">');
		$('<td>' + resource['updated'] + '</td>').appendTo($tr);
		$('<td>' + resource['kind'] + '</td>').appendTo($tr);
		$('<td>' + resource['size'] + '</td>').appendTo($tr);

		$tr.click(function(){
			_self.rightWindow.find("table tr").removeClass('selected');
			$(this).addClass("selected");

			// 追加のイベントを発火
			if(resource['click'] != null){
				resource['click']('right', resource["id"], _self.params);
			}
		})

		$tr.dblclick(function(){
			// フォルダは一階層下を描画
			var id = $tr.data("resource-id");
			var resouce = _self.getResouce(_self.resources, id);
			if(resouce['type'] == 'folder'){
				_self.drawRightWindow(resouce);
			}

			// 追加のイベントを発火
			if(resource['dblclick'] != null){
				resource['dblclick']('right', resource["id"], _self.params);
			}
		});

		// 右クリック
		$tr.bind('contextmenu', function(e) {

			_self.showContextMenu(e, $(this).data("resource-id"));


			return false;
		});


		// ファイルの拡張子表示判定
		if(_self.thisWindow.find('.show-extension').prop("checked")){
			_self.thisWindow.find("span.extension").show();
		}
	}

	// リソースのIDからリソースを取得する
	this.getResouce=function(resouces, id){
		var _self = this;
		var ret = null;
		for(var i=0; i<resouces.length; i++){
			if(String(resouces[i]["id"]) == id){
				ret = resouces[i];
			}
			else{
				if(resouces[i]["children"] != null && resouces[i]["children"].length > 0){
					ret = _self.getResouce(resouces[i]["children"], id)
				}
			}
			if(ret != null){
				break;
			}
		}
		return ret;
	}

	// コンテキストメニューの表示
	this.showContextMenu=function(e, resourceId){

		var _self = this;
		var contextMenuHeight = 345;
		var contextMenuWidth = 250;
		var property = null;

		// 画面全体を保護
		var contextBackground = $('<div class="sie-module-win-explore-context-background"></div>').prependTo(_self.home);

		var contextMenu = $('<div class="sie-module-win-explore-context-menu"></div>').prependTo(_self.home);
		contextMenu.css({
				width:to_px(contextMenuWidth),
				height:to_px(contextMenuHeight),
				top:to_px(e.pageY - contextMenuHeight), 
				left:to_px(e.pageX - contextMenuWidth)
			});
		var ul = $('<ul></ul>').appendTo(contextMenu);
		var li1 = $('<li>開く(O)</li>').appendTo(ul);
		var li2 = $('<li>編集(E)</li>').appendTo(ul);
		var li3 = $('<li>新規(N)</li>').appendTo(ul);
		var li4 = $('<li>印刷(P)</li>').appendTo(ul);
		var li5 = $('<li class="delim"><hr></li>').appendTo(ul);
		var li6 = $('<li>共有</li>').appendTo(ul);
		var li7 = $('<li>プログラムから開く(H)</li>').appendTo(ul);
		var li8 = $('<li class="delim"><hr></li>').appendTo(ul);
		var li9 = $('<li>送る(N)</li>').appendTo(ul);
		var li10 = $('<li>コピー(C)</li>').appendTo(ul);
		var li11 = $('<li>ショートカットの作成(S)</li>').appendTo(ul);
		var li12 = $('<li>ファイルの場所を開く(I)</li>').appendTo(ul);
		var li13 = $('<li>プロパティ(R)</li>').appendTo(ul);

		contextBackground.click(function(){
			contextBackground.remove();
			contextMenu.remove();
			if(property != null){
				property.remove();
			}
			contextBackground=null;
			contextMenu=null;
			property=null;
		});

		contextMenu.click(function(){
			contextBackground.remove();
			contextMenu.remove();
			if(property != null){
				property.remove();
			}
			contextBackground=null;
			contextMenu=null;
			property=null;
		});

		li13.click(function(e){
			contextMenu.remove();
			// プロパティウィンドウを表示
			property = $('<div class="sie-module-win-explore-context-property"></div>').prependTo(_self.home);
			property.css({
				top:to_px(e.pageY - 300), 
				left:to_px(e.pageX - 190)
			});

			var resource = _self.getResouce(_self.resources, resourceId);

			var titlebar = $('<div class="titlebar">' + resource['name'] + 'のプロパティ</div>').appendTo(property);
			var close = $('<div class="close"><i class="fa fa-times" aria-hidden="true"></i></div>').appendTo(titlebar);

			close.click(function(){
					contextBackground.remove();
					contextMenu.remove();
					if(property != null){
						property.remove();
					}
					contextBackground=null;
					contextMenu=null;
					property=null;
				});



			var general = $('<div class="general"></div>').appendTo(property);
			$('<div class="tabs"><div class="tab1">全般</div><div class="tab2">セキュリティ</div><div class="tab3">詳細</div><div class="tab4">以前のバージョン</div></div>').appendTo(property);

			$('<div class="icon"></div>').appendTo(general).css({'background-image': 'url(' + resource['icon'] + ')'});
			$('<div class="name">' + resource['name'] + '</div>').appendTo(general);

			($('<div class="delim"><hr/></div>').appendTo(general)).css({top:to_px(60)});

			$('<div class="label">ファイルの種類:</div>').appendTo(general).css({top:to_px(80)});
			$('<div class="value">' + resource['prop_type'] + '</div>').appendTo(general).css({top:to_px(80)});

			$('<div class="label">説明:</div>').appendTo(general).css({top:to_px(110)});
			$('<div class="value">' + resource['prop_discr'] + '</div>').appendTo(general).css({top:to_px(110)});


			($('<div class="delim"><hr/></div>').appendTo(general)).css({top:to_px(140)});

			$('<div class="label">場所:</div>').appendTo(general).css({top:to_px(160)});
			$('<div class="value">' + resource['prop_location'] + '</div>').appendTo(general).css({top:to_px(160)});

			$('<div class="label">サイズ:</div>').appendTo(general).css({top:to_px(190)});
			$('<div class="value">' + resource['prop_size'] + '</div>').appendTo(general).css({top:to_px(190)});

			$('<div class="label" style="line-height:18px;">ディスク上<br/>のサイズ:</div>').appendTo(general).css({top:to_px(220)});
			$('<div class="value">' + resource['prop_disk_size'] + '</div>').appendTo(general).css({top:to_px(220)});


			($('<div class="delim"><hr/></div>').appendTo(general)).css({top:to_px(250)});


			$('<div class="label">作成日時:</div>').appendTo(general).css({top:to_px(270)});
			$('<div class="value">' + resource['prop_created'] + '</div>').appendTo(general).css({top:to_px(270)});

			$('<div class="label">更新日時:</div>').appendTo(general).css({top:to_px(300)});
			$('<div class="value">' + resource['prop_updated'] + '</div>').appendTo(general).css({top:to_px(300)});

			$('<div class="label">アクセス日時:</div>').appendTo(general).css({top:to_px(330)});
			$('<div class="value">' + resource['prop_accessed'] + '</div>').appendTo(general).css({top:to_px(330)});

			($('<div class="delim"><hr/></div>').appendTo(general)).css({top:to_px(360)});

			$('<div class="label">属性:</div>').appendTo(general).css({top:to_px(380)});
			$('<div class="value" style="left:50px;"><input type="checkbox">読み取り専用(R)<input type="checkbox">隠しﾌｧｲﾙ(H)</div>').appendTo(general).css({top:to_px(380)});
			$('<div class="button">詳細設定(D)</div>').appendTo(general).css({top:to_px(380)});



			$('<div class="button">OK</div>').appendTo(property).css({top:to_px(540), left:to_px(100)})
				.click(function(){
					contextBackground.remove();
					contextMenu.remove();
					if(property != null){
						property.remove();
					}
					contextBackground=null;
					contextMenu=null;
					property=null;
				});
			$('<div class="button">キャンセル</div>').appendTo(property).css({top:to_px(540), left:to_px(190)})
				.click(function(){
					contextBackground.remove();
					contextMenu.remove();
					if(property != null){
						property.remove();
					}
					contextBackground=null;
					contextMenu=null;
					property=null;
				});
			$('<div class="button">適用(A)</div>').appendTo(property).css({top:to_px(540), left:to_px(280)})
				.click(function(){
					contextBackground.remove();
					contextMenu.remove();
					if(property != null){
						property.remove();
					}
					contextBackground=null;
					contextMenu=null;
					property=null;
				});

			property.draggable();
			return false;
		});


	}


}

