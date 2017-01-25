    function removeItem(){
        $(this).parent().css({"display":"none"});
        $(this).parent().find("input.c4").val("true");
    }

    function reIndex(){
        if ($('#filterContainer >div').size()>0) {
            $('#filterContainer').css({"display":"block"});
            $('#noReportFilter').css({"display":"none"});
            $('#hasFilters').val("true");
        } else {
            $('#filterContainer').css({"display":"none"});
            $('#noReportFilter').css({"display":"block"});
            $('#hasFilters').val("false");
            return false;
        }
        $.each($('#filterContainer img'), function(index){
            $(this).attr("id", 'btnDelete'+index)
            .unbind('click', removeItem)
            .bind('click', removeItem);
        });

        $.each($('#filterContainer select.c2'), function(index){
            $(this).attr("id", 'reportFilterItems['+index+'].filterOperandId').attr("name",'reportFilterItems['+index+'].filterOperandId');
        });
        $.each($('#filterContainer  input.c3'), function(index){
            $(this).attr("id", 'reportFilterItems['+index+'].id').attr("name",'reportFilterItems['+index+'].id');
        });
        $.each($('#filterContainer  input.c4'), function(index){
            $(this).attr("id", 'reportFilterItems['+index+'].markedForDeletion').attr("name",'reportFilterItems['+index+'].markedForDeletion');
        });
        $.each($('#filterContainer  input.c5'), function(index){
            $(this).attr("id", 'reportFilterItems['+index+'].filterType').attr("name",'reportFilterItems['+index+'].filterType');
        });

        $.each($('#filterContainer select.c1'), function(index){
            if ($(this).find("option:selected").attr("datatype")!=null && $(this).find("option:selected").attr("datatype")!=undefined) {
                switch ($(this).find("option:selected").attr("datatype").toLowerCase()){
                        case "datetime":;
                            $(this).parent().find("input:text").datepicker();
                        break;
                        case "date":;
                            $(this).parent().find("input:text").datepicker();
                        break;
                }
                $(this).attr("id", 'reportFilterItems['+index+'].reportFieldId').attr("name",'reportFilterItems['+index+'].reportFieldId')
                .change(function(){
                    switch ($(this).find("option:selected").attr("datatype").toLowerCase()){
                        case "string":;
                            $(this).parent().find("input:text").val("").datepicker('destroy');
                            $(this).parent().find("input:text").attr("id", 'reportFilterItems['+index+'].filterStringValue').attr("name",'reportFilterItems['+index+'].filterStringValue');
                            populateOperand(filterData.filterOperandString,$(this).parent());
                            $(this).parent().find("input[type=hidden][class=c5]").val("string");
                        break;
                        case "integer":;
                            $(this).parent().find("input:text").val("").datepicker('destroy');
                            $(this).parent().find("input:text").attr("id", 'reportFilterItems['+index+'].filterIntegerValue').attr("name",'reportFilterItems['+index+'].filterIntegerValue');
                            populateOperand(filterData.filterOperandInteger,$(this).parent());
                            $(this).parent().find("input[type=hidden][class=c5]").val("numeric");
                        break;
                        case "int":;
                            $(this).parent().find("input:text").val("").datepicker('destroy');
                            $(this).parent().find("input:text").attr("id", 'reportFilterItems['+index+'].filterIntegerValue').attr("name",'reportFilterItems['+index+'].filterIntegerValue');
                            populateOperand(filterData.filterOperandInteger,$(this).parent());
                            $(this).parent().find("input[type=hidden][class=c5]").val("numeric");
                        break;
                        case "double":;
                            $(this).parent().find("input:text").val("").datepicker('destroy');
                            $(this).parent().find("input:text").attr("id", 'reportFilterItems['+index+'].filterDoubleValue').attr("name",'reportFilterItems['+index+'].filterDoubleValue');
                            populateOperand(filterData.filterOperandInteger,$(this).parent());//should be for the double type-incidentally it is same for integer
                            $(this).parent().find("input[type=hidden][class=c5]").val("numeric");
                        break;
                        case "datetime":;
                            $(this).parent().find("input:text").val("").datepicker();
                            $(this).parent().find("input:text").attr("id", 'reportFilterItems['+index+'].filterDateValue').attr("name",'reportFilterItems['+index+'].filterDateValue');
                            populateOperand(filterData.filterOperandDate,$(this).parent());
                            $(this).parent().find("input[type=hidden][class=c5]").val("date");
                        break;
                        case "date":;
                            $(this).parent().find("input:text").val("").datepicker();
                            $(this).parent().find("input:text").attr("id", 'reportFilterItems['+index+'].filterDateValue').attr("name",'reportFilterItems['+index+'].filterDateValue');
                            populateOperand(filterData.filterOperandDate,$(this).parent());
                            $(this).parent().find("input[type=hidden][class=c5]").val("date");
                        break;
                        case "boolean":;
                        	$(this).parent().find("input:text").val("").datepicker('destroy');
                        	$(this).parent().find("input:text").attr("id", 'reportFilterItems['+index+'].filterBooleanValue').attr("name",'reportFilterItems['+index+'].filterBooleanValue');
                        	populateOperand(filterData.filterOperandBoolean,$(this).parent());
                        	$(this).parent().find("input[type=hidden][class=c5]").val("numeric");
                    break;
                    }
                });
            }
        });
        //reSize();
    }

    function populateOperand(data,pModel){
        var operandOptions = "";
        for(var j=0;j<data.length;j++) {
           operandOptions += '<option value="'+data[j].id+'">'+data[j].label+'</option>';
        }
        pModel.find("select.c2").html(operandOptions);
    }

$(document).ready(function(){
    reIndex();
    $('#btnAdd').click(function() {
        var lModel = $('#model').clone(true);
        lModel.removeAttr("id")
              .css({"display":"block","padding":"5px"});
        var filterOptions = '<option value="" datatype=" ">Select Field</option> ';
        for(var j=0;j<filterData.filterfields.length;j++) {
           filterOptions += '<option value="'+filterData.filterfields[j].id+'" datatype="'+filterData.filterfields[j].datatype+'">'+filterData.filterfields[j].name+'</option>';
        }
        lModel.find("select.c1").html(filterOptions);
        $('#filterContainer').append(lModel);
        reIndex();
    });
});
