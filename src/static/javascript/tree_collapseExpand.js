
function collapseExpandTreeNode(obj) {
	
	var nodeid=$(obj).attr("nodeid");
	var childNodeNames = "childOf:" + nodeid;
	var childNodes = document.getElementsByName(childNodeNames);
	if (childNodes != null) {
		// When collapse is clicked the complete hierarchy has to be expanded.
	    if(obj.className=="icon_collapse") {
	    	collapseHierarchy(obj);
	    }// When expand is clicked the complete hierarchy has to be collapsed.
	    else if(obj.className == "icon_expand") {
	    	expandHierarchy(obj);
	    }
	}
}

function collapseHierarchy(obj) {
	var nodeid=$(obj).attr("nodeid");
	var iscollapsed = $(obj).attr("iscollapsed");
	var childNodeNames = "childOf:" + nodeid;
	var childNodes = document.getElementsByName(childNodeNames);
	if (childNodes != null) {		
		for(var i=0;i<childNodes.length;i++){
			var parentDiv = childNodes[i].parentNode;
			var currentNodeId = "#" + parentDiv.id;			
			$(currentNodeId).css({"display":"none"});
			var indx = $(parentDiv).attr("indx");						
			var currentNodeCollapseExpandIconDivId = "divIcon" + indx; 
			var currentNodeCollapseExpandIconDiv = document.getElementById(currentNodeCollapseExpandIconDivId);
			//if collapse/expand div is there, then this node needs to be worked upon further.
			if(currentNodeCollapseExpandIconDiv!=null){
			  collapseHierarchy(currentNodeCollapseExpandIconDiv);
			}
		}
		if (iscollapsed == undefined || iscollapsed != "false") {
			obj.className="icon_expand";
		}
	}
}

function expandHierarchy(obj) {
	var nodeid=$(obj).attr("nodeid");
	var iscollapsed = $(obj).attr("iscollapsed");
	var childNodeNames = "childOf:" + nodeid;
	var childNodes = document.getElementsByName(childNodeNames);
	if (childNodes != null) {
		for(var i=0;i<childNodes.length;i++){
			var parentDiv = childNodes[i].parentNode;
			var currentNodeId = "#" + parentDiv.id;			
			$(currentNodeId).css({"display":"block"});
			var indx = $(parentDiv).attr("indx");						
			var currentNodeCollapseExpandIconDivId = "divIcon" + indx; 
			var currentNodeCollapseExpandIconDiv = document.getElementById(currentNodeCollapseExpandIconDivId);
			//if collapse/expand div is there, then this node needs to be worked upon further.  
			if(currentNodeCollapseExpandIconDiv!=null){
			  expandHierarchy(currentNodeCollapseExpandIconDiv);
			}
		}
		if (iscollapsed == undefined || iscollapsed != "false") {
			obj.className="icon_collapse";
		}
	}
}