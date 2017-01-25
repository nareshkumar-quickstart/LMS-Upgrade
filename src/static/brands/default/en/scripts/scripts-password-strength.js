
    function passwordStrength(password){
        var desc = new Array();
            desc[0] = "Does not meet length requirement";
            desc[1] = "Weak";
            desc[2] = "Better";
            desc[3] = "Medium";
            desc[4] = "Strong";
            desc[5] = "Strongest";

        var score   = 0;


        //if password bigger than 6 give 1 point
        if (password.length < 8) score = 0;

        //if password bigger than 6 give 1 point
        if (password.length > 8) score++;

        //if password has both lower and uppercase characters give 1 point
        if (password.length > 8 && ( password.match(/[a-z]/) ) && ( password.match(/[A-Z]/) ) ) score++;

        //if password has at least one number give 1 point
        if (password.length > 8 && password.match(/\d+/)) score++;

        //if password has at least one special caracther give 1 point
        if (password.length > 8 && password.match(/.[!,@,#,$,%,^,&,*,?,_,~,-,(,)]/) ) score++;

        //if password bigger than 12 give another 1 point
        if (password.length > 12) score++;

        $("#password-strength").html("<b>Password Strength: </b>"+desc[score]);

    }

    var symbol = '\u25CF';
    var interval;
    function MaskFieldsAsPassword(textbox, passwdField, completeMask, intervalTime){
    	var self = this;
    	var loopCount = ( textbox && textbox.length ? (textbox.length > 0 ? textbox.length : 0 ) : 0);
    	for(var i=0 ; i<loopCount ; i++){
    		registerControlForPasswsMask(textbox[i], passwdField[i], completeMask, intervalTime, self);
    	}   	
    }
    
    function registerControlForPasswsMask(textbox, passwdField, completeMask, intervalTime, self){
    	self.addListener(textbox, 'keydown', function(e){ 
    		self.forceNumberInput(); 
		});
    	
    	self.addListener(textbox, 'keyup', function(e){ 
    		self.maskPasswd(textbox, passwdField, false);
		});

    	self.maskPasswd(textbox, passwdField, true);//This is mandatory so that SSN will be encrypted without delay on page load
		interval = setInterval(function(){
				self.maskPasswd(textbox, passwdField, true);
				}, intervalTime);
    	
    	self.addListener(textbox, 'click', function(e){ 
    		interval = self.positionCaret(textbox);
		});
    	
    	window.onbeforeunload  = function(){		
    		clearInterval(interval);
    		return;
    	};
    }
    
    MaskFieldsAsPassword.prototype = {
    		maskPasswd : function (textbox, passwdField, completeMask){
    		var plainpassword = '';
    		if(textbox.value != ''){
    			for(var i=0; i<textbox.value.length; i++){
    				if(textbox.value.charAt(i) == symbol){
    					plainpassword += passwdField.value.charAt(i);
    				}else{
    					plainpassword += textbox.value.charAt(i);
    				}
    			}
    		}else{ 
    			plainpassword = textbox.value; 
    		}

    		passwdField.value = plainpassword;

    		if ("createEvent" in document) {
    		    var evt = document.createEvent("HTMLEvents");
    		    evt.initEvent("change", false, true);
    		    passwdField.dispatchEvent(evt);
    		}else
    			passwdField.fireEvent("onchange");
    		
    		var maskedstring = this.encodeMaskedPassword(plainpassword, completeMask ? true : false, textbox);
    		textbox.value = maskedstring;
    	},
    	
    	encodeMaskedPassword : function encodeMaskedPassword(passwordstring, completeMask, textbox){
    		var characterlimit = (completeMask == true) ? 0 : 1;
    		for(var maskedstring = '', i=0; i<passwordstring.length; i++){
    			if(i < passwordstring.length-characterlimit){ 
    				maskedstring += symbol; 
    			} else {
    				maskedstring += passwordstring.charAt(i); 
    			}
    		}
    		
    		return maskedstring;
    	},
	
		addListener : function(eventnode, eventname, eventhandler){
			if(typeof document.addEventListener != 'undefined'){
				return eventnode.addEventListener(eventname, eventhandler, true);
			}else if(typeof document.attachEvent != 'undefined'){
				return eventnode.attachEvent('on' + eventname, eventhandler);
			}
		},			    	
	
		forceNumberInput : function (){
    		if (event.keyCode != 8 && event.keyCode != 46 && (event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105 )) {
                event.preventDefault(); 
            }
    	},
     
    	positionCaret : function(textbox){
    		if(textbox.createTextRange){
	    		var range = textbox.createTextRange(),valuelength = textbox.value.length,character = 'character';
				range.moveEnd(character, valuelength);
				range.moveStart(character, valuelength);
				range.select();	
    		}else{
    			var valuelength = textbox.value.length;
				if(!(textbox.selectionEnd == valuelength && textbox.selectionStart <= valuelength))
				{
					textbox.selectionStart = valuelength;
					textbox.selectionEnd = valuelength;
				}
    		}
    	}
    };
    