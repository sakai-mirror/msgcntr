

function initNodes(){

    $("#tabOne").click(function() { // bind click event to link
        $('#picker > ul').tabs('select', 0); // switch to third tab
        return false;
    });

    $('#tabTwo').click(function() { // bind click event to link
        $('#picker > ul').tabs('select', 1); // switch to third tab
        return false;
    });

    $('#tabThree').click(function() { // bind click event to link
        $('#picker > ul').tabs('select', 2); // switch to third tab
        return false;
    });

    $(".roleFilter").click(function() { // bind click event to link
        if(console)console.log("clicked");
        var filter = $(this).text();
        if(console)console.log("filter by "+filter);
        filterByRole(filter);
        return false;
    });

    /**
    $(".roleDropdown").change( function() {
        var filter = $(this).val();
        if(console)console.log("filter by "+filter);
    });

    $(".sectionDropdown").change( function() {
        var filter = $(this).val();
        if(console)console.log("filter by "+filter);
    });
     **/




}


function initData(){

    $(".listItems").empty();

    $.getJSON("picker/demo_site_membership.json", function(data){
        $.each(data.membership_collection, function(i,item){
            //console.log(item.userDisplayName + " "+item.userEid);
            $(".listItems").append("<li id=id"+i+" tabIndex=' -1'>" +item.userDisplayName+"("+item.userEid+")</li>");
            $("#id"+i).click( function() {
                singleAddListItem(this,true) ;
            });

        });

    });

} ;


function initRoles(){

    $(".roleListItems").empty();
    $.getJSON("picker/demo_site.json", function(data){
        $.each(data.userRoles, function(i,item){
            console.log(item);
            $(".roleListItems").append("<li id=roleId"+i+" tabIndex=' -1'>" +item +"</li>");
            $("#roleId"+i).click( function() {
                singleAddListItem(this,true) ;
            });

            $(".roleDropdown").append("<option id=roleItem"+i+" tabIndex=' -1'>" +item +"</option>");
            $("#roleItem"+i).click( function() {
                //singleAddListItem(this,true) ;
            });
        });

    });

} ;

function initGroups(){

    $(".groupListItems").empty();
    $.getJSON("picker/site.json", function(data){
        //var  groupList = data.providerGroupId.split("+");
        $.each(data.siteGroups, function(i,item){
            console.log(item.title);
            $(".groupListItems").append("<li id="+item.id+" tabIndex=' -1'>" +item.title +"</li>");
            $("#"+item.id).click( function() {
                singleAddListItem(this,true) ;
            });

            $(".sectionDropdown").append("<option id=sectionItem"+i+" value="+ item.id+" tabIndex=' -1'>" +item.title +"</option>");
            $("#sectionItem"+i).click( function() {
                //singleAddListItem(this,true) ;
            });
        });

    });
} ;



function filterByRole(roleVal){

    $(".listItems").empty();
    $.getJSON("picker/demo_site_membership.json", function(data){
        $.each(data.membership_collection, function(i,item){
            //console.log(item.userDisplayName + " "+item.userEid);
            if(item.memberRole == roleVal){
                $(".listItems").append("<li id=id"+i+" tabIndex=' -1'>" +item.userDisplayName+"("+item.userEid+")</li>");
                $("#id"+i).click( function() {
                    singleAddListItem(this,true) ;
                });
            }
        });

    });

}

function fiterByGroup(data){

}




/* Handling keyboard access with Fluid's keyboard-a11y.js plug-in
		 * TO DO:
		 * Add drag and drop to the Container
		 * Fix miss-use of name: alternative field? rel? or just naming dim-id and id? since ultimately ids will be passed back with the JSON
		 * Change input buttons to buttons for symantics and formatting
		 * Change formatting to be a little more generic
		 * Pick new move arrows
		 */

/* Functions:
         * initPicker - manages initialization: disable add button, add click event, add keyboard handling events
         * makeSelectable - adds correct keyboard events for each container
         *
         *
         * debugOut - sends a message to the console (update with IE version?)
         */

/* Options:
         * debug - true or false - default false, true turns on copious console output
         * source_id
         * collection_id
         * item_identifier - (can be class or tag name)
         */

/*
         * keyboard
         * on source select
         * dim this element, move this element to the bottom of container, scroll container to bottom
         * if all elements are dim, ?
         * else select the next non-dim element in source
         */
var focusElm;
var currCollectionRow;
var debug = true;

$(document).ready(function() {

    //initPicker();
    //initData();
    //initRoles();
    //initGroups();
    //initNodes();

});



function initPicker() {

    $('.submitbutton').attr("disabled","disabled");

    // initailize the source container, collection items get initialized as they are added to collection
    $("#source li").click( function() {
        singleAddListItem(this,true) ;
    });

    makeSelectable('source',keyAddListItem);
    makeSelectable('collection',removeListItem);

}

function makeSelectable(containerId,activateAction){
    var container = jQuery('#' + containerId + ' .scroller');
    var items = jQuery('li', container);
    var selectionHandlers = {willSelect: highlightRow, willUnselect: unhighlightRow};

    items.selectable(container, selectionHandlers);
    items.activatable(activateAction);
}


function highlightNextRow(whichList) {
    var currRow = currentItem(whichList);
    debugOut('highlightNextRow:' + whichList, true); // DEBUG
    highlightRow(theNextRow(currRow),whichList);
}

