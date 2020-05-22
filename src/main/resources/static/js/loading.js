var loading=new function(){

	this.show=false;
	
	this.start=function(){
		var _self = this;
		if(this.show){
			return;
		}
		$.LoadingOverlay("show", {imageColor:'#666', imageResizeFactor:'0.7'});
		var checkCookie=function(){
			if(!_self.show){
				return;
			}
			var cookieKey='stopLoading';
			var isFileDownload = false;

			var position = document.cookie.indexOf(cookieKey);
			if (position >= 0) {
				isFileDownload = true;
			}
			if (isFileDownload) {
				$.LoadingOverlay("hide", true);
				_self.show=false;
			}
			else{
				setTimeout(function(){
					checkCookie();
				}, 500);
			}
		}
		
		
		this.show=true;
		checkCookie();
	}

	this.stop=function(){
		$.LoadingOverlay("hide", true);
		_self.show=false;
	}
}


