var SieApiController=function($_body, _backUrl, _production){
	
	this.$body = $_body;
	this.backUrl = _backUrl;
	
	this.controller = null;
	this.panel = null;
	
	this.isShowGuide=true;
	
	this.production = _production;
	
	this.init=function(){
		var _self = this;
		
		_self.controller=$('<div class="sie-api-controller"></div>').prependTo(_self.$body);
		_self.panel=$('<div class="sie-api-controller-panel"></div>').prependTo(_self.$body);

		_self.btnStop  = $('<button>訓練終了</button>').prependTo(_self.panel);
		_self.btnGuide  = $('<button>ガイド非表示</button>').prependTo(_self.panel);
		
		_self.btnStop.click(function(){
			_self.dialog=$('<div class="sie-api-controller-dialog">本当にこの訓練を終了してよろしいですか？</div>').prependTo(_self.$body);
		
			_self.btnNo=$('<div class="button no">いいえ</div>').appendTo(_self.dialog);
			_self.btnYes=$('<div class="button yes">はい</div>').appendTo(_self.dialog);
			
			_self.btnNo.click(function(){
				_self.dialog.remove();
				_self.dialog = null;
			});
			_self.btnYes.click(function(){
				
				if(_self.production){
					location.href=_self.backUrl;
				}
				else{
					window.close();
				}
			});
		})

		_self.btnGuide.click(function(){
			if(_self.isShowGuide){
				$('.sie-api-scenario-window-inner').css({opacity: 0});
				$('.sie-api-scenario-window').css({opacity: 0});
				_self.btnGuide.text('ガイド再表示');
				_self.isShowGuide=false;
			}
			else{
				$('.sie-api-scenario-window-inner').css({opacity: 1});
				$('.sie-api-scenario-window').css({opacity: 1});
				_self.btnGuide.text('ガイド非表示');
				_self.isShowGuide=true;
			}
		});
		
	}

	
	
	this.init();
}

