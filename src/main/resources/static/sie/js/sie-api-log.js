var SieApiLog=function(_host, _port, _userId, _skipRequest){

	this.host = _host;
	this.port = _port;
	this.userId = _userId;
	this.skipRequest = _skipRequest;		// HTTPリクエストのスキップ（デバッグ用）

	// 操作履歴の出力
	this.put=function(themeNo, actionId){
		var _self=this;

		if(_skipRequest == null || _skipRequest == false){
			$.ajax({
				type : 'get',
				url : 'sie/php/log.php' + '?user_id=' + _self.userId + '&theme_no=' + themeNo + '&action_id=' + actionId,
				dataType : 'jsonp',
				success : function (data, textStatus) {
					// 成功
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					// エラー（ユーザには通知しない）
					console.log("SieApiLog error:" + XMLHttpRequest.status + "|" + textStatus + "|" + errorThrown.message );
				}
			});
		}
		else{
			console.log('user_id=' + _self.userId + '&theme_no=' + themeNo + '&action_id=' + actionId);
		}
	}

	// 履修履歴の出力
	this.certificate=function(themeNo){
		var _self=this;

		if(_skipRequest == null || _skipRequest == false){
			$.ajax({
				type : 'get',
				url : 'sie/php/certificate.php' + '?user_id=' + _self.userId + '&theme_no=' + themeNo,
				dataType : 'jsonp',
				success : function (data, textStatus) {
					// 成功
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					// エラー（ユーザには通知しない）
					console.log("SieApiLog error:" + XMLHttpRequest.status + "|" + textStatus + "|" + errorThrown.message );
				}
			});
		}
		else{
			console.log('user_id=' + _self.userId + '&theme_no=' + themeNo);
		}
	}
}


