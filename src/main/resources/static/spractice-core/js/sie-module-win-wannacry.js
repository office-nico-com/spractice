// WannaCryモジュール
var SieModuleWinWannacry = function(moduleId){
	this.id = moduleId;
	this.title = "@WanaDecryptor@"
	this.titlebar = "Wana Decrypt0r 2.0";
	this.icon = "sie/img/icon-wannacry.png"
	this.isShowDeskTop=true;
	this.isWindowOpen=true;
	this.moduleClass="sie-module-win-wannacry";
	this.delayedStart = 0;

	this.view = '<div class="sie-module-win-wannacry-body">';
	this.view += '<div class="lock"></div>';
	
	this.view += '<div class="box1">';
	this.view += '<div class="message">Payment will be raised on</div>';
	this.view += '<div class="date">5/22/2017 14:14:04</div>';
	this.view += '<div class="timeleft">Time Left</div>';
	this.view += '<div class="countdown">03:00:00:00</div>';
	this.view += '</div>';

	this.view += '<div class="box2">';
	this.view += '<div class="message">Your files will be lost on</div>';
	this.view += '<div class="date">5/26/2017 14:14:04</div>';
	this.view += '<div class="timeleft">Time Left</div>';
	this.view += '<div class="countdown">07:00:00:00</div>';
	this.view +='</div>';

	this.view += '<div class="about about1">Abount bitcoin</div>';
	this.view += '<div class="about about2">How to buy bitcoins?</div>';
	this.view += '<div class="about about3">Contact Us</div>';

	this.view += '<div class="ooops">Ooops, your files have been encrypted!</div>';

	this.view += '<div class="lang">Japanese</div>';
	this.view += '<div class="lang-pulldown">▼</div>';

	this.view += '<div class="description">'

	this.view += '<p>私のコンピュータに何が起こったのですか？</p>';
	this.view += '重要なファイルは暗号化されています。<br/>';
	this.view += '文書、写真、ビデオ、データベース、およびその他のファイルの多くは、暗号化されているためアクセスできなくなりました。たぶんあなたはファイルを回復する方法を探していますが、時間を無駄にすることはありません。誰も私たちの解読サービスなしであなたのファイルを回復することはできません。<br/><br/>';
	this.view += '<p>ファイルを回復できますか？</p>';
	this.view += '確かに。すべてのファイルを安全かつ簡単に復元できることを保証します。しかし、十分に時間がありません。<br/>あなたは無料でいくつかのファイルを解読することができます。 &lt;Decrypt&gt;をクリックして今すぐ試してください。<br/>しかし、すべてのファイルを解読したい場合は、支払う必要があります。<br/>お支払いを送信するのに3日しかかかりません。その後、価格は倍になります。<br/>また、7日間で支払いを行わないと、ファイルを永久に回復することはできません。<br/>私たちは6ヶ月で払うことができないほど貧しい人々のために無料イベントを開催します<br/><br/>';
	this.view += '<p>私はどのように支払うのですか？</p>';
	this.view += '支払いはBitcoinでのみ受け付けます。詳細については、&lt;About bitcoin&gt;をクリックしてください。<br/>Bitcoinの現在の価格を確認し、ビットコアを購入してください。<br/>詳細については、&lt;How to buy bitcoins&gt;をクリックしてください。そして、このウィンドウで指定されたアドレスに正しい金額を送ってください。<br/>お支払い後、&lt;Check Payment&gt;をクリックしてください。 月曜日から金曜日までの午前9時〜午前11時（グリニッジ標準時）。<br/>';
	this.view += '支払いが確認されたらすぐにファイルの復号化を開始できます。 <br/><br/>';
	this.view += '<p>接触</p>';
	this.view += '援助が必要な場合は、&lt;Contact Us&gt;をクリックしてメッセージを送信してください。<br/>';
	this.view += 'このソフトウェアを削除しないことを強くお勧めします。支払いを処理して支払いが処理されるまで、しばらくのうちにアンチウィルスを無効にすることを強くお勧めします。<br/>あなたのアンチウイルスが更新され、自動的にこのソフトウェアを削除した場合、あなたが支払ってもあなたのファイルを回復することはできません！<br/><br/>';
	this.view += '</div>';

	this.view += '<div class="sendto">'
	this.view += '<div class="message">Send $600 worth of bitcoin to this address:</div>';
	this.view += '<div class="email">Example12tPguxNyatSD519dxAA98BkdeSMw</div>';
	this.view += '<div class="button">Copy</div>';
	this.view += '</div>';

	this.view += '<div class="button1">Check Payment</div>';
	this.view += '<div class="button2">Decrypt</div>';

	this.view += '</div>';

	this.width=970;
	this.height=600;
	this.onCreateEvents=[];
	this.onActiveEvents=[];
	this.onResizeEvents=[];
	this.params=null;
	this.thisWindow=null;


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

		var dt = new Date();
		dt.setDate(dt.getDate() + 3);
		var year = dt.getFullYear();
		var month = dt.getMonth()+1;
		var date = dt.getDate();
		var hours = dt.getHours();
		var minutes = dt.getMinutes();
		var seconds = dt.getSeconds();

		var str = '' + month + "/"
		str += date + "/";
		str += year + " ";
		str += ('00' + hours).slice(-2) + ":";
		str += ('00' + minutes).slice(-2) + ":";
		str += ('00' + seconds).slice(-2);
		params["window"].find(".box1 .date").text(str);

		var dt = new Date();
		dt.setDate(dt.getDate() + 7);
		var year = dt.getFullYear();
		var month = dt.getMonth()+1;
		var date = dt.getDate();
		var hours = dt.getHours();
		var minutes = dt.getMinutes();
		var seconds = dt.getSeconds();

		var str = '' + month + "/"
		str += date + "/";
		str += year + " ";
		str += ('00' + hours).slice(-2) + ":";
		str += ('00' + minutes).slice(-2) + ":";
		str += ('00' + seconds).slice(-2);
		params["window"].find(".box2 .date").text(str);




		var ptime = (3 * 24 * 60 * 60);
		var ltime = (7 * 24 * 60 * 60);

		var countdown=function(){
			ptime -=1;
			ltime -=1;


			if(ptime > 0){
				pday = truncateDecimal(truncateDecimal(truncateDecimal(ptime / 60) / 60) / 24);
				var src = ptime - (pday * 24 * 60  * 60);
				phour = truncateDecimal(truncateDecimal(src / 60) / 60);
				src = src - (phour * 60 * 60)
				pmin = truncateDecimal(src / 60);
				psec = src - (pmin * 60)

				var pstr = ('00' + pday).slice(-2) + ":";
				pstr += ('00' + phour).slice(-2) + ":";
				pstr += ('00' + pmin).slice(-2) + ":";
				pstr += ('00' + psec).slice(-2);

				params["window"].find(".box1 .countdown").text(pstr);
			}


			if(ltime > 0){
				lday = truncateDecimal(truncateDecimal(truncateDecimal(ltime / 60) / 60) / 24);
				var src = ltime - (pday * 24 * 60  * 60);
				lhour = truncateDecimal(truncateDecimal(src / 60) / 60);
				src = src - (phour * 60 * 60)
				lmin = truncateDecimal(src / 60);
				lsec = src - (pmin * 60)

				var lstr = ('00' + lday).slice(-2) + ":";
				lstr += ('00' + lhour).slice(-2) + ":";
				lstr += ('00' + lmin).slice(-2) + ":";
				lstr += ('00' + lsec).slice(-2);

				params["window"].find(".box2 .countdown").text(lstr);
			}

			if(ptime == 0 && ltime == 0){
				return;
			}

			setTimeout(function(){
				countdown();
			}, 1000);
		}
		countdown();

	});


	// 小数切り捨て
	function truncateDecimal(n) {
		if (n >= 0) {
			return Math.floor(n)
		} else {
			return Math.ceil(n)
		}
	}

	// リサイズイベント
	this.addOnResizeEvent(function(params){
	});

}

