function showHideDiv(hideDivisionNo, context)
{
  var tmpdiv = hideDivisionNo + "__hide_division_";
  var tmpimg = hideDivisionNo + "__img_hide_division_";
  var divisionNo = getTheElement(tmpdiv);
  var imgNo = getTheElement(tmpimg);
  if(divisionNo)
  {
    if(divisionNo.style.display =="block" || divisionNo.style.display =="table-row")
    {
      divisionNo.style.display="none";
      if (imgNo)
      {
        imgNo.src = context + "/images/expand.gif";
      }
    }
    else
    {
      if(navigator.product == "Gecko")
      {
        divisionNo.style.display="table-row";
      }
      else
      {
        divisionNo.style.display="block";
      }
      if(imgNo)
      {
        imgNo.src = context + "/images/collapse.gif";
      }
    }
  }
}

function getTheElement(thisid)
{

  var thiselm = null;

  if (document.getElementById)
  {
    thiselm = document.getElementById(thisid);
  }
  else if (document.all)
  {
    thiselm = document.all[thisid];
  }
  else if (document.layers)
  {
    thiselm = document.layers[thisid];
  }

  if(thiselm)   
  {
    if(thiselm == null)
    {
      return;
    }
    else
    {
      return thiselm;
    }
  }
}
