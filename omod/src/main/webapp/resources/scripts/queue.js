var tableObject;
var searchResultsData = [];
var timeout;
var searchFromSystem = false;
var highlightedKeyboardRowIndex, dTable;

function startRefresh(){
	getPatientsFromQueue();
}

var toggleQueueSystemTables = function () {
	if (jq('#search-in-db').is(':checked')) {
		console.log("show system");
		jq('.page-label').html(patientInSystemLabel);
		jq('.in-system').show();
		jq('.queue').hide();
		searchFromSystem = true;
	} else {
		console.log("show queue");
		jq('.page-label').html(opdQueueLabel);
		jq('.in-system').hide();
		jq('.queue').show();
		searchFromSystem = false;
	}
}

var updateSearchResults = function(results){
	searchResultsData = results || [];
	var dataRows = [];
	_.each(searchResultsData, function(result){
		var patient_name = result.patientName.replace("null","");
		
		if (result.referralConcept && result.referralConcept.conceptId == 2548){
			patient_name += " <span class='recent-lozenge'>From Lab</span>";
		}
		
		dataRows.push([result.patientIdentifier, patient_name, result.age,  result.sex, result.visitStatus, result.status]);
	});

	dTable.api().clear();
	
	if(dataRows.length > 0) {
		dTable.fnAddData(dataRows);
	}

	refreshInQueueTable();
}
var updateMCHSearchResults = function(results){
	searchResultsData = results || [];
	var dataRows = [];
	_.each(searchResultsData, function(result){
		var patient_name = result.patientName.replace("null","");

		if (result.referralConcept && result.referralConcept.conceptId == 2548){
			patient_name += " <span class='recent-lozenge'>From Lab</span>";
		}

		dataRows.push([result.patientIdentifier, patient_name, result.age, result.clinic, result.sex, result.visitStatus, result.status]);
	});

	dTable.api().clear();

	if(dataRows.length > 0) {
		dTable.fnAddData(dataRows);
	}

	refreshInQueueTable();
}

var selectRow = function(selectedRowIndex) {
	handlePatientRowSelection.handle(searchResultsData[selectedRowIndex]);
}

var refreshInQueueTable = function(){
	var rowCount = searchResultsData.length;
	if(rowCount == 0){
		tableObject.find('td.dataTables_empty').html("No patient in queue");
	}
	dTable.fnPageChange(0);
}

var getPatientsFromQueue = function(){
	tableObject.find('td.dataTables_empty').html('<span><img class="search-spinner" src="'+emr.resourceLink('uicommons', 'images/spinner.gif')+'" /></span>');
	jq.getJSON(emr.fragmentActionLink("patientqueueapp", "patientQueue", "getPatientsInQueue"),
		{
		  'opdId': jq('#queue-choice').val()
		})
	.success(function(results) {
		updateSearchResults(results.data);
		if(results.user==="triageUser"){
			//jq(".user-processing").text("Nurse Processing");
		}else{
			//jq(".user-processing").text("Doctor Processing");
		}

	})
	.fail(function(xhr, status, err) {
		updateSearchResults([]);
	});
};

var startTimer = function () {
	if (jq("#queue-choice").val() != 0 && !searchFromSystem){
		console.log("starting timer");
		startRefresh();
		if (timeout) {
			clearTimeout(timeout);    		
		}
		timeout = setInterval(startRefresh, 30000);
	}
}

var bindPatientQueueSearchEvent = function () {
    jq("#patient-search").on("keyup", function(){
        if (!searchFromSystem) {
            var searchPhrase = jq(this).val();
            console.log("Searching for: " + searchPhrase);
            dTable.api().search(searchPhrase).draw();
        }
    });

    jq("#patient-search-clear-button").on("click", function(){
        jq("#patient-search").val('');
        if (!searchFromSystem) {
            dTable.api().search('').draw();
        }
    });
}

var isTableEmpty = function(){
	if(searchResultsData.length > 0){
		return false
	}
	return !dTable || dTable.fnGetNodes().length == 0;
};
	
jq(function(){
    tableObject = jq("#patient-queue");

    startTimer();

    jq('#queue-choice').change(function() {
    	if (!searchFromSystem) {
    		getPatientsFromQueue();
    		if (timeout) {
    			clearTimeout(timeout);    		
    		}
    		timeout = setInterval(startRefresh, 30000);
    	}
    });
	
    dTable = tableObject.dataTable({
        bFilter: true,
        bJQueryUI: true,
        bLengthChange: false,
        iDisplayLength: 15,
        sPaginationType: "full_numbers",
        bSort: false,
        sDom: 't<"fg-toolbar ui-toolbar ui-corner-bl ui-corner-br ui-helper-clearfix datatables-info-and-pg"ip>',
        oLanguage: {
            "sInfo": "Patients in queue",
            "sInfoEmpty": " ",
            "sZeroRecords": "No patients in queue",
            "oPaginate": {
                "sFirst": "First",
                "sPrevious": "Previous",
                "sNext": "Next",
                "sLast": "Last"
            }
        },

        fnDrawCallback : function(oSettings){
            if(isTableEmpty()){
                //this should ensure that nothing happens when the use clicks the
                //row that contain the text that says 'No data available in table'
                return;
            }

            if(highlightedKeyboardRowIndex != undefined && !isHighlightedRowOnVisiblePage()){
                unHighlightRow(dTable.fnGetNodes(highlightedKeyboardRowIndex));
            }

            //fnDrawCallback is called on each page redraw so we need to remove any previous handlers
            //otherwise there will multiple hence the logic getting executed multiples times as the
            //user the goes back and forth between pages
            tableObject.find('tbody tr').unbind('click');
            tableObject.find('tbody tr').unbind('hover');

            tableObject.find('tbody tr').click(
                function(){
                    highlightedMouseRowIndex = dTable.fnGetPosition(this);
                    selectRow(highlightedMouseRowIndex);
                }
            );
        },
		
		fnRowCallback : function (nRow, aData, index){
			if (searchResultsData[index].referralConcept && searchResultsData[index].referralConcept.conceptId == 2548){
				nRow.className += " from-lab";
				return nRow;
			}
		}
    });

    bindPatientQueueSearchEvent();
});