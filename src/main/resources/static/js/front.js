var frontJs = new function(){
	
	this.tabLeft=20;
	this.tabWidth=150;
	this.tabGroupMargin = 5;
	
	this.tabControl = function(){
		
		const _self = this;
		
		const maxWidth = $('div.main').width();

		console.log("max width = " + maxWidth);

		const totalTabCount = $('a.tab-link').length;
		const tabCount = Math.floor((maxWidth / _self.tabWidth) - 2);

		let groupSelect = false;
		console.log("tab count = " + tabCount);
		
		
		let groups = [];
		
		let viewTabCount = 0;
		$('a.tab-link').each(function(index, obj){
			if(index > tabCount){
				$(obj).hide();

				console.log("label = " + $(obj).children('div.tab').text());
				console.log("link = " + $(obj).attr('href'));
				const group = {label:$(obj).children('div.tab').text(), link:$(obj).attr('href'), current : $(obj).children('div.tab').hasClass('current')}
				groupSelect |= $(obj).children('div.tab').hasClass('current')

				groups.push(group);
			}
			else{
				$(obj).show();
				viewTabCount++;
			}
		});
		
		const tabGroupLeft = (viewTabCount * _self.tabWidth) +  _self.tabLeft + _self.tabGroupMargin;
		$('div.tab-group').css({'left': tabGroupLeft + 'px'})
		if(viewTabCount >= totalTabCount){
			$('div.tab-group').hide();
		}
		else{
			
			let html = '<ul>';
			for(let i=0; i<groups.length; i++){
				html += '<li ';
				if(groups[i]['current']){
					html += 'class="current"';
				}
				html += '>';
				html += '<a href="' + groups[i]['link'] + '" ';
				html += '>' + groups[i]['label'] + '</a>';
				html += '</li>';
			}
			html += '</ul>';
			$('#idTagGroup').html(html);
			$('div.tab-group').show();
			if(groupSelect){
				$('div.tab-group').addClass('current')
			}
		}
		
	}
	
}

$(function(){
	frontJs.tabControl();
	$(window).resize(function(){
		frontJs.tabControl();
	});
});


