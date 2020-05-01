var SieApiLog=function(_host, _port, _userId, _skipRequest){

	this.host = _host;
	this.port = _port;
	this.userId = _userId;
	this.skipRequest = _skipRequest;		// HTTP���N�G�X�g�̃X�L�b�v�i�f�o�b�O�p�j

	// ���엚���̏o��
	this.put=function(themeNo, actionId){
		var _self=this;

		if(_skipRequest == null || _skipRequest == false){
			$.ajax({
				type : 'get',
				url : 'sie/php/log.php' + '?user_id=' + _self.userId + '&theme_no=' + themeNo + '&action_id=' + actionId,
				dataType : 'jsonp',
				success : function (data, textStatus) {
					// ����
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					// �G���[�i���[�U�ɂ͒ʒm���Ȃ��j
					console.log("SieApiLog error:" + XMLHttpRequest.status + "|" + textStatus + "|" + errorThrown.message );
				}
			});
		}
		else{
			console.log('user_id=' + _self.userId + '&theme_no=' + themeNo + '&action_id=' + actionId);
		}
	}

	// ���C�����̏o��
	this.certificate=function(themeNo){
		var _self=this;

		if(_skipRequest == null || _skipRequest == false){
			$.ajax({
				type : 'get',
				url : 'sie/php/certificate.php' + '?user_id=' + _self.userId + '&theme_no=' + themeNo,
				dataType : 'jsonp',
				success : function (data, textStatus) {
					// ����
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					// �G���[�i���[�U�ɂ͒ʒm���Ȃ��j
					console.log("SieApiLog error:" + XMLHttpRequest.status + "|" + textStatus + "|" + errorThrown.message );
				}
			});
		}
		else{
			console.log('user_id=' + _self.userId + '&theme_no=' + themeNo);
		}
	}
}