function highlightPrevRow(whichList) {
    var currRow = currentItem(whichList);
    debugOut('highlightPrevRow:' + whichList, true); // DEBUG
    highlightRow(thePrevRow(currRow,whichList),whichList);
}

function currentItem(whichList) {
    if (whichList == 'source') {
        if (currSourceRow) return currSourceRow;
        else return theNextRow('#source li:last');
    } else {
        if (currCollectionRow) return currCollectionRow;
        else return theNextRow('#collection li:last');
    }
}

function theNextRow(currElm) {
    var nextElm = $(currElm).next();
    debugOut('nextElm: ' + $(nextElm).attr('id') + ' - ' + $(nextElm).attr('name'), true); // DEBUG
    if (!$(nextElm).attr('id') && !$(nextElm).attr('name')) { // we're at the bottom, cycle to top
        debugOut('end cycling to top', true); // DEBUG
        nextElm = $(currElm).siblings()[0];
    }
    if ($(nextElm).attr('disabled')) {
        debugOut($(nextElm).text() + ' - is disabled trying next element', true); // DEBUG
        return theNextRow(nextElm);
    }
    return nextElm;
}

function thePrevRow(currElm,whichList) {
    var nextElm = $(currElm).prev();
    debugOut('prevElm: ' + $(nextElm).attr('id') + ' - ' + $(nextElm).attr('name'), true); // DEBUG
    if (!$(nextElm).attr('id') && !$(nextElm).attr('name')) { // we're at the bottom, cycle to top
        nextElm = $('#' + whichList + ' li:last');
    }
    if ($(nextElm).attr('disabled')) {
        debugOut($(nextElm).text() + ' - is disabled trying next element', true); // DEBUG
        return thePrevRow(nextElm);
    }
    return nextElm;
}

function unhighlightRow(rowElm) {
    $(rowElm).removeClass('key-highlight');
    // in case the mouse has added a highlight remove that too
    $('li.highlight').removeClass('highlight');
}
function highlightRow(rowElm) {
    $(rowElm).addClass('key-highlight');
}

function setFocus(elm) {
    focusElm = elm;
    setTimeout("focusElm.focus();",0);  // gFocusItem must be a global
    return false;
}

function keyAddListItem(item) {
    if ( $(item).attr('class') != "dim" ) {
        removePlaceholder();
        clearHighlight();
        addListItem(item);
        //setFocus($(item));
        scrollBottom("collection-scroller");
        jQuery().selectNext();
        //if (highlight) $('#collection li:last').addClass('highlight');
        //$('#source-scroller').focus();
        //currCollectionRow = $('#collection li:last');
    }
    setCounter();
}


function singleAddListItem(item,highlight) {


    if ( $(item).attr('class') != "dim" ) {
        removePlaceholder();
        clearHighlight();
        addListItem(item);
        setFocus($(item));
        scrollBottom("collection-scroller");
        if (highlight) $('#collection li:last').addClass('highlight');
        $('#source-scroller').focus();
        currCollectionRow = $('#collection li:last');
    }
    setCounter();
}

function addListItem(item) {
    var newItem = $(item).clone(true);
    $(newItem).attr('name',$(item).attr('id'));
    $(newItem).attr('id','');

    $(newItem).click( function() {
        removeListItem(this) ;
    });

    $(newItem).appendTo('#collection ul');
    $(item).addClass("dim");
    $(item).attr("disabled","disabled");
}

function removeListItem(item) {
    $('#' + $(item).attr('name')).removeClass('dim');
    $('#' + $(item).attr('name')).attr("disabled","");
    $(item).remove();
    clearHighlight();
    setCounter();
    if ($('#collection li').length === 0) {
        replacePlaceholder();
        $('.submitbutton').attr("disabled","disabled");
    }
}

function removePlaceholder() {
    $('#placeholder').remove() ;
    $('.submitbutton').attr("disabled","");
}

function addAllRows() {
    if ( $('#placeholder') ) {
        $('#placeholder').remove() ;
        $('.submitbutton').attr("disabled","");
    }
    $('#source li').not('.dim').each(function(count) {
        addListItem(this);
    });
    setCounter();
    clearHighlight(); // reset the last highlight
}

function removeAllRows() {
    $('#collection li').each(function(count) {
        removeListItem(this);
    });
    return false;
}

function setCounter() {
    $('#counter').html(containerCounter());
}

function containerCounter() {
    return ($('#collection li').length) ? ($('#collection li').length) : '0';
}

function scrollBottom(list) {
    var bScroll = document.getElementById(list);
    bScroll.scrollTop = bScroll.scrollHeight;
}

function scrollTop(list) {
    var bScroll = document.getElementById(list);
    bScroll.scrollTop = 0;
}

function replacePlaceholder() {
    $('#collection ul').prepend("<li id='placeholder'>-</li>");
}

function clearHighlight(item) {
    // removes both the clicked highlight and the key-highlight just in case
    $('li.highlight').removeClass('highlight');
    $('li.key-highlight').removeClass('key-highlight');
}

function debugOut(string) {
    // console output for FF (be nice to add something for IE, but what?)
    if (window.console) console.log(string);
}




function saveCollection() {
    alert("Doesn't do anything in this prototype. Add your own Add Receipients function here!")
}



