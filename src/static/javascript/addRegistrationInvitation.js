//---------------------------//
//     TREE BROWSER CODE     //
// Copyright Andrew Vos 2006 //
//---------------------------//

var parentNodeCount = 0;
var nodeCount = 0;

function onParentNodeImageClick(node) {
    var divNode = document.getElementById(node.name.replace("parentNodeImage","parentNodeDiv"));
	var imageNode = node;

 	if (divNode.style.display == "none"){
		imageNode.src = "brands/default/en/images/open.gif";
		divNode.style.display = "";
	}
	else {
		imageNode.src = "brands/default/en/images/closed.gif";
		divNode.style.display = "none";
	}
}
function onParentNodeTextClick(node) {
    var divNode = document.getElementById(node.name.replace("parentNodeText","parentNodeDiv"));
    var imageNode = document.getElementsByName(node.name.replace("parentNodeText","parentNodeImage"))[0];

	if (divNode.style.display == 'none') {
		imageNode.src = "brands/default/en/images/open.gif";
		divNode.style.display = '';
	}
	else {
		imageNode.src = "brands/default/en/images/closed.gif";
		divNode.style.display = 'none';
	}
}

function onNodeTextClick(node) {
    var imageNode = document.getElementsByName(node.name.replace("nodeText","nodeImage"))[0];
    setSelectedNode(imageNode);
}
function onNodeImageClick(node, url, target){
    var imageNode = node
    setSelectedNode(imageNode);
}

function setSelectedNode(imageNode){
    for (index = 0; index < this.nodeCount; index++) {
		document.getElementsByName("nodeImage" + index)[0].src = "brands/default/en/images/list.gif";
    }
   	imageNode.src = "brands/default/en/images/pageSelected.png";
}
function expandAll(){
	for (index = 0; index < this.parentNodeCount; index++) {
		document.getElementById("parentNodeDiv" + index).style.display = "";
        document.getElementsByName("parentNodeImage" + index)[0].src = "brands/default/en/images/open.gif";     
	}
}
function collapseAll(){
	for (index = 0; index < this.parentNodeCount; index++) {
		document.getElementById("parentNodeDiv" + index).style.display = "none";
        document.getElementsByName("parentNodeImage" + index)[0].src = "brands/default/en/images/closed.gif";     
	}
}

function startParentNode(text, id, selected){
	var xx;
	if(selected=="true"){
		xx="checked";
	}else{
		xx="";
	}
	document.write('<table border="0" cellpadding="1" cellspacing="0" height="24px">');
	document.write('  <tr>');
	document.write('    <td><img src="brands/default/en/images/closed.gif" name="parentNodeImage' + parentNodeCount + '" onclick="onParentNodeImageClick(this)" style="cursor:pointer;margin:2px;"/></td>');
	//document.write('	<td style="width:15px;">'); 
	//document.write('<input type="checkbox" onclick="javascript:alert(2);"/>');
	//document.write('	</td>');
	document.write('    <td><input type="checkbox" onclick="getLearnerGrpByOrgGroup(this.id);" name="groups" '+xx+' value='+id+' id=_orgGroup'+id+' "/><a class="parentTreeNode" name="parentNodeText' + parentNodeCount + '" onclick="onParentNodeTextClick(this)" style="cursor:pointer;">');
	document.write(text);
	document.write('</a>');
	document.write('  </td>');
	document.write('  </tr>');
	document.write('  <tr>');
	document.write('    <td></td><!-- SPACING -->');
	document.write('	<td colspan="2"><DIV id="parentNodeDiv' + parentNodeCount + '" style="display:none">');	
    this.parentNodeCount = this.parentNodeCount + 1;
}
function endParentNode(){
	document.write('</DIV></td>');
	document.write('  </tr>');
	document.write('</table>');
}
function addNode(text, url, target, id, selected){
	var xx;
	if(selected=="true"){
		xx="checked";
	}else{
		xx="";
	}
	document.write('<table border="0" cellpadding="0" cellspacing="0" height="24px">');
	document.write('  <tr>');
	document.write('	<td width="20px" style="margin:2px;">');
    document.write('<a href="' + url + '" target="' + target + '" onfocus="this.hideFocus=true;" style="outline-style:none;margin:2px;">');
	document.write('<img src="brands/default/en/images/list.gif" border="0" name="nodeImage' + this.nodeCount + '" onclick="onNodeImageClick(this);" />');
    document.write('</a>');
	document.write('	</td>');
	document.write('	<td width="20px">');
	document.write('<input type="checkbox" onclick="getLearnerGrpByOrgGroup(this.id);" name="groups" '+xx+' value='+id+' id=_orgGroup'+id+' "/>');
	document.write('	</td>');
	document.write('    <td><a name="nodeText' + this.nodeCount + '" onclick="onNodeTextClick(this);" href="' + url + '" target="' + target + '" class="normalTreeNode" onfocus="this.hideFocus=true;" style="outline-style:none;">' + text + '</a></td>');
	document.write('  </tr>');
	document.write('</table>');
    this.nodeCount = this.nodeCount + 1;
}
function addExpandCollapseAll(){
	document.write('<table width="30px" border="0" cellpadding="0" cellspacing="2">');
	document.write('  <tr>');
	document.write('    <td align="right" width="50%" style="padding-top:3px;"><a onclick="expandAll();" class="expandCollapse" style="cursor:pointer;"><img src="brands/default/en/images/closed.gif" width="14" height="14" alt="Expand All" /></a></td>');
	document.write('    <td alight="left" width="50%" style="padding-top:3px;"><a onclick="collapseAll();" class="expandCollapse" style="cursor:pointer;"><img src="brands/default/en/images/open.gif" width="14" height="14" alt="Collapse All" /></a></td>');
	document.write('  </tr>');
	document.write('</table>');  
}



