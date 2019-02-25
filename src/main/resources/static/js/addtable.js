


function findObj(theObj, theDoc){  

var p, i, foundObj;

    if(!theDoc) theDoc = document; 

	if( (p = theObj.indexOf("?")) > 0 && parent.frames.length) {

	theDoc = parent.frames[theObj.substring(p+1)].document;

	theObj = theObj.substring(0,p);  }

	if(!(foundObj = theDoc[theObj]) && theDoc.all) foundObj = theDoc.all[theObj];

	for (i=0; !foundObj && i < theDoc.forms.length; i++)

	foundObj = theDoc.forms[i][theObj]; 

	for(i=0; !foundObj && theDoc.layers && i < theDoc.layers.length; i++)

	foundObj = findObj(theObj,theDoc.layers[i].document);

	if(!foundObj && document.getElementById) foundObj = document.getElementById(theObj); 

	return foundObj;

}

//添加一个参与人填写行



//删除指定行

function DeleteSignRow(rowid){

 var signFrame = findObj("SignFrame",document);

 var signItem = findObj(rowid,document);


 //获取将要删除的行的Index

 var rowIndex = signItem.rowIndex;


 //删除指定Index的行

 signFrame.deleteRow(rowIndex);


 //重新排列序号，如果没有序号，这一步省略

 for(i=rowIndex;i<signFrame.rows.length;i++){

  signFrame.rows[i].cells[0].innerHTML = i.toString();

 }

}//清空列表

function ClearAllSign(){

 if(confirm('确定要清空所有选石吗？')){

  var signFrame = findObj("SignFrame",document);

  var rowscount = signFrame.rows.length;

  //循环删除行,从最后一行往前删除

  for(i=rowscount - 1;i > 0; i--){

   signFrame.deleteRow(i);

  }


 }

}

