jQuery.sap.declare("semantic.recognizer.util.Helper");

jQuery.sap.require("sap.ui.core.format.DateFormat");

semantic.recognizer.util.Helper = {

	formatCount : function(aItems) {
		return aItems && aItems.length ? "(" + aItems.length + ")" : "";
	},
	
	asynchGetJSON : function(thiz, sPath, fDoneCallback, fFailCallback, fAlwaysCallback) {
		return jQuery.ajax({
			url : sPath,
			type : "GET",
			dataType : "json",
			context : thiz
		}).done(fDoneCallback).fail(fFailCallback).always(fAlwaysCallback);
	},

	asynchPostJSON : function(thiz, sPath, oData, fDoneCallback, fFailCallback, fAlwaysCallback) {
		return jQuery.ajax({
			url : sPath,
			type : "POST",
			dataType : "json",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(oData),
			context : thiz
		}).done(fDoneCallback).fail(fFailCallback).always(fAlwaysCallback);
	}
};
