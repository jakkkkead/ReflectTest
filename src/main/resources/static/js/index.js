$(function(){
	$(".priceRange").click(function(){
		var val = $(this).text()
		$("#priceRange").html(val)
	})
	$(".markSelect").click(function(){
		var val = $(this).text()
		$("#markSelectror").html(val)
	})
})
function toPre(){
	if(param.pageIndex == 0){
		return
	}
	index = param.pageIndex-param.pageSize
	param.pageIndex = index
	//搜索
	doSearch()
}
function toNext(){
	if(param.pageIndex> param.total){
		return
	}
	index = param.pageIndex+param.pageSize
	param.pageIndex = index
	//搜索
	doSearch()
}


	
