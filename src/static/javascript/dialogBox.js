// Used for dialog box...

function confirmDelete (conditionResult, successMessage, failureMessage, successCallbackFunction) {
	if (conditionResult) {
		showDeleteConfirmationAlert(successMessage, successCallbackFunction);
	}		
	else{			
		showMessageAlert(failureMessage);
	}
}

function showMessageAlert(message) {
	$('#messageAlert').modal({
		overlayId:'confirmModalOverlay',
		containerId:'confirmModalContainer',
		close:false,
		position: ["40%",],
		onShow: function (dialog) {
            dialog.data.find('.message_box_text').append(message);
        }
   });
}

function showDeleteConfirmationAlert(message, callback) {
    $('#deleteConfirmation').modal({
        close:false,
        position: ["40%",],
        overlayId:'confirmModalOverlay',
        containerId:'confirmModalContainer', 
        onShow: function (dialog) {
            dialog.data.find('.message_box_text').append(message);

            // if the user clicks "yes"
            dialog.data.find('.yes').click(function () {
                // call the callback
                if (jQuery.isFunction(callback)) {
                    callback.apply();
                }
                // close the dialog
                jQuery.modal.close();
            });
        }
    });
}