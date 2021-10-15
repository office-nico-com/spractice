var SieApiMarker=function($_target,  _selector, _left, _top){

	this.$target = $_target;
	this.selector = _selector;
	// https://www.silhouette-ac.com/detail.html?id=121233&sw=%E6%8C%87
	this.marker="sie/img/sie-api-marker/marker1.png";
	this.$marker=null;
	this.left=0;
	this.top=0;
	if(_left != null){
		this.left = _left;
	}
	if(_top != null){
		this.top = _top;
	}

	this.show=function(){
		var _self = this;
		if(_self.$marker == null){
			var $obj = _self.$target.find(_self.selector);
			if($obj.length > 0){
				_self.$marker = $('<img src="' + _self.marker + '" class="sie-api-marker-img blinking"/>').appendTo($obj);
				_self.$marker.css({left:to_px(_self.left), top:to_px(_self.top)});
			}
		}
	}

	// 表示時に位置指定するメソッド
	this.point=function($_target,  _selector, _left, _top, _zIndex){
		var _self = this;
		_self.$target = $_target;
		_self.selector = _selector;
		if(_left != null){
			_self.left = _left;
		}
		if(_top != null){
			_self.top = _top;
		}
		_self.zIndex = _zIndex;
		_self.hide();
		if(_self.$marker == null){
			var $obj = _self.$target.find(_self.selector);
			if($obj.length > 0){
				_self.$marker = $('<img src="' + _self.marker + '" class="sie-api-marker-img blinking"/>').appendTo($obj);
				_self.$marker.css({left:to_px(_self.left), top:to_px(_self.top)});
				if(_self.zIndex != null){
					_self.$marker.css({'z-index':_self.zIndex});
				}
			}
		}
	}

	this.hide=function(){
		var _self = this;
		if(_self.$marker != null){
			_self.$marker.remove();
			_self.$marker = null;
		}
	}
}

