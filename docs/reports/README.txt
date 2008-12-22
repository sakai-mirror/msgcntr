There are a couple of simple configuration items that enable the extended
reporting in Forums. By default, the additional capabilities are hidden in all
sites.  Also, the extended reports always require "instructor" type access,
matching the permission profile of the existing statistics page.

The "Extended Statistics" link will appear next to the Statistics link.


Note first, that this extension relies on the contrib "Extraction Service",
which is under: 

  /contrib/reporting/extraction/branches/ctools-fall

and the "ForumExtract" job as under:

  /contrib/reporting/extraction-umich/branches/ctools-fa08

The data routines included there are planned to be ported to be exposed by the
Entity Broker.  At this point, the ForumExtract job may be a good candidate to
merge back into the mainline of msgcntr.


To enable the reporting for a given site, add the site property
forums.stats.extended.  It can be any non-empty value; true is suggested.

To enable the reporting globally, a value can be entered in sakai.properties for
forums.stats.extended.global.  As with the site property, any non-empty value is
considered true.

All of the reports rely on a JavaScript file in the admin Resources:
public/forums/forums.js.  This is what drives the data grids for both the Forum
and User views.

