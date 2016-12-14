function doGet(request) {
  if(request != null) {

    var ss = SpreadsheetApp.openById("REPLACE ME WITH SPREADSHEET ID");
    var sheet = ss.getSheetByName("Sheet1");
    var firstRowRange = sheet.getRange(1, 1, 1, sheet.getLastColumn());
  
    var sheetHeaders=firstRowRange.getValues();
    // columns start at one, but arrays start at 0, so this is the right number for inserting into an array that will be placed into the columns
    var newColumnArrayCount=sheet.getLastColumn();
    
    // initialize new row to be inserted before it gets filled with data
    var newRow = new Array(sheetHeaders[0].length);
    for(var x=0;x<newColumnArrayCount;x++) newRow[x]="";
    
    for (var i in request.parameters) {
      var foundRow=false;
      for(var x=0;x<sheetHeaders[0].length;x++) {
        if(i.toString().toLowerCase()==sheetHeaders[0][x].toString().toLowerCase()) {
          newRow[x]=request.parameter[i];
          foundRow=true;
          break;
        }
      }
      if(foundRow==false) {
        if(sheet.getLastColumn()==sheet.getMaxColumns()) {
          sheet.insertColumnAfter(sheet.getLastColumn());
        }
        sheetHeaders[0][newColumnArrayCount]=i;
        newRow[newColumnArrayCount]=request.parameter[i];
        firstRowRange=sheet.getRange(1, 1, 1, sheet.getLastColumn()+1);
        firstRowRange.setValues(sheetHeaders);
        firstRowRange = sheet.getRange(1, 1, 1, sheet.getLastColumn());
        sheetHeaders=firstRowRange.getValues();
        newColumnArrayCount++;
      }
    }

    if(newRow[0]=="") {
      Logger.log("setting date");
      newRow[0]=new Date();
    }
    
    // Appends a new row to the bottom of the
    // spreadsheet containing the values in the array
    sheet.appendRow(newRow);
  }
}
