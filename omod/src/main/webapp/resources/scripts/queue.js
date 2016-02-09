var searchResultsData = [];
var highlightedKeyboardRowIndex, dTable;

jq(function(){
    var tableObject = jq("#patient-queue");
    var updateSearchResults = function(results){
        searchResultsData = results || [];
        var dataRows = [];
        _.each(searchResultsData, function(result){
            dataRows.push([result.patientIdentifier, result.patientName, result.age, result.sex, result.visitStatus, result.status]);
        });
        if(dataRows.length > 0) {
            dTable.fnAddData(dataRows);
        }
        else{
            dTable.api().clear();
        }
        refreshTable();
    }

    var selectRow = function(selectedRowIndex) {
        handlePatientRowSelection.handle(searchResultsData[selectedRowIndex]);
    }

    var refreshTable = function(){
        var rowCount = searchResultsData.length;
        if(rowCount == 0){
            tableObject.find('td.dataTables_empty').html("No patient in queue");
        }
        dTable.fnPageChange(0);
    }

    var getPatientsInQueue = function(opdId, searchPhrase){
        tableObject.find('td.dataTables_empty').html('<span><img class="search-spinner" src="'+emr.resourceLink('uicommons', 'images/spinner.gif')+'" /></span>');
        jq.getJSON(emr.fragmentActionLink("patientqueueui", "patientQueue", "getPatientsInQueue"),
            {
              'opdId': opdId,
              'phrase': searchPhrase
            })
        .success(function(results) {
            updateSearchResults(results.data);
            if(results.user==="triageUser"){
                jq(".user-processing").text("Nurse Processing");
            }else{
                jq(".user-processing").text("Doctor Processing");
            }

        })
        .fail(function(xhr, status, err) {
            tableObject.find('td.dataTables_empty').html("<span class='patient-search-error'>" + config.messages.searchError + "</span>");
        });
    };

    jq('#queue-choice').change(function() {
        getPatientsInQueue(jq(this).val());
    });

    var isTableEmpty = function(){
        if(searchResultsData.length > 0){
            return false
        }
        return !dTable || dTable.fnGetNodes().length == 0;
    };

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
        }
    });

    jq("#patient-search").on("keyup", function(){
        var searchPhrase = jq(this).val();
        console.log("Searching for: " + searchPhrase);
        dTable.api().search(searchPhrase).draw();
    });

    jq("#patient-search-clear-button").on("click", function(){
        jq("#patient-search").val('');
        dTable.api().search('').draw();
    });
});