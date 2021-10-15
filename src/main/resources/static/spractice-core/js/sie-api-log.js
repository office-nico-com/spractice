var SieApiLog=function(_operationLogUrl, _completionUrl, _chekCompletionUrl, _clientKeycode, _skipRequest){

	this.operationLogUrl = _operationLogUrl;
	this.completionUrl = _completionUrl;
	this.chekCompletionUrl = _chekCompletionUrl;
	this.clientKeycode = _clientKeycode;
	this.skipRequest = _skipRequest;
	// トランザクション情報の取得、同一logインスタンスであれば同一トランザクション情報がDBに記録される
	this.tran = String((new Date()).getTime()) + String(Math.floor( Math.random() * (10000000 + 1 - 100000) ) + 100000);
	
	this.startTransaction=function(){
		this.tran = String((new Date()).getTime()) + String(Math.floor( Math.random() * (10000000 + 1 - 100000) ) + 100000);
	}

	this.put=function(scenarioKeycode, keycode){
		var _self=this;

		if(_skipRequest == null || _skipRequest == false){
			$.ajax({
				type : 'get',
				url : _self.operationLogUrl + '?clientKeycode=' + _self.clientKeycode + '&scenarioKeycode=' + scenarioKeycode + '&keycode=' + keycode + "&transaction=" + _self.tran,
				dataType : 'json',
				success : function (data, textStatus) {
					// 処理しない
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					console.log("SieApiLog error:" + XMLHttpRequest.status + "|" + textStatus + "|" + errorThrown.message );
				}
			});
		}
		else{
			console.log('clientKeycode=' + _self.clientKeycode + '&scenarioId=' + scenarioId + '&keycode=' + keycode);
		}
	}

	this.completion=function(completionPointKeycode, completionConditionCode){
		var _self=this;

		if(_skipRequest == null || _skipRequest == false){
			$.ajax({
				type : 'get',
				url : _self.completionUrl + '?clientKeycode=' + _self.clientKeycode + '&completionPointKeycode=' + completionPointKeycode + "&completionConditionCode=" + completionConditionCode + "&transaction=" + _self.tran,
				dataType : 'json',
				success : function (data, textStatus) {

				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					console.log("SieApiLog error:" + XMLHttpRequest.status + "|" + textStatus + "|" + errorThrown.message );
				}
			});
		}
		else{
			console.log(_self.completionUrl + '?clientKeycode=' + _self.clientKeycode + '&completionPointKeycode=' + completionPointKeycode);
		}
	}

	this.isCompletion=function(completionPointKeycode, completedFunction, unCompletedFunction){
		var _self=this;
		$.ajax({
			type : 'get',
			url : _self.chekCompletionUrl + '?clientKeycode=' + _self.clientKeycode + '&completionPointKeycode=' + completionPointKeycode + "&transaction=" + _self.tran,
			dataType : 'json',
			success : function (data, textStatus) {
				console.log("** completion info | " + data["completionPointId"] + " = " + data["count"]);
				if(data["completed"]==true){
					completedFunction();
				}
				else{
					unCompletedFunction();
				}
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				console.log("SieApiLog error:" + XMLHttpRequest.status + "|" + textStatus + "|" + errorThrown.message );
			}
		});
	}
}
