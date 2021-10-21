
$(function(){
  $(".gnav__main .list").hover(function(e) {
    if ($(window).width() > 789) {
      //$(this).children(".gnav__sub").stop(true, false).slideToggle(200);
      e.preventDefault();
      $(this).addClass('active');
    }
  },function(e){
    if ($(window).width() > 789) {
      //$(this).children(".gnav__sub").stop(true, false).slideToggle(200);
      e.preventDefault();
      $(this).removeClass('active');
    }
  });
  $(".gnav__main .list--sub").hover(function(e) {
    if ($(window).width() > 789) {
      //$(this).children(".gnav__sub").stop(true, false).slideToggle(200);
      e.preventDefault();
      $(this).addClass('active');
    }
  },function(e){
    if ($(window).width() > 789) {
      //$(this).children(".gnav__sub").stop(true, false).slideToggle(200);
      e.preventDefault();
      $(this).removeClass('active');
    }
  });
  
  //ヘッダー固定
  var nav = $('.header');
  $(window).scroll(function () {
    if ( $(this).scrollTop() > 0 ) {
      nav.addClass('fixed');
		} else {
      nav.removeClass('fixed');
  }});
  
  //ページトップ スムーズスクロール
  $('a[href^="#"]').click(function(){
    var speed = 500;
    var href= $(this).attr("href");
    var target = $(href == "#" || href == "" ? 'html' : href);
    var position = target.offset().top;
    $("html, body").animate({scrollTop:position}, speed, "swing");
    return false;
  });
  
  //top 右タブ
	$(".top-tab__list >li").hover(function() {
	$(this).addClass("hover");
	}, function() {
	$(this).removeClass("hover");

	});
});

$(document).ready(function() {
	$(".faed_effect,.main__logo").each(function() {
		$(this).addClass('faedIn_effect');
	});
});
$(document).ready(function() {
	$(".solid-block").each(function() {
		$(this).addClass('solid_effect');
	});
});
$(document).ready(function() {
	$(".visual_left_effect").each(function() {
		$(this).addClass('visual_left_fadeIn');
	});
});
$(window).scroll(function() {
	$(".effect").each(function() {
		var imgPos = $(this).offset().top;
		var scroll = $(window).scrollTop();
		var windowHeight = $(window).height();
		if (scroll > imgPos - windowHeight + windowHeight / 4) {
			$(this).addClass('fadeIn');
		}
	});
	
});


$(function() {
	$('.products-bg').hover(function() {
		$(this).find(".products-cover").addClass("Cover");
	}, function() {
		$(".products-cover").removeClass("Cover");
	});
	$('.buttn_effect').hover(function() {
		$(this).addClass("buttnIn_effect");
	}, function() {
		$(".buttn_effect").removeClass("buttnIn_effect");
	});
	$('.works__list .works__body').hover(function() {
		$(this).find(".works__body__cover").addClass("Cover");
		$(this).find(".works__cat").addClass("Hide");
	}, function() {
		$(".works__body__cover").removeClass("Cover");
		$(".works__cat").removeClass("Hide");
	});
})
$(window).resize(function(){
    var w = $(window).width();
    var x = 1100;
    if (x <= w) {
       $('.gnav').removeClass('menu_open menu_openIn');
$('.header-nav__sp').removeClass('open');
    }
	});
$(function() {
  $('.header-nav__sp').click(function() {
		$(this).toggleClass('open');
		$('.gnav').toggleClass('menu_open menu_openIn');
	});
	
	$(window).scroll(function() {
		$(".block_fade_in").each(function() {
			var imgPos = $(this).offset().top;
			var scroll = $(window).scrollTop();
			var windowHeight = $(window).height();
			if (scroll > imgPos - windowHeight + windowHeight / 5) {
				$(this).addClass("fade_on");
			} else {
				$(this).removeClass("fade_on");
			}
		});
    
    //page-topの表示
    // ドキュメントの高さ
    var scrollHeight = $(document).height();
    // ウィンドウの高さ+スクロールした高さ→ 現在のトップからの位置
    var scrollPosition = $(window).height() + $(window).scrollTop();
    // フッターの高さ
    var footHeight = $(".footer__link").height() + $(".footer-bottom").height();
    
    // トップから150px以上スクロールしたら
		if ($(this).scrollTop() > 150) { 
		// ページトップのリンクをフェードインする
			$(".page-top").fadeIn();
		} else if($(document).height() < $(window).height() + 150) { 
			$(".page-top").show();
		} else{// それ以外は
		// ページトップのリンクをフェードアウトする
			$(".page-top").fadeOut();
		}

    // スクロール位置がフッターまで来たら
    if ( scrollHeight - scrollPosition  <= footHeight - 100 ) {
      // ページトップリンクをフッターに固定
      var bottom = $(".footer-bottom").height() + ($(".footer__link").height() / 2)-($(".page-top").height() / 2);
      $(".page-top").css({"bottom": bottom});
    } else {
      // ページトップリンクを右下に固定
      $(".page-top").css({"position":"fixed", "top": "auto","bottom": "10px"});
    }
	});
});
$(document).scroll(function() {
	//サービス用
	$('.fade_on ul.top__service-list li').each(function(i) {
		$(this).children(".slide_effectLR").delay(300 * i).queue(function() {
			$(this).addClass('slideIn_effectLR').dequeue();
		});
	});
	$('.fade_on .img-effect').each(function(i) {
		$(this).delay(400 * i).queue(function() {
			$(this).addClass('fadeInSv').dequeue();
		});
	});
	$('.fade_on .sv_tetx').each(function(i) {
		$(this).delay(400 * i).queue(function() {
			$(this).addClass('sv_tetxIn').dequeue();
		});
	});
	//サービス用END
	//見出し用
	$(".fade_on .effect_visual").each(function() {
		$(this).addClass('fadeIn_visual');
	});
});