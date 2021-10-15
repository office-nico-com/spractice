var Editor = function(_targetId){
	
	var _self = this;
	this.targetId = _targetId;
	this.obj = $('#' + _targetId);
	this.handleCtl = false;
	this.save =null;

	this.obj.css({
					'font-size':'12px', 
					'background-color':'#000050', 
					'color':'#fff', 
					'padding-top':'15px', 
					'padding-bottom':'15px'
				});
	
	
	this.editor = $('<div id="' + _targetId + '_fulleditor" class="full_editor_74382"></div>').prependTo($('body'));
	this.textarea = $('<textarea></textarea>').appendTo(this.editor);
	this.close = $('<div>閉じる</div>').appendTo(this.editor);
	
	this.historyPos = null;
	this.histories = [];
	
	this.histories.push({value:this.obj.val(), cursor:0});
	
	this.keydown=function(e, obj, full){

		if((e.keyCode!=90 || !_self.handleCtl) && (e.keyCode!=89 || !_self.handleCtl)){
			if(this.historyPos != null){
				this.histories.splice(this.historyPos, this.histories.length - this.historyPos);
			}
			this.historyPos = null;
		}
		
		// console.log(e.keyCode + "|" + _self.handleCtl);
		
		// タブキーが押された時以外は即リターン
		if( e.keyCode!=9 && e.keyCode!=83 && e.keyCode!=17 && e.keyCode!=122 &&(e.keyCode!=27 && !full) && e.keyCode!=13 && e.keyCode!=90 && e.keyCode!=89){ return; }
		
		// タブキーを押したときのデフォルトの挙動を止める
		if(e.keyCode==9){
			e.preventDefault();

			// 現在のカーソルの位置と、カーソルの左右の文字列を取得しておく
			var cursorPosition = obj.selectionStart;
			var cursorLeft     = obj.value.substr( 0, cursorPosition );
			var cursorRight    = obj.value.substr( cursorPosition, obj.value.length );

			// テキストエリアの中身を、
			// 「取得しておいたカーソルの左側」+「タブ」+「取得しておいたカーソルの右側」
			// という状態にする。
			obj.value = cursorLeft+"\t"+cursorRight;

			// カーソルの位置を入力したタブの後ろにする
			obj.selectionEnd = cursorPosition+1;
		}
		else if(e.keyCode==17){
			e.preventDefault();
			_self.handleCtl = true;
		}
		else if(e.keyCode==83 && _self.handleCtl){
			if(_self.save != null){
				e.preventDefault();
				_self.save();
				_self.handleCtl=false;
			}
		}
		else if(e.keyCode==122 && !full){
			e.preventDefault();
			_self.fullWindow();
			return false;
		}
		else if((e.keyCode==122 || e.keyCode==27) && full){
			e.preventDefault();
			_self.closeFullWindow();
			return false;
		}
		else if(e.keyCode == 13){
			e.preventDefault();
			
			this.histories.push({value:$(obj).val(), cursor:$(obj).get(0).selectionStart});
			
			var val = $(obj).val();
			var cursorPosition = $(obj).get(0).selectionStart;
			
			var str1 = val.substring(0, cursorPosition);
			
			
			var lines = str1.split(/\r\n|\n|\r/);
			var linefirst = "";
			if(lines != null && lines.length > 1){
				var lastline = lines[lines.length - 1];
				for(var i=0; i < lastline.length; i++){
					if(lastline[i] == " " || lastline[i] == "\t"){
						linefirst += lastline[i];
					}
					else{
						break;
					}
				}
			}
			var str2 = val.substring(cursorPosition, val.length);

			if(str1[str1.length - 1] == '{' || str1[str1.length - 1] == '('){
				var c = "";
				if(str1[str1.length - 1] == '{'){
					c = "}"
				}
				else if(str1[str1.length - 1] == '('){
					c = ")"
				}
				$(obj).val(str1 + "\r\n" + linefirst + linefirst + "\r\n" + linefirst + c + str2);
				$(obj).get(0).setSelectionRange(cursorPosition + 1 + linefirst.length * 2, cursorPosition + 1 + linefirst.length * 2);
			}
			else{
				$(obj).val(str1 + "\r\n" + linefirst + str2);
				$(obj).get(0).setSelectionRange(cursorPosition + 1 + linefirst.length, cursorPosition + 1 + linefirst.length);
			}
			this.histories.push({value:$(obj).val(), cursor:$(obj).get(0).selectionStart});
		}
		else if(e.keyCode==90 && _self.handleCtl){
			

			// ctl + z
			e.preventDefault();
			if(this.historyPos == null){

				var currentValue = $(obj).val();
				var currentCursor = $(obj).get(0).selectionStart;

				$(obj).val(this.histories[this.histories.length -1].value);
				$(obj).get(0).setSelectionRange(this.histories[this.histories.length -1].cursor, this.histories[this.histories.length -1].cursor);
				this.historyPos = this.histories.length -1;

				this.histories.push({value:currentValue, cursor:currentCursor});
			}
			else if(this.historyPos > 0){
				this.historyPos--;
				$(obj).val(this.histories[this.historyPos].value);
				$(obj).get(0).setSelectionRange(this.histories[this.historyPos].cursor, this.histories[this.historyPos].cursor);
			}
			
		}
		else if(e.keyCode==89 && _self.handleCtl){
			// ctl + y
			e.preventDefault();
			if(this.historyPos != null && this.historyPos < this.histories.length - 1){
				this.historyPos++;
				$(obj).val(this.histories[this.historyPos].value);
				$(obj).get(0).setSelectionRange(this.histories[this.historyPos].cursor, this.histories[this.historyPos].cursor);
			}
		}

		return true;
	}
	
	this.keyup=function(e, obj, full){
		if(e.keyCode!=17){ return; }
		e.preventDefault();
		_self.handleCtl = false;
	}

	this.obj.keydown(function(e){_self.keydown(e, this, false)}).keyup(function(e){_self.keyup(e, this, false)});
	this.textarea.keydown(function(e){_self.keydown(e, this, true)}).keyup(function(e){_self.keyup(e, this, true)});
	

	this.setSaveAction=function(_save){
		_self.save = _save;
	}
	
	// 全画面表示
	this.fullWindow=function(){
		var _self = this;
		$('html,body').scrollTop(0);
		document.body.style.overflow="hidden";

		
		var cursorPosition = _self.obj.get(0).selectionStart;
		_self.textarea.val(_self.obj.val());
		_self.editor.show();
		_self.textarea.focus();
		_self.textarea.get(0).setSelectionRange(cursorPosition, cursorPosition);
		
		
		this.histories=[];
		this.histories.push({value:this.obj.val(), cursor:0});

	}

	// 全画面表示
	this.closeFullWindow=function(){
		var _self = this;

		var cursorPosition = _self.textarea.get(0).selectionStart;
		_self.obj.val(_self.textarea.val());
		_self.obj.focus();
		_self.obj.get(0).setSelectionRange(cursorPosition, cursorPosition);

		_self.editor.hide();
		document.body.style.overflow="";
	}

	this.close.click(function(){
		_self.closeFullWindow();
	});
	
	return this;
}
