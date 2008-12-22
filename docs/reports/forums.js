/*
 * University of Michigan 2008
 * 
 * Some code from examples in: 
 * Ext JS Library 2.2
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.onReady(function(){

    var statsMode = 'forumMode';
    var f = forums || {};
    if (f.statsMode && f.statsMode == 'user')
        statsMode = 'userMode';

    Ext.QuickTips.init();

    var xg = Ext.grid;

    var readers = {
        forumMode: new Ext.data.XmlReader({record: 'forumView', id: 'topicId'}, [
            {name: 'forum', type: 'string'},
            {name: 'topic', type: 'string'},
            {name: 'nMessage', type: 'int'},
            {name: 'nUsers', type: 'int'},
            {name: 'postPerUser', type: 'int'},
            {name: 'nThread', type: 'int'},
            {name: 'nReply', type: 'int'},
            {name: 'wordCount', type: 'int'},
            {name: 'wordsPerPost', type: 'int'}
        ]),
        userMode:  new Ext.data.XmlReader({record: 'userView'}, [
            {name: 'forumTopic', type: 'string'},
            {name: 'message', type: 'string'},
            {name: 'reply', type: 'boolean'},
            {name: 'thread', type: 'boolean'},
            {name: 'User', type: 'string'},
            {name: 'wordCount', type: 'int'},
            {name: 'date', type: 'date', dateFormat: 'Y-m-d G:i:s.u'}
        ])};

    var groupingField = {forumMode: 'forum', userMode: 'User'};
    var sorting = {forumMode: {field: 'topic', direction: 'ASC'}, userMode: {field: 'User', direction: 'ASC'}};
    var viewOptions = {
        forumMode: {
            forceFit:true,
            showGroupName: false,
            enableNoGroups:false, // REQUIRED!
            hideGroupedColumn: true
        },
        userMode: {
            forceFit:true,
            showGroupName: false,
            enableNoGroups:false, // REQUIRED!
            hideGroupedColumn: false
        }};

    var summary = new Ext.grid.GroupSummary(); 

    Ext.grid.GroupSummary.Calculations['nonzeroAverage'] = function(v, record, field, data){
        var c = ((record.data[field]||0) === 0)
            ? (data[field+'count']
                ? data[field+'count']
                : 0)
            : (data[field+'count']
                ? ++data[field+'count']
                : (data[field+'count'] = 1));
        var t = (data[field+'total'] = ((data[field+'total']||0) + (record.data[field]||0)));
        return c === 0 ? '--' : (t === 0 ? 0 : t / c);
    }

    var dstore = new Ext.data.GroupingStore({
            reader: readers[statsMode],
            url: '/extraction-tool/data/edu.umich.ctools.extraction.ForumExtract/' + forums.currentSiteId + '/' + statsMode,
            sortInfo: sorting[statsMode],
            groupField: groupingField[statsMode]
        });

    var gridColumns = {
        userMode: [
            {
                header: "User",
                width: 20,
                sortable: true,
                groupable: true,
                dataIndex: 'User'
            },{
                header: "Forum/Topic",
                width: 60,
                sortable: true,
                dataIndex: 'forumTopic'
            },{
                header: "Message",
                width: 60,
                sortable: true,
                groupable: false,
                dataIndex: 'message'
            },{
                header: "Date",
                width: 20,
                sortable: true,
                dataIndex: 'date',
                summaryType: 'max',
                renderer: Ext.util.Format.dateRenderer('m/d/Y')
            },{
                header: "Thread",
                width: 20,
                sortable: true,
                groupable: false,
                dataIndex: 'thread',
                renderer: function(v) { return v ? 'Y' : 'N'; }
            },{
                header: "Reply",
                width: 20,
                sortable: true,
                groupable: false,
                dataIndex: 'reply',
                renderer: function(v) { return v ? 'Y' : 'N'; }
            },{
                header: "Word Count",
                width: 20,
                sortable: true,
                groupable: false,
                dataIndex: 'wordCount',
                summaryType:'average'
            }],
        forumMode: [
            {
                //id: 'forum',
                header: "Forum",
                width: 40,
                sortable: true,
                dataIndex: 'forum',
                /*
                editor: new Ext.form.TextField({
                   allowBlank: false
                })
                */
            },{
                header: "Topic",
                width: 40,
                sortable: true,
                groupable: false,
                dataIndex: 'topic',
                summaryType: 'count',
                hideable: false,
                summaryRenderer: function(v, params, data){
                    return ((v === 0 || v > 1) ? '(' + v +' Topics)' : '(1 Topic)');
                }
            },{
                header: "Messages",
                width: 20,
                sortable: true,
                gropuable: false,
                dataIndex: 'nMessage',
                summaryType:'sum'
            },{
                header: "Users",
                width: 20,
                sortable: true,
                groupable: false,
                dataIndex: 'nUsers',
                summaryType:'sum'
            },{
                header: "Posts/User",
                width: 20,
                sortable: true,
                groupable: false,
                dataIndex: 'postPerUser',
                summaryType:'nonzeroAverage'
            },{
                header: "Threads",
                width: 20,
                sortable: true,
                groupable: false,
                dataIndex: 'nThread',
                summaryType:'sum'
            },{
                header: "Replies",
                width: 20,
                sortable: true,
                groupable: false,
                dataIndex: 'nReply',
                summaryType:'sum'
            },{
                header: "Total Words",
                width: 20,
                sortable: true,
                groupable: false,
                dataIndex: 'wordCount',
                summaryType:'sum'
            },{
                header: "Words/Post",
                width: 20,
                sortable: true,
                groupable: false,
                dataIndex: 'wordsPerPost',
                summaryType:'nonzeroAverage'
            }]};

    var grid = new xg.EditorGridPanel({
        ds: dstore,
        columns: gridColumns[statsMode],
        view: new Ext.grid.GroupingView({
            forceFit:true,
            showGroupName: false,
            enableNoGroups:false, // REQUIRED!
            hideGroupedColumn: true
        }),

        plugins: summary,

        frame:true,
        width: 800,
        height: 450,
        clicksToEdit: 1,
        collapsible: true,
        animCollapse: false,
        trackMouseOver: false,
        //enableColumnMove: false,
        title: 'Forum Summary',
        iconCls: 'icon-grid',
        renderTo: 'forums-summary' //document.body
    });

    dstore.load();
});

