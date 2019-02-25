$("#btn_ruku").click(function(){
	 $("#ruku").show();
	 $("#searchAndsum").hide();
	 $("#manage").hide();
	 $("#btn_ruku").css("background","blue");
	 $("#btn_search").css("background","grey");
	 $("#btn_manage").css("background","grey");
});
$("#btn_search").click(function(){
	 $("#ruku").hide();
	 $("#searchAndsum").show();
	 $("#manage").hide();
	 $("#btn_ruku").css("background","grey");
	 $("#btn_search").css("background","blue");
	 $("#btn_manage").css("background","grey");
});
$("#btn_manage").click(function(){
	 $("#ruku").hide();
	 $("#searchAndsum").hide();
	 $("#manage").show();
	 $("#btn_ruku").css("background","grey");
	 $("#btn_manage").css("background","blue");
	 $("#btn_search").css("background","grey");
});
$('#queryType').change(function(){ 
	var id = $(this).val();
	if(id=="stone_purchdate") {
		$("#div_date").show();
		$("#div_date2").show();
		$("#div_mainNo").hide();
		$("#div_subNo").hide();
		$("#div_supplier").hide();
	}else if(id=="stone_mainStoneNo") {
		$("#div_date").hide();
		$("#div_date2").hide();
		$("#div_mainNo").show();
		$("#div_subNo").hide();
		$("#div_supplier").hide();
	}else if(id=="stone_substoNo") {
		$("#div_date").hide();
		$("#div_date2").hide();
		$("#div_mainNo").hide();
		$("#div_subNo").show();
		$("#div_supplier").hide();
	}else if(id=="stone_supplierName") {
		$("#div_date").hide();
		$("#div_date2").hide();
		$("#div_mainNo").hide();
		$("#div_subNo").hide();
		$("#div_supplier").show();
	}
});



$(document).ready(function() {
	$('#exampletable').DataTable();
    $('#exampletableshow').DataTable();
    
    $('#purchDateStart').editableSelect({ effects: 'slide' });//可输入的选择框
    $('#purchDateEnd').editableSelect({ effects: 'slide' });//可输入的选择框
    $('#mainNo').editableSelect({ effects: 'slide' });//可输入的选择框
    $('#substoNo').editableSelect({ effects: 'slide' });//可输入的选择框
    $('#supplierName').editableSelect({ effects: 'slide' });//可输入的选择框
} );