$(function(){

		// グローバルメニューのサブメニューに選択状態のものがあれば上位も選択状態にする
		$('ul.global-nav li.global-nav-menu').each(function(i, li){
			if($(li).find(".selected").length > 0){
				$(li).children("button").addClass('selected');
			}
		});

		$('ul.navi-list-1 li').each(function(i, li){

			// 左側メニューにサブメニューがある場合はクラスを追加
			if($(li).children('ul.navi-list-2').length > 0){
				$(li).addClass('group-nav');
				$(li).children('a').append('<div class="pull-down"><i class="fas fa-angle-right"></i></div>');
			}

			// メニューをマウスオーバーした場合の処理
			$(li).hover(function(){
									// グループメニューはプルダウン
									if($(this).hasClass('group-nav')){
										$(this).children('ul.navi-list-2').slideDown('fast');
										// $(this).children('ul.navi-list-2').show();
										$(this).addClass("show-sub");
									}
								},
								function(){
									if($(this).hasClass('group-nav')){
										if(!$(this).hasClass('group-selected')){
											// $(this).children('ul.navi-list-2').slideUp('fast');
											$(this).children('ul.navi-list-2').hide();
											$(this).removeClass("show-sub");
										}
									}
								});
		});

		$('#leftNavList').hover(function(){
															if($('#mainContainer').hasClass('left-nav-collapsed')){
																$('#mainContainer').addClass('hover');
															}
														},
														function(){
															if($('#mainContainer').hasClass('left-nav-collapsed')){
																$('#mainContainer').removeClass('hover');
															}
														});

		$('ul.navi-list-2 li.selected').each(function(i, li){
			$(this).parent().parent().addClass('group-selected');
		});

		// 左側ハンバーガーメニューをクリックした場合の処理
		$('#leftNavTrigger').click(function(){
			if($('#mainContainer').hasClass('left-nav-collapsed')){
				$('#mainContainer').removeClass('left-nav-collapsed');
				$.cookie("left_close", 'false', { expires: 200, path:'/' });
			}
			else{
				$('#mainContainer').addClass('left-nav-collapsed');
				$.cookie("left_close", 'true', { expires: 200, path:'/' });
			}
		});

		if($('#rightDrawer').length > 0){
			$overlay = $('<div class="right-drawer-overlay"></div>').insertAfter($('#rightDrawer'));
			$overlay.click(function(){$('body').removeClass("show-drawer")});
			$close = $('<div class="right-drawer-close"></div>').prependTo($('#rightDrawer'));
			$('<div class="x1"></div>').prependTo($close);
			$('<div class="x2"></div>').prependTo($close);
			$close.click(function(){$('body').removeClass("show-drawer")});
		}

		$('#globalNav button').click(function(){
			location.href=$(this).data("link");
		});


		// 左側メニューがクローズの場合は初期状態でクローズにする
		if($.cookie("left_close") == "true"){
			$('#mainContainer').addClass('left-nav-collapsed');
		}
		else{
			$('#mainContainer').removeClass('left-nav-collapsed');
		}

		// 左側メニュー初期表示時にエフェクトがかかると違和感があるので、エフェクトはあとからセットする
		setTimeout(function(){
			$('div.main-container div.left-nav').css({'transition-duration':'0.15s'});
			$('div.main-container div.left-nav div.left-nav-scroll-container').css({'transition-duration':'0.15s'})
			$('div.main-container div.left-nav div.left-nav-scroll-container div.left-nav-trigger').css({'transition-duration':'0.15s'})
			$('div.main-container div.left-nav div.left-nav-scroll-container ul li a').css({'transition-duration':'0.15s'})
			$('div.main-container div.left-nav div.left-nav-scroll-container ul li a div.pull-down i').css({'transition-duration':'0.15s'})
			$('div.main-container div.left-nav div.left-nav-scroll-container ul li a span').css({'transition-duration':'.15s,.025s', 'transition-delay':'0s,.09s;'})
			$('div.main-container div.content-outer').css({'transition-duration':'0.15s'})
		}, 1);

		
		$('table.table tr td a, table.table tr th a').hover(
				function(){
					if(!$(this).hasClass('no-hover')){
						$(this).parent().parent().find("a").addClass('hover');
					}
				},
				function(){
					if(!$(this).hasClass('no-hover')){
						$(this).parent().parent().find("a").removeClass('hover');
					}
				}
		);
});


const h  = (string) => {
	if(typeof string !== 'string') {
		return string;
	}
	return string.replace(/[&'`"<>]/g, function(match) {
		return {
			'&': '&amp;',
			"'": '&#x27;',
			'`': '&#x60;',
			'"': '&quot;',
			'<': '&lt;',
			'>': '&gt;',
		}[match]
	});
}

const hbr  = (string) => {
	if(typeof string !== 'string') {
		return string;
	}
	var str = string.replace(/[&'`"<>]/g, function(match) {
		return {
			'&': '&amp;',
			"'": '&#x27;',
			'`': '&#x60;',
			'"': '&quot;',
			'<': '&lt;',
			'>': '&gt;',
		}[match]
	});
	
	str = str.replace(/\r\n/g, "\n");
	str = str.replace(/\r/g, "");
	str = str.replace(/\n/g, "<br/>");

	return str;
}

// 整数チェック
// https://webllica.com/javascript-number-check-function/
const isNum  = (string, minus) => {
	let pattern = /^([1-9]\d*|0)$/;
	if(minus != null && minus){
		pattern = /^[-]?([1-9]\d*|0)$/;
	}
	return pattern.test(string);
}

// 小数点数チェック
const isFloat  = (string, minus) => {
	let pattern = /^([1-9]\d*|0)(\.\d+)?$/;
	if(minus != null && minus){
		pattern = 	/^[-]?([1-9]\d*|0)(\.\d+)?$/;
	}
	return pattern.test(string);
}


